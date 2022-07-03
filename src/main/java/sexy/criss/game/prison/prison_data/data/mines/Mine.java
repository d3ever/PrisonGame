package sexy.criss.game.prison.prison_data.data.mines;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.utilities.SkullCreator;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.Arrays;
import java.util.List;

public enum Mine {
    FARM(MineType.DEFAULT, "farm", "фермы", 2, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Шахта сена"), Lists.newArrayList(), 1, (short) 3), SkullCreator.HAY_BLOCK)),
    STONE_MINE(MineType.DEFAULT, "stone", "&7камня", 3, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Шахта камня"), Lists.newArrayList(), 1, (short) 3), SkullCreator.STONE_BLOCK)),
    WOOL(MineType.DEFAULT, "wool", "шерсти", 4, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Шахта шерсти"), Lists.newArrayList(), 1, (short) 3), SkullCreator.SHEEP_BLOCK)),
    NETHER(MineType.DEFAULT, "nether", "ада", 5, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Шахта огня"), Lists.newArrayList(), 1, (short) 3), SkullCreator.FIRE_BLOCK)),
    EMERALD(MineType.DEFAULT, "emerald", "изумрудов", 6, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Шахта изумрудов"), Lists.newArrayList(), 1, (short) 3), SkullCreator.EMERALD_BLOCK)),
    ICE(MineType.DEFAULT, "ice", "льда", 7, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Ледянная шахта"), Lists.newArrayList(), 1, (short) 3), SkullCreator.ICE_BLOCK)),
    SPONGE(MineType.DEFAULT, "sponge", "мокрых губок", 8, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Шахта с губками"), Lists.newArrayList(), 1, (short) 3), SkullCreator.SPONGE_BLOCK)),
    SPIDER(MineType.DEFAULT, "spider", "паучьего логово", 9, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Паучья шахта"), Lists.newArrayList(), 1, (short) 3), SkullCreator.WEB_BLOCK)),
    FOREST(MineType.DEFAULT, "forest", "тёмного леса", 10, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Тёмный лес"), Lists.newArrayList(), 1, (short) 3), SkullCreator.LOG_BLOCK)),
    PYRAMID(MineType.DEFAULT, "pyramid", "загадочной пирамиды", 13, 0, PermissionGroup.PLAYER,
            SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Загадочная пирамида"), Lists.newArrayList(), 1, (short) 3), SkullCreator.SAND_BLOCK)),

    VAULT(MineType.ACCESS, "cellar", "заброшенного подвала", 0, 1, PermissionGroup.PLAYER, SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), Util.getColoredText("Abandoned cellar"), Lists.newArrayList(), 1, (short) 3), SkullCreator.MINE_VAULT_BASE64)),

    VIP_MINE(MineType.DONATE, "vip", "VIP", 2, 0, PermissionGroup.VIP, SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(),
            "&6&lVIP&7 шахта", Lists.newArrayList(), 1, (short) 3), SkullCreator.VIP_MINE_BASE64));

    MineType type;
    String id, name;
    PermissionGroup group;
    int level, access;
    ItemStack icon;
    Material tempMat;
    Location location;

    Mine(MineType type, String id, String name, int level, int access, PermissionGroup group, ItemStack icon) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.level = level;
        this.access = access;
        this.group = group;
        this.icon = icon;
    }

    public Location getLocation(String key) {
        if(this.location == null) {
            this.location = Util.getLoc(Main.getDatFile().get(key));
            return this.location;
        }
        return this.location;
    }

    public MineType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAccess() {
        return access;
    }

    public PermissionGroup getGroup() {
        return group;
    }

    public int getLevel() {
        return level;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public ItemStack getIcon(boolean b) {
        if(b) {
            if(tempMat != null) {
                ItemStack i = icon.clone();
                i.setType(tempMat);
                return i;
            } else return icon;
        }
        return icon;
    }

    public static Mine fromIcon(ItemStack stack) {
        return Arrays.stream(values()).filter(t->t.icon.equals(stack)).findFirst().orElse(null);
    }

    public Mine setType(Material mat) {
        this.tempMat = mat;
        return this;
    }

    public Mine setLore(List<String> lore) {
        ItemMeta m = icon.getItemMeta();
        m.setLore(Util.f(lore));
        icon.setItemMeta(m);
        return this;
    }

}
