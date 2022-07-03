package sexy.criss.game.prison.commands;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.bosses.EntityTypes;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.game.prison.configuration.Configs;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.api.player.RMCSPlayer;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.LoreCreator;
import sexy.criss.gen.util.Util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SpeedCommands {

    public static void init() {
        lc_command();
        test_command();
        val_command();
        items();
        prison_command();
        spawner_command();
        reset_command();
    }

    private static void lc_command() {
        GENAPI.getCommandsAPI().register("lc", "/lc [key]", RMCSPlayer::isDeveloper, (p, args) -> {
            if(args.length == 0) Util.s(p.getPlayer(), "Usage: /lc [key]");
            else {
                Util.s(p.getPlayer(), Util.getColoredText("&7Локация "+args[0]+" была обновлена"));
                Main.getDatFile().parse(args[0], Util.getLoc(p.getPlayer().getLocation()));
            }
        });
    }

    private static void test_command() {
        GENAPI.getCommandsAPI().register("test", "/test <arg>", RMCSPlayer::isDeveloper, (p, args) -> {
            Spawner spawner = new Spawner(p.getPlayer().getLocation(), EntityTypes.WITCH, 100);
            spawner.update();
        });
    }

    private static void items() {
        GENAPI.getCommandsAPI().register("items", "/items", RMCSPlayer::isAdministrator, (p, args) -> {
            if(args.length == 0) {
                DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0Доступные предметы", 6);
                PrisonItem.items.values().forEach(prisonItem -> inv.addItem(new DynamicItem(prisonItem.getUsableItem(), (p1, clickType, slot) -> p.getPlayer().getInventory().addItem(prisonItem.getUsableItem()))));
                inv.open(p.getPlayer());
            } else {
                String k = args[0];
                if(PrisonItem.items.containsKey(k)) {
                    p.getPlayer().getInventory().addItem(PrisonItem.items.get(k).getUsableItem());
                } else {
                    Util.ps("Prison", p.getPlayer(), "&fТакого ключа нет.");
                }
            }
        });
    }

    private static void val_command() {
        GENAPI.getCommandsAPI().register("val", "/val", RMCSPlayer::isDeveloper, ((p, args) -> {
            PrisonPlayer pp = PrisonPlayer.getPlayer(p.getPlayer().getUniqueId());
            boolean b = pp.hasAutoSell();
            if(pp.hasAutoSell()) pp.removeAutoSell();
            else pp.grantAutoSell();
            Util.ps("Prison", p.getPlayer(), b ? "removed" : "granted");
        }));
    }

    private static void prison_command() {
        GENAPI.getCommandsAPI().register("prison", "/prison", RMCSPlayer::isPlayer, (p1, args) -> {
            Player p = p1.getPlayer();
            RPlayer rp = RPlayer.get(p);
            new LoreCreator()
                    .addLines(Lists.newArrayList(
                            "&6Доступные команды:",
                            "&a/blocks &7- открыть меню блоков",
                            "&a/mine &7- открыть меню шахт",
                            "&a/faction &7- открыть меню выбора фракции",
                            "&a/base &7- телепортироваться на базу",
                            "&a/fleave &7- покинуть фракцию за 1000$",
                            "&a/kit &7- открыть меню наборов",
                            "&a/start &7- получить набор новичка",
                            "&a/promocode [промокод] &7- использовать промокод",
                            "&a/gift [игрок] &7- передать игроку предмет",
                            "&a/pay [игрок] [сумма] &7- передать игроку деньги",
                            "&a/balance &7- посмотреть баланс",
                            "&a/donate &7- открыть меню валютного магазина",
                            "&a/helpop [вопрос] &7- задать вопрос персоналу"))
                    .addLineIf(rp.hasGroup(PermissionGroup.VIP), "&a/shop &7- открыть меню магазина")
                    .addLinesIf(rp.hasGroup(PermissionGroup.HELPER), Lists.newArrayList(
                            "&3/sct &7- выключить чат персонала",
                            "&3/sc [сообщение] &7- писать в чат персонала",
                            "&3/warn [игрок] &7- выдать игроку предупреждение",
                            "&3/mute [игрок] [время] [причина] &7- выдать наказание игроку",
                            "&3/ahelpop [игрок] [сообщение] &7- ответить на последнее сообщение"))
                    .addLinesIf(rp.hasGroup(PermissionGroup.MODERATOR), Lists.newArrayList(
                            "&5/ban [игрок] [время] [причина] &7- выдать наказание игроку",
                            "&5/unwarn [игрок] &7- снять наказание с игрока"
                            ))
                    .addLinesIf(rp.hasGroup(PermissionGroup.ADMINISTRATOR), Lists.newArrayList(
                            "&c/spawner &7- добавить точку спавна",
                            "&c/r_boss &7- воскресить всех боссов",
                            "&c/base [faction] &7- телепортироваться на базу фракции",
                            "&c/setlockchest &7- уставить сундук",
                            "&c/setbase [faction] &7- установить базу для фракции",
                            "&c/stats set [player] &7- установить статистику игроку",
                            "&c/lc [key] &7- установить локацию по ключу")).build().forEach(s -> Util.ps("Prison", p, s));
        });
    }

    private static void spawner_command() {
        GENAPI.getCommandsAPI().register("spawner", "/spawner [type] [interval]", RMCSPlayer::isAdministrator, (p, args) -> {
            if(args.length < 2) Util.ps("Prison", p.getPlayer(), "&c/spawner [type] [interval]");
            else {
                EntityTypes type;
                int interval;
                try {
                    type = EntityTypes.valueOf(args[0]);
                }catch (Exception ex) {
                    Util.ps("Prison", p.getPlayer(), "&7Доступные монстры:");
                    Util.s(p.getPlayer(), "&a"+ Arrays.toString(EntityTypes.values()));
                    return;
                }
                try {
                    interval = Integer.parseInt(args[1]);
                }catch (Exception ex) {
                    Util.ps("Prison", p.getPlayer(), "&7Введите интервал.");
                    return;
                }
                Location l = p.getPlayer().getLocation();
                FileConfiguration c = Configs.MOBS.getConfig();
                String n = getName(type.getName().toLowerCase(), c);
                ConfigurationSection s = c.createSection(n);
                s.set("type", type.name());
                s.set("world", l.getWorld().getName());
                s.set("x", l.getX());
                s.set("y", l.getY());
                s.set("z", l.getZ());
                s.set("interval", interval);
                Configs.MOBS.save();
                (new Spawner(l, type, interval)).update();
                Util.ps("Prison", p.getPlayer(), "&fТочка спавна &6%s&7 с интерваллом &6&d&f была добавлена", n, interval);
            }
        });
    }

    private static void reset_command() {
        GENAPI.getCommandsAPI().register("r_boss", "/r_boss", RMCSPlayer::isAdministrator, (p, args) -> {
            Spawner.spawners.values().forEach(Spawner::update);
            Util.ps("Prison", p.getPlayer(), "&6Все монстры были обновленны.");
        });
    }

    private static String getName(String id, FileConfiguration c) {
        AtomicInteger last = new AtomicInteger(0);
        String finalId = id;
        c.getKeys(false).forEach(s -> {
            if(s.startsWith(finalId)) last.set(Integer.parseInt(s.replaceAll("\\D+", "")));
        });
        id = id.concat(String.valueOf(last.get()+1));
        return id;
    }

}
