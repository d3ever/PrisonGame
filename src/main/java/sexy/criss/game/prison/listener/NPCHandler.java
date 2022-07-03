package sexy.criss.game.prison.listener;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.boosters.Shop;
import sexy.criss.game.prison.boosters.ShopMenu;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.game.prison.quests.values.*;

public class NPCHandler extends SexyListener {

    @EventHandler
    public void sell(NPCRightClickEvent e) {
        NPC npc = e.getNPC();
        Player p = e.getClicker();
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        String n = npc.getName().toLowerCase();
        if(npc.getName().toLowerCase().contains("торговец")) { Main.SHOP_BOT.gui_open(p); return; }
        if(n.contains("сборщик ресурсов")) ShopMenu.open(p, Shop.getShop("a"));
        if(n.contains("землекоп")) { Quest.getByClass(DirtQuest.class).handle(p); return; }
        if(n.contains("шахтёр")) { Quest.getByClass(StoneQuest.class).handle(p); return; }
        if(n.contains("фермер")) { Quest.getByClass(HayQuest.class).handle(p); return; }
        if(n.contains("лесоруб")) { Quest.getByClass(LogQuest.class).handle(p); return; }
        if(n.contains("арбузник")) { Quest.getByClass(MelonQuest.class).handle(p); return; }
        if(n.contains("забытый пустыней")) { Quest.getByClass(PyramidQuest.class).handle(p); return; }
        if(n.contains("сборщий мицелия")) { Quest.getByClass(MyceliumQuest.class).handle(p); return; }
        if(n.contains("изучаем подводный мир")) { Quest.getByClass(PrismarineQuest.class).handle(p); return; }
        if(n.contains("ледниковый период")) { Quest.getByClass(IceQuest.class).handle(p); return; }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }
}
