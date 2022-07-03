package sexy.criss.game.prison.additional.autopickup.commands;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntitySound;
import net.minecraft.server.v1_16_R3.SoundCategory;
import net.minecraft.server.v1_16_R3.SoundEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftSound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;
import sexy.criss.gen.util.UtilPlayer;

public class AutoCommand extends SpigotCommand {
    private static final ItemStack off = Util.createItem(Material.GOLDEN_PICKAXE, "&fАвто-продажа &4ВЫКЛЮЧИТЬ");
    private static final ItemStack on  = Util.createItem(Material.GOLDEN_PICKAXE, "&fАвто-продажа &2ВКЛЮЧИТЬ");

    public AutoCommand() {
        super("autopickup", PermissionGroup.VIP, "/auto", "ap", "auto", "autopick");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        toPlayer(p);
    }

    public static void toPlayer(Player p) {
        DynamicInventory inv = GENAPI.getInventoryAPI().createNewInventory("&0Настройка добычи блоков", 1);
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        if(!pp.hasAutoSell()) inv.addItem(1, 5, GET_BLOCKED(p));
        else inv.addItem(1, 5, getButton(p, Main.getAutoPickup.autosell_list.contains(p), inv));

        inv.open(p);
    }

    private static DynamicItem getButton(Player p, boolean b, DynamicInventory inv) {
        return b ? new DynamicItem(off, (p1, clickType, slot) -> {
            Main.getAutoPickup.autosell_list.remove(p);
            sendSound(CraftSound.getSoundEffect(Sound.BLOCK_STONE_BUTTON_CLICK_OFF), p);
            inv.addItem(1, 5, getButton(p, false, inv));
        }) : new DynamicItem(on, (p1, clickType, slot) -> {
            Main.getAutoPickup.autosell_list.add(p);
            sendSound(CraftSound.getSoundEffect(Sound.BLOCK_STONE_BUTTON_CLICK_ON), p);
            inv.addItem(1, 5, getButton(p, true, inv));
        });
    }

    private static DynamicItem GET_BLOCKED(Player p) {
        return new DynamicItem(Util.createItem(Material.BARRIER, "&4&lЗАКРЫТО"), (p1, clickType, slot) -> {
            Util.ps("Prison", p, "У вас нет доступа к этому аттрибуту.");
            Util.ps("Prison", p, "Напишите &6/donate&f, чтобы получить подробную информацию.");
        });
    }

    private static void sendSound(SoundEffect effect, Player p) {
        PacketPlayOutEntitySound packet = new PacketPlayOutEntitySound(effect, SoundCategory.AMBIENT, UtilPlayer.getNMSPlayer(p), 1, 1);
        UtilPlayer.getNMSPlayer(p).playerConnection.sendPacket(packet);
    }

}
