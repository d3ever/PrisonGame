package sexy.criss.game.prison.listener.players.damage;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.passives.PassiveType;
import sexy.criss.gen.util.Util;
import sexy.criss.gen.util.UtilPlayer;

import java.util.Random;

public class PlayerDamageHandler extends SexyListener {

    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        if(e.getDamager().getType() != EntityType.PLAYER || e.getEntity().getType() != EntityType.PLAYER) return;
        Player target = (Player) e.getEntity();
        Player damager;
        if(e.getDamager() instanceof Projectile) damager = (Player) ((Projectile) e.getDamager()).getShooter();
        else damager = (Player) e.getDamager();
        PrisonPlayer pp = PrisonPlayer.getPlayer(damager.getUniqueId());
        PrisonPlayer pd = PrisonPlayer.getPlayer(target.getUniqueId());
        if((pp.hasFraction() && pd.hasFraction()) && (pp.getFraction().equals(pd.getFraction()))) {
            if(new Random().nextBoolean()) Util.s(damager, "&cОгонь по своим невозможен.");
            e.setCancelled(true);
        }
        if(!e.isCancelled()) {
            if (pd.hasPassive(PassiveType.AGILITY, 1)) {
                if (new Random().nextInt(100) + 1 <= pd.getPassive(PassiveType.AGILITY) * 3) {
                    Util.s(target, "&6Вы уклонились от удара.");
                    Util.s(damager, "&cВы промахнулись.");
                }
            }
            if(pp.hasPassive(PassiveType.VAMPIRE, 1)) {
                if(new Random().nextInt(100) + 1 <= pp.getPassive(PassiveType.VAMPIRE) * 2) {
                    double health = damager.getMaxHealth() >= damager.getHealth() ? damager.getMaxHealth() : damager.getHealth()+2;
                    damager.setHealth(health);
                }
            }
            if (pp.hasPassive(PassiveType.CURSE, 1)) {
                if(new Random().nextInt(100) + 1 <= pp.getPassive(PassiveType.CURSE) * 2) {
                    PotionEffectType[] effects = new PotionEffectType[]{PotionEffectType.CONFUSION, PotionEffectType.SLOW, PotionEffectType.BLINDNESS, PotionEffectType.WEAKNESS};
                    target.addPotionEffect(new PotionEffect(effects[new Random().nextInt(effects.length)], 200, 0, true, true, true));
                    Util.s(damager, "&5Вы наложили проклятие.");
                }
            }
            double damage = e.getDamage() + pp.getLevelClass().getExtraDMG() + (pp.hasPassive(PassiveType.STRENGH, 1) ? (double)(pp.getPassive(PassiveType.STRENGH) * 4 / 100) : 0);
            e.setDamage(damage);
        }
    }

    @Override
    public String getName() {
        return "Player listener";
    }

    @Override
    public String getType() {
        return "damage";
    }
}
