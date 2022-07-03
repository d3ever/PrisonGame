package sexy.criss.game.prison.listener;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.prison_data.PrisonPlayer;

import java.util.Map;

public class FractionListHandler extends SexyListener {
    public static Map<String, Integer> gui_list_map = Maps.newHashMap();

    @EventHandler
    public void invoke(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
//        if(e.getCurrentItem() == null) return;
//        if(p.getOpenInventory() == null) return;
//        if(e.getClickedInventory() == null) return;
//        if(!p.getOpenInventory().getTitle().contains(Reference.FRACTION_LIST_GUI_TITLE.get().substring(0, Reference.FRACTION_LIST_GUI_TITLE.get().length() - 8))) return;
//        e.setCancelled(true);
//        switch (e.getCurrentItem().getType()) {
//            case ENDER_PEARL:
//                if(gui_list_map.get(p.getName()) > 1)  {
//                    gui_list_map.put(p.getName(), gui_list_map.get(p.getName()) - 1);
//                    FactionCommand.gui_list(p, gui_list_map.get(p.getName()), FactionCommand.max_page);
//                    p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1);
//                }
//                break;
//            case ARROW:
//                if(gui_list_map.get(p.getName()) < FactionCommand.max_page) {
//                    gui_list_map.put(p.getName(), gui_list_map.get(p.getName()) + 1);
//                    FactionCommand.gui_list(p, gui_list_map.get(p.getName()), FactionCommand.max_page);
//                    p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
//                }
//                break;
//            default:
//                String id = Util.strip(e.getCurrentItem().getItemMeta().getDisplayName());
//                Fraction f = Fraction.getById(id);
//                pp.setFraction(f);
//                f.addMember(p.getName());
//                p.sendMessage(Reference.FRACTION_PLAYER_JOIN.get(f.getName()));
//                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
//                p.closeInventory();
//                break;
//        }
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
