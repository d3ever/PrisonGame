package sexy.criss.game.prison.commands;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.commands.manager.AbstractCommand;
import sexy.criss.game.prison.language.Reference;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.promocodes.Promo;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.util.Util;

import java.util.List;

public class PromocodeCommand extends AbstractCommand {
    private List<String> bad_ids;

    public PromocodeCommand() {
        super("promocode", Lists.newArrayList("promo", "promote"));
        this.unavailableFromConsole();
        this.bad_ids = Lists.newArrayList("test", "create", "info", "name", "id");
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        switch (args.length) {
            case 0:
                p.sendMessage(Reference.COMMAND_USAGE.get(cmd + " <промокод>", "чтобы ввести промокод и получить вознаграждение."));
                break;
            case 1:
                    String id = args[0].toLowerCase();
                    if (id.equals("create")) {
                        p.sendMessage(Reference.COMMAND_USAGE.get(cmd + " create <промокод>", "чтобы создать промокод."));
                        return;
                    }
                    if (bad_ids.contains(id)) {
                        p.sendMessage(Reference.PROMO_ID_FAIL.get(args[0]));
                        return;
                    }
                    if (Promo.getById(id) == null) {
                        p.sendMessage(Reference.PROMO_NOT_EXIST.get(args[0]));
                        return;
                    }
                    Promo promo = Promo.getById(id);

                    if (promo.getOwn().equalsIgnoreCase(p.getName())) {
                        p.sendMessage(Reference.PROMO_YOU_IS_OWN.get());
                        return;
                    }

                    if (pp.hasPromo(id)) {
                        p.sendMessage(Reference.PROMO_USED.get(args[0]));
                        return;
                    }

                    pp.addPromo(id);
                    pp.addGold(promo.getReward());
                    PrisonPlayer tar = PrisonPlayer.getPlayer(Bukkit.getOfflinePlayer(promo.getOwn()).getUniqueId());
                    tar.setMoney(tar.getBalance() + promo.getRewardOwn());
                    tar.save();
                    p.sendMessage(Reference.PROMO_USE.get(args[0], promo.getReward()));
                    break;
            default:
                if(!RPlayer.get(p).isAdministrator()) {
                    p.sendMessage(Reference.PERMISSION.get());
                    return;
                }
                if(!args[0].equalsIgnoreCase("create")) {
                    p.sendMessage(Reference.COMMAND_USAGE.get(cmd + " create <промокод>", "чтобы создать промокод"));
                    return;
                }
                String n = args[1].toLowerCase();
                if(Promo.getById(n) != null) {
                    p.sendMessage(Reference.PROMO_CREATE_FAILURE.get(args[1]));
                    return;
                }

                new Promo(n, p.getName(), 10, 5);
                pp.addPromo(n);
                p.sendMessage(Reference.PROMO_CREATE.get(args[1]));
                break;
        }
    }

}
