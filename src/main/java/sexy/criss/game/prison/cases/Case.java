package sexy.criss.game.prison.cases;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.cases.invoke.DefaultCase;
import sexy.criss.gen.util.Util;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Case implements Listener {
    public static Map<CaseType, Case> caseMap = Maps.newHashMap();

    private String title;
    private CaseType type;
    private int rows;
    private List<DroppedItem> content;
    private double moneySize;
    private Set<Location> locations;

    public Case(CaseType type, String title, List<DroppedItem> content, int rows, double moneySize) {
        this.title = title;
        this.content = content;
        this.rows = rows;
        this.type = type;
        this.moneySize = moneySize;
        this.locations = Sets.newHashSet();

        caseMap.put(type, this);
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        locations.forEach(location -> {
            Hologram holo = HologramsAPI.createHologram(Main.getInstance(), location.clone().add(0, 1, 0));
            holo.appendTextLine(Util.f("&6Обычный сундук"));
        });
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public List<DroppedItem> getContent() {
        return content;
    }

    public double getMoneySize() {
        return moneySize;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void addLocation(Location loc) {
        this.locations.add(loc);
    }

    @EventHandler
    public void onEvent(PlayerInteractEvent e) {
        if(e.getClickedBlock() != null && this.locations != null) {
            if(this.locations.contains(e.getClickedBlock().getLocation())) {
                e.setCancelled(true);
                this.handle(e.getPlayer());
            }
        }
    }

    public abstract void handle(Player p);

    public static void register() {
        new DefaultCase();
    }

}
