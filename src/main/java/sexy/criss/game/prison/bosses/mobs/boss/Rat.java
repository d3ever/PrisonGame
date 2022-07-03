package sexy.criss.game.prison.bosses.mobs.boss;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.Util;

import java.util.Random;

public class Rat extends EntitySilverfish {
    Random rand = new Random();
    Spawner spawner;
    private final double speed = 0.6;

    public Rat(Spawner spawner) {
        super(EntityTypes.SILVERFISH, ((CraftWorld) spawner.getSpawnLocation().getWorld()).getHandle());

        this.setCustomName(boss_name());
        this.setCustomNameVisible(true);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(180);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(3);
        this.getAttributeInstance(GenericAttributes.ATTACK_KNOCKBACK).setValue(1);
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(Double.MAX_VALUE);
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(12);
        this.setHealth(12);

        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 64.0F));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 7, true));
        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 128.0F));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(6, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, 1, true, false, null));

        this.spawner = spawner;
        this.spawner.register(this);
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            if(damagesource.getEntity() != null && (damagesource.getEntity() instanceof Player)) {
                if(new Random().nextDouble() <= .4) ((Player) damagesource.getEntity()).damage(1);
            }
            return super.damageEntity(damagesource, f);
        }
    }

    @Override
    public void die(DamageSource damagesource) {
        boss_drop();
        if(damagesource.getEntity() != null && damagesource.getEntity() instanceof Player) {
            double money = (10.0 + (double)this.rand.nextInt(15)) / 100.0;
            PrisonPlayer pp = PrisonPlayer.getPlayer(((Player) damagesource.getEntity()).getUniqueId());
            pp.addGold(money);
            Util.s((Player)damagesource.getEntity(), "&f+"+money+"$");
        }
        super.die(damagesource);
    }

    int t = 20;
    @Override
    public void tick() {
        if(this.getHealth() < this.getMaxHealth()) {
            if (t-- <= 0) {
                this.heal(1, EntityRegainHealthEvent.RegainReason.REGEN);
                t = 20;
            }
        }
        this.setCustomName(boss_name());
        super.tick();
        if (this.getGoalTarget() == null) {
            return;
        }
        if (this.getGoalTarget() != null && this.getGoalTarget().world != this.world) {
            this.setGoalTarget(null);
            return;
        }
        if (!(this.getGoalTarget() instanceof EntityPlayer)) {
            this.setGoalTarget(null);
            return;
        }
        if (this.getGoalTarget() != null && this.passengers.get(0) == null) {
            double distance;
            boolean isSameWorld = this.getBukkitEntity().getLocation().getWorld() == this.getGoalTarget().getBukkitEntity().getLocation().getWorld();
            double d = distance = isSameWorld ? this.getBukkitEntity().getLocation().distance(this.getGoalTarget().getBukkitEntity().getLocation()) : 32.0;
            if (distance > 16.0) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0);
            else this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        }
    }

    public void boss_drop() {
        if ((double)this.rand.nextFloat() <= 0.6) {
            this.getBukkitEntity().getWorld().dropItemNaturally(this.getBukkitEntity().getLocation(), PrisonItem.getItem("rat_meat"));
        }
        if ((double)this.rand.nextFloat() <= 0.1) {
            this.getBukkitEntity().getWorld().dropItemNaturally(this.getBukkitEntity().getLocation(), PrisonItem.getItem("seeds"));
        }
    }

    public ChatComponentText boss_name() {
        return new ChatComponentText(Util.f("&cКрыса "+((int) getHealth()) + "♥"));
    }
}
