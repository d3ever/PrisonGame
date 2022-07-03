package sexy.criss.game.prison.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.language.Reference;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.factions.Faction;

public class FractionHandler extends SexyListener {

	@EventHandler
	public void clickGUI(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
		ItemStack stack = e.getCurrentItem();
		if(!Reference.FRACTION_GUI_TITLE.get().equals(p.getOpenInventory().getTitle())) return;
		Faction f = Faction.fromIcon(stack);
		e.setCancelled(true);
		if(f != null) {
			pp.setFraction(f);
			p.sendMessage(Reference.FRACTION_JOIN.get(f.getName()));
			p.closeInventory();
		}
	}

	@Override
	public String getName() {
		return "Fraction Listener";
	}

	@Override
	public String getType() {
		return "private";
	}

}