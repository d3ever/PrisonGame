package sexy.criss.game.prison.cases.invoke;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.cases.Case;
import sexy.criss.game.prison.cases.CaseContent;
import sexy.criss.game.prison.cases.CaseType;
import sexy.criss.game.prison.cases.DroppedItem;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.SimpleItemStack;
import sexy.criss.gen.util.Util;

import java.util.*;

public class DefaultCase extends Case {

    public DefaultCase() {
        super(CaseType.DEFAULT_CASE, "&7Кейс", new CaseContent()
                .addItem(PrisonItem.getItem("spade_1"), 6, 1)
                .addItem(PrisonItem.getItem("spade_2"), 4, 1)
                .addItem(Util.createItem(Material.COOKED_BEEF, "&6Жаренный стейк", Lists.newArrayList()), 12, 4)
                .addItem(PrisonItem.getItem("pickaxe_1"), 6, 1)
                .addItem(PrisonItem.getItem("pickaxe_2"), 4, 1)
                .addItem(PrisonItem.getItem("newpickaxe_1"), 1, 1)
                .addItem(PrisonItem.getItem("pickaxetwo_1"), 1, 1)
                .addItem(PrisonItem.getItem("sword_1"), 5, 1)
                .addItem(PrisonItem.getItem("cap_1"), 5, 1)
                .addItem(PrisonItem.getItem("chest_1"), 5, 1)
                .addItem(PrisonItem.getItem("legg_1"), 5, 1)
                .addItem(PrisonItem.getItem("boots_1"), 5, 1)
                .addItem(PrisonItem.getItem("cave"), 1, 1)
                .addItem(Util.createItem(Material.APPLE, "&6Обычное яблоко"), 20, 4)
                .addItem(Util.createItem(Material.PAPER, "&6Туалетная бумага"), 20, 4)
                .addItem(PrisonItem.getItem("bow_1"), 15, 1)
                .addItem(PrisonItem.getItem("firebow_1"), 10, 1)
                .build(), 3, 10);
    }

    @Override
    public void handle(Player p) {
        if (new SimpleItemStack(p.getItemInHand().getType()).am(1).b().equals(PrisonItem.getItem("key")))
            Util.s(p, "&7Вам нужен специальный ключ, чтобы открыть сундук");
        else {
            ItemStack it = p.getItemInHand();
            if (it.getAmount() > 1) it.setAmount(it.getAmount() - 1);
            else it.setType(Material.AIR);
            Set<ItemStack> items = Sets.newHashSet();
            double money = new Random().nextInt(10);
            for (DroppedItem item : this.getContent()) {
                if (items.size() >= 3) break;
                if (new Random().nextInt(100) >= item.getPercent()) continue;
                items.add(item.getStack());
            }
            Inventory inv = Bukkit.createInventory(null, 9 * 3, Util.f("&0Кейс"));
            for (ItemStack t : items) {
                inv.setItem(new Random().nextInt((9 * 3) - 1), t);
            }
            p.openInventory(inv);
            PrisonPlayer.getPlayer(p.getUniqueId()).addGold(money);
            PrisonPlayer.getPlayer(p.getUniqueId()).usedKeys++;
            Util.s(p, "&6+" + money + " за открытие сундука");
        }
    }
}
