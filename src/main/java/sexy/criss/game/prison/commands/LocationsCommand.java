package sexy.criss.game.prison.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.prison_data.data.mines.Mine;
import sexy.criss.gen.api.GENAPI;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;
import sexy.criss.gen.util.configurations.DatFile;
import sexy.criss.gen.util.inventory.SimpleItemStack;

public class LocationsCommand extends SpigotCommand {
    private DatFile dat = Main.getDatFile();

    public LocationsCommand() {
        super("locations", PermissionGroup.ADMINISTRATOR, "/locations - для открытия интерфейса");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        DynamicInventory gui = GENAPI.getInventoryAPI().createNewInventory("&cLocation setter", 2);
        gui.addItem(1, 1, new DynamicItem(new SimpleItemStack(Mine.FARM.getIcon().getType(), Mine.FARM.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.FARM.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(1, 2, new DynamicItem(new SimpleItemStack(Mine.STONE_MINE.getIcon().getType(), Mine.STONE_MINE.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.STONE_MINE.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(1, 3, new DynamicItem(new SimpleItemStack(Mine.WOOL.getIcon().getType(), Mine.WOOL.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.WOOL.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(1, 4, new DynamicItem(new SimpleItemStack(Mine.EMERALD.getIcon().getType(), Mine.EMERALD.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.EMERALD.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(1, 5, new DynamicItem(new SimpleItemStack(Mine.FOREST.getIcon().getType(), Mine.FOREST.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.FOREST.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(1, 6, new DynamicItem(new SimpleItemStack(Mine.ICE.getIcon().getType(), Mine.ICE.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.ICE.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(1, 7, new DynamicItem(new SimpleItemStack(Mine.NETHER.getIcon().getType(), Mine.NETHER.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.NETHER.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(1, 8, new DynamicItem(new SimpleItemStack(Mine.PYRAMID.getIcon().getType(), Mine.PYRAMID.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.PYRAMID.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(1, 9, new DynamicItem(new SimpleItemStack(Mine.SPIDER.getIcon().getType(), Mine.SPIDER.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.SPIDER.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(2, 1, new DynamicItem(new SimpleItemStack(Mine.SPONGE.getIcon().getType(), Mine.SPONGE.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.SPONGE.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(2, 2, new DynamicItem(new SimpleItemStack(Mine.VAULT.getIcon().getType(), Mine.VAULT.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.VAULT.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(2, 3, new DynamicItem(new SimpleItemStack(Mine.VIP_MINE.getIcon().getType(), Mine.VIP_MINE.getName(), "&7Установить локацию"), (p1, clickType, slot) -> {
            dat.parse(Mine.VIP_MINE.getId(), Util.getLoc(p.getLocation()));
            setLoc(p);
        }));
        gui.addItem(2, 5, new DynamicItem(new SimpleItemStack(Material.COOKED_BEEF, "&6Торговец"), (p1, clickType, slot) -> {
            dat.parse("bot_shop", Util.getLoc(p.getLocation()));
        }));
        gui.open(p);
    }

    public void setLoc(Player p) {
        Util.ps("Locations", p, "&7Локация установлена");
    }
}
