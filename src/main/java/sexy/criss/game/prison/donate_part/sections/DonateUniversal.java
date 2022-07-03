package sexy.criss.game.prison.donate_part.sections;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.boosters.Booster;
import sexy.criss.game.prison.donate_part.DonateGui;
import sexy.criss.game.prison.donate_part.util.DonateButton;
import sexy.criss.game.prison.notify.Notify;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.util.Util;

public record DonateUniversal(Player p) {

    public void handle() {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0Премиальные услуги", 5);
        inv.addItem(3, 4, new DonateButton(Notify.isDisabled(p.getName()), "&6Отключить потребности", new ItemStack(Material.RABBIT_FOOT), Lists.newArrayList(
                "",
                "&fВы больше никогда не будете",
                "&fходить в туалет",
                "",
                "&fВремя: &aвечность"), 100) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                Notify.disable(p);
            }
        }.build(inv, 3, 4));
        inv.addItem(3, 6, new DonateButton(PrisonPlayer.getPlayer(p.getUniqueId()).hasAutoSell(), "&6Автопродажа",
                Util.createItem(Material.FURNACE, ""), Lists.newArrayList("",
                "&fВсе вскопанные блоки",
                "&fбудут автоматически продаваться",
                "",
                "&6Для активации напишите в чат: &a/ap",
                "",
                "&fВремя: &aвечность"), 200) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                PrisonPlayer.getPlayer(p.getUniqueId()).grantAutoSell();
            }
        }.build(inv, 3, 6));
        inv.addItem(2, 2, new DonateButton("&6Глобальный бустер монет", Util.createItem(Material.GOLD_BLOCK, ""),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &630 минут"), 100) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new Booster(Booster.BoosterType.MONETARY, p.getName(), 2, 30).activate();
            }
        }.build(inv, 2, 2));

        inv.addItem(3, 2, new DonateButton("&6Глобальный бустер блоков", Util.createItem(Material.IRON_BLOCK, ""),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &630 минут"), 100) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new Booster(Booster.BoosterType.BLOCKS, p.getName(), 2, 30).activate();
            }
        }.build(inv, 3, 2));

        inv.addItem(4, 2, new DonateButton("&6Глобальный бустер ключей", Util.createItem(Material.COAL_BLOCK, ""),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &630 минут"), 100) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new Booster(Booster.BoosterType.KEYS, p.getName(), 2, 30).activate();
            }
        }.build(inv, 4, 2));

        inv.addItem(3, 8, new DynamicItem(Util.createItem(Material.FIREWORK_ROCKET, "&6Личные бустеры"), (p1, clickType, slot) -> {
            new DonateBoosters(p).handle();
        }));

        inv.addItem(5, 9, new DynamicItem(Util.createItem(Material.DIAMOND, "&fБаланс: &6"+RPlayer.get(p).getCoins().getCoins()), (p1, clickType, slot) -> {}));
        inv.addItem(5, 1, new DynamicItem(Util.createItem(Material.DARK_OAK_DOOR, "&6Назад"), (p1, clickType, slot) -> {
            new DonateGui(p).handle();
        }));
        inv.open(p);
    }

}
