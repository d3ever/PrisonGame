package sexy.criss.game.prison.commands;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.factions.Faction;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.Arrays;
import java.util.List;

public class addCommand extends SpigotCommand {

    public addCommand() {
        super("stats", PermissionGroup.OWNER, "/stats [set/reset] [игрок] [категория] [число]");
    }
    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        String target;
        switch (args.length) {
            case 3:
                if(args[0].equalsIgnoreCase("reset")) {
                    target = args[1];
                    PrisonPlayer.getPlayer(Bukkit.getOfflinePlayer(target).getUniqueId()).reset();
                    Util.ps("Prison", sender, "&fСтатистика игрока &6%s&f была сброшена.", target);
                }
                break;
            case 4:
                if ("set".equals(args[0].toLowerCase())) {
                    target = args[1];
                    String mod = args[2].toUpperCase();
                    int val = Integer.MAX_VALUE;
                    Modifer mode = null;
                    String f = null;
                    try {
                        val = Integer.parseInt(args[3]);
                    }catch (Exception ex) {
                        f = args[3].toLowerCase();
//                        Util.ps("sEdit", sender, "&7Введите число! %s - не является числом", args[3]);
                    }
                    if(Faction.fromId(f) == null && val == Integer.MAX_VALUE) {
                        List<String> fac = Lists.newArrayList();
                        Arrays.asList(Faction.values()).forEach(x -> fac.add(x.getId()));
                        Util.ps("Prison", sender, "&fВозможные фракции: &6"+ Arrays.toString(fac.toArray()) + "&f.");
                        return;
                    }
                    try {
                        mode = Modifer.valueOf(mod);
                    }catch (Exception ex) {
                        Util.ps("Prison", sender, "&fВозможные категории: &6"+ Arrays.toString(Modifer.values()) + "&f.");
                        return;
                    }

                    modifer(mode, val, PrisonPlayer.getPlayer(Bukkit.getOfflinePlayer(target).getUniqueId()), f);
                    Util.ps("Prison", sender, "&fСтатистика &6%s &fигроку &6%s &fбыла установлена", mod, target);
                    Util.ps("Prison", sender, "&fс новым значением &6%s", f != null ? f : String.valueOf(val));
                } else {
                    this.notEnoughArguments(sender, usageMessage);
                }
                break;
            default:
                this.notEnoughArguments(sender, usageMessage);
        }
    }

    private enum Modifer {
        MONEY, FACTION, EXP, LEVEL, KILLS, DEATHS, TOTALBLOCKS, ACCESS;
    }

    private void modifer(Modifer mod, int val, PrisonPlayer p, String f) {
        String sum = String.valueOf(val);
        char opper = sum.charAt(0);
        int n = opper == '+' || opper == '-' ? Integer.parseInt(sum.substring(1)) : 0;
        boolean b = n != 0;
        switch (mod) {
            case MONEY:
                if(!b) p.setMoney(val);
                else {
                    switch (opper) {
                        case '+':
                            p.setMoney(p.getBalance()+n);
                            break;
                        case '-':
                            p.setMoney(p.getBalance() >= n ? p.getBalance()-n : 0);
                            break;
                    }
                }
                break;
            case EXP:
                if(!b) p.setExp(val);
                else {
                    switch (opper) {
                        case '+':
                            p.setExp(p.getExp()+n);
                            break;
                        case '-':
                            p.setExp(p.getExp() >= n ? p.getExp()-n : 0);
                            break;
                    }
                }
                break;
            case KILLS:
                    if (!b) p.setKills(n);
                    else {
                        switch (opper) {
                            case '+':
                                p.setKills(p.getKills() + n);
                                break;
                            case '-':
                                p.setKills(p.getKills() >= n ? p.getKills() - n : 0);
                                break;
                        }
                    }
                break;
            case LEVEL:
                if(!b) p.setLevel(val);
                else {
                    switch (opper) {
                        case '+':
                            p.setLevel(p.getLevel()+n);
                            break;
                        case '-':
                            p.setLevel(p.getLevel() >= n ? p.getLevel()-n : 0);
                            break;
                    }
                }
                break;
            case ACCESS:
                if(!b) {
                    p.getAccess().clear();
                    p.getAccess().add(n);
                } else {
                    switch (opper) {
                        case '+':
                            p.getAccess().add(n);
                            break;
                        case '-':
                            p.getAccess().remove(n);
                            break;
                    }
                }
                break;
            case DEATHS:
                if(!b) p.setDeaths(val);
                else {
                    switch (opper) {
                        case '+':
                            p.setDeaths(p.getDeaths()+n);
                            break;
                        case '-':
                            p.setDeaths(p.getDeaths() >= n ? p.getDeaths()-n : 0);
                            break;
                    }
                }
                break;
            case TOTALBLOCKS:
                if(!b) p.setTotalBlocks(val);
                else {
                    switch (opper) {
                        case '+':
                            p.setTotalBlocks(p.getTotalBlocks()+n);
                            break;
                        case '-':
                            p.setTotalBlocks(p.getTotalBlocks() >= n ? p.getTotalBlocks()-n : 0);
                            break;
                    }
                }
                break;
            case FACTION:
                if(Faction.fromId(f) == null) return;
                p.setFraction(Faction.fromId(f));
                break;
        }
    }
}
