package sexy.criss.game.prison.prison_data.data.factions;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.utilities.SkullCreator;
import sexy.criss.gen.util.Util;

import java.util.Arrays;

public enum Faction {
    ASIAN("&e", "asian", "&eАзиат", 5, Util.createItem(Material.GOLD_INGOT, "&eАзиаты", Lists.newArrayList(
            "",
            "&fМинимальный уровень: &65",
            "",
            "&6Нажмите, чтобы выбрать фракцию"), 1, (short) 3), 6),
    WHITE("&f", "white", "&fБелый", 5, Util.createItem(Material.IRON_INGOT, "&fБелые", Lists.newArrayList(
            "",
            "&fМинимальный уровень: &65",
            "",
            "&6Нажмите, чтобы выбрать фракцию"), 1, (short) 3), 2),
    BLACK("&8", "nigger", "&8Ниггер", 5, Util.createItem(Material.COAL, "&8Ниггеры", Lists.newArrayList(
            "",
            "&fМинимальный уровень: &65",
            "",
            "&6Нажмите, чтобы выбрать фракцию"), 1, (short) 3), 4);

    String id;
    String name;
    int level;
    int slot;
    ItemStack icon;
    Location location;
    String color;

    Faction(String color, String id, String name, int level, ItemStack icon, int slot) {
        this.color = color;
        this.id = id;
        this.name = name;
        this.level = level;
        this.icon = icon;
        this.slot = slot;
    }

    public String getColor() {
        return color;
    }

    public Location getLocation(String key) {
        if(this.location == null) {
            this.location = Util.getLoc(Main.getDatFile().get(key));
            return this.location;
        }
        return this.location;
    }

    public void setLocation(Location l) {
        this.location = l;
    }

    public static Faction fromIcon(ItemStack stack) {
        return Arrays.stream(values()).filter(t->t.icon.equals(stack)).findFirst().orElse(null);
    }

    public static Faction fromId(String id) {
        return Arrays.stream(values()).filter(t->t.id.equals(id)).findFirst().orElse(null);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return Util.f(name);
    }

    public int getLevel() {
        return level;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getIcon() {
        return icon;
    }
}
