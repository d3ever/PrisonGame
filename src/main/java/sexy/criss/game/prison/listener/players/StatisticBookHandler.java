package sexy.criss.game.prison.listener.players;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.boosters.SellEvent;
import sexy.criss.game.prison.boosters.Shop;
import sexy.criss.game.prison.commands.KitCommand;
import sexy.criss.game.prison.commands.MineCommand;
import sexy.criss.game.prison.commands.ShopCommand;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.levels.Levels;
import sexy.criss.game.prison.prison_data.data.passives.Passives;
import sexy.criss.game.prison.timer.SexyTimer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.List;
import java.util.Map;

public class StatisticBookHandler extends SexyListener {
    public static ItemStack STATISTIC_BOOK = Util.createItem(Material.BOOK, "&7Личное дело", Lists.newArrayList("", "&7Нажмите в игре &6ПКМ&7 чтобы открыть"), 1, (short) 0);

    @EventHandler
    public void onPlayerStatistic(InventoryClickEvent e) {
        if(STATISTIC_BOOK.equals(e.getCurrentItem())) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT) {
                toPlayer(p);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().equals(STATISTIC_BOOK)) e.setCancelled(true);
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(Util.getLoc(Main.getDatFile().get("clinic")));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack cur = p.getItemInHand();
        if(cur.equals(STATISTIC_BOOK) && (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR))) {
            e.setCancelled(true);
            toPlayer(p);
        }
    }

    public void toPlayer(Player p) {
        List<String> l = Lists.newArrayList("", "&7нажмите, чтобы выполнить");
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0Ваше личное дело", 5);
        inv.addItem(3, 4, new DynamicItem(Util.createItem(Material.WOODEN_AXE, "&6Улучшение инструментов", l), (p1, clickType, slot) -> {
            new UpgradeGui(p, PrisonPlayer.getPlayer(p.getUniqueId())).handle();
        }));
        inv.addItem(2, 5, new DynamicItem(Util.createItem(Material.PAPER, "&6Прогресс", l), (p1, clickType, slot) -> g_progress(p)));
        inv.addItem(3, 2, new DynamicItem(Util.createItem(Material.BLAZE_POWDER, "&6Продать блоки", l), (p1, clickType, slot) -> {
            if(RPlayer.get(p).hasGroup(PermissionGroup.VIP)) {
                List<ItemStack> cont = Lists.newArrayList();
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    ItemStack is = p.getInventory().getItem(i);
                    if (Shop.getShop("a").getPrices().getPrice(is) > 0.0) cont.add(is);
                }
                if (cont.size() > 0) {
                    p.closeInventory();
                    Shop.getShop("a").sellall(p, "", SellEvent.Type.SELLALL);
                }
            } else Util.permissions("Prison", p, PermissionGroup.VIP);
        }));
        inv.addItem(3, 8, new DynamicItem(Util.createItem(Material.CHEST, "&6Наборы", l), (p1, clickType, slot) -> KitCommand.toPlayer(p)));
        inv.addItem(3, 6, new DynamicItem(Util.createItem(Material.LECTERN, "&6Способности", l), (p1, clickType, slot) -> {
            if(PrisonPlayer.getPlayer(p.getUniqueId()).getLevel() >= 6) g_skills(p);
            else Util.ps("Prison", p, "&fЛичная статистика доступна с уровня &66&f и выше.");
        }));
        inv.addItem(5, 5, new DynamicItem(Util.createItem(Material.LANTERN, "&6Магазин", l), (p1, clickType, slot) -> {
            if(RPlayer.get(p).hasGroup(PermissionGroup.VIP)) ShopCommand.handle(p);
            else teleport(p, "shop");
        }));
        inv.addItem(5, 4, new DynamicItem(Util.createItem(Material.RED_MUSHROOM_BLOCK, "&6Больница", l), (p1, clickType, slot) -> {
            teleport(p, "clinic");
        }));
        inv.addItem(5, 3, new DynamicItem(Util.createItem(Material.RAIL, "&6Шахты", l), (p1, clickType, slot) -> {
            MineCommand.toPlayer(p, 1);
        }));
        inv.open(p);
    }

    private void teleport(Player p, String key) {
        new SexyTimer(p, 3) {
            @Override
            protected void handle() {
                p.teleport(Util.getLoc(Main.getDatFile().get(key)));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            }
        }.run();
    }

    private ItemStack format(ItemStack item, String s, List<String> lore) {
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(Util.f(s));
        if(lore != null) m.setLore(Util.f(lore));
        item.setItemMeta(m);
        return item;
    }

    public void g_progress(Player p) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        RPlayer rp = RPlayer.get(p);
        int level = pp.getLevel();
        boolean access = pp.levelImprove() || pp.getLevel() >= Levels.getMaxLevel();
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&7Ваша статистика по игре", 3);
        inv.addItem(3, 1, new DynamicItem(Material.PAPER, "&6Ваша позиция в общем рейтинге", Lists.newArrayList(
                "",
                "&7Ваше место &6"+pp.getTopNumber(),
                "&7Всего игроков: &6"+PrisonPlayer.getTopPlayers().keySet().size()
        ), (p1, clickType, slot) -> {}));
        if(!pp.hasMaxLevel()) {
            inv.addItem(2, 5, new DynamicItem(Material.OAK_SIGN, "&7ИНФОРМАЦИЯ ПО УРОВНЮ", Lists.newArrayList(
                    "",
                    "&fСледующий → &6"+(pp.getLevel()+1),
                    "&f↓ Стоимость ↓",
                    "&6"+pp.getBalance()+"/"+Levels.getByInt(pp.getLevel()+1).getGold(),
                    "&f↓   Блоки   ↓ ",
                    "&6"+pp.getTotalBlocks()+"/"+Levels.getByInt(pp.getLevel()+1).getTotal_blocks(),
                    ""
            ), (p1, clickType, slot) -> {}));
        }
        if(pp.hasMaxLevel()) {
            inv.addItem(2, 5, new DynamicItem(Material.DRAGON_EGG, "&c&lМАКСИМАЛЬНЫЙ УРОВЕНЬ", Lists.newArrayList(
                    "",
                    "&7Вы достигли максимального уровня",
                    "&7мы благодарим вас за этот великий",
                    "&7и долгий труд. Обратитесь к ",
                    "&7администратору и приложите",
                    "&7скриншот этого предмета.",
                    "",
                    Util.getRandomColor("С уважением RewForce")
            ), (p1, clickType, slot) -> p1.playSound(p1.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1)));
        }
        if(access && !pp.hasMaxLevel()) {
            int extra = pp.getLevelClass().getExtraDMG();
            int next_extra = Levels.getByInt(pp.getLevel()+1).getExtraDMG();
            boolean change_extra = extra != next_extra;
            inv.addItem(2, 5, new DynamicItem(Material.FIREWORK_ROCKET, "&6НОВЫЙ УРОВЕНЬ", Lists.newArrayList(
                    "",
                    "&fУровень &6"+pp.getLevel()+"→"+(pp.getLevel()+1),
                    "&fУрон &6"+(change_extra?extra+"→"+next_extra:"&7без изменений"),
                    "",
                    "&6Нажмите ЛКМ, чтобы использовать"
            ), (p1, clickType, slot) -> {
                g_progress(p);
                pp.levelUpper();
                Util.ps("Prison", p, "&fПоздравляем! Вы получили новый уровень");
                Util.ps("Prison", p, "&fа значит и новые возможности.");
            }));
        }
        inv.open(p);
    }

    public void g_skills(Player p) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        double m = pp.getBalance();
        Map<String, Integer> pass = pp.getPassives();
        RPlayer rp = RPlayer.get(p);

        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&7Личные модификаторы", 6);
        Passives.getAll().forEach(pas -> {
            for(int i = 1; i <= pas.getMax_level(); i++) {
                int level = i;
                inv.addItem(pas.getSlots(level)[0], pas.getSlots(level)[1], new DynamicItem(pas.getIcon(p, pp, rp, level), (p1, clickType, slot) -> {
                    if(!pp.hasPassive(pas.getType(), level)) {
                        if(pp.hasMoney(pas.getPrice(level))) {
                            pp.takeMoney(pas.getPrice(level));
                            pp.addPassive(pas);
                            p.closeInventory();
                            Util.ps("Prison", p, "&fБыл изучен новый модификатор: &6%s &fи его уровень &6%d", pas.getName(), level);
                        } else {
                            enought(p);
                        }
                    }
                }));
            }
        });
        inv.open(p);
    }

    public void enought(Player p) {
        Util.ps("Prison", p, "&fУ вас недостаточно средств, чтобы изучить модификатор");
    }

    @Override
    public String getName() {
        return "Statistic book";
    }

    @Override
    public String getType() {
        return "inventory events";
    }
}
