package sexy.criss.game.prison.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.commands.manager.AbstractCommand;
import sexy.criss.game.prison.language.Reference;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.factions.Faction;
import sexy.criss.game.prison.utilities.GuiCreator;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class FactionCommand extends AbstractCommand {

    public FactionCommand() {
        super("faction", "", Reference.COMMAND_USAGE.get("faction", "открыть интерфейс выбора фракций"), "fraction");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender s, String cmd, String[] args) {
        Player p = (Player) s;
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        boolean has = pp.hasFraction();

        if (args.length == 0) {
            toPlayer(p);
        } else {
            if (args[0].toLowerCase().equalsIgnoreCase("quit") || args[0].equalsIgnoreCase("leave")) {
                if (has) {
                    if (pp.hasMoney(1500)) {
                        p.sendMessage(Reference.FRACTION_QUIT.get(pp.getFraction().getName()));
                        pp.takeMoney(1500);
                        pp.setFraction(null);
                    } else {
                        p.sendMessage(Reference.BALANCE_ENOUGHT.get(1500 - pp.getBalance()));
                    }
                }
            } else toPlayer(p);
        }
    }

    public void toPlayer(Player p) {
        if(PrisonPlayer.getPlayer(p.getUniqueId()).hasFraction()) {
            p.sendMessage(Reference.FRACTION_NOT_NULL.get());
            return;
        }

        Map<Integer, ItemStack> content = Arrays.stream(Faction.values()).collect(Collectors.toMap(Faction::getSlot, Faction::getIcon));
        new GuiCreator(1).title(Reference.FRACTION_GUI_TITLE.get()).invokeItems(content).toPlayer(p);
    }

}
