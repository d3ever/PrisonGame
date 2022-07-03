package worldguard_preset.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;

import java.util.Set;

public class Regions {
    private static WorldGuard wg = WorldGuard.getInstance();

    public static String getRegion(Location l) {
        RegionContainer rc = wg.getPlatform().getRegionContainer();
        RegionQuery rq = rc.createQuery();
        ApplicableRegionSet ars = rq.getApplicableRegions(BukkitAdapter.adapt(l));
        return ars.getRegions().stream().findFirst().orElse(null).getId();
    }

    public static Set<ProtectedRegion> getRegions(Location l) {
        RegionContainer rc = wg.getPlatform().getRegionContainer();
        RegionQuery rq = rc.createQuery();
        ApplicableRegionSet ars = rq.getApplicableRegions(BukkitAdapter.adapt(l));
        return ars.getRegions();
    }

    public static Location getCenter(Location l1, Location l2) {
        double x = (l1.getX()+l2.getX()) / 2;
        double y = (l1.getY()+l2.getX()) / 2;
        double z = (l1.getZ()+l2.getX()) / 2;
        return new Location(l1.getWorld(), x, y, z);
    }

}
