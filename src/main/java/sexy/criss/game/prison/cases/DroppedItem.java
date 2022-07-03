package sexy.criss.game.prison.cases;

import org.bukkit.inventory.ItemStack;

public class DroppedItem {
    ItemStack item;
    int percent;
    int amount;

    public DroppedItem(ItemStack is) {
        this.item = is;
        this.percent = 100;
        this.amount = 1;
    }

    public DroppedItem(ItemStack is, int percent) {
        this.item = is;
        this.percent = percent;
        this.amount = 1;
    }

    public DroppedItem(ItemStack is, int percent, int amount) {
        this.item = is;
        this.percent = percent;
        this.amount = amount;
    }

    public DroppedItem getItem() {
        return this;
    }

    public ItemStack getStack() {
        item.setAmount(amount);
        return item;
    }

    public int getPercent() {
        return percent;
    }

    public int getAmount() {
        return amount;
    }
}
