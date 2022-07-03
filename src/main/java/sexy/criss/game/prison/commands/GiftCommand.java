package sexy.criss.game.prison.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.achievements.Achievement;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Task;
import sexy.criss.gen.util.Util;

public class GiftCommand extends SpigotCommand {

    public GiftCommand() {
        super("gift", PermissionGroup.PLAYER, "/gift [игрок]");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        switch (args.length) {
            case 0 -> this.notEnoughtArgs(p);
            default -> {
                Player target = Bukkit.getPlayerExact(args[0]);
                if(target == null) {
                    Util.ps("Prison", p, "&fИгрок &6%s&f не в сети.", args[0]);
                    return;
                }
                if(p.getLocation().distance(target.getLocation()) > 5) {
                    Util.ps("Prison", p, "&fИгрок слишком далеко.");
                    return;
                }

                transit(p, target, p.getItemInHand());

            }
        }
    }

    private void transit(Player p, Player t, ItemStack item) {
        if(item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta()) {
            Util.ps("Prison", p, "&fВы не можете передать воздух.");
            return;
        }
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        Task.schedule(() -> {
            if(!pp.hasAchievement(Achievement.FIRST_TRANSIT)) {
                pp.addAchievement(Achievement.FIRST_TRANSIT);
                Util.s(p, "&6+10$ за выполнение задания");
                pp.addGold(10);
            }
        }, 21L);
        Util.ps("Prison", p, "&fВы подарили предмет &6%s&f игроку &6%s&f.", item.getItemMeta().getDisplayName(), t.getName());
        p.getInventory().setItemInMainHand(null);
        if(t != null) {
            if(!Util.inventoryIsFull(t)) {
                t.getInventory().addItem(item);
                Util.ps("Prison", t, "&fИгрок &6%s&f передал вам подарок.", p.getName());
            } else {
                Util.ps("Prison", t, "&fИгрок &6%s&f передал вам подарок, но у вас нет места.", p.getName());
                Util.s(t, "        &fМы выбросили предмет к вашим ногам.");
                t.getWorld().dropItem(t.getLocation(), item);
            }
        }else {
            Util.ps("Prison", p, "&fИгрок &6%s&f вышел до заключения сделки, предмет был выброшен.");
            p.getWorld().dropItem(p.getLocation(), item);
        }
    }

    private void notEnoughtArgs(Player p) {
        Util.ps("Prison", p, "&fИспользование: &6/gift [игрок]");
    }

}
