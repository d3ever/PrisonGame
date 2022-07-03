package sexy.criss.game.prison.prison_data.data.mines;

import net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.CraftParticle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.commands.MineCommand;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MineTask extends BukkitRunnable {
    private Location loc = new Location(Bukkit.getWorld("spawn"), 521, 56, -605);
    @Override
    public void run() {
        double x = loc.getX(), y = loc.getY(), z = loc.getZ();
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(CraftParticle.toNMS(Particle.SOUL_FIRE_FLAME), true,
                x, y, z, (float) x, (float) y, (float) z, .1f, 40);
        if(Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet));
        }
        try {
            Collection<Entity> p = Bukkit.getWorld("spawn").getNearbyEntities(loc, 0.8, 0.5, 2);
            if(p.size() > 0) {
                p.stream().filter(entity -> entity.getType().equals(EntityType.PLAYER) && !((Player)entity).getOpenInventory().getTitle().toLowerCase().contains(MineCommand.MINE_TITLE.split("-")[0])).forEach(entity -> {
                    MineCommand.toPlayer(((Player) entity), 1);
                });
            }
        } catch (Exception ex) {
            return;
        }
    }
}
