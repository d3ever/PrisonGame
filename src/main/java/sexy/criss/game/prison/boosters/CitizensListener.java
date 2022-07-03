/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable
 *  net.citizensnpcs.api.event.NPCDamageByEntityEvent
 *  net.citizensnpcs.api.event.NPCRightClickEvent
 *  net.citizensnpcs.api.npc.NPC
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package sexy.criss.game.prison.boosters;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import sexy.criss.game.prison.Main;

public class CitizensListener
implements Listener {
    public CitizensListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onNPCInteract(NPCRightClickEvent e) {
        NPC npc = e.getNPC();
        if (QuickSell.getInstance().npcs.contains(String.valueOf(npc.getId()))) {
            String action = QuickSell.getInstance().npcs.getString(String.valueOf(npc.getId()));
            Shop shop = Shop.getShop(action.split(" ; ")[0]);
            if (shop == null) {
                QuickSell.local.sendTranslation((CommandSender)e.getClicker(), "messages.unknown-shop", false, new Variable[0]);
            } else if (action.split(" ; ")[1].equalsIgnoreCase("SELL")) {
                ShopMenu.open(e.getClicker(), shop);
            } else if (shop.hasUnlocked(e.getClicker())) {
                shop.sellall(e.getClicker(), "", SellEvent.Type.CITIZENS);
            } else {
                QuickSell.local.sendTranslation((CommandSender)e.getClicker(), "messages.no-access", false, new Variable[0]);
            }
        }
    }

    @EventHandler
    public void onDamage(NPCDamageByEntityEvent e) {
        NPC npc = e.getNPC();
        Entity damager = e.getDamager();
        if (damager instanceof Player && QuickSell.getInstance().npcs.contains(String.valueOf(npc.getId()))) {
            Player p = (Player)damager;
            String action = QuickSell.getInstance().npcs.getString(String.valueOf(npc.getId()));
            Shop shop = Shop.getShop(action.split(" ; ")[0]);
            if (shop == null) {
                QuickSell.local.sendTranslation((CommandSender)p, "messages.unknown-shop", false, new Variable[0]);
            } else {
                shop.showPrices(p);
            }
        }
    }
}

