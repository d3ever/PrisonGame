package sexy.criss.game.prison.bosses.mobs.boss;

import net.minecraft.server.v1_16_R3.*;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.gen.util.UNMS;

public class BlazeBoss extends EntityMonster {
    Spawner spawner;
    private final int health = 600, speed = 0, damage = 14, money = 1000;

    protected BlazeBoss(Spawner spawner) {
        super(EntityTypes.BLAZE, UNMS.getWorldServer(spawner.getSpawnLocation().getWorld()));
        this.spawner = spawner;

        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(health);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(damage);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(180);
        this.getAttributeInstance(GenericAttributes.ATTACK_KNOCKBACK).setValue(0);
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(Double.MAX_VALUE);
        this.goalSelector.a(new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true, true));
        this.goalSelector.a(new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 180, 180));
        this.goalSelector.a(new PathfinderGoalHurtByTarget(this, EntityHuman.class));
        this.goalSelector.a(new PathfinderGoalMeleeAttack(this, damage, true));
        setHealth(health);
        setBaby(true);
    }
}
