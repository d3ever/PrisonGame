package sexy.criss.game.prison.notify;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.configuration.Configs;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.passives.PassiveType;

import java.util.*;

public class Notify extends SexyListener {
    private static Random r = new Random();
    private static List<String> disabled;
    private static Map<String, Integer> counter;
    private static Map<String, Integer> toilet;
    private static final String modifer = "Мыло";
    private static List<String> first;
    private static List<String> second;
    private static Set<Material> water = Sets.newHashSet(Material.WATER);

    public static void load() {
        counter = Maps.newHashMap();
        toilet = Maps.newHashMap();
        if(Configs.CONFIG.getConfig().contains("notify-bypass"))
            disabled = Configs.CONFIG.getConfig().getStringList("notify-bypass");
        else disabled = Lists.newArrayList();

        first = Arrays.asList(
                "Фууу! Вы воняете! Идите и помойтесь уже, наконец!",
                "Как же от вас несет! Помойтесь!",
                "Что это за вонь? О, господи, сходите в ванну!",
                "Вам бы не помешало сходить помыться..");
        second = Arrays.asList("О-ой! Кажется, мне нужно в туалет!",
                "Я хочу в туалет! Сейчас!",
                "Мне нужно.. в туалет. Давай, быстрее!",
                "Где унитаз? Быстро, быстро!!");

        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(Notify::push);
            }
        }.runTaskTimer(Main.getInstance(), 0, 1200);

        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if(water.contains(p.getLocation().getBlock().getType())) {
                        if (p.hasPotionEffect(PotionEffectType.CONFUSION)) {
                            p.sendMessage(ChatColor.BLUE + "Вы помылись и чувствуете себя чистеньким.");
                            p.removePotionEffect(PotionEffectType.CONFUSION);
                        }

                        counter.remove(p.getName());
                    }
                });
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    public static void disable(Player p) {
        if(!disabled.contains(p.getName()))
        disabled.add(p.getName());
    }

    public static void push(Player p) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        String name = p.getName();
        Integer amount = counter.get(name);
        if (amount == null) {
            amount = 0;
        }

        counter.put(name, amount = amount + 1);
        boolean doIt = true;
        int needs = 0;
        if (pp.getPassive(PassiveType.NEEDS) > 0) needs = pp.getPassive(PassiveType.NEEDS) * 5;
        if (amount >= 10 + needs) {
            if (isDisabled(name)) return;

            for (ItemStack is : p.getInventory()) {
                try {
                    if (is.getItemMeta().getDisplayName().contains(modifer)) {
                        if (pp.getPassive(PassiveType.NEEDS) > 0) needs = pp.getPassive(PassiveType.NEEDS) * 10;

                        doIt = amount >= 20 + needs;
                        break;
                    }
                } catch (Exception ignored) {
                }
            }

            if (doIt) {
                p.sendMessage(ChatColor.DARK_GREEN + getRandomFirst());
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2147483647, 2));
            }
        }

        amount = toilet.get(name);
        if (amount == null) amount = 0;

        toilet.put(name, amount = amount + 1);
        if (amount >= 15 + pp.getPassive(PassiveType.NEEDS) * 5) {
            if (isDisabled(name)) return;

            p.sendMessage(ChatColor.DARK_PURPLE + getRandomSecond());
            p.removePotionEffect(PotionEffectType.SLOW);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2147483647, 0));
        }
    }

    @EventHandler
    public void click(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(e.getClickedBlock().getType().equals(Material.LEVER)) {
                if (!p.hasPotionEffect(PotionEffectType.SLOW)) {
                    p.sendMessage(ChatColor.RED + "Вы еще не хотите в туалет!");
                    return;
                }

                p.removePotionEffect(PotionEffectType.SLOW);
                toilet.remove(p.getName());
                boolean used = false;

                for(int i = 0; i < p.getInventory().getSize(); ++i) {
                    ItemStack is = p.getInventory().getItem(i);
                    if (is != null && is.getType().equals(Material.PAPER)) {
                        int amount = is.getAmount();
                        if (amount == 1) {
                            p.getInventory().setItem(i, null);
                        } else {
                            --amount;
                            is.setAmount(amount);
                            p.getInventory().setItem(i, is);
                        }

                        p.sendMessage(ChatColor.GREEN + "Вы использовали 1 бумагу.");
                        used = true;
                        break;
                    }
                }

                if (!used) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2147483647, 2));
                    p.sendMessage(ChatColor.DARK_GREEN + "Ох, как хорошо! Ну а теперь подмыться в душик!");
                }
            }
        }
    }

    public static boolean isDisabled(String name) {
        return disabled.contains(name);
    }

    public static void unload() {
        FileConfiguration conf = Configs.CONFIG.getConfig();
        if(!conf.contains("notify-bypass")) conf.createSection("notify-bypass");
        conf.set("notify-bypass", disabled);
        Configs.CONFIG.save();
    }

    private static String getRandomFirst() {
        return first.get(r.nextInt(first.size()));
    }

    private static String getRandomSecond() {
        return second.get(r.nextInt(second.size()));
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }
}
