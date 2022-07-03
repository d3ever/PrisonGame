package sexy.criss.game.prison.listener;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import sexy.criss.game.prison.listener.manager.SexyListener;

import java.util.List;

public class BlockListener extends SexyListener {

	@EventHandler
	public void onLeaf(LeavesDecayEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onBurn(BlockBurnEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onTnt(BlockExplodeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onIce(BlockFromToEvent e) {
		List<Material> blocked = Lists.newArrayList(Material.PACKED_ICE, Material.SNOW_BLOCK, Material.ICE, Material.BLUE_ICE, Material.SNOW);
		if(blocked.contains(e.getBlock().getType())) e.setCancelled(true);
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onEnt(EntityChangeBlockEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onBurn2(BlockIgniteEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
    }

	@Override
	public String getName() {
		return "Block Listener";
	}

	@Override
	public String getType() {
		return "blocks";
	}

}
