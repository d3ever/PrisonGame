package sexy.criss.game.prison.random;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.gen.util.Util;

import java.util.Random;

public class RandomEffects extends BukkitRunnable {
    @Override
    public void run() {
        if(Bukkit.getOnlinePlayers().isEmpty()) return;
        Bukkit.getOnlinePlayers().forEach(p -> {
            String notify = "&3СЛУЧАЙНОЕ СОБЫТИЕ";
            String type;
            PotionEffect ef;
            Random r = new Random();
            switch (r.nextInt(5)) {
                case 1 -> {
                    type = "&6ПРЫЖКИ В ВЫСОТУ!";
                    ef = new PotionEffect(PotionEffectType.JUMP, 300, 1, true, true, true);
                }
                case 2 -> {
                    type = "&6ВРЕМЯ ДРАКИ";
                    ef = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 2, true, true, true);
                }
                case 3 -> {
                    type = "&6КТО СВЕТ ВЫКЛЮЧИЛ?";
                    ef = new PotionEffect(PotionEffectType.BLINDNESS, 200, 3, true, true, true);
                }
                case 4 -> {
                    type = "&6Сколько питаюсь в столовой, такого ещё не было";
                    ef = new PotionEffect(PotionEffectType.CONFUSION, 300, 1, true, true, true);
                }
                case 5 -> {
                    type = "&6Золотая лихорадка";
                    ef = new PotionEffect(PotionEffectType.FAST_DIGGING, 300, 2, true, true, true);
                }
                case 6 -> {
                    type = "&6Суставы болят, скорее всего на погоду";
                    ef = new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 4, true, true, true);
                }
                case 7 -> {
                    type = "&6Может перекусить в буфете?";
                    ef = new PotionEffect(PotionEffectType.HUNGER, 20 * 2, 1, true, true, true);
                }
                default -> {
                    type = "&6Максимальное непредсказуемо";
                    ef = new PotionEffect(PotionEffectType.values()[new Random().nextInt(PotionEffectType.values().length - 1)], 150, 4, true, true, true);
                }
            }
            p.addPotionEffect(ef);
            Lists.newArrayList("", notify, type, "").forEach(s -> Util.s(p, s));
        });

    }
}
