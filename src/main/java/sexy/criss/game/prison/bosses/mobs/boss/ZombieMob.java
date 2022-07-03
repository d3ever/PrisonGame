package sexy.criss.game.prison.bosses.mobs.boss;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.gen.util.Component;
import sexy.criss.gen.util.UNMS;

public class ZombieMob extends EntityZombie {
    Spawner spawner;
    double speed = .26;

    public ZombieMob(Spawner spawner) {
        super(EntityTypes.ZOMBIE, UNMS.getWorldServer(spawner.getSpawnLocation()));

        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(50);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(128.0D);
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(-1.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(5);
        this.setHealth(50);
        ((LivingEntity)this.getBukkitEntity()).setRemoveWhenFarAway(false);
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 180f));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 10.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, EntityHuman.class));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float)(180 / 2.0)));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 5, true));
        this.setCustomName(name());
        this.setCustomNameVisible(true);
        this.canPickUpLoot = false;

        this.spawner = spawner;
        spawner.register(this);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if(damagesource.getEntity().getBukkitEntity() instanceof Player) {
            Player p = (Player)damagesource.getEntity().getBukkitEntity();
            p.sendTitle("", ""+((int)getHealth())+"♥");
        }
        return super.damageEntity(damagesource, f);
    }

    int t1 = 25;
    @Override
    public void tick() {
        if(t1-- <= 0) {
            this.heal(2, EntityRegainHealthEvent.RegainReason.REGEN);
            t1 = 25;
        }

        if(this.getGoalTarget() != null) {
            if(getBukkitEntity().getLocation().distance(spawner.getSpawnLocation()) > 20) {
                getBukkitEntity().teleport(spawner.getSpawnLocation());
            }
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        } else this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0);
        super.tick();
    }

    public ChatComponentText name() {
        return Component.of("&cЗомби");
    }

}
