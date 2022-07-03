package sexy.criss.game.prison.listener.players.tags;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.factions.Faction;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;
import sexy.criss.gen.util.UtilAlgo;
import sexy.criss.gen.util.UtilPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerTag extends BukkitRunnable {
    public static final Map<PermissionGroup, String> prefix_images = ImmutableMap.<PermissionGroup, String>builder()
            .put(PermissionGroup.VIP, "%img_vip%")
            .put(PermissionGroup.VIP_PLUS, "%img_vip_plus%")
            .put(PermissionGroup.PREMIUM, "%img_premium%")
            .put(PermissionGroup.PREMIUM_PLUS, "%img_premium_plus%")
            .put(PermissionGroup.SPONSOR, "%img_sponsor%")
            .put(PermissionGroup.YOUTUBE, "%img_youtube%")
            .put(PermissionGroup.SPECIAL, "%img_special%")
            .put(PermissionGroup.OWNER, "%img_owner%")
            .put(PermissionGroup.BETA, "%img_beta%")
            .put(PermissionGroup.DEVELOPER, "%img_dev%")
            .put(PermissionGroup.HELPER, "%img_helper%")
            .put(PermissionGroup.MODERATOR, "%img_moder%")
            .put(PermissionGroup.BUILDER, "%img_builder%")
            .put(PermissionGroup.ADMINISTRATOR, "%img_admin%")
            .build();
    Player p;

    public PlayerTag(Player p) {
        this.p = p;
    }

    public void update() {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        String s = RPlayer.get(p).getShortPrefix();
        if(RPlayer.get(p).isDeveloper()) p.setPlayerListName(Util.f("&f&l✈ &c神の使者"));
        else p.setPlayerListName(Util.f(s+p.getName() + " &6"+pp.getLevel()));
    }

    private String getColor(Faction f) {
        return f==null ? "&7" : f.getColor();
    }

}
