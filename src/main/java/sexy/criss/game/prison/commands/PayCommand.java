package sexy.criss.game.prison.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

public class PayCommand extends SpigotCommand {

    public PayCommand() {
        super("pay", PermissionGroup.PLAYER, "/pay [игрок] [сумма]");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        if(args.length == 2) {
            double money;
            Player target = Bukkit.getPlayerExact(args[0]);
            if(target == null) {
                Util.ps("Prison", p, "&fИгрока &6%s&f нет в сети.");
                return;
            }
            if(!StringUtils.isNumeric(args[1])) {
                Util.ps("Prison", p, "&fВы должны ввести сумму в цифрах. &6%s&f - не является числом.", args[1]);
                return;
            }
            money = Integer.parseInt(args[1]);
            if(!pp.hasMoney(money)) {
                Util.ps("Prison", p, "&fНа вашем балансе нет такой суммы.");
                return;
            }
            if(money > 1500) {
                Util.ps("Prison", p, "&fНа вашем аккаунте лимит переводов: &61500");
                return;
            }
            PrisonPlayer tar = PrisonPlayer.getPlayer(target.getUniqueId());
            tar.addGold(money);
            pp.takeGold(money);
            Util.ps("Prison", p, "&fВы перевели игроку &6%s&f золото в размере &6%.2f", target.getName(), money);
            Util.ps("Prison", target, "&fИгрок &6%s&f перевёл вам золото в размере &6%.2f", p.getName(), money);
        } else this.notEnoughArguments(sender, usageMessage);
    }
}
