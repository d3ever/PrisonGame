package sexy.criss.game.prison.small_system.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.small_system.SPlayer;

public class GeneralHandler extends SexyListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        SPlayer.get(p);
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        SPlayer.get(p).save();
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
