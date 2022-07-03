package sexy.criss.game.prison.utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sexy.criss.gen.util.Util;

import java.util.Map;

public class GuiCreator {

    String title;
    int size;
    Map<Integer, ItemStack> items;

    public GuiCreator(int rows) {
        this.size = 9 * rows;
    }

    public GuiCreator title(String title) {
        this.title = Util.f(title);
        return this;
    }

    public GuiCreator invokeItems(Map<Integer, ItemStack> items) {
        this.items = items;
        return this;
    }

    public void toPlayer(Player p) {
        Inventory i = Bukkit.createInventory(null, this.size, this.title);
        items.forEach(i::setItem);
        p.openInventory(i);
    }

}
