package sexy.criss.game.prison.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sexy.criss.game.prison.commands.manager.AbstractCommand;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.mines.Mine;
import sexy.criss.game.prison.prison_data.data.mines.MineType;
import sexy.criss.game.prison.utilities.GuiCreator;
import sexy.criss.game.prison.utilities.SkullCreator;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.LoreCreator;
import sexy.criss.gen.util.Util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MineCommand extends AbstractCommand {
    public static final String MINE_TITLE = "&7#MINES-%d";
    public static Map<Integer, Map<Integer, Mine>> mine_pages = Maps.newHashMap();
    public static int MAX_PAGE;

    public MineCommand() {
        super("mine");
        this.unavailableFromConsole();
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        Player p = (Player) sender;
        toPlayer(p, 1);
    }

    public static void register_pages() {
        // 10 | 16
        List<Mine> mineList = Arrays.stream(Mine.values()).filter(x -> x.getType().equals(MineType.DEFAULT)).sorted(Comparator.comparingInt(Mine::getLevel)).collect(Collectors.toList());
        int i = 10;
        int page = 1;
        for(Mine m : mineList) {
            if(i > 16) {
                page = page + 1;
                i = 10;
            }
            if(!mine_pages.containsKey(page)) {
                mine_pages.put(page, Maps.newHashMap());
            }
            mine_pages.get(page).put(i, m);
            i++;
        }
        MAX_PAGE = page;
    }

    public static void toPlayer(Player p, int page) {
        PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
        Map<Integer, ItemStack> content = Maps.newHashMap();
        ItemStack panel = Util.createItem(Material.BLACK_STAINED_GLASS_PANE, "&r", Lists.newArrayList(), 1, (short) 0);
        content.put(25, SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), "&6Предыдущая страница", Lists.newArrayList(
                "",
                "&6Нажмите для перехода"
        ), 1, (short) 3), SkullCreator.ARROW_LEFT_BASE64));
        content.put(18, SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), "&6Только по пропуску", Lists.newArrayList(
                "",
                "&fДанные шахты можно",
                "&fоткрыть только по пропуску"
        ), 1, (short) 3), SkullCreator.ACCESS_MINES_BASE64));
        content.put(19, SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), "&6Донат шахты", Lists.newArrayList(
                "",
                "&fСписок шахт для игроков купивших",
                "&fпривилегию, подробности на сайте:",
                "&6www.rewforce.xyz"
        ), 1, (short) 3), SkullCreator.DONATE_MINE_BASE64));
        content.put(26, SkullCreator.itemWithBase64(Util.createItem(SkullCreator.getSkullMaterial(), "&6Следующая страница", Lists.newArrayList(
                "",
                "&6Нажмите для перехода"
        ), 1, (short) 3), SkullCreator.ARROW_RIGHT_BASE64));

        mine_pages.get(page).forEach((s, x) -> {
            boolean level = pp.getLevel() >= x.getLevel();
            boolean access = pp.hasAccess(x.getAccess());
            String teleport = level ? "&fНажмите для &6телепортации" : "&cРабота на данной шахте невозможна";
            content.put(s, x.setType(Material.BARRIER).setLore(new LoreCreator()
                    .addEmpty()
                    .addLineIf(x.getLevel() != 0, "&fМинимальный уровень: %s%d")
                    .addLineIf(x.getAccess() != 0, "&fУровень доступа: %s%d")
                    .addLineIf(!(x.getGroup() == PermissionGroup.PLAYER), "&7Доступна от группы: &b%s&7 и выше")
                    .addEmpty()
                    .addLine(teleport).build(Util.color(level), x.getLevel(), Util.color(access), x.getAccess(), x.getGroup())).getIcon(!level));
        });
        for (int i = 0; i < 9 * 3; i++) {
            if(content.containsKey(i)) continue;
            content.put(i, panel);
        }
        new GuiCreator(3).title(Util.f(MINE_TITLE, page)).invokeItems(content).toPlayer(p);
    }

}
