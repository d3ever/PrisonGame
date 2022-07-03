package sexy.criss.game.prison.bosses.mobs.boss;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.Component;
import sexy.criss.gen.util.UNMS;
import sexy.criss.gen.util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class GiantBoss extends EntityGiantZombie {
    Spawner spawner;
    String name = Util.f("&cГигант");
    BossBar bar;
    int health = 3000;
    int money = 7500;
    double damage = 25;
    double followRange = 128.0D;
    double knobackResistence = 2.147483647E9D;
    float speed = 3;
    int total;
    int jump_delay = 0;
    int harm_delay = 0;
    int hp_delay = 5;
    Map<String, Integer> attackers;

    public GiantBoss(Spawner spawner) {
        super(EntityTypes.GIANT, UNMS.getWorldServer(spawner.getSpawnLocation()));
        attackers = Maps.newHashMap();
        bar = Bukkit.createBossBar(name, BarColor.WHITE, BarStyle.SEGMENTED_12, BarFlag.PLAY_BOSS_MUSIC);

        ((LivingEntity)this.getBukkitEntity()).setRemoveWhenFarAway(false);
        ((LivingEntity)this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 1), true);
        ((LivingEntity)this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 2), true);
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(this.health);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(this.followRange);
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(this.knobackResistence);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.speed);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(this.damage);
        this.setHealth(this.health);
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float)this.followRange));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 10.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float)(this.followRange / 2.0D)));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, this.damage, true));
        (this.spawner = spawner).register(this);
        this.setCustomName(Component.of(name));
        this.setCustomNameVisible(true);
        this.canPickUpLoot = false;
        this.persistent = true;
        this.expToDrop = 0;
        this.total = 0;

        this.spawner = spawner;
        spawner.register(this);
    }

    @Override
    public boolean damageEntity(DamageSource source, float f) {
        final Entity entity = source.getEntity();
        if (entity instanceof Player p) {
            if (!this.attackers.containsKey(p.getName())) this.attackers.put(p.getName(), (int)f);
            else this.attackers.put(p.getName(), (int)(this.attackers.get(p.getName()) + f));
            this.total += (int)f;

            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0, false, false, false));
        }
        return super.damageEntity(source, f);
    }

    @Override
    public void tick() {
        if (this.spawner.getCurrent() != null && this.spawner.getSpawnLocation().distance(this.spawner.getCurrent().getBukkitEntity().getLocation()) > 1.0D) {
            this.spawner.getCurrent().setLocation(this.spawner.getSpawnLocation().getX(), this.spawner.getSpawnLocation().getY(), this.spawner.getSpawnLocation().getZ(), 0.0F, 0.0F);
        }

        bar.setProgress((getHealth() / getMaxHealth()));
        bar.getPlayers().stream().filter(player -> player.getLocation().distance(spawner.getSpawnLocation()) < 30).forEach(bar::removePlayer);
        List<org.bukkit.entity.Entity> near = getBukkitEntity().getNearbyEntities(25, 25, 25).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        if(near.size() > 0) {
            near.stream().filter(e -> !bar.getPlayers().contains((Player)e)).forEach(e -> {
                Player p = (Player) e;
                bar.addPlayer(p);
            });
        }
        try {
            final boolean isSameWorld = this.getBukkitEntity().getLocation().getWorld() == this.getGoalTarget().getBukkitEntity().getLocation().getWorld();
            final double distance = isSameWorld ? this.getBukkitEntity().getLocation().distance(this.getGoalTarget().getBukkitEntity().getLocation()) : 32.0;
            if (distance <= 16.0 && this.getGoalTarget() != null) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
            else this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0);
        }catch (NullPointerException ignored) {
        }

        if(hp_delay--<=0) {
            if(this.getHealth()<getMaxHealth()) this.heal(7, EntityRegainHealthEvent.RegainReason.REGEN);
            hp_delay=5;
        }

        if(this.getHealth() < health * 0.6 && this.harm_delay-- <= 0) {
            getBukkitEntity().getNearbyEntities(25, 5, 25).stream().filter(e -> e instanceof Player).forEach(e -> {
                ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4));
                ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
                this.harm_delay = 240;
            });
        }

        if (this.getHealth() < health * 0.2) {
            --this.jump_delay;
            if (this.jump_delay <= 0) {
                this.jump_delay = 100;
                this.superJump();
            }
        }

        if (this.getGoalTarget() != null) {
            double distance = this.getBukkitEntity().getLocation().distance(this.getGoalTarget().getBukkitEntity().getLocation());
            if (distance < 9.0D) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
            else this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        }

        super.tick();
    }

    @Override
    public void die() {
        if (this.spawner != null) {
            this.spawner.iDead();
        }

        if (this.killer instanceof EntityPlayer) {
            Player p = (Player)this.killer.getBukkitEntity();
            if (this.spawner != null) {
                PrisonPlayer.getPlayer(p.getUniqueId()).addMob(EntityType.GIANT);
            }
        }

        if (this.killer != null) {
            Bukkit.broadcastMessage("§cЗомби Гигант был повержен! Нападавшие получили его ценные сокровища!");
            Map<String, Double> percents = Util.calculatePercents(this.attackers, this.total);
            percents.keySet().forEach(s -> {
                Player p = Bukkit.getPlayerExact(s);
                PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
                int money = (int) (percents.get(s) * this.money / 100);
                pp.addGold(money);
                Util.ps("Prison", p, "&fБитва с &cГигантом&f завершена");
                Util.ps("Prison", p, "&fВы получили &6"+money+"$&f за нанесённый урон по боссу.");
            });
        }
        getBukkitEntity().getWorld().dropItem(getBukkitEntity().getLocation(), Util.update(PrisonItem.getItem("star"), getRandom().nextInt(18)+8));
        super.die();
    }

    public void superJump() {
        this.getBukkitEntity().getWorld().playEffect(this.getBukkitEntity().getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
        (new BukkitRunnable() {
            public void run() {
                getBukkitEntity().getWorld().playSound(getBukkitEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                getBukkitEntity().getNearbyEntities(12.5, 5, 12.5).stream().filter(p -> p instanceof Player).forEach(p -> p.setVelocity(p.getVelocity().add(new Vector(0, 1, 0).multiply(1))));

            }
        }).runTaskLater(Main.getInstance(), 25L);
    }

}
