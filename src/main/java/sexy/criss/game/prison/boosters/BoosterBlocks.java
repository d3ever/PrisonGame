package sexy.criss.game.prison.boosters;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import sexy.criss.gen.player.RPlayer;

import java.util.List;

public class BoosterBlocks extends Booster {
    public BoosterBlocks(final double multiplier) {
        super(Booster.BoosterType.BLOCKS, multiplier, true, true);
    }
    
    public String getOwner() {
        return "Your rank";
    }
    
    public long formatTime() {
        return -1L;
    }
    
    public List<String> getAppliedPlayers() {
        List<String> players = Lists.newArrayList();
        Bukkit.getOnlinePlayers().forEach(player -> {
            RPlayer rp = RPlayer.get(player);
            switch (rp.getMainPermissionGroup()) {
                case ADMINISTRATOR, OWNER -> {
                    if(this.getMultiplier() == 2) players.add(player.getName());
                }
            }
        });
//        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("booster.blocks."+this.getMultiplier())).forEach(player -> players.add(player.getName()));
        return players;
    }
}