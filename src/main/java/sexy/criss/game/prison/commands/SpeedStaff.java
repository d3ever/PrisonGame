package sexy.criss.game.prison.commands;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.player.RMCSPlayer;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SpeedStaff {
    private static Set<Player> players;
    private static Set<String> questions;

    public static void register() {
        players = Sets.newHashSet();
        questions = Sets.newHashSet();
        sct();
        sc();
        help();
        ahelp();
    }

    private static void sct() {
        GENAPI.getCommandsAPI().register("sct", "/sct", RMCSPlayer::isHelper, (p, args) -> {
            boolean res;
            if(!players.contains(p.getPlayer())) {
                res = true;
                players.add(p.getPlayer());
            } else {
                res = false;
                players.remove(p.getPlayer());
            }

            String result = res ? "&aвключен" : "&cвыключен";
            Util.ps("персонал", p.getPlayer(), "&7Чат персонала был %s&7.", result);
        });
    }

    private static void sc() {
        GENAPI.getCommandsAPI().register("sc", "/sc [сообщение]", RMCSPlayer::isHelper, (p1, args) -> {
            if(!players.contains(p1.getPlayer())) Util.ps("персонал", p1.getPlayer(), "&cВы выключили персональный чат.");
            else {
                Player p = p1.getPlayer();
                if(args.length == 0) {
                    Util.ps("персонал", p, "&a/sc [сообщение] &7- чтобы отправить сообщение");
                } else {
                    StringBuilder str = new StringBuilder();
                    Arrays.asList(args).forEach(s -> str.append(" ").append(s));
                    String m = str.toString().substring(1);
                    Bukkit.getOnlinePlayers().stream().filter(t -> RPlayer.get(t).hasGroup(PermissionGroup.HELPER)).forEach(t -> {
                        Util.ps("персонал", t, RPlayer.get(p).getShortPrefix()+p.getName() + "&7: "+m);
                    });
                }
            }
        });
    }

    private static void help() {
        GENAPI.getCommandsAPI().register("helpop", "/helpop [сообщение]", RMCSPlayer::isPlayer, (p, args) -> {
            if(args.length == 0) {
                Util.ps("helpop", p.getPlayer(), "&a/helpop [сообщение]");
            } else {
                if(questions.contains(p.getPlayer().getName())) Util.ps("helpop", p.getPlayer(), "&7Вы уже задали вопрос, дождитесь ответа.");
                else {
                    StringBuilder str = new StringBuilder();
                    Arrays.asList(args).forEach(s -> str.append(" ").append(s));
                    String m = str.toString().substring(1);
                    List<Player> s_l = Bukkit.getOnlinePlayers().stream().filter(t -> RPlayer.get(t).hasGroup(PermissionGroup.HELPER)).collect(Collectors.toList());
                    if (s_l.size() <= 0)
                        Util.ps("helpop", p.getPlayer(), "&7На данный момент никого из персонала нет на сервере.");
                    else {
                        Util.ps("helpop", p.getPlayer(), "&7" + m);
                        s_l.forEach(t -> Util.ps("helpop", t, p.getShortPrefix()+p.getPlayer().getName() + "&7: " + m));
                        questions.add(p.getPlayer().getName());
                    }
                }
            }
        });
    }

    private static void ahelp() {
        GENAPI.getCommandsAPI().register("ahelpop", "/ahelpop [игрок] [сообщение]", RMCSPlayer::isHelper, (p, args) -> {
            if(args.length < 2) {
                Util.ps("ahelpop", p.getPlayer(), "&a/ahelpop [игрок] [сообщение] &7- ответить игроку");
            } else {
                Player t = Bukkit.getPlayerExact(args[0]);
                if(t == null) {
                    if(questions.contains(args[0])) {
                        Util.ps("ahelpop", p.getPlayer(), "&7Игрок &6%s&7 не в сети. Его вопрос был удалён.", args[0]);
                        questions.remove(args[0]);
                    } else Util.ps("ahelpop", p.getPlayer(), "&7Игрок &6%s&7 не в сети и он не задавал вопрос.", args[0]);
                } else {
                    if(questions.contains(t.getName())) {
                        questions.remove(t.getName());
                        StringBuilder str = new StringBuilder();
                        for(int i = 1; i < args.length; i++) {
                            str.append(" ").append(args[i]);
                        }
                        String m = str.toString().substring(1);
                        {
                            //TODO: user section
                            t.playSound(t.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            Util.ps("ahelpop", t, "&6Поддержка ответила вам на ваш вопрос.");
                            Util.ps("ahelpop", t, "&6Сообщение от поддержки:");
                            Util.s(t, p.getName()+"&7: "+m);
                        }
                        {
                            //TODO: staff section
                            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_STRIDER_HAPPY, 1, 1);
                            Util.ps("ahelpop", p.getPlayer(), "&6Вы ответили игроку &a%s&6 на его вопрос.", t.getName());
                            Util.ps("ahelpop", p.getPlayer(), "&6Сообщение которое вы отправили:");
                            Util.s(p.getPlayer(), p.getShortPrefix()+p.getName() +"&7: " + m);
                        }
                    }
                }
            }
        });
    }

}
