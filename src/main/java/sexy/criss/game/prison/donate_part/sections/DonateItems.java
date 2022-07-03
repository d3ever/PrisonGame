package sexy.criss.game.prison.donate_part.sections;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.donate_part.DonateGui;
import sexy.criss.game.prison.donate_part.util.DonateButton;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.util.Util;

public record DonateItems(Player p) {

    public void handle() {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0Премиальные услуги", 3);
        inv.addItem(2, 3, new DonateButton("&6Огненный меч x1", PrisonItem.getItem("firesword_1"), Lists.newArrayList("", "&fПронзай своих врагов", "&fПоджигай своих врагов"), 600) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                p.getInventory().addItem(PrisonItem.getPrisonItem("firesword_1").getUsableItem());
            }
        }.build(inv, 2, 3));
        inv.addItem(2, 4, new DonateButton("&6Обычный ключ x30", Util.update(PrisonItem.getItem("key"), "&6Обычный ключ", null), Lists.newArrayList(), 100) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                p.getInventory().addItem(Util.update(PrisonItem.getItem("key"), "&6Обычный ключ", null, 30));
            }
        }.build(inv, 2, 4));
        inv.addItem(2, 5, new DonateButton("&6Редкая кирка x1", PrisonItem.getPrisonItem("newpickaxe_1").getUsableItem(), Lists.newArrayList(), 90) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                p.getInventory().addItem(PrisonItem.getPrisonItem("newpickaxe_1").getUsableItem());
            }
        }.build(inv, 2, 5));
        inv.addItem(2, 6, new DonateButton("&6Счастливая кирка x1", PrisonItem.getPrisonItem("pickaxetwo_1").getUsableItem(), Lists.newArrayList(), 150) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                p.getInventory().addItem(PrisonItem.getPrisonItem("pickaxetwo_1").getUsableItem());
            }
        }.build(inv, 2, 6));
        inv.addItem(2, 7, new DonateButton("&6Заброшенный подвал x1", PrisonItem.getPrisonItem("cave").getUsableItem(), Lists.newArrayList(), 200) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                p.getInventory().addItem(PrisonItem.getPrisonItem("cave").getUsableItem());
            }
        }.build(inv, 2, 7));
        inv.addItem(3, 9, new DynamicItem(Util.createItem(Material.DIAMOND, "&fБаланс: &6"+RPlayer.get(p).getCoins().getCoins()), (p1, clickType, slot) -> {}));
        inv.addItem(3, 1, new DynamicItem(Util.createItem(Material.DARK_OAK_DOOR, "&6Назад"), (p1, clickType, slot) -> {
            new DonateGui(p).handle();
        }));
        inv.open(p);
    }

}
