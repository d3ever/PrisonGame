package sexy.criss.game.prison.bosses.mobs.animals.working;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Material;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.gen.util.Component;
import sexy.criss.gen.util.UNMS;
import sexy.criss.gen.util.Util;

public class MeatPig extends EntityPig {
    Spawner spawner;

    public MeatPig(Spawner spawner) {
        super(EntityTypes.PIG, UNMS.getWorldServer(spawner.getSpawnLocation()));
        this.setCustomName(Component.of(getName()));
        this.setCustomNameVisible(true);

        this.getAttributeInstance(GenericAttributes.ATTACK_KNOCKBACK).setValue(Double.MAX_VALUE);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(.4);

        this.goalSelector.a(1, new PathfinderGoalPanic(this, 3));

        this.spawner = spawner;
        spawner.register(this);
    }

    public void meat() {
        getBukkitEntity().getWorld().dropItemNaturally(getBukkitEntity().getLocation(), Util.createItem(Material.PORKCHOP, "&6Сырая свинина"));
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if(random.nextDouble() <= .1) {
            meat();
        }
        return super.damageEntity(damagesource, 0);
    }

    @Override
    public String getName() {
        return Util.f("&6Поросёнок");
    }
}
