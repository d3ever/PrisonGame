package sexy.criss.game.prison.prison_data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.game.prison.configuration.Configs;
import sexy.criss.gen.util.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PrisonItem {
   public static Map<String, PrisonItem> items = Maps.newHashMap();

   private String id;
   private String next;
   private String name;
   private Material material;
   private List<String> lore;
   private Map<Enchantment, Integer> enchantments;
   private boolean drop = false;
   private short data = 0;
   private int price;
   private Map<Material, Integer> requirements;
   private Map<EntityType, Integer> mob_requirements;


   public PrisonItem(String id, ConfigurationSection cItem) {
      this.id = id;
      this.material = Material.valueOf(cItem.getString("material"));
      this.name = Util.f(cItem.getString("name"));
      this.enchantments = Maps.newHashMap();
      this.requirements = Maps.newHashMap();
      this.mob_requirements = Maps.newHashMap();

      this.lore = Lists.newArrayList();


      this.lore.add(0, ChatColor.BLACK + id);
      ConfigurationSection sec;
      if(cItem.contains("enchantments")) {
         sec = cItem.getConfigurationSection("enchantments");
         sec.getValues(false).forEach((s, o) -> {
            this.enchantments.put(Enchantment.getByName(s), Integer.parseInt(String.valueOf(o)));
         });
      }

      if(cItem.contains("requirements")) {
         sec = cItem.getConfigurationSection("requirements");
         sec.getValues(false).forEach((s, o) -> this.requirements.put(Material.getMaterial(s), Integer.parseInt(String.valueOf(o))));
      }

      if(cItem.contains("mob-requirements")) {
         sec = cItem.getConfigurationSection("mob-requirements");
         sec.getValues(false).forEach((s, o) -> this.mob_requirements.put(EntityType.valueOf(s), Integer.parseInt(String.valueOf(o))));
      }

      if(cItem.contains("price")) this.price = cItem.getInt("price");
      if(cItem.contains("next")) this.next = cItem.getString("next");
      if(cItem.contains("drop")) this.drop = cItem.getBoolean("drop");
      if(cItem.contains("data")) this.data = (short)cItem.getInt("data");

      items.put(id, this);
   }

   public int getLevel() {
      return Integer.parseInt(this.id.split("_")[1]);
   }

   public ItemStack getUsableItem() {
      ItemStack is = new ItemStack(this.material);
      ItemMeta im = is.getItemMeta();
      im.setDisplayName(name);
      im.setLore(this.lore);
      if(this.enchantments.size() > 0) this.enchantments.forEach((enchantment, integer) -> im.addEnchant(enchantment, integer, false));


      if(is.getType().getMaxDurability() > 16) im.setUnbreakable(true);

      is.setItemMeta(im);
      is.setDurability(this.data);
      return is;
   }

   public ItemStack getUpgradingItem(PrisonPlayer pp) {
      PrisonItem nextItem = getPrisonItem(this.next);
      ItemStack is = new ItemStack(nextItem.material, 1, this.data);
      ItemMeta im = is.getItemMeta();
      im.setDisplayName(Util.f("&fУлучшение: &6" + nextItem.name));
      List<String> lore = Lists.newArrayList();
      boolean hasBal = pp.hasMoney(nextItem.price);
      lore.add(Util.f("&fЦена: " + (hasBal?"&2":"&4") + nextItem.price + "$"));
      if(nextItem.requirements.size() > 0 || nextItem.mob_requirements.size() > 0) {
         lore.add("&fНеобходимо: ");

         if(nextItem.requirements.size() > 0) lore.add(ChatColor.GRAY + "  Блоки: ");

         boolean allowed = true;
         boolean good;
         ChatColor color;
         for(Map.Entry<Material, Integer> entry : this.requirements.entrySet()) {
            good = pp.hasBlocks(entry.getKey(), entry.getValue());
            color = good ? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
            lore.add("  &7" + entry.getKey().name() + " → " + color + pp.getBlock_log().getOrDefault(entry.getKey(), 0) + " / " + entry.getValue());
            if(!good) allowed = false;
         }

         if(nextItem.mob_requirements.size() > 0) lore.add(ChatColor.GRAY + "  Монстры: ");

         for(Map.Entry<EntityType, Integer> entry : this.mob_requirements.entrySet()) {
            good = pp.hasMob(entry.getKey(), entry.getValue());
            color = good ? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
            lore.add("  &7" + entry.getKey().name() + " → " + color + pp.getMob(entry.getKey()) + " / " + entry.getValue());
            if(!good) allowed = false;
         }

         if(nextItem.enchantments.size() > 0) nextItem.enchantments.forEach((enchantment, integer) -> im.addEnchant(enchantment, integer, false));
      }

      im.setLore(Util.f(lore));

      if(is.getType().getMaxDurability() > 16) im.setUnbreakable(true);
      im.addItemFlags(ItemFlag.values());

      is.setItemMeta(im);
      return is;
   }

   public ItemStack getMaxLevelItem() {
      ItemStack is = this.getUsableItem();
      ItemMeta im = is.getItemMeta();
      im.setDisplayName(ChatColor.RESET + this.name);
      this.lore.set(0, ChatColor.BLACK + "level_max");
      this.lore.add("");
      this.lore.add(ChatColor.GOLD + "Ваш предмет максимального уровня");
      im.setLore(this.lore);

      if(is.getType().getMaxDurability() > 16) im.setUnbreakable(true);

      is.setItemMeta(im);
      return is;
   }

   public PrisonItem getNext() {
      return items.getOrDefault(this.next, null);
   }

   public boolean hasNext() {
      return this.getNext() != null;
   }

   public static PrisonItem getPrisonItem(ItemStack is) {
      return items.get(ChatColor.stripColor(is.getItemMeta().getLore().get(0)));
   }

   public static PrisonItem getPrisonItem(String id) {
      return items.get(id);
   }

   public static ItemStack getItem(String id) {
      if(getPrisonItem(id) == null) System.out.println(id);
      return getPrisonItem(id).getUsableItem();
   }

   public static boolean isCustomItem(ItemStack is) {
      if(is == null) return false;
      if(!(is.hasItemMeta()) || !(is.getItemMeta().hasLore())) return false;

      try {
         return items.containsKey(ChatColor.stripColor(is.getItemMeta().getLore().get(0)));
      } catch (Exception ex) {
         return false;
      }
   }

   public static boolean isAvailable(String id) {
      return items.containsKey(id);
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public Material getMaterial() {
      return this.material;
   }

   public List<String> getLore() {
      return this.lore;
   }

   public Map<Enchantment, Integer> getEnchantments() {
      return this.enchantments;
   }

   public boolean willDrop() {
      return this.drop;
   }

   public short getData() {
      return this.data;
   }

   public int getPrice() {
      return this.price;
   }

   public Map<Material, Integer> getRequirements() {
      return this.requirements;
   }

   public Map<EntityType, Integer> getMob_requirements() {
      return this.mob_requirements;
   }

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(!(o instanceof PrisonItem)) {
         return false;
      } else {
         PrisonItem that = (PrisonItem)o;
         return this.id.equals(that.id);
      }
   }

   public int hashCode() {
      return this.id.hashCode();
   }
}