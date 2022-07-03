package sexy.criss.game.prison.boosters;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import sexy.criss.gen.player.RPlayer;

import java.util.List;

public class BoosterMonetary extends Booster {
    public BoosterMonetary(final double multiplier) {
        super(multiplier, true, true);
    }
    
    public String getOwner() {
        return "Your rank";
    }
    
    public long formatTime() {
        return -1L;
    }
    
    public String getMessage() {
        return "PermissionBoosters.booster-use";
    }
    
    public List<String> getAppliedPlayers() {
        List<String> players = Lists.newArrayList();
        Bukkit.getOnlinePlayers().forEach(player -> {
            RPlayer rp = RPlayer.get(player);
            switch (rp.getMainPermissionGroup()) {
                case VIP, BETA -> {
                    if(this.getMultiplier() == 1.1) players.add(player.getName());
                }
                case HELPER, VIP_PLUS, BUILDER -> {
                    if(this.getMultiplier() == 1.2) players.add(player.getName());
                }
                case PREMIUM, YOUTUBE -> {
                    if(this.getMultiplier() == 1.3) players.add(player.getName());
                }
                case PREMIUM_PLUS, SPECIAL, MODERATOR -> {
                    if(this.getMultiplier() == 1.4) players.add(player.getName());
                }
                case SPONSOR -> {
                    if(this.getMultiplier() == 2) players.add(player.getName());
                }
                case ADMINISTRATOR, OWNER -> {
                    if(this.getMultiplier() == 2.5) players.add(player.getName());
                }
            }
        });
//        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("permbooster."+this.getMultiplier())).forEach(player -> players.add(player.getName()));
        return players;
    }
}