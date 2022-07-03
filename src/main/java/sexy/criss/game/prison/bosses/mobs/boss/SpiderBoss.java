package sexy.criss.game.prison.bosses.mobs.boss;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import sexy.criss.game.prison.bosses.mobs.util.BossUtil;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.Component;
import sexy.criss.gen.util.UNMS;
import sexy.criss.gen.util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class SpiderBoss extends EntitySpider {
    Spawner spawner;
    CraftEntity ent;
    Map<String, Integer> attackers;
    int totalDamage;
    int hpDelay;
    int t1;
    int t2;
    BossBar bar;
    int time;
    int cl = 0;

    public SpiderBoss(Spawner spawner) {
        super(EntityTypes.SPIDER, UNMS.getWorldServer(spawner.getSpawnLocation()));
        t1 = 240;
        t2 = 180;
        time = 0;
        bar = Bukkit.createBossBar(Util.f("&cДревний паук"), BarColor.WHITE, BarStyle.SEGMENTED_12, BarFlag.PLAY_BOSS_MUSIC);

        this.setCustomName(Component.of(getName()));
        this.setCustomNameVisible(true);

        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(getGeneralHealth());
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(getSpeed());
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(-1);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(getAttackDamage());
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(180);

        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 180));
        this.goalSelector.a(2, new PathfinderGoalMoveTowardsRestriction(this, 10));
        this.goalSelector.a(3, new PathfinderGoalRandomStroll(this, 1));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, getAttackDamage(), true));
        this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(6, new PathfinderGoalHurtByTarget(this, EntityHuman.class));

        this.setHealth(getGeneralHealth());
        ((LivingEntity) this.getBukkitEntity()).setRemoveWhenFarAway(false);
        this.setCanPickupLoot(false);
        this.persistent = true;
        this.expToDrop = 0;
        this.drops.clear();
        this.ent = getBukkitEntity();
        this.attackers = Maps.newHashMap();

        this.spawner = spawner;
        spawner.register(this);
    }

    @Override
    public boolean damageEntity(final DamageSource source, final float a) {
        final Entity entity = source.getEntity();
        if (entity != null && entity.getBukkitEntity() instanceof Player p) {
            if (!this.attackers.containsKey(p.getName())) this.attackers.put(p.getName(), (int)a);
            else this.attackers.put(p.getName(), (int)(this.attackers.get(p.getName()) + a));

            this.totalDamage += (int)a;

            System.out.println(this.attackers);
            System.out.println(totalDamage);
            if (this.random.nextFloat() < 0.1) {
                for (final org.bukkit.entity.Entity e : this.getBukkitEntity().getNearbyEntities(10.0, 5.0, 10.0)) {
                    if (e.getType() == EntityType.PLAYER) {
                        ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));
                    }
                }
            }
        }
        return super.damageEntity(source, a);
    }

    @Override
    public void tick() {
        if (this.spawner.getCurrent() != null && this.spawner.getSpawnLocation().distance(this.spawner.getCurrent().getBukkitEntity().getLocation()) > 20.0) {
            this.spawner.getCurrent().setLocation(this.spawner.getSpawnLocation().getX(), this.spawner.getSpawnLocation().getY(), this.spawner.getSpawnLocation().getZ(), 0.0f, 0.0f);
        }
        if(this.spawner.getCurrent() != null) {
            bar.setProgress((getHealth() / getMaxHealth()));
            bar.getPlayers().stream().filter(player -> (player.getWorld().equals(getBukkitEntity().getWorld()) ||
                    (player.getWorld().equals(getBukkitEntity().getWorld()) && player.getLocation().distance(spawner.getSpawnLocation()) < 30))).forEach(bar::removePlayer);
            getNearPlayers().stream().filter(p -> !bar.getPlayers().contains(p)).forEach(p -> bar.addPlayer(p));
        }

        try {
            final boolean isSameWorld = this.getBukkitEntity().getLocation().getWorld() == this.getGoalTarget().getBukkitEntity().getLocation().getWorld();
            final double distance = isSameWorld ? this.getBukkitEntity().getLocation().distance(this.getGoalTarget().getBukkitEntity().getLocation()) : 32.0;
            if (distance <= 16.0 && this.getGoalTarget() != null) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(getSpeed());
            else this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0);
        }catch (NullPointerException ex) {
        }

        if (this.hpDelay-- <= 0) {
            if (this.getHealth() < this.getMaxHealth()) this.heal(2.0f, EntityRegainHealthEvent.RegainReason.REGEN);
            this.hpDelay = 20;
        }

        if (this.getGoalTarget() != null) {
            if(cl++ >= 20) {
                time++;
                cl=0;
            }

            --this.t2;
            if(this.t2 <= 0) {
                getBukkitEntity().getWorld().strikeLightningEffect(getBukkitEntity().getLocation());
                getBukkitEntity().setVelocity(new Vector(0, 1, 0).multiply(2));
                minions();
                this.t2 = 120;
            }
            --this.t1;
            if (this.t1 <= 0) {
                this.t1 = 400;
                BossUtil.superJump(getBukkitEntity());
            }
        }
        super.tick();
    }

    public void minions() {
        List<org.bukkit.entity.Entity> near = getBukkitEntity().getNearbyEntities(10, 10, 10).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        if(near.size() > 0) {
            near.forEach(e -> {
                Player player = (Player) e;
                for(int i = 0; i < getRandom().nextInt(3)+1; i++) {
                    CaveSpider spider = getBukkitEntity().getWorld().spawn(getBukkitEntity().getLocation(), CaveSpider.class);
                    spider.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(3);
                    spider.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(-1);
                    spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(5);
                    spider.setHealth(5);
                    spider.setCustomName(Util.f("&2Пещерный паук"));
                    spider.setCustomNameVisible(true);
                    spider.setTarget(player);
                }
            });
        }
    }

    public void die() {
        bar.removeAll();
        bar = null;
        if (this.spawner != null) this.spawner.iDead();

        if (this.killer != null) {
            final Map<String, Double> percents = Util.calculatePercents(this.attackers, this.totalDamage);
            if(percents.values().stream().max(Comparator.naturalOrder()).isPresent()) {
                int max = percents.values().stream().max(Comparator.naturalOrder()).get().intValue();
                if(percents.size() > 0 && percents.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue)).isPresent()) {
                    Map.Entry<String, Double> leader = percents.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue)).get();
                    Map.Entry<String, Integer> v = (attackers.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).orElse(null));
                    int percent = (v.getValue() / 400) * 100;
                    int m = ((int) (leader.getValue() * 400));
                    m = m < 0 ? 0 : Math.min(m, 400);
                    List<String> deathMessage = Lists.newArrayList(
                            "",
                            getName()+" был повержен за " + time + " " + Util.getFormated(time, "секунда", "секунды", "секунд"),
                            "&f - Всего нападавших",
                            "&6   " + percents.keySet().size() + "",
                            "&f - Последний нанёс удар",
                            "&6   " + this.killer.getName() + " &8[&6" + PrisonPlayer.getPlayer(this.killer.getUniqueID()).getLevel() + "&8]",
                            "&f - Нанёс больше всего урона",
                            "&6   " + leader.getKey() + ": " + percent + "% и получил " + m + "$",
                            ""
                    );
                    Util.f(deathMessage).forEach(Bukkit::broadcastMessage);
                }
            }
            for (final String key : percents.keySet()) {
                Player p = Bukkit.getPlayerExact(key);
                final PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
                double money = percents.get(key) * 400;
                money = money < 0 ? 0 : money > 400 ? 400 : money;
                pp.addGold((int)money);
                pp.addMob(EntityType.SPIDER);
                Util.ps("Prison", p, "&fБитва с боссом "+getName()+"&f завершена");
                Util.ps("Prison", p, "&fВы получили &6"+((int)money) +"$&f за нанесённый урон по боссу.");
            }
        }
        super.die();
    }

    @Override
    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
    }

    private Set<Player> getNearPlayers() {
        return this.getNearPlayers(spawner.getSpawnLocation(), 30);
    }

    private Set<Player> getNearPlayers(Location l, int radius) {
        Set<Player> set = Sets.newHashSet();
        getBukkitEntity().getWorld().getEntitiesByClass(Player.class).stream().filter(p -> distance(p.getLocation(), l)<= radius).forEach(set::add);
        return set;
    }

    private double distance(Location a, Location b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2.0D) + Math.pow(a.getZ() - b.getZ(), 2.0D));
    }

    public String getName() {
        return Util.f("&cДревний паук");
    }
    public double getSpeed() {
        return .03;
    }
    public double getAttackDamage() {
        return 10;
    }
    public float getGeneralHealth() {
        return 350;
    }
}
