package sexy.criss.game.prison.bosses.mobs.animals.working;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.bosses.interfaces.IBossParameters;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.Component;
import sexy.criss.gen.util.UNMS;
import sexy.criss.gen.util.Util;

public class SheepMob extends EntitySheep {
    Spawner spawner;

    public SheepMob(Spawner spawner) {
        super(EntityTypes.SHEEP, UNMS.getWorldServer(spawner.getSpawnLocation()));

        this.setCustomName(getCustomerName());
        this.setCustomNameVisible(true);

        this.spawner = spawner;
        spawner.register(this);
    }

    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        if (itemstack.getItem() == Items.SHEARS) {
            if (!this.world.isClientSide && this.canShear()) {
                Player p = (Player) entityhuman.getBukkitEntity();
                PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
                if(pp.getLevel() < 5) {
                    if(getRandom().nextBoolean())
                    Util.ps("Prison", p, "&fВам нужен минимум &66&f уровень.");
                    return EnumInteractionResult.FAIL;
                }
                if (!CraftEventFactory.handlePlayerShearEntityEvent(entityhuman, this, itemstack, enumhand)) {
                    return EnumInteractionResult.PASS;
                } else {
                    this.shear(p, SoundCategory.PLAYERS);
                    return EnumInteractionResult.SUCCESS;
                }
            } else {
                return EnumInteractionResult.CONSUME;
            }
        } else {
            return super.b(entityhuman, enumhand);
        }
    }

    public void shear(Player p, SoundCategory soundcategory) {
        this.world.playSound(null, this, SoundEffects.ENTITY_SHEEP_SHEAR, soundcategory, 1.0F, 1.0F);
        this.setSheared(true);
        int i = 1 + this.random.nextInt(3);
        p.getInventory().addItem(Util.update(Util.createItem(org.bukkit.Material.WHITE_WOOL, "&6Шерсть"), i));
        new BukkitRunnable() {
            @Override
            public void run() {
                setColor(EnumColor.WHITE);
            }
        }.runTaskLater(Main.getInstance(), 20 * 60);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    public ChatComponentText getCustomerName() {
        return Component.of("&6Овца");
    }

    public double getSpeed() {
        return 0;
    }

    public double getAttackDamage() {
        return 0;
    }

    public double getGeneralHealth() {
        return 0;
    }
}
