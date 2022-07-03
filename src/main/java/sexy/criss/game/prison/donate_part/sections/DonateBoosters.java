package sexy.criss.game.prison.donate_part.sections;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.boosters.Booster;
import sexy.criss.game.prison.boosters.PrivateBooster;
import sexy.criss.game.prison.donate_part.util.DonateButton;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.util.Util;

public record DonateBoosters(Player p) {

    public void handle() {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0Премиальные услуги", 5);
        inv.addItem(2, 1, new DonateButton("&6Личный бустер монет", Util.createItem(Material.GOLD_INGOT, ""),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 день"), 100) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.MONETARY, p.getName(), 2, 1440).activate();
            }
        }.build(inv, 2, 1));

        inv.addItem(2, 3, new DonateButton("&6Личный бустер монет", Util.update(Util.createItem(Material.GOLD_INGOT, ""), 7),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 неделя"), 300) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.MONETARY, p.getName(), 2, 10080).activate();
            }
        }.build(inv, 2, 3));

        inv.addItem(2, 5, new DonateButton("&6Личный бустер монет", Util.update(Util.createItem(Material.GOLD_BLOCK, ""), 1),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 месяц"), 800) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.MONETARY, p.getName(), 2, 43200).activate();
            }
        }.build(inv, 2, 5));

        inv.addItem(2, 7, new DonateButton("&6Личный бустер монет", Util.update(Util.createItem(Material.GOLD_BLOCK, ""), 3),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &63 месяца"), 2000) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.MONETARY, p.getName(), 2, 129600).activate();
            }
        }.build(inv, 2, 7));
        inv.addItem(2, 9, new DonateButton("&6Личный бустер монет", Util.update(Util.createItem(Material.GOLD_BLOCK, ""), 12),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 год"), 5000) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.MONETARY, p.getName(), 2, 518400).activate();
            }
        }.build(inv, 2, 9));

        inv.addItem(3, 1, new DonateButton("&6Личный бустер блоков", Util.createItem(Material.IRON_INGOT, ""),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 день"), 100) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.BLOCKS, p.getName(), 2, 1440).activate();
            }
        }.build(inv, 3, 1));

        inv.addItem(3, 3, new DonateButton("&6Личный бустер блоков", Util.update(Util.createItem(Material.IRON_INGOT, ""), 7),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 неделя"), 300) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.BLOCKS, p.getName(), 2, 10080).activate();
            }
        }.build(inv, 3, 3));

        inv.addItem(3, 5, new DonateButton("&6Личный бустер блоков", Util.update(Util.createItem(Material.IRON_BLOCK, ""), 1),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 месяц"), 800) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.BLOCKS, p.getName(), 2, 43200).activate();
            }
        }.build(inv, 3, 5));

        inv.addItem(3, 7, new DonateButton("&6Личный бустер блоков", Util.update(Util.createItem(Material.IRON_BLOCK, ""), 3),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &63 месяца"), 2000) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.BLOCKS, p.getName(), 2, 129600).activate();
            }
        }.build(inv, 3, 7));
        inv.addItem(3, 9, new DonateButton("&6Личный бустер блоков", Util.update(Util.createItem(Material.IRON_BLOCK, ""), 12),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 год"), 5000) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.BLOCKS, p.getName(), 2, 518400).activate();
            }
        }.build(inv, 3, 9));

        inv.addItem(4, 1, new DonateButton("&6Личный бустер ключей", Util.createItem(Material.COAL, ""),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 день"), 100) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.KEYS, p.getName(), 2, 1440).activate();
            }
        }.build(inv, 4, 1));

        inv.addItem(4, 3, new DonateButton("&6Личный бустер ключей", Util.update(Util.createItem(Material.COAL, ""), 7),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 неделя"), 300) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.KEYS, p.getName(), 2, 10080).activate();
            }
        }.build(inv, 4, 3));

        inv.addItem(4, 5, new DonateButton("&6Личный бустер ключей", Util.update(Util.createItem(Material.COAL_BLOCK, ""), 1),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 месяц"), 800) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.KEYS, p.getName(), 2, 43200).activate();
            }
        }.build(inv, 4, 5));

        inv.addItem(4, 7, new DonateButton("&6Личный бустер ключей", Util.update(Util.createItem(Material.COAL_BLOCK, ""), 3),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &63 месяца"), 2000) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.KEYS, p.getName(), 2, 129600).activate();
            }
        }.build(inv, 4, 7));
        inv.addItem(4, 9, new DonateButton("&6Личный бустер ключей", Util.update(Util.createItem(Material.COAL_BLOCK, ""), 12),
                Lists.newArrayList("",
                        "&fМножитель: &6x2 (+100%)",
                        "&fВремя: &61 год"), 5000) {
            @Override
            public void reward(Player p, PrisonPlayer pp, RPlayer rp) {
                new PrivateBooster(Booster.BoosterType.KEYS, p.getName(), 2, 518400).activate();
            }
        }.build(inv, 4, 9));

        inv.addItem(5, 9, new DynamicItem(Util.createItem(Material.DIAMOND, "&fБаланс: &6"+RPlayer.get(p).getCoins().getCoins()), (p1, clickType, slot) -> {}));
        inv.addItem(5, 1, new DynamicItem(Util.createItem(Material.DARK_OAK_DOOR, "&6Назад"), (p1, clickType, slot) -> {
            new DonateUniversal(p).handle();
        }));
        inv.open(p);
    }
}
