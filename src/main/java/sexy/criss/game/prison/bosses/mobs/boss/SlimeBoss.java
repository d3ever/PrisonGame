package sexy.criss.game.prison.bosses.mobs.boss;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.Component;
import sexy.criss.gen.util.UNMS;
import sexy.criss.gen.util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class SlimeBoss extends EntitySlime {
    Spawner spawner;
    int hp = 5;
    int health = 500;
    int t = 120;
    double damage = 15;
    double speed = 0.014;
    float follow = 128;
    double money = 600;
    int debuff = 75;
    int t_damage;
    Map<String, Integer> attackers;

    public SlimeBoss(Spawner spawner) {
        super(EntityTypes.SLIME, UNMS.getWorldServer(spawner.getSpawnLocation()));

        ((LivingEntity) this.getBukkitEntity()).setRemoveWhenFarAway(false);
        ((LivingEntity) this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 1, true, true, true));
        ((LivingEntity) this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 0, true, true, true));
        ((LivingEntity) this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 1, true, true, true));
        this.setSize(5, false);
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(health);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(follow);
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(-1);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.speed);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(this.damage);
        this.setHealth((float) this.health);
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, follow));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float) (follow / 2.0D)));
        this.setCustomName(name());
        this.setCustomNameVisible(true);
        this.canPickUpLoot = false;
        this.persistent = true;
        this.expToDrop = 0;
        this.attackers = Maps.newHashMap();
        this.t_damage = 0;

        this.spawner = spawner;
        spawner.register(this);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        Entity entity = damagesource.getEntity();
        if(entity instanceof Player p) {
            p.sendTitle("", Util.f("&4"+((int)getHealth())+"❤"));
            if ((double)(new Random()).nextFloat() <= 0.1D) p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));

            if (!this.attackers.containsKey(p.getName())) this.attackers.put(p.getName(), (int)f);
            else this.attackers.put(p.getName(), (int)((float)this.attackers.get(p.getName()) + f));

            this.t_damage += (int)f;
        }
        return super.damageEntity(damagesource, f);
    }

    @Override
    public void tick() {
        if (this.spawner.getCurrent() != null && this.spawner.getSpawnLocation().distance(this.spawner.getCurrent().getBukkitEntity().getLocation()) > 15.0D)
            this.spawner.getCurrent().setLocation(this.spawner.getSpawnLocation().getX(), this.spawner.getSpawnLocation().getY(), this.spawner.getSpawnLocation().getZ(), 0.0F, 0.0F);

        if(hp-- <= 0) {
            heal(1, EntityRegainHealthEvent.RegainReason.REGEN);
            hp=5;
        }

        if ((double)this.getHealth() < (double)this.health * 0.7D) {
            if (this.debuff-- <= 0) {
                this.debuff = 160;
                this.debuff();
            }
        }

        if (this.getGoalTarget() != null) {
            boolean isSameWorld = this.getBukkitEntity().getLocation().getWorld() == this.getGoalTarget().getBukkitEntity().getLocation().getWorld();
            double distance = isSameWorld ? this.getBukkitEntity().getLocation().distance(this.getGoalTarget().getBukkitEntity().getLocation()) : 32.0D;
            if (distance <= 16.0D && this.getGoalTarget() != null) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.speed);
            else this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
        }

        super.tick();
    }

    @Override
    public void die() {
        if (this.spawner != null) this.spawner.iDead();

        if ((double)getRandom().nextFloat() <= 0.05D)
            this.getBukkitEntity().getLocation().getWorld().dropItem(getBukkitEntity().getLocation(), PrisonItem.getItem("newpickaxe_1"));

        if (this.killer != null) {
            Bukkit.broadcastMessage(Util.f("&4Древний слизень был повержен! Нападавшие получили его ценные сокровища!"));
            Map<String, Double> percents = Util.calculatePercents(this.attackers, this.t_damage);
            percents.keySet().forEach(s -> {
                Player p = Bukkit.getPlayerExact(s);
                PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
                double money = percents.get(s) * this.money;
                money = money < 0 ? 0 : money;
                pp.addGold(money);
                pp.addMob(EntityType.SLIME);
                Util.ps("Prison", p, "&fБитва с боссом &cДревний слизень&f завершена");
                Util.ps("Prison", p, "&fВы получили &6"+money+"$&f за нанесённый урон по боссу.");
            });
        }
        super.die();
    }

    public void debuff() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            List<org.bukkit.entity.Entity> near = getBukkitEntity().getNearbyEntities(7, 5, 7).stream().filter(e -> e instanceof Player).collect(Collectors.toList());
            if(near.size() > 0) {
                near.forEach(e -> {
                    Player p = (Player) e;
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 10));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 200));
                    p.damage(6.0D);
                });
            }
        });
    }

    public ChatComponentText name() {
        return Component.of("&cДревний слизень");
    }

}
