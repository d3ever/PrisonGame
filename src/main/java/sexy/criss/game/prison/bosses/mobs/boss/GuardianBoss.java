package sexy.criss.game.prison.bosses.mobs.boss;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.server.v1_16_R3.*;
import net.minecraft.server.v1_16_R3.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
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

public class GuardianBoss extends EntityGuardianElder {
    Spawner spawner;
    int health = 1000;
    double damage = 15.0D;
    double followRange = 128.0D;
    double knobackResistence = 2.147483647E9D;
    double speed = 0.5D;
    double money = 5000.0D;
    int t1 = 450, t2 = 700, t3 = 300;
    Map<String, Integer> attackers;
    int total = 0;
    int hp = 20;
    BossBar bar;

    public GuardianBoss(Spawner spawner) {
        super(EntityTypes.ELDER_GUARDIAN, UNMS.getWorldServer(spawner.getSpawnLocation()));

        ((LivingEntity)this.getBukkitEntity()).setRemoveWhenFarAway(false);
        ((LivingEntity)this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 2, true, true, true));
        ((LivingEntity)this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 2, true, true, true));
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(this.health);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(this.followRange);
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(this.knobackResistence);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.speed);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(this.damage);
        this.setHealth((float)this.health);
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float)this.followRange));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 10.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float)(this.followRange / 2.0D)));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, this.damage, true));
        this.setCustomName(name());
        this.setCustomNameVisible(true);
        this.canPickUpLoot = false;
        this.persistent = true;
        this.expToDrop = 0;
        this.attackers = Maps.newHashMap();
        bar = Bukkit.createBossBar(Util.f(name().getText()), BarColor.WHITE, BarStyle.SEGMENTED_12, BarFlag.PLAY_BOSS_MUSIC);

        this.spawner = spawner;
        spawner.register(this);
    }

    @Override
    public boolean damageEntity(DamageSource source, float f) {
        Entity entity = source.getEntity();
        if(entity instanceof Player p) {
            p.sendTitle("", Util.f("&4"+((int)getHealth())+"❤"));
            if (!this.attackers.containsKey(p.getName())) this.attackers.put(p.getName(), (int)f);
            else this.attackers.put(p.getName(), (int)((float)this.attackers.get(p.getName()) + f));

            this.total += (int)f;

            if ((double)getRandom().nextFloat() < 0.1D) {
                List<org.bukkit.entity.Entity> near = getBukkitEntity().getNearbyEntities(10, 5, 10).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
                if(near.size() > 0) {
                    near.forEach(e -> {
                        Player t = (Player) e;
                        t.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0, true, true, true));
                    });
                }
            }

        }
        return super.damageEntity(source, f);
    }

    @Override
    public void tick() {
        if (this.spawner.getCurrent() != null && this.spawner.getSpawnLocation().distance(this.spawner.getCurrent().getBukkitEntity().getLocation()) > 1.0D) {
            this.spawner.getCurrent().setLocation(this.spawner.getSpawnLocation().getX(), this.spawner.getSpawnLocation().getY(), this.spawner.getSpawnLocation().getZ(), 0.0F, 0.0F);
        }

        bar.getPlayers().stream().filter(player -> player.getLocation().distance(spawner.getSpawnLocation()) < 30).forEach(bar::removePlayer);
        List<org.bukkit.entity.Entity> near = getBukkitEntity().getNearbyEntities(25, 25, 25).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        if(near.size() > 0) {
            near.stream().filter(e -> !bar.getPlayers().contains((Player)e)).forEach(e -> {
                Player p = (Player) e;
                bar.addPlayer(p);
            });
        }

        if(hp-- <= 0) {
            if(this.getHealth() < getMaxHealth()) this.heal(3, EntityRegainHealthEvent.RegainReason.REGEN);
            hp = 20;
        }

        if(this.getGoalTarget() != null) {
            if(t1--<=0) {
                guardians();
                t1=450;
            }
            if(t2--<=0) {
                debuff();
                t2=700;
            }
            if(t3--<=0) {
                throwDebuff();
                t3=300;
            }
        }
        super.tick();
    }

    @Override
    public void die() {
        if (this.spawner != null) this.spawner.iDead();

        if (this.killer != null) {
            Bukkit.broadcastMessage("§4Хранитель подводного мира был повержен! Нападавшие получили его ценные сокровища!");
            Map<String, Double> percents = Util.calculatePercents(this.attackers, this.total);
            percents.keySet().forEach(s -> {
                Player p = Bukkit.getPlayerExact(s);
                PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
                double money = percents.get(s) * this.money;
                money = money < 0 ? 0 : money;
                pp.addGold(money);
                pp.addMob(EntityType.GUARDIAN);
                Util.ps("Prison", p, "&fБитва с &cХранителем подводного мира&f завершена");
                Util.ps("Prison", p, "&fВы получили &6"+money+"$&f за нанесённый урон по боссу.");
            });

            if(getRandom().nextDouble() <= 0.00001) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
                    player.setVelocity(new Vector(0, .3, 0));
                });
                Material mat = getRandom().nextBoolean() ? Material.WATER_BUCKET : Material.LAVA_BUCKET;
                getBukkitEntity().getWorld().dropItem(getBukkitEntity().getLocation(), Util.createItem(mat, "&6Интересно", Lists.newArrayList(
                        "Довольно интересный момент",
                        "Что можно сделать с этим",
                        "к примеру на плотах? GEN",
                        "15 уровень - свобода"
                )));
                List<org.bukkit.entity.Entity> near = getBukkitEntity().getNearbyEntities(10, 10, 10).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
                if(near.size() > 0) {
                    near.forEach(e -> {
                        Player p = (Player) e;
                        p.setVelocity(new Vector(0, 1, 0).multiply(2));
                        p.setFlying(true);
                        p.setFlySpeed(0);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                p.setVelocity(new Vector(0, -2, 0).multiply(4));
                                p.setFlying(false);
                            }
                        }.runTaskLater(Main.getInstance(), 35);
                    });
                }
            }

            this.getBukkitEntity().getLocation().getWorld().dropItem(getBukkitEntity().getLocation(), Util.update(PrisonItem.getItem("start"), getRandom().nextInt(15)+20));
        }
        super.die();
    }

    public void guardians() {
        List<org.bukkit.entity.Entity> near = getBukkitEntity().getNearbyEntities(10, 10, 10).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        if(near.size() > 0) {
            near.forEach(e -> {
                Player player = (Player) e;
                for(int i = 0; i < getRandom().nextInt(3)+1; i++) {
                    Guardian guardian = getBukkitEntity().getWorld().spawn(getBukkitEntity().getLocation(), Guardian.class);
                    guardian.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
                    guardian.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(-1);
                    guardian.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50);
                    guardian.setHealth(50);
                    guardian.setCustomName(Util.f("&cЗащитник подводного мира"));
                    guardian.setCustomNameVisible(true);
                    guardian.setTarget(player);
                }
            });
        }
    }

    public void debuff() {
        List<org.bukkit.entity.Entity> near = getBukkitEntity().getNearbyEntities(15, 15, 15).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        if(near.size() > 0) {
            near.forEach(e -> {
                Player p = (Player) e;
                double damage = 15;
                EntityDamageEvent event = new EntityDamageEvent(p, EntityDamageEvent.DamageCause.CUSTOM, damage);
                Bukkit.getPluginManager().callEvent(event);
                if(!event.isCancelled()) {
                    p.damage(damage, getBukkitEntity());
                    if(p.isDead()) this.heal(40, EntityRegainHealthEvent.RegainReason.REGEN);
                    else {
                        p.setFireTicks(240);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 500, 0, true, true, true));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 500, 1, true, true, true));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 254, true, true, true));
                        p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0F, 1000.0F);
                        p.setVelocity(new Vector(Math.random() * (getRandom().nextBoolean() ? 1.5D : -1.5D), Math.random() * 1.5D, Math.random() * (getRandom().nextBoolean() ? 1.5D : -1.5D)));
                    }
                }
            });
        }
    }

    public void throwDebuff() {
        List<org.bukkit.entity.Entity> near = getBukkitEntity().getNearbyEntities(15, 15, 15).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        if(near.size() > 0) {
            near.forEach(e -> {
                Player p = (Player) e;
                Location l = p.getLocation();
                Location l2 = getBukkitEntity().getLocation();
                p.setVelocity(new Vector(l2.getX() - l.getX(), l2.getY() + 1.5D - l.getY(), l2.getZ() - l.getZ()));
                if (getRandom().nextBoolean()) {
                    double damage = (double)getRandom().nextInt(6) + 1;
                    EntityDamageEvent event = new EntityDamageEvent(p, EntityDamageEvent.DamageCause.CUSTOM, damage);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        p.damage(damage, getBukkitEntity());
                    }
                }
            });
        }
    }

    public ChatComponentText name() {
        return Component.of("&cХранитель подводного мира");
    }
}
