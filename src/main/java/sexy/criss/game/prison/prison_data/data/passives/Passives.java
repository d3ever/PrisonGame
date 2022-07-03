package sexy.criss.game.prison.prison_data.data.passives;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.util.LoreCreator;
import sexy.criss.gen.util.Util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Passives {
    public static Map<PassiveType, Passives> passivesMap = Maps.newHashMap();

    public static void register() {
        new Passives(PassiveType.STRENGH, "strenght", "Сила", Lists.newArrayList(), 3, 1, Util.createItem(Material.IRON_SWORD, ""), "6:5,5:4,5:6", 365);
        new Passives(PassiveType.FORTUNE, "fortune", "Удача", Lists.newArrayList(), 4, 1, Util.createItem(Material.RABBIT_FOOT, ""), "5:5,4:6,3:4,3:6", 150);
        new Passives(PassiveType.CURSE, "curse", "Проклятье", Lists.newArrayList(), 3, 1, Util.createItem(Material.FERMENTED_SPIDER_EYE, ""), "3:7,3:8,2:7", 150);
        new Passives(PassiveType.AGILITY, "agility", "Изворотливость", Lists.newArrayList(), 4, 1, Util.createItem(Material.FEATHER, ""), "4:5,4:4,3:5,2:5", 150);
        new Passives(PassiveType.VAMPIRE, "vampiric", "Вампиризм", Lists.newArrayList(), 3, 1, Util.createItem(Material.MAGMA_CREAM, ""), "3:3,3:2,2:3", 150);
        new Passives(PassiveType.NEEDS, "needs", "Потребности", Lists.newArrayList(), 3, 1, Util.createItem(Material.LIGHT_GRAY_DYE, ""), "6:7,6:8,5:7", 200);
    }

    public static Passives getByType(PassiveType type) {
        if(!passivesMap.containsKey(type)) register();
        return passivesMap.get(type);
    }

    public static Passives getStrenght() {
        return passivesMap.get(PassiveType.STRENGH);
    }

    public static Passives getAgility() {
        return passivesMap.get(PassiveType.AGILITY);
    }

    public static Passives getCurse() {
        return passivesMap.get(PassiveType.CURSE);
    }

    public static Passives getVampiric() {
        return passivesMap.get(PassiveType.VAMPIRE);
    }

    public static Passives getFortune() {
        return passivesMap.get(PassiveType.FORTUNE);
    }

    public static Passives getNeeds() {
        return passivesMap.get(PassiveType.NEEDS);
    }

    public static Collection<Passives> getAll() {
        return passivesMap.values();
    }

    PassiveType type;
    String id, name;
    List<String> desc;
    int max_level;
    double percent;
    ItemStack icon;
    int price;
    String[] slots;

    public Passives(PassiveType type, String id, String name, List<String> desc, int max_level, double percent, ItemStack icon, String slots, int price) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.max_level = max_level;
        this.percent = percent;
        this.icon = icon;
        this.slots = slots.split(",");
        this.price = price;
        passivesMap.put(type, this);
    }

    public PassiveType getType() {
        return this.type;
    }

    public int getPrice(int level) {
        return price * level;
    }

    public int[] getSlots(int level) {
        String[] v = this.slots[level - 1].split(":");
        return new int[]{Integer.parseInt(v[0]), Integer.parseInt(v[1])};
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getDesc() {
        return desc;
    }

    public int getMax_level() {
        return max_level;
    }

    public double getPercent() {
        return percent;
    }

    public boolean available(Player p, int level) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        boolean has = pp.hasMoney(getPrice(level));
        boolean learned = pp.getPassive(this.type) >= level;
        boolean access = (pp.getPassive(this.type) + 1) >= level || level <= (pp.getPassive(this.type)+1);
        return has && access;
    }

    public ItemStack getIcon(Player p, PrisonPlayer pp, RPlayer rp, int level) {
        String curl = Util.ROMAN_NUMBERS.getOrDefault(level, String.valueOf(level));
        String name = level == max_level ? "&6"+this.name+" "+curl+" [МАКС]" : "&6"+this.name+" "+curl;
        boolean has = pp.hasMoney(getPrice(level));
        boolean learned = pp.getPassive(this.type) >= level;
        boolean access = ((pp.getPassive(this.type) + 1) >= level || level <= (pp.getPassive(this.type)+1));
        String color = has ? "&a" : "&c";
        if(learned) {
            return fromItem(this.icon, Util.f(name), new LoreCreator()
                    .addLinesIf(this.desc != null || !this.desc.isEmpty(), this.desc)
                    .addEmpty()
                    .addLine("&6Вы изучили данный модификатор")
                    .build());
        }
        if (!access && !learned) {
            return fromItem(Material.BARRIER, name, new LoreCreator()
                    .addEmpty()
                    .addLine("&cпредпоказ невозможен")
                    .addEmpty()
                    .addLine("&fИзучите предыдущие уровни")
                    .addLine("&fчтобы получить доступ к")
                    .addLine("&fданному модификатору")
                    .build());
        } else if (access && !learned) {
            if (has) {
                return fromItem(Material.YELLOW_WOOL, name, new LoreCreator()
                        .addLinesIf(this.desc != null || !this.desc.isEmpty(), this.desc)
                        .addEmpty()
                        .addLine("&6нажмите ЛКМ для изучения")
                        .addLine("&fбудет списано &6" + getPrice(level) + "&f золота")
                        .build());
            } else {
                return fromItem(Material.RED_WOOL, name, new LoreCreator()
                        .addLinesIf(this.desc != null || !this.desc.isEmpty(), this.desc)
                        .addEmpty()
                        .addLine("&fДля изучения нужно ещё " + color + (getPrice(level) - pp.getBalance()) + "&f золота")
                        .build());
            }
        }
        return new ItemStack(Material.BEDROCK);
    }

    private ItemStack fromItem(Material m, String s, List<String> l) {
        ItemStack stack = new ItemStack(m, 1);
        ItemMeta meta = stack.getItemMeta();
        if(false) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1, true);
        meta.setDisplayName(Util.f(s));
        meta.setLore(l);
        meta.addItemFlags(ItemFlag.values());
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack fromItem(ItemStack is, String s, List<String> l) {
        ItemMeta m = is.getItemMeta();
        if(true) m.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1, true);
        m.setDisplayName(s);
        m.setLore(l);
        m.addItemFlags(ItemFlag.values());
        is.setItemMeta(m);
        return is;
    }

}
