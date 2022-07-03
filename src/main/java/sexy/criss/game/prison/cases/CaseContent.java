package sexy.criss.game.prison.cases;

import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CaseContent {
    private final List<DroppedItem> content = Lists.newArrayList();

    public List<DroppedItem> build() {
        return content;
    }

    public CaseContent addItem(ItemStack item, int percent, int amount) {
        this.content.add(new DroppedItem(item, percent, amount));
        return this;
    }
}
