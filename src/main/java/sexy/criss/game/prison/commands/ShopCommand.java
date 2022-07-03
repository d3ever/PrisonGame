package sexy.criss.game.prison.commands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.timer.SexyTimer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ShopCommand extends SpigotCommand {

    public ShopCommand() {
        super("shop", PermissionGroup.PLAYER, "", "magazine");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        handle(p);
    }

    public static void handle(Player p) {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0БЫСТРЫЙ МАГАЗИН", 6);
        inv.addItem(2, 6, getItem(p, "cap_1", 75));
        inv.addItem(3, 6, getItem(p, "chest_1", 100));
        inv.addItem(4, 6, getItem(p, "legg_1", 100));
        inv.addItem(5, 6, getItem(p, "boots_1", 75));

        inv.addItem(2, 2, getItem(p, "sword_1", 150));
        inv.addItem(2, 3, getItem(p, "bow_1", 90));
        inv.addItem(3, 3, getItem(p, "pickaxe_1", 50));
        inv.addItem(4, 3, getItem(p, "spade_1", 6));
        inv.addItem(5, 3, getItem(p, "axe_1", 4));
        inv.open(p);
    }

    private static DynamicItem getItem(Player p, String id, int price) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        boolean access = pp.hasMoney(price);
        String l = access ? "&6Нажмите, чтобы приобрести товар" : "&cВам не хватает ещё "+(price-pp.getBalance())+"$";
        return new DynamicItem(Util.format(PrisonItem.getPrisonItem(id).getUsableItem(), "&fСтоимость: &6"+price, Lists.newArrayList("", l)), (rp, clickType, slot) -> {
            if(pp.hasMoney(price)) {
                pp.takeMoney(price);
                p.getInventory().addItem(PrisonItem.getPrisonItem(id).getUsableItem());
                p.closeInventory();
            } else {
                Util.ps("Prison", p, "&fУ вас недостаточно средств, чтобы купить данный товар.");
                p.closeInventory();
            }
        });
    }

}
