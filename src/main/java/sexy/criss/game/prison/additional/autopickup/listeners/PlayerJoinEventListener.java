package sexy.criss.game.prison.additional.autopickup.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.additional.autopickup.AutoPickup;
import sexy.criss.game.prison.additional.autopickup.utils.PickupPlayer;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.player.RPlayer;

public class PlayerJoinEventListener implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Main.getAutoPickup.autopickup_list.add(player);
        if(PrisonPlayer.getPlayer(player.getUniqueId()).hasAutoSell()) Main.getAutoPickup.autosell_list.add(player);
    }

}