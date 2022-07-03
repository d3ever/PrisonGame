package sexy.criss.game.prison.listener.players;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import sexy.criss.game.prison.listener.manager.SexyListener;

public class EntityHandler extends SexyListener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if(!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) e.setCancelled(true);
    }

    @Override
    public String getName() {
        return "Entity Listener";
    }

    @Override
    public String getType() {
        return "entity";
    }
}
