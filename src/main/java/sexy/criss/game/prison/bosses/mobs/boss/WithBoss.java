package sexy.criss.game.prison.bosses.mobs.boss;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.*;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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

public class WithBoss extends EntityWitch implements Listener {
    Main plugin = Main.getInstance();
    Spawner spawner;
    Map<String, Integer> attackers;
    int total_damage;
    double speed = 0;
    double damage = 15;
    List<PotionEffect> potions;
    BossBar bar;
    private final List<String> phrases_1 = Arrays.asList("&cТак будет лучше, дорогие мои.", "&cНемного лазури и корня мандрагоры.. вуаля!", "&cЯ приготовила нечто особенное для вас! Bon Appétit!");
    private final List<String> phrases_2 = Arrays.asList("&cПрочь, прочь от меня!", "&cНемедленно угомонитесь, дрянные детишки!", "&cВаше поведение переходит уже все границы!", "&cПаучки, накажите их!");
    private final List<String> phrases_3 = Arrays.asList("&cДостаточно! Я проклинаю вас!", "&cХватит!!", "&cПоздравляю, вы вывели мамочку из себя!", "&cВас все равно никогда меня не одолеть!");

    int time = 0;
    int hp = 20, t1 = 450, t2 = 700, t3 = 300, t4 = 50, money = 1500;
    int health = 600;
    int cool = 20;

    public WithBoss(Spawner spawner) {
        super(EntityTypes.WITCH, UNMS.getWorldServer(spawner.getSpawnLocation()));

        ((LivingEntity)this.getBukkitEntity()).setRemoveWhenFarAway(false);
        ((LivingEntity)this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 2), true);
        ((LivingEntity)this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 3), true);
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(this.health);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(180);
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(Float.MAX_VALUE);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.speed);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(this.damage);
        this.setHealth((float)this.health);
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 180));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 10.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 180));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, this.damage, true));
        bar = Bukkit.createBossBar(Util.f("&c&lОдержимая ведьма"), BarColor.WHITE, BarStyle.SEGMENTED_6, BarFlag.PLAY_BOSS_MUSIC);
        this.setCustomName(name());
        this.setCustomNameVisible(true);
        this.canPickUpLoot = false;
        this.persistent = true;
        this.expToDrop = 0;
        this.attackers = Maps.newHashMap();
        this.total_damage = 0;

        {
            this.potions = Lists.newArrayList();
            this.potions.add(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0, true, true, true));
            this.potions.add(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0, true, true, true));
            this.potions.add(new PotionEffect(PotionEffectType.SLOW, 200, 0, true, true, true));
            this.potions.add(new PotionEffect(PotionEffectType.WITHER, 200, 0, true, true, true));
            this.potions.add(new PotionEffect(PotionEffectType.POISON, 200, 0, true, true, true));
            this.potions.add(new PotionEffect(PotionEffectType.HUNGER, 200, 0, true, true, true));
        }

        this.spawner = spawner;
        spawner.register(this);
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if(damagesource != null && damagesource.getEntity() instanceof Player p) {
            int phase = this.getPhase();
            if (phase == 2) f *= 0.7D;
            else if (phase == 3) f *= 0.4D;


            if (!this.attackers.containsKey(p.getName())) this.attackers.put(p.getName(), (int)f);
            else this.attackers.put(p.getName(), (int) (this.attackers.get(p.getName()) + f));
            this.total_damage += (int)f;
        }
        return super.damageEntity(damagesource, f);
    }

    @Override
    public void tick() {
        if(getBukkitEntity().getLocation().distance(spawner.getSpawnLocation()) >= 1) getBukkitEntity().teleport(spawner.getSpawnLocation());
        bar.setProgress((getHealth() / getMaxHealth()));
        bar.getPlayers().stream().filter(player -> (player.getWorld().equals(getBukkitEntity().getWorld()) || (player.getWorld().equals(getBukkitEntity().getWorld()) &&
                                player.getLocation().distance(spawner.getSpawnLocation()) < 30))).forEach(bar::removePlayer);
        getNearPlayers().stream().filter(p -> !bar.getPlayers().contains(p)).forEach(p -> bar.addPlayer(p));

        if(cool-- <= 0) {
            time++;
            cool = 20;
            time = time %120;
            Set<Player> players = getNearPlayers();
            if(players.isEmpty()) {
                setHealth((float) Math.min(getMaxHealth(), getHealth() + getMaxHealth() * 0.025D));
            } else {
                switch (getPhase()) {
                    case 3 -> {
                        if (this.time % 10 == 0) {
                            if (this.time % 60 == 0)
                                this.sendMessage(this.phrases_3.get(getRandom().nextInt(this.phrases_3.size())));

                            this.cave_spiders();
                        }
                    }
                    case 2 -> {
                        if (this.time % 40 == 0) {
                            Vector v = getBukkitEntity().getLocation().toVector();
                            this.sendMessage(this.phrases_2.get(getRandom().nextInt(this.phrases_2.size())));
                            players.forEach(p -> {
                                p.setVelocity(p.getLocation().toVector().subtract(v).normalize().multiply(4));
                                p.damage(8);
                            });

                            for (int i = 0; i < 4; ++i) spiders();
                        }
                    }
                    case 1 -> {
                        if (this.time % 30 == 0) {
                            PotionEffect pe = switch (getRandom().nextInt(4)) {
                                case 0 -> new PotionEffect(PotionEffectType.BLINDNESS, 140, 0);
                                case 1 -> new PotionEffect(PotionEffectType.WEAKNESS, 140, 1);
                                case 2 -> new PotionEffect(PotionEffectType.POISON, 140, 1);
                                default -> null;
                            };

                            boolean sendMessage = this.time % 40 == 0;
                            Player px;
                            if(pe != null) {
                                if(sendMessage)
                                    this.sendMessage(this.phrases_1.get(getRandom().nextInt(this.phrases_1.size())));
                                players.forEach(p -> p.addPotionEffect(pe));
                            } else {
                                this.sendMessage("&cГорите все в адском пламени!");
                                players.forEach(p -> p.setFireTicks(140));
                            }
                        }
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    public void die() {
        bar.removeAll();
        bar = null;
        if(spawner != null) spawner.iDead();
        if(this.killer != null) {
            Bukkit.broadcastMessage(Util.f("&4Одержимая ведьма была повержена! Нападавшие получили ее ценные сокровища!"));
            Map<String, Double> percents = Util.calculatePercents(this.attackers, this.total_damage);
            percents.keySet().forEach(s -> {
                Player p = Bukkit.getPlayerExact(s);
                PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
                double money = percents.get(s) * this.money;
                money = money < 0 ? 0 : money;
                if(pp != null) {
                    pp.addMob(EntityType.WITCH);
                    pp.addGold(money);
                    Util.ps("Prison", p, "&fВы получили &6"+money+"$&f за участие в убийстве босса.");
                }
            });
        }
        if ((double)getRandom().nextFloat() <= 0.05D)
            this.getBukkitEntity().getLocation().getWorld().dropItem(this.getBukkitEntity().getLocation(), PrisonItem.getItem("newpickaxe_1"));
        getBukkitEntity().getWorld().dropItem(getBukkitEntity().getLocation(), Util.update(PrisonItem.getItem("star"), getRandom().nextInt(15)+1));
        super.die();
    }

    public void spiders() {
        List<Entity> near = getBukkitEntity().getNearbyEntities(10, 10, 10).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        if(near.size() > 0) {
            near.forEach(e -> {
                Player player = (Player) e;
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 150, 255, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 255, true));
                player.setFireTicks(20);
                for(int i = 0; i < getRandom().nextInt(3)+1; i++) {
                    Spider spider = getBukkitEntity().getWorld().spawn(getBukkitEntity().getLocation(), Spider.class);
                    spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
                    spider.setHealth(40);
                    spider.setCustomName(Util.f("&8Ведьмин ткач"));
                    spider.setCustomNameVisible(true);
                    spider.setTarget(player);
                }
            });
        }
    }

    public void cave_spiders() {
        List<Entity> near = getBukkitEntity().getNearbyEntities(10, 10, 10).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        if(near.size() > 0) {
            near.forEach(e -> {
                Player player = (Player) e;
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 150, 255, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 255, true));
                player.setFireTicks(20);
                for(int i = 0; i < getRandom().nextInt(3)+1; i++) {
                    Spider spider = getBukkitEntity().getWorld().spawn(getBukkitEntity().getLocation(), Spider.class);
                    spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                    spider.setHealth(20);
                    spider.setCustomName(Util.f("&8Серая смерть"));
                    spider.setCustomNameVisible(true);
                    spider.setTarget(player);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            if(spider != null && !spider.isDead()) {
                                Util.sendParticle(Particle.EXPLOSION_LARGE, spider.getLocation(), 0, 0, 0, 0, 3);
                                List<Entity> en = spider.getNearbyEntities(5, 5, 5).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
                                if(en.size() > 0) {
                                    en.forEach(e -> {
                                        Player p = (Player)e;
                                        p.damage(10, spider);
                                    });
                                }

                                spider.remove();
                            }
                        }
                    }.runTaskLater(Main.getInstance(), 120);
                }
            });
        }
    }

    public void debuff() {
        List<Entity> near = getBukkitEntity().getNearbyEntities(15, 15, 15).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
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
        List<Entity> near = getBukkitEntity().getNearbyEntities(15, 15, 15).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
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

    @EventHandler
    public void entityDeaths(EntityDeathEvent e) {
        Entity ent = e.getEntity();
        if(ent.getType().equals(EntityType.CAVE_SPIDER) && ent.getCustomName().toLowerCase().contains("серая смерть")) {
            Util.sendParticle(Particle.EXPLOSION_LARGE, ent.getLocation(), 0, 0, 0, 0, 3);
            List<Entity> en = ent.getNearbyEntities(5, 5, 5).stream().filter(et -> et instanceof Player).collect(Collectors.toList());
            if(en.size() > 0) {
                en.forEach(pl -> {
                    Player p = (Player)pl;
                    p.damage(10, ent);
                });
            }
        }
    }

    @EventHandler
    public void onPlayerDeaths(PlayerDeathEvent e) {
        if(e.getEntity().getLocation().distance(getBukkitEntity().getLocation()) <= 30) {
            heal(50, EntityRegainHealthEvent.RegainReason.REGEN);
        }
    }

    private int getPhase() {
        double max = getMaxHealth();
        double current = getHealth();
        if (current / max > 0.66D) return 1;
        else return current / max > 0.33D ? 2 : 3;
    }

    private void sendMessage(String s) {
        s = Util.f("&c&lОдержимая Ведьма&r: " + s);
        final String msg = s;
        getBukkitEntity().getNearbyEntities(30, 30, 30).stream().filter(e -> e instanceof Player).forEach(e -> {
            Player p = (Player)e;
            Util.s(p, msg);
        });
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

    public ChatComponentText name() {
        return Component.of("&4Одержимая ведьма");
    }
}
