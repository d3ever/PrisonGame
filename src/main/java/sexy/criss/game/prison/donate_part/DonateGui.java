package sexy.criss.game.prison.donate_part;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.donate_part.sections.DonateItems;
import sexy.criss.game.prison.donate_part.sections.DonateUniversal;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.util.Util;

public record DonateGui(Player p) {

    public void handle() {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0Премиальные услуги", 6);
        inv.addItem(3, 3, new DynamicItem(Util.createItem(Material.CHEST, "&6Предметы"), (p1, clickType, slot) -> new DonateItems(p).handle()));
        inv.addItem(3, 7, new DynamicItem(Util.createItem(Material.REPEATING_COMMAND_BLOCK, "&6Универсальное"), (p1, clickType, slot) -> {
            new DonateUniversal(p).handle();
        }));
        inv.open(p);
    }

}
