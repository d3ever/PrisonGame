package sexy.criss.game.prison.listener;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.commands.MineCommand;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.mines.Mine;
import sexy.criss.game.prison.prison_data.data.mines.MineType;
import sexy.criss.game.prison.utilities.GuiCreator;
import sexy.criss.game.prison.utilities.SkullCreator;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.LoreCreator;
import sexy.criss.gen.util.Util;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MineHandler extends SexyListener {
    public static final String DONATE_TITLE = "&7Донат шахты";
    public static final String ACCESS_TITLE = "&7Шахты с пропусками";
    private final String[] n = new String[] { "предыдущая", "донат", "следующая", "пропуску" };

    @EventHandler
    public void default_mines(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        String title = e.getWhoClicked().getOpenInventory().getTitle();
        if(!title.contains(Util.f(MineCommand.MINE_TITLE.split("-")[0]))) return;
        e.setCancelled(true);
        ItemStack cur = e.getCurrentItem();
        if(cur == null || !cur.hasItemMeta() || !cur.getItemMeta().hasDisplayName()) return;
        String t = cur.getItemMeta().getDisplayName().toLowerCase();
        int page = Integer.parseInt(title.split("-")[1]);
        if(t.contains(n[1])) {
            toPlayer(p);
            return;
        }
        if(t.contains(n[3])) {
            toPlayerAccess(p);
            return;
        }
        if(t.contains(n[0])) {
            if(page > 1) {
                MineCommand.toPlayer(p, page - 1);
            }
            return;
        }
        if(t.contains(n[2])) {
            if(page < MineCommand.MAX_PAGE) {
                MineCommand.toPlayer(p, page + 1);
            }
        }
        if(Mine.fromIcon(cur) != null) {
            Mine m = Mine.fromIcon(cur);
            if(m.getGroup().getId() > RPlayer.get(p).getMainPermissionGroup().getId()) {
                Util.ps("Prison", p, "&cС вашей группой невозможно попасть на данную шахту");
                Util.s(p, "&f        Минимальная группа с которой можно попасть");
                Util.s(p, "&f        на данную шахту &b%s&7.", m.getGroup().getName());
                return;
            }
            if(m.getAccess() > 0) {
                int max_access = pp.getAccess().stream().mapToInt(value ->  value).max().orElse(0);
                if(max_access < m.getAccess()) {
                    Util.ps("Prison", p, "&fУ вас нет пропуска на данную шахту.");
                    return;
                }
            }
            if(pp.getLevel() < m.getLevel()) {
                Util.ps("Prison", p, "&fС вашим уровнем ещё рано приходить на данную шахту");
                return;
            }
            if (m.getLocation(m.getId()) == null) {
                Util.ps("Prison", p, "&fЛокация шахты не установлена, обратитесь к администрации.");
                return;
            }
            p.teleport(m.getLocation(m.getId()));
            Util.ps("Prison", p, "&fВы были телепортированы к шахте &b%s&7.", m.getName());
        }
    }

    @EventHandler
    public void access_mines(InventoryClickEvent e) {
        if(!clear(e, ACCESS_TITLE)) return;
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack button = e.getCurrentItem();
        String t = button.getItemMeta().getDisplayName().toLowerCase();
        if(t.contains("назад")) {
            MineCommand.toPlayer(p, 1);
        }
    }

    @EventHandler
    public void donate_mines(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        String title = e.getWhoClicked().getOpenInventory().getTitle();
        if(!title.equals(Util.f(DONATE_TITLE))) return;
        e.setCancelled(true);
        ItemStack cur = e.getCurrentItem();
        if(cur == null || !cur.getItemMeta().hasDisplayName()) return;
        String t = cur.getItemMeta().getDisplayName().toLowerCase();
        if(t.contains("назад")) {
            MineCommand.toPlayer(p, 1);
        }
    }

    public static boolean clear(InventoryClickEvent e, String title) {
        return e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getWhoClicked().getOpenInventory().getTitle() != null && e.getWhoClicked().getOpenInventory().getTitle().contains(Util.f(title));
    }

    public static void toPlayerAccess(Player p) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        Map<Integer, ItemStack> content = Maps.newHashMap();
        content.put(8, SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), "&cНазад", Lists.newArrayList(
                "", "&7Нажмите, чтобы вернуться"), 1, (short) 3), SkullCreator.BACK_BUTTON));
        AtomicInteger i = new AtomicInteger(0);
        Arrays.stream(Mine.values()).filter(x -> x.getType().equals(MineType.ACCESS)).forEach(x -> {
            boolean access = pp.hasAccess(x.getAccess());
            String teleport = access ? "&6Нажмите для телепортации" : "&cРабота на данной шахте невозможна";
            content.put(i.getAndIncrement(), x.setLore(new LoreCreator()
                    .addEmpty()
                    .addLine("&fУровень доступа: &6%d")
                    .addEmpty()
                    .addLine(teleport).build(x.getAccess())).getIcon());
        });
        new GuiCreator(1).title(ACCESS_TITLE).invokeItems(content).toPlayer(p);
    }

    public static void toPlayer(Player p) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        RPlayer rp = RPlayer.get(p);
        Map<Integer, ItemStack> content = Maps.newHashMap();
        content.put(8, SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), "&cНазад", Lists.newArrayList(
                "", "&7Нажмите, чтобы вернуться"), 1, (short) 3), SkullCreator.BACK_BUTTON));
        AtomicInteger i = new AtomicInteger(0);
        Arrays.stream(Mine.values()).filter(x -> x.getType().equals(MineType.DONATE)).forEach(x -> {
            boolean access = rp.hasGroup(x.getGroup());
            boolean level = pp.getLevel() >= x.getLevel();
            String teleport = access && level ? "&7Нажмите для &bтелепортации" : "&cРабота на данной шахте невозможна";
            content.put(i.getAndIncrement(), x.setLore(new LoreCreator()
                    .addEmpty()
                    .addLine("&fМинимальный уровень: %s%d")
                    .addLineIf(!(x.getGroup() == PermissionGroup.PLAYER), "&7Доступна от группы: &b%s&7 и выше")
                    .addEmpty()
                    .addLine(teleport).build(Util.color(level), x.getLevel(), x.getGroup())).getIcon());
        });
        new GuiCreator(1).title(DONATE_TITLE).invokeItems(content).toPlayer(p);
    }

    @Override
    public String getName() {
        return "Mine Listener";
    }

    @Override
    public String getType() {
        return "mines";
    }
}
