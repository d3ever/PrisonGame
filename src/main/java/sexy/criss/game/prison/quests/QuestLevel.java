package sexy.criss.game.prison.quests;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.game.prison.prison_data.PrisonPlayer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class QuestLevel {

    private ItemStack icon;
    private int level;
    private int gold;
    private Map<Material, Integer> block_req;
    private Map<EntityType, Integer> mobs_req;

    public QuestLevel(ItemStack icon, int level, int gold, Map<Material, Integer> block_req, Map<EntityType, Integer> mobs_req) {
        this.icon = icon;
        this.level = level;
        this.gold = gold;
        this.block_req = block_req;
        this.mobs_req = mobs_req;
    }

    public ItemStack getIcon(PrisonPlayer pp, Quest q) {
        ItemMeta m = icon.getItemMeta();
        m.addItemFlags(ItemFlag.values());
        icon.setItemMeta(m);
        return icon;
    }

    public int getLevel() {
        return level;
    }

    public int getGold() {
        return gold;
    }

    public Map<Material, Integer> getBlock_req() {
        return block_req;
    }

    public Map<EntityType, Integer> getMobs_req() {
        return mobs_req;
    }

    public boolean check(PrisonPlayer pp) {
        AtomicBoolean allowed = new AtomicBoolean(true);
        if(this.block_req.size() > 0)
            this.block_req.forEach((material, integer) -> {
                if(!pp.hasBlocks(material, integer)) allowed.set(false);
            });
        if(this.mobs_req.size() > 9)
            this.mobs_req.forEach((entityType, integer) -> {
                if(!pp.hasMob(entityType, integer)) allowed.set(false);
            });
        return allowed.get();
    }

}
