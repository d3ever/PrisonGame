package sexy.criss.game.prison.boosters.commands;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.boosters.Booster;
import sexy.criss.game.prison.boosters.BoosterMenu;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.List;

public class BoostersCommand extends SpigotCommand {

    public BoostersCommand() {
        super("boosters", PermissionGroup.PLAYER, "");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        handle(p);
    }

    public static void handle(Player p) {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0Ваши бустеры", 1);
        List<String> l = Lists.newArrayList("", "&6Нажмите, чтобы получить информацию");
        inv.addItem(1, 3, new DynamicItem(Material.GOLD_BLOCK, "&6Бустеры монет", l, (p1, clickType, slot) -> {
            menu(p, "монет", Booster.BoosterType.MONETARY);
        }));
        inv.addItem(1, 5, new DynamicItem(Material.IRON_BLOCK, "&6Бустеры блоков", l, (p1, clickType, slot) -> {
            menu(p, "блоков", Booster.BoosterType.BLOCKS);
        }));
        inv.addItem(1, 7, new DynamicItem(Material.COAL_BLOCK, "&6Бустеры ключей", l, (p1, clickType, slot) -> {
            menu(p, "ключей", Booster.BoosterType.KEYS);
        }));
        inv.open(p);
    }

    private static void menu(Player p, String name, Booster.BoosterType type) {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0Бустеры "+name, 6);
        for(Booster b : Booster.getBoosters(p.getName(), type)) {
            inv.addItem(new DynamicItem(BoosterMenu.getBoosterItem(b), (p1, clickType, slot) -> {}));
        }
        inv.addItem(6, 1, new DynamicItem(Util.createItem(Material.DARK_OAK_DOOR, "&6Назад"), (p1, clickType, slot) -> {
            handle(p);
        }));
        inv.open(p);
    }


}
