package sexy.criss.game.prison.bosses.updater;

import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerUpdater extends BukkitRunnable {

   public void run() {
      Spawner.spawners.values().forEach(Spawner::update);
   }
}