package sexy.criss.game.prison.bots;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.util.Util;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ShopBot extends SexyListener {
    public static NPC SHOP_NPC;
    private final Map<UUID, Map<Integer, PrisonItem>> items_map = Maps.newHashMap();
    private boolean allowed = true;
    private final Location spawn = new Location(Bukkit.getWorld("prison1"), -225.564d, 95d, 273.543d, 90f, 0f);
    private final Location toChest = new Location(Bukkit.getWorld("prison1"), -220.513, 95d, 274.341d, -0.9f, 50.1f);

    public ShopBot() {
        createNPC();
        register();
    }

    public void createNPC() {
        NPC npc = CitizensAPI.getNPCRegistry().getById(449);
        String name = "&6Торговец";
        if(npc == null) npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, UUID.randomUUID(), 449, Util.f(name));
        npc.despawn();
        npc.spawn(spawn);
        SHOP_NPC = npc;
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if(e.getPlayer().getOpenInventory().getTitle().toLowerCase().contains("магазин")) {
            Util.s(p, "&6Ты приходи ещё, может приглядиться что-нибудь.");
        }
    }

    public void startBack(Player p) {
        Location l = p.getLocation();
        SHOP_NPC.getNavigator().setTarget(l);
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!(SHOP_NPC.getNavigator().isNavigating())) {
                    items_map.get(p.getUniqueId()).forEach((integer, prisonItem) -> { p.getInventory().addItem(prisonItem.getUsableItem());});
                    p.removePotionEffect(PotionEffectType.JUMP);
                    ((CraftPlayer) p).setWalkSpeed(.2f);
                    Util.s(p, "&6Держи свой товар. Ты заходи если что, буду рад тебя обслужить.");
                    ((Player) SHOP_NPC.getEntity()).getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    ((Player) SHOP_NPC.getEntity()).getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    items_map.clear();
                    allowed = true;
                    SHOP_NPC.teleport(spawn, PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT);
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 10);
    }

    public void startNext(Player p) {
        p.setWalkSpeed(0);
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10000000, 250, false, false, false));
        SHOP_NPC.getNavigator().setTarget(toChest);
        allowed = true;
        Map<Integer, PrisonItem> set = items_map.get(p.getUniqueId());
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!SHOP_NPC.getNavigator().isNavigating()) {
                    if(items_map.containsKey(p.getUniqueId()) && !items_map.get(p.getUniqueId()).isEmpty()) {
                        set.forEach((integer, prisonItem) -> {
                            Player bot = ((Player) SHOP_NPC.getEntity());
                            if(bot.getItemInHand() == null) bot.setItemInHand(prisonItem.getUsableItem());
                            bot.getInventory().setItemInOffHand(prisonItem.getUsableItem());
                        });
                    }
                    startBack(p);
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 10);
    }

    public void gui_open(Player p) {
        if(!allowed) {
            if(items_map.size() >= 0 && items_map.entrySet().stream().findAny().isPresent() && items_map.entrySet().stream().findAny().get().getValue().size() > 0) {
                Util.s(p, "&6Торговец на данный момент собирает заказ. Немного подождите");
                return;
            } else allowed = true;
        }
        UUID uuid = p.getUniqueId();
        allowed = false;
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        RPlayer rp = RPlayer.get(p);
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("Магазин", 3);
        inv.addItem(1, 3, new DynamicItem(Util.createItem(Material.LEATHER_HELMET, Util.getColoredText("Кожаный шлем"), getLore(pp, 75)), (p1, clickType, slot) -> {
            if(addTo(p, PrisonItem.getPrisonItem("cap_1").getUsableItem(), 75)) {
                inv.addItem(1, 3, getEmpty());
                inv.addItem(3, 5, getConfirm(p, true));
            }
        }));
        inv.addItem(1, 4, new DynamicItem(Util.createItem(Material.LEATHER_CHESTPLATE, Util.getColoredText("Кожаный нагрудник"), getLore(pp, 100)), (p1, clickType, slot) -> {
            if(addTo(p, PrisonItem.getPrisonItem("chest_1").getUsableItem(), 100)) {
                inv.addItem(1, 4, getEmpty());
                inv.addItem(3, 5, getConfirm(p, true));
            }
        }));
        inv.addItem(1, 5, new DynamicItem(Util.createItem(Material.LEATHER_LEGGINGS, Util.getColoredText("Кожаные штаны"), getLore(pp, 100)), (p1, clickType, slot) -> {
            if(addTo(p, PrisonItem.getPrisonItem("legg_1").getUsableItem(), 100)) {
                inv.addItem(1, 5, getEmpty());
                inv.addItem(3, 5, getConfirm(p, true));
            }
        }));
        inv.addItem(1, 6, new DynamicItem(Util.createItem(Material.LEATHER_BOOTS, Util.getColoredText("Кожаный шлем"), getLore(pp, 75)), (p1, clickType, slot) -> {
            if(addTo(p, PrisonItem.getPrisonItem("boots_1").getUsableItem(), 75)) {
                inv.addItem(1, 6, getEmpty());
                inv.addItem(3, 5, getConfirm(p, true));
            }
        }));

        inv.addItem(2, 3, new DynamicItem(Util.createItem(Material.WOODEN_SWORD, Util.getColoredText("Деревянный меч"), getLore(pp, 150)), (p1, clickType, slot) -> {
            if(addTo(p, PrisonItem.getPrisonItem("sword_1").getUsableItem(), 150)) {
                inv.addItem(2, 3, getEmpty());
                inv.addItem(3, 5, getConfirm(p, true));
            }
        }));
        inv.addItem(2, 4, new DynamicItem(Util.createItem(Material.BOW, Util.getColoredText("Деревянная лопата"), getLore(pp, 90)), (p1, clickType, slot) -> {
            if(addTo(p, PrisonItem.getPrisonItem("bow_1").getUsableItem(), 90)) {
                inv.addItem(2, 4, getEmpty());
                inv.addItem(3, 5, getConfirm(p, true));
            }
        }));
        inv.addItem(2, 5, new DynamicItem(Util.createItem(Material.WOODEN_SHOVEL, Util.getColoredText("Деревянная лопата"), getLore(pp, 6)), (p1, clickType, slot) -> {
            if(addTo(p, PrisonItem.getPrisonItem("spade_1").getUsableItem(), 6)) {
                inv.addItem(2, 5, getEmpty());
                inv.addItem(3, 5, getConfirm(p, true));
            }
        }));
        inv.addItem(2, 6, new DynamicItem(Util.createItem(Material.WOODEN_PICKAXE, Util.getColoredText("Деревянная кирка"), getLore(pp, 30)), (p1, clickType, slot) -> {
            if(addTo(p, PrisonItem.getPrisonItem("pickaxe_1").getUsableItem(), 30)) {
                inv.addItem(2, 6, getEmpty());
                inv.addItem(3, 5, getConfirm(p, true));
            }
        }));
        inv.addItem(2, 7, new DynamicItem(Util.createItem(Material.WOODEN_AXE, Util.getColoredText("Деревянный топор"), getLore(pp, 3)), (p1, clickType, slot) -> {
            if(addTo(p, PrisonItem.getPrisonItem("axe_1").getUsableItem(), 3)) {
                inv.addItem(2, 7, getEmpty());
                inv.addItem(3, 5, getConfirm(p, true));
            }
        }));
        inv.addItem(3, 5, getConfirm(p, false));
        inv.open(p);
    }

    private DynamicItem getConfirm(Player p, boolean b) {
        return b ? new DynamicItem(Util.createItem(Material.LIME_TERRACOTTA, "&aПОДТВЕРДИТЬ", Lists.newArrayList("", "&6Нажмите, чтобы подтвердить покупку")), (p1, clickType, slot) -> {
            p.closeInventory();
            allowed = true;

            if(items_map.containsKey(p.getUniqueId()) && !items_map.get(p.getUniqueId()).isEmpty()) {
                startNext(p);
                AtomicInteger ai = new AtomicInteger(0);
                items_map.get(p.getUniqueId()).forEach((integer, prisonItem) -> ai.addAndGet(integer));
                PrisonPlayer.getPlayer(p.getUniqueId()).takeMoney(ai.get());
            }
        }) : new DynamicItem(Util.createItem(Material.IRON_DOOR, "&cЗАКРЫТЬ МАГАЗИН", Lists.newArrayList("", "&6Нажмите, чтобы выйти")), (p1, clickType, slot) -> {
            p.closeInventory();
            allowed = true;
        });
    }

    private void updateConfirm(DynamicInventory inv, DynamicItem item) {
        inv.addItem(3, 5, item);
    }

    private ItemStack format(ItemStack stack, String s, List<String> l) {
        ItemMeta m = stack.getItemMeta();
        m.setDisplayName(Util.f(s));
        m.setLore(Util.f(l));
        stack.setItemMeta(m);
        return stack;
    }

    private DynamicItem getAdded() {
        return new DynamicItem(Material.YELLOW_WOOL, "&6&lУЖЕ В КОРЗИНЕ", Lists.newArrayList(), (p, clickType, slot) -> {});
    }

    private DynamicItem getEmpty() {
        return new DynamicItem(Material.BEDROCK, "&a&lДОБАВЛЕН", Lists.newArrayList(), (p, clickType, slot) -> {});
    }

    private List<String> getLore(PrisonPlayer pp, int price) {
        return Lists.newArrayList("", "&7Стоимость "+(pp.hasMoney(price)?"&6":"&c")+pp.getBalance()+"/"+price, "", "&6Нажмите, чтобы выполнить");
    }

    public boolean addTo(Player p, ItemStack item, int price) {
        if(!this.items_map.containsKey(p.getUniqueId())) this.items_map.put(p.getUniqueId(), Maps.newHashMap());
        Map<Integer, PrisonItem> sets = this.items_map.get(p.getUniqueId());
        if(sets.containsValue(PrisonItem.getPrisonItem(item))) return false;

        sets.put(price, PrisonItem.getPrisonItem(item));
        this.items_map.put(p.getUniqueId(), sets);
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }
}
