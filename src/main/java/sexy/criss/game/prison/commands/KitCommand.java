package sexy.criss.game.prison.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.configuration.Configs;
import sexy.criss.game.prison.configuration.Configuration;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.ClickAction;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.LoreCreator;
import sexy.criss.gen.util.Util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class KitCommand extends SpigotCommand {
    private static Map<String, Map<String, Integer>> time_map = Maps.newHashMap();

    public KitCommand() {
        super("kit", PermissionGroup.PLAYER, "/kit");
        this.unavailableFromConsole();
    }

    public static void load() {
        time_map = Maps.newHashMap();
        time_map.put("player", Maps.newHashMap());
        time_map.put("vip", Maps.newHashMap());
        time_map.put("vipp", Maps.newHashMap());
        time_map.put("prem", Maps.newHashMap());
        time_map.put("premp", Maps.newHashMap());
        time_map.put("sponsor", Maps.newHashMap());
        time_map.put("beta", Maps.newHashMap());
        time_map.put("yt", Maps.newHashMap());
        time_map.put("mod", Maps.newHashMap());
        time_map.put("help", Maps.newHashMap());
        time_map.put("build", Maps.newHashMap());
        time_map.put("admin", Maps.newHashMap());
        time_map.put("own", Maps.newHashMap());
        time_map.put("dev", Maps.newHashMap());
        time_map.put("special", Maps.newHashMap());
        if(!Configs.CONFIG.getConfig().contains("times")) Configs.CONFIG.getConfig().createSection("times");
        Configs.CONFIG.getConfig().getConfigurationSection("times").getKeys(false).forEach(s -> {
            Map<String, Integer> map = Maps.newHashMap();
            Configs.CONFIG.getConfig().getConfigurationSection("times."+s).getKeys(false).forEach(s1 -> {
                time_map.put(s, new HashMap<>(){{put(s1, Configs.CONFIG.getConfig().getInt("times."+s+"."+s1));}});
            });
        });
        new BukkitRunnable(){
            @Override
            public void run() {
                time_map.keySet().forEach(s -> {
                    try {
                        time_map.get(s).forEach((s1, aLong) -> {
                            if(time_map.get(s).get(s1) - 1 <= 0) time_map.get(s).remove(s1); else
                                time_map.get(s).put(s1, time_map.get(s).get(s1) - 1);
                        });
                    }catch (Exception ignored) {

                    }
                });
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
    }

    public static void unload() {
        FileConfiguration conf = Configs.CONFIG.getConfig();
        if(!conf.contains("times")) conf.createSection("times");
        ConfigurationSection s = conf.getConfigurationSection("times");
        time_map.forEach((s1, stringLongMap) -> {
            ConfigurationSection sec = s.contains(s1) ? s.getConfigurationSection(s1) : s.createSection(s1);
            stringLongMap.forEach(sec::set);
        });
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        toPlayer(p);
    }

    public static void toPlayer(Player p) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        List<String> l = Lists.newArrayList("", "&7Нажмите&e ЛКМ&7, чтобы использовать");
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("Все наборы", 4);
        new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    update(2, 1, Material.WOODEN_AXE, PermissionGroup.PLAYER, p, inv, "player", 1, (p1, clickType, slot) -> {
                        if(time_map.get("player").containsKey(p.getName()) && Util.getTime() - time_map.get("player").get(p.getName()) >= 5) removeMap("player", p.getName());
                        if(!time_map.get("player").containsKey(p.getName())) {
                            updateMap("player", p.getName(), 3600);
                            p.getInventory().addItem(PrisonItem.getPrisonItem("axe_2").getUsableItem());
                            p.getInventory().addItem(Util.createItem(Material.BREAD, "&7Хлеб", Lists.newArrayList(), 32, (short) 0));
                        } else notTime(p, time_map.get("player").get(p.getName()));
                    });

                    update(2, 2, Material.STONE_AXE, PermissionGroup.VIP, p, inv, "vip", 10, 86400);
                    update(2, 3, Material.IRON_AXE, PermissionGroup.VIP_PLUS, p, inv, "vipp", 20, 86400);
                    update(3, 1, Material.DIAMOND_AXE, PermissionGroup.PREMIUM, p, inv, "prem", 30, 86400);
                    update(3, 2, Material.GOLDEN_AXE, PermissionGroup.PREMIUM_PLUS, p, inv, "premp", 40, 86400);
                    update(3, 3, Material.NETHERITE_AXE, PermissionGroup.SPONSOR, p, inv, "sponsor", 80, 86400);

                    update(2, 9, Material.WOODEN_PICKAXE, PermissionGroup.BETA, p, inv, "beta", 15, 86400);
                    update(3, 9, Material.STONE_PICKAXE, PermissionGroup.YOUTUBE, p, inv, "yt", 30, 86400);

                    update(2, 5, Material.WOODEN_PICKAXE, PermissionGroup.BUILDER, p, inv, "build", 20, 86400);
                    update(2, 6, Material.WOODEN_PICKAXE, PermissionGroup.SPECIAL, p, inv, "special", 20, 86400);
                    update(2, 7, Material.WOODEN_PICKAXE, PermissionGroup.HELPER, p, inv, "help", 25, 86400);
                    update(3, 5, Material.WOODEN_PICKAXE, PermissionGroup.MODERATOR, p, inv, "mod", 50, 86400);
                    update(3, 6, Material.WOODEN_PICKAXE, PermissionGroup.ADMINISTRATOR, p, inv, "admin", 200, 86400);
                    update(3, 7, Material.WOODEN_PICKAXE, PermissionGroup.OWNER, p, inv, "own", 500, 86400);
                }catch (Exception ex) {
                    ex.printStackTrace();
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
        inv.open(p);
    }

    private static void update(int x, int y, Material m, PermissionGroup perm, Player p, DynamicInventory inv, String k, int time, ClickAction action) {
        boolean b = time_map.containsKey(k) && time_map.get(k).containsKey(p.getName());
        String t = b ? "&8(&6" + getTime(time_map.get(k).get(p.getName())) + "&8)" : "";
        String name = getPrefix(perm);
        name = name.length() > 0 ? name : "ОБЫЧНЫЙ";
        name = name + " " + t;
        inv.addItem(x, y, new DynamicItem(m, name, new LoreCreator()
                .addLinesIf(b, Lists.newArrayList("", "&cНабор временно недоступен", "", "&fДействует ограничение"))
                .addEmpty()
                .addLines(Lists.newArrayList(
                        "&fВы получите",
                        "  &f↪ &6Деревянный топор &fx1",
                        "  &f↪ &6Хлеб &fx16"
                ))
                .addEmpty()
                .addLineIf(!b, "&6Нажмите, чтобы получить набор")
                .build(), action));
    }

    private static String getTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, sec);
    }

    private static void update(int x, int y, Material m, PermissionGroup perm, Player p,DynamicInventory inv, String k, int reward, int time) {
        boolean b = time_map.containsKey(k) && time_map.get(k).containsKey(p.getName());
        String t = b ? "&8(&6" + getTime(time_map.get(k).get(p.getName())) + "&8)" : "";
        String name = getPrefix(perm);
        name = name.length() > 0 ? name : "ОБЫЧНЫЙ";
        name = name.equals("ОБЫЧНЫЙ") ? name + " " + t : name + t;
        inv.addItem(x, y, new DynamicItem(m, name, new LoreCreator().addEmpty()
                .addLinesIf(b, Lists.newArrayList("", "&cНабор временно недоступен", "", "&fДействует ограничение"))
                .addEmpty()
                .addLines(Lists.newArrayList(
                        "&fВы получите",
                        "  &f↪ &6Монеты " + reward
                ))
                .addEmpty()
                .addLineIf(!b, "&6Нажмите, чтобы получить набор")
                .build(), (p1, clickType, slot) -> addReward(k, p, reward, time, perm)));
    }

    private static String getPrefix(PermissionGroup pg) {
        return switch (pg) {
            case OWNER -> "&4Владелец";
            case ADMINISTRATOR -> "&cАдминистратор";
            case PREMIUM_PLUS -> "&6Премиум &cплюс";
            case PREMIUM -> "&2Премиум";
            case VIP_PLUS -> "&3Вип плюс";
            case VIP -> "&6Вип";
            case HELPER -> "&bПомощник";
            case DEVELOPER -> "&c&lРазработчик";
            case BUILDER -> "&eСтроитель";
            case BETA -> "&aБета";
            case SPECIAL -> "&bСпециальный";
            case YOUTUBE -> "&f&lYou&c&lTube";
            case SPONSOR -> "&6&l&nСпонсор";
            case MODERATOR -> "&5Модератор";
            default -> "";
        };
    }

    public static void addReward(String key, Player p, int gold, int hours, PermissionGroup group) {
        RPlayer rp = RPlayer.get(p);
        if(!rp.hasGroup(group)) {
            Util.ps("Prison", p, "NOT_ENOUGH_PERMISSIONS_FOR_COMMAND");
            Util.ps("Prison", p, "COMMAND_PERMISSION_SPECIFICATION", group.getNormalName());
            p.closeInventory();
            return;
        }
        if (avialable(key, p, hours)) {
            Util.s(p, "&fВы использовали набор группы %s&f и получили &6%df&f золота", group.getUncoloredLongPrefix(), gold);
            PrisonPlayer.getPlayer(p.getUniqueId()).addGold(gold);
            updateMap(key, p.getName());
            return;
        }
        if(!avialable(key, p, hours)) {
            if(!time_map.get(key).containsKey(p.getName())) updateMap(key, p.getName());
            Util.s(p, "&fСледующий набор можно будет получить: &6" + getTimeString(hours));
        }
    }

    public static void removeMap(String key, String s) {
        Map<String, Integer> map = time_map.get(key);
        map.remove(s);
        time_map.put(key, map);
    }

    public static void updateMap(String key, String s) {
        Map<String, Integer> map = time_map.get(key);
        map.put(s, 86400);
        time_map.put(key, map);
    }

    public static void updateMap(String key, String s, int seconds) {
        Map<String, Integer> map = time_map.get(key);
        map.put(s, seconds);
        time_map.put(key, map);
    }

    private static String getTimeString(int l) {
        return new SimpleDateFormat("kk:mm:ss").format(l);
    }

    public static Date pattern(int days, int hours, int minutes, int seconds) {
        Calendar cal = Calendar.getInstance();
        if(days != 0) cal.set(Calendar.DAY_OF_YEAR, days);
        if(hours != 0) cal.set(Calendar.HOUR_OF_DAY, hours);
        if(minutes != 0) cal.set(Calendar.MINUTE, minutes);
        if(seconds != 0) cal.set(Calendar.SECOND, seconds);
        return cal.getTime();
    }

//    public static boolean avialable(String key, Player p, long time, int seconds) {
//        return !time_map.get(key).containsKey(p.getName()) || time_map.get(key).containsKey(p.getName()) && (time - time_map.get(key).get(p.getName()) >= seconds);
//    }

    public static boolean avialable(String key, Player p, long time) {
        return !time_map.containsKey(key) || !time_map.get(key).containsKey(p.getName()) || (time_map.containsKey(key) && time_map.get(key).containsKey(p.getName()) && time_map.get(key).get(p.getName()) <= 0);
    }

    public static void notTime(Player p, long time) {
        Util.s(p, "&7Набор можно будет получить через: %d", time);
    }
}
