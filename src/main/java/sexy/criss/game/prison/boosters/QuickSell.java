/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Localization
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable
 *  me.mrCookieSlime.CSCoreLibPlugin.PluginUtils
 *  me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils
 *  net.citizensnpcs.api.CitizensAPI
 *  net.citizensnpcs.api.npc.NPC
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.java.JavaPlugin
 */
package sexy.criss.game.prison.boosters;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Localization;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import me.mrCookieSlime.CSCoreLibPlugin.PluginUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.boosters.commands.*;
import sexy.criss.game.prison.boosters.cscorelib.CSCoreLibLoader;

public class QuickSell implements CommandExecutor {
    private static QuickSell instance;
    public static Config cfg;
    public static Localization local;
    public static Map<UUID, Shop> shop;
    public static List<SellEvent> events;
    private ShopEditor editor;
    private boolean citizens = false;
    private boolean backpacks = false;
    private boolean mcmmo = false;
    private boolean prisongems = false;
    public Config npcs;

    public void register() {
        CSCoreLibLoader loader = new CSCoreLibLoader(Main.getInstance());
        if (loader.load()) {
            if (!new File("data-storage/QuickSell/boosters/").exists()) {
                new File("data-storage/QuickSell/boosters").mkdirs();
            }
            instance = this;
            shop = new HashMap<>();
            events = new ArrayList<>();
            this.editor = new ShopEditor(this);
            PluginUtils utils = new PluginUtils(Main.getInstance());
            utils.setupConfig();
            utils.setupMetrics();
            utils.setupLocalization();
            local = utils.getLocalization();
            local.setDefault("messages.prefix", new String[]{"\u00a78[\u00a7b\uff23\u00a7e\u0418\u00ad\u00a7b\uff24&8]"});
            local.setDefault("messages.sell", new String[]{"&a&l+ ${MONEY} &7[ &e\u041f\u0440\u043e\u0434\u0430\u043d\u043e &o{ITEMS} &e\u041f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432&7 ]"});
            local.setDefault("messages.no-access", new String[]{"&4\u0423 \u0412\u0430\u0441 \u043d\u0435\u0442 \u0434\u043e\u0441\u0442\u0443\u043f\u0430 \u043a \u044d\u0442\u043e\u043c\u0443 \u043c\u0430\u0433\u0430\u0437\u0438\u043d\u0443!"});
            local.setDefault("messages.total", new String[]{"&2\u0412\u0441\u0435\u0433\u043e: &6+ ${MONEY}"});
            local.setDefault("messages.get-nothing", new String[]{"&4\u0418\u0437\u0432\u0438\u043d\u0438\u0442\u0435, \u043d\u043e \u0443 \u0412\u044b \u043d\u0438\u0447\u0435\u0433\u043e \u043d\u0435 \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u0438 \u0437\u0430 \u044d\u0442\u0438 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b :("});
            local.setDefault("messages.dropped", new String[]{"&c\u0412\u044b \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u0438 \u043d\u0435\u043a\u043e\u0442\u043e\u0440\u044b\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b \u043d\u0430\u0437\u0430\u0434, \u0442\u0430\u043a-\u043a\u0430\u043a \u043c\u044b \u043d\u0435 \u0441\u043c\u043e\u0433\u043b\u0438 \u0438\u0445 \u043f\u0440\u043e\u0434\u0430\u0442\u044c..."});
            local.setDefault("messages.no-permission", new String[]{"&c\u0423\u043f\u0441\u0441.. \u041c\u043d\u0435 \u043a\u0430\u0436\u0435\u0442\u0441\u044f \u0432\u0430\u043c \u043b\u0443\u0447\u0448\u0435 \u043d\u0435 \u0432\u044b\u043f\u043e\u043b\u043d\u044f\u0442\u044c \u044d\u0442\u0443 \u043a\u043e\u043c\u0430\u043d\u0434\u0443 :)"});
            local.setDefault("command.booster.permission", new String[]{"&c&c\u0423\u043f\u0441\u0441.. \u041c\u043d\u0435 \u043a\u0430\u0436\u0435\u0442\u0441\u044f \u0432\u0430\u043c \u043b\u0443\u0447\u0448\u0435 \u043d\u0435 \u0432\u044b\u043f\u043e\u043b\u043d\u044f\u0442\u044c \u044d\u0442\u0443 \u043a\u043e\u043c\u0430\u043d\u0434\u0443 :)"});
            local.setDefault("command.permission", new String[]{"&c&c\u0423\u043f\u0441\u0441.. \u041c\u043d\u0435 \u043a\u0430\u0436\u0435\u0442\u0441\u044f \u0432\u0430\u043c \u043b\u0443\u0447\u0448\u0435 \u043d\u0435 \u0432\u044b\u043f\u043e\u043b\u043d\u044f\u0442\u044c \u044d\u0442\u0443 \u043a\u043e\u043c\u0430\u043d\u0434\u0443 :)"});
            local.setDefault("command.usage", new String[]{"&4\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c: &c%usage%"});
            local.setDefault("command.reload.done", new String[]{"&7\u0412\u0441\u0435 \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u044b \u0431\u044b\u043b\u0438 \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u044b!"});
            local.setDefault("messages.unknown-shop", new String[]{"&c\u041d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u044b\u0439 \u041c\u0430\u0433\u0430\u0437\u0438\u043d!"});
            local.setDefault("messages.no-items", new String[]{"&c\u0418\u0437\u0432\u0438\u043d\u0438\u0442\u0435, \u043d\u043e \u0443 \u0412\u0430\u0441 \u043d\u0435\u0442\u0443 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432, \u043a\u043e\u0442\u043e\u0440\u044b\u0435 \u043c\u043e\u0436\u043d\u043e \u0431\u044b\u043b\u043e \u0431\u044b \u043f\u0440\u043e\u0434\u0430\u0442\u044c!"});
            local.setDefault("command.price-set", new String[]{"&8%item% \u0442\u0435\u043f\u0435\u0440\u044c \u043f\u0440\u043e\u0434\u0430\u0451\u0442\u0441\u044f \u043f\u043e \u0446\u0435\u043d\u0435 &8$%price% &7\u0432 \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0435 '%shop%'"});
            local.setDefault("command.shop-created", new String[]{"&7\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0441\u043e\u0437\u0434\u0430\u043b\u0438 \u043d\u043e\u0432\u044b\u0439 \u041c\u0430\u0433\u0430\u0437\u0438\u043d \u043f\u043e\u0434 \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435\u043c &b%shop%"});
            local.setDefault("command.shop-deleted", new String[]{"&c\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0443\u0434\u0430\u043b\u0438\u043b\u0438 \u041c\u0430\u0433\u0430\u0437\u0438\u043d \u043f\u043e\u0434 \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435\u043c &4%shop%"});
            local.setDefault("menu.accept", new String[]{"&a> \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043f\u0440\u043e\u0434\u0430\u0442\u044c"});
            local.setDefault("menu.estimate", new String[]{"&e> \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043e\u0446\u0435\u043d\u0438\u0442\u044c"});
            local.setDefault("menu.cancel", new String[]{"&c> \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043e\u0442\u043c\u0435\u043d\u0438\u0442\u044c"});
            local.setDefault("menu.title", new String[]{"&6&l$ \u041f\u0440\u043e\u0434\u0430\u0432\u0430\u0439\u0442\u0435 \u0441\u0432\u043e\u0438 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b $"});
            local.setDefault("messages.estimate", new String[]{"&e\u0412\u044b \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u0435 &6${MONEY} &e\u0437\u0430 \u044d\u0442\u0438 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b"});
            local.setDefault("command.disabled", new String[]{"&c\u042d\u0442\u0430 \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u0431\u044b\u043b\u0430 \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u0430"});
            local.setDefault("booster.reset", new String[]{"&c\u0421\u0431\u0440\u043e\u0441 \u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044f \u0438\u0433\u0440\u043e\u043a\u0430 %player% 1.0x"});
            local.setDefault("boosters.reset", new String[]{"&c\u0421\u0431\u0440\u043e\u0441 \u0432\u0441\u0435\u0445 \u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u0435\u0439 \u0434\u043e 1.0x"});
            local.setDefault("command.prices.permission", new String[]{"&c&c\u0423\u043f\u0441\u0441.. \u041c\u043d\u0435 \u043a\u0430\u0436\u0435\u0442\u0441\u044f \u0432\u0430\u043c \u043b\u0443\u0447\u0448\u0435 \u043d\u0435 \u0432\u044b\u043f\u043e\u043b\u043d\u044f\u0442\u044c \u044d\u0442\u0443 \u043a\u043e\u043c\u0430\u043d\u0434\u0443 :)"});
            local.setDefault("editor.create-shop", new String[]{"&a&l! &7\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0432\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 \u0434\u043b\u044f \u0432\u0430\u0448\u0435\u0433\u043e \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0430 \u0432 \u0447\u0430\u0442\u0435!", "&7&o\u0426\u0432\u0435\u0442\u043e\u0432\u044b\u0435 \u043a\u043e\u0434\u044b \u043f\u043e\u0434\u0434\u0435\u0440\u0436\u0438\u0432\u0430\u044e\u0442\u0441\u044f!"});
            local.setDefault("editor.rename-shop", new String[]{"&a&l! &7\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0432\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u043e\u0432\u043e\u0435 \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 \u0434\u043b\u044f \u0432\u0430\u0448\u0435\u0433\u043e \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0430 \u0432 \u0447\u0430\u0442\u0435!", "&7&o\u0426\u0432\u0435\u0442\u043e\u0432\u044b\u0435 \u043a\u043e\u0434\u044b \u043f\u043e\u0434\u0434\u0435\u0440\u0436\u0438\u0432\u0430\u044e\u0442\u0441\u044f!"});
            local.setDefault("editor.renamed-shop", new String[]{"&a&l! &7\u041c\u0430\u0433\u0430\u0437\u0438\u043d \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0435\u0440\u0435\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d!"});
            local.setDefault("editor.set-permission-shop", new String[]{"&a&l! &7\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0432\u0432\u0435\u0434\u0438\u0442\u0435 \u043f\u0435\u0440\u043c\u0438\u0448\u0435\u043d \u0434\u043b\u044f \u0432\u0430\u0448\u0435\u0433\u043e \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0430!", "&7&o\u0412\u043f\u0438\u0448\u0438\u0442\u0435 \"none\", \u0447\u0442\u043e\u0431\u044b \u0443\u043a\u0430\u0437\u0430\u0442\u044c \u043f\u0435\u0440\u043c\u0438\u0448\u0435\u043d\u044b"});
            local.setDefault("editor.permission-set-shop", new String[]{"&a&l! &7\u041f\u0435\u0440\u043c\u0438\u0448\u0435\u043d\u044b \u0434\u043b\u044f \u0432\u0430\u0448\u0435\u0433\u043e \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0430 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u044b!!"});
            local.setDefault("messages.booster-use.MONETARY", new String[]{"&a&l+ ${MONEY} &7(&e%multiplier%x \u0411\u0443\u0441\u0442\u0435\u0440. &7&o\u041d\u0430\u0432\u0435\u0434\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439 &7)"});
            local.setDefault("messages.booster-use.BLOCKS", new String[]{"&a&l+ ${BLOCKS} &7(&e%multiplier%x \u0411\u0443\u0441\u0442\u0435\u0440. &7&o\u041d\u0430\u0432\u0435\u0434\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439 &7)"});
            local.setDefault("messages.booster-use.KEYS", new String[]{"&a&l+ ${KEYS} &7(&e%multiplier%x \u0411\u0443\u0441\u0442\u0435\u0440. &7&o\u041d\u0430\u0432\u0435\u0434\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439 &7)"});
            local.setDefault("messages.pbooster-use.MONETARY", new String[]{"&a&l+ ${MONEY} &7(&e%multiplier%x \u0411\u0443\u0441\u0442\u0435\u0440. &7&o\u041d\u0430\u0432\u0435\u0434\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439 &7)"});
            local.setDefault("messages.pbooster-use.BLOCKS", new String[]{"&a&l+ ${BLOCKS} &7(&e%multiplier%x \u0411\u0443\u0441\u0442\u0435\u0440. &7&o\u041d\u0430\u0432\u0435\u0434\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439 &7)"});
            local.setDefault("messages.pbooster-use.KEYS", new String[]{"&a&l+ ${KEYS} &7(&e%multiplier%x \u0411\u0443\u0441\u0442\u0435\u0440. &7&o\u041d\u0430\u0432\u0435\u0434\u0438\u0442\u0435 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u043f\u043e\u0434\u0440\u043e\u0431\u043d\u043e\u0441\u0442\u0435\u0439 &7)"});
            local.setDefault("booster.extended.MONETARY", new String[]{"&6%player% &e\u043f\u0440\u043e\u0434\u043b\u0438\u043b(\u0430) \u0434\u0435\u043d\u0435\u0436\u043d\u044b\u0439 \u0411\u0443\u0441\u0442\u0435\u0440 %multiplier%x \u0435\u0449\u0435 \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("booster.extended.BLOCKS", new String[]{"&6%player% &e\u043f\u0440\u043e\u0434\u043b\u0438\u043b(\u0430) \u0411\u0443\u0441\u0442\u0435\u0440 \u0431\u043b\u043e\u043a\u043e\u0432 %multiplier%x \u0435\u0449\u0435 \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("booster.extended.KEYS", new String[]{"&6%player% &e\u043f\u0440\u043e\u0434\u043b\u0438\u043b(\u0430) \u0411\u0443\u0441\u0442\u0435\u0440 \u043a\u043b\u044e\u0447\u0435\u0439 %multiplier%x \u0435\u0449\u0435 \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("pbooster.extended.MONETARY", new String[]{"&e\u0412\u0430\u0448 \u0434\u0435\u043d\u0435\u0436\u043d\u044b\u0439 \u0411\u0443\u0441\u0442\u0435\u0440 %multiplier%x \u0431\u044b\u043b \u043f\u0440\u043e\u0434\u043b\u0435\u043d \u0435\u0449\u0435 \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("pbooster.extended.BLOCKS", new String[]{"&e\u0412\u0430\u0448 \u0411\u0443\u0441\u0442\u0435\u0440 \u0431\u043b\u043e\u043a\u043e\u0432 %multiplier%x \u0431\u044b\u043b \u043f\u0440\u043e\u0434\u043b\u0435\u043d \u0435\u0449\u0435 \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("pbooster.extended.KEYS", new String[]{"&e\u0412\u0430\u0448 \u0411\u0443\u0441\u0442\u0435\u0440 \u043a\u043b\u044e\u0447\u0435\u0439 %multiplier%x \u0431\u044b\u043b \u043f\u0440\u043e\u0434\u043b\u0435\u043d \u0435\u0449\u0435 \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("booster.activate.MONETARY", new String[]{"&6&l%player% &e\u0430\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043b(\u0430) \u0434\u0435\u043d\u0435\u0436\u043d\u044b\u0439 \u0411\u0443\u0441\u0442\u0435\u0440 %multiplier%x \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("booster.activate.BLOCKS", new String[]{"&6&l%player% &e\u0430\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043b(\u0430) \u0411\u0443\u0441\u0442\u0435\u0440 \u0431\u043b\u043e\u043a\u043e\u0432 %multiplier%x \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("booster.activate.KEYS", new String[]{"&6&l%player% &e\u0430\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043b(\u0430) \u0411\u0443\u0441\u0442\u0435\u0440 \u043a\u043b\u044e\u0447\u0435\u0439 %multiplier%x \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("booster.deactivate.MONETARY", new String[]{"&4\u0414\u0435\u043d\u0435\u0436\u043d\u044b\u0439 \u0411\u0443\u0441\u0442\u0435\u0440 &c%multiplier%x \u0438\u0433\u0440\u043e\u043a\u0430 %player% \u0431\u043e\u043b\u044c\u0448\u0435 \u043d\u0435 \u0434\u0435\u0439\u0441\u0442\u0432\u0443\u0435\u0442!"});
            local.setDefault("booster.deactivate.BLOCKS", new String[]{"&4\u0411\u0443\u0441\u0442\u0435\u0440 \u0431\u043b\u043e\u043a\u043e\u0432 &c%multiplier%x \u0438\u0433\u0440\u043e\u043a\u0430 %player% \u0431\u043e\u043b\u044c\u0448\u0435 \u043d\u0435 \u0434\u0435\u0439\u0441\u0442\u0432\u0443\u0435\u0442!"});
            local.setDefault("booster.deactivate.KEYS", new String[]{"&4\u0411\u0443\u0441\u0442\u0435\u0440 \u043a\u043b\u044e\u0447\u0435\u0439 &c%multiplier%x \u0438\u0433\u0440\u043e\u043a\u0430 %player% \u0431\u043e\u043b\u044c\u0448\u0435 \u043d\u0435 \u0434\u0435\u0439\u0441\u0442\u0432\u0443\u0435\u0442!"});
            local.setDefault("pbooster.activate.MONETARY", new String[]{"&e\u0412\u0430\u043c \u0431\u044b\u043b \u0432\u044b\u0434\u0430\u043d \u0434\u0435\u043d\u0435\u0436\u043d\u044b\u0439 \u0411\u0443\u0441\u0442\u0435\u0440 %multiplier%x \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("pbooster.activate.BLOCKS", new String[]{"&e\u0412\u0430\u043c \u0431\u044b\u043b \u0432\u044b\u0434\u0430\u043d \u0411\u0443\u0441\u0442\u0435\u0440 \u0431\u043b\u043e\u043a\u043e\u0432 %multiplier%x \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("pbooster.activate.KEYS", new String[]{"&e\u0412\u0430\u043c \u0431\u044b\u043b \u0432\u044b\u0434\u0430\u043d \u0411\u0443\u0441\u0442\u0435\u0440 \u043a\u043b\u044e\u0447\u0435\u0439 %multiplier%x \u043d\u0430 %time% \u043c\u0438\u043d\u0443\u0442."});
            local.setDefault("pbooster.deactivate.MONETARY", new String[]{"&4\u0412\u0430\u0448 \u0434\u0435\u043d\u0435\u0436\u043d\u044b\u0439 \u0411\u0443\u0441\u0442\u0435\u0440 &c%multiplier%x \u0431\u043e\u043b\u044c\u0448\u0435 \u043d\u0435 \u0434\u0435\u0439\u0441\u0442\u0432\u0443\u0435\u0442!"});
            local.setDefault("pbooster.deactivate.BLOCKS", new String[]{"&4\u0412\u0430\u0448 \u0411\u0443\u0441\u0442\u0435\u0440 \u0431\u043b\u043e\u043a\u043e\u0432 &c%multiplier%x \u0431\u043e\u043b\u044c\u0448\u0435 \u043d\u0435 \u0434\u0435\u0439\u0441\u0442\u0432\u0443\u0435\u0442!"});
            local.setDefault("pbooster.deactivate.KEYS", new String[]{"&4\u0412\u0430\u0448 \u0411\u0443\u0441\u0442\u0435\u0440 \u043a\u043b\u044e\u0447\u0435\u0439 &c%multiplier%x \u0431\u043e\u043b\u044c\u0448\u0435 \u043d\u0435 \u0434\u0435\u0439\u0441\u0442\u0432\u0443\u0435\u0442!"});
            local.save();
            cfg = utils.getConfig();
            this.npcs = new Config("plugins/QuickSell/citizens_npcs.yml");
            if (cfg.contains("options.open-only-shop-with-permission")) {
                cfg.setValue("shop.enable-hierarchy", cfg.getBoolean("options.open-only-shop-with-permission"));
                cfg.setValue("options.open-only-shop-with-permission", null);
                cfg.save();
            }
            if (cfg.contains("boosters.same-multiplier-increases-time")) {
                cfg.setValue("boosters.extension-mode", cfg.getBoolean("boosters.same-multiplier-increases-time"));
                cfg.setValue("boosters.same-multiplier-increases-time", null);
                cfg.save();
            }
            if (cfg.getBoolean("shop.enable-logging")) {
                QuickSell.registerSellEvent((p, type, itemsSold, money) -> {
                    SellProfile profile = SellProfile.getProfile(p);
                    profile.storeTransaction(type, itemsSold, money);
                });
            }
            this.citizens = Main.getInstance().getServer().getPluginManager().isPluginEnabled("Citizens");
            this.reload();
            new SellListener();
            if (this.isCitizensInstalled()) new CitizensListener();

            new BoosterCommand();
            new PrivateBoosterCommand();
            new SellCommand();
            new StopboostersCommand();
            new ThanksCommand();
            new BoostersCommand();

            for (int i = 0; i < 1000; ++i) {
                if (!new File("data-storage/QuickSell/boosters/" + i + ".booster").exists()) continue;
                try {
                    if (new Config(new File("data-storage/QuickSell/boosters/" + i + ".booster")).getBoolean("private")) {
                        new PrivateBooster(i);
                        continue;
                    }
                    new Booster(i);
                    continue;
                }
                catch (ParseException parseException) {
                    // empty catch block
                }
            }
            Main.getInstance().getServer().getScheduler().runTaskTimer(Main.getInstance(), Booster::update, 0L, (long)cfg.getInt("boosters.refresh-every") * 20L);
        }
    }

    public void unregister() {
        cfg = null;
        shop = null;
        local = null;
        events = null;
        for (SellProfile profile : SellProfile.profiles.values()) {
            profile.save();
        }
        SellProfile.profiles = null;
        Booster.active = null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sell")) {
            if (cfg.getBoolean("options.enable-commands")) {
                if (sender instanceof Player) {
                    if (Shop.list().size() == 1) {
                        ShopMenu.open((Player)sender, Shop.list().get(0));
                    } else if (args.length > 0) {
                        Shop shop = Shop.getShop(args[0]);
                        if (shop != null) {
                            if (shop.hasUnlocked((Player)sender)) {
                                ShopMenu.open((Player)sender, shop);
                            } else {
                                local.sendTranslation(sender, "messages.no-access", false, new Variable[0]);
                            }
                        } else {
                            ShopMenu.openMenu((Player)sender);
                        }
                    } else if (cfg.getBoolean("options.open-only-shop-with-permission")) {
                        if (Shop.getHighestShop((Player)sender) != null) {
                            ShopMenu.open((Player)sender, Shop.getHighestShop((Player)sender));
                        } else {
                            local.sendTranslation(sender, "messages.no-access", false, new Variable[0]);
                        }
                    } else {
                        ShopMenu.openMenu((Player)sender);
                    }
                } else {
                    sender.sendMessage(Reference.CONSOLE);
                }
            } else {
                local.sendTranslation(sender, "commands.disabled", false, new Variable[0]);
            }
        } else if (cmd.getName().equalsIgnoreCase("sellall")) {
            if (cfg.getBoolean("options.enable-commands")) {
                if (sender instanceof Player) {
                    if (args.length > 0) {
                        Shop shop = Shop.getShop(args[0]);
                        if (shop != null) {
                            if (shop.hasUnlocked((Player)sender)) {
                                shop.sellall((Player)sender, "", SellEvent.Type.SELLALL);
                            } else {
                                local.sendTranslation(sender, "messages.no-access", false, new Variable[0]);
                            }
                        } else {
                            local.sendTranslation(sender, "messages.unknown-shop", false, new Variable[0]);
                        }
                    } else if (cfg.getBoolean("options.open-only-shop-with-permission")) {
                        if (Shop.getHighestShop((Player)sender) != null) {
                            Shop.getHighestShop((Player)sender).sellall((Player)sender, "", SellEvent.Type.SELLALL);
                        } else {
                            local.sendTranslation(sender, "messages.no-access", false, new Variable[0]);
                        }
                    } else {
                        local.sendTranslation(sender, "commands.sellall.usage", false, new Variable[0]);
                    }
                } else {
                    sender.sendMessage(Reference.CONSOLE);
                }
            } else {
                local.sendTranslation(sender, "commands.disabled", false, new Variable[0]);
            }
        } else if (cmd.getName().equalsIgnoreCase("prices")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("QuickSell.prices")) {
                    if (args.length > 0) {
                        Shop shop = Shop.getShop(args[0]);
                        if (shop != null) {
                            if (shop.hasUnlocked((Player)sender)) {
                                shop.showPrices((Player)sender);
                            } else {
                                local.sendTranslation(sender, "messages.no-access", false, new Variable[0]);
                            }
                        } else {
                            local.sendTranslation(sender, "messages.unknown-shop", false, new Variable[0]);
                        }
                    } else if (cfg.getBoolean("options.open-only-shop-with-permission")) {
                        if (Shop.getHighestShop((Player)sender) != null) {
                            Shop.getHighestShop((Player)sender).showPrices((Player)sender);
                        } else {
                            local.sendTranslation(sender, "messages.no-access", false, new Variable[0]);
                        }
                    } else {
                        local.sendTranslation(sender, "commands.prices.usage", false, new Variable[0]);
                    }
                } else {
                    local.sendTranslation(sender, "commands.prices.permission", false, new Variable[0]);
                }
            } else {
                sender.sendMessage(Reference.CONSOLE);
            }
        } else if (cmd.getName().equalsIgnoreCase("booster")) {
            if (args.length == 4) {
                Booster.BoosterType type;
                Booster.BoosterType boosterType = type = args[0].equalsIgnoreCase("all") ? null : Booster.BoosterType.valueOf(args[0].toUpperCase());
                if ((type != null || args[0].equalsIgnoreCase("all")) && (sender instanceof ConsoleCommandSender || sender.hasPermission("QuickSell.booster"))) {
                    try {
                        if (type != null) {
                            Booster booster = new Booster(type, args[1], Double.valueOf(args[2]), Integer.parseInt(args[3]));
                            booster.activate();
                        }
                        block12: for (Booster.BoosterType bT : Booster.BoosterType.values()) {
                            switch (bT) {
                                case BLOCKS: 
                                case KEYS: {
                                    new Booster(bT, args[1], Double.valueOf(args[2]), Integer.parseInt(args[3])).activate();
                                    continue block12;
                                }
                                default: {
                                    new Booster(bT, args[1], Double.valueOf(args[2]), Integer.parseInt(args[3])).activate();
                                }
                            }
                        }
                    }
                    catch (NumberFormatException x) {
                        sender.sendMessage(Reference.PREFIX + "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 \u00a7c>/booster [\u0442\u0438\u043f] [\u0438\u0433\u0440\u043e\u043a] [\u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c] [\u0434\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c]");
                    }
                } else {
                    local.sendTranslation(sender, "commands.booster.permission", false, new Variable[0]);
                }
            } else {
                sender.sendMessage(Reference.PREFIX + "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 \u00a7c>/booster [\u0442\u0438\u043f] [\u0438\u0433\u0440\u043e\u043a] [\u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c] [\u0434\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c]");
            }
        } else if (cmd.getName().equalsIgnoreCase("pbooster")) {
            if (args.length == 4) {
                Booster.BoosterType type;
                Booster.BoosterType boosterType = type = args[0].equalsIgnoreCase("all") ? null : Booster.BoosterType.valueOf(args[0].toUpperCase());
                if ((type != null || args[0].equalsIgnoreCase("all")) && (sender instanceof ConsoleCommandSender || sender.hasPermission("QuickSell.booster"))) {
                    try {
                        if (type != null) {
                            PrivateBooster booster = new PrivateBooster(type, args[1], Double.valueOf(args[2]), Integer.parseInt(args[3]));
                            booster.activate();
                        }
                        block13: for (Booster.BoosterType bT : Booster.BoosterType.values()) {
                            switch (bT) {
                                case BLOCKS: 
                                case KEYS: {
                                    new PrivateBooster(bT, args[1], Double.valueOf(args[2]), Integer.parseInt(args[3])).activate();
                                    continue block13;
                                }
                                default: {
                                    new PrivateBooster(bT, args[1], Double.valueOf(args[2]), Integer.parseInt(args[3])).activate();
                                }
                            }
                        }
                    }
                    catch (NumberFormatException x) {
                        sender.sendMessage(Reference.PREFIX + "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 \u00a7c>/pbooster [\u0442\u0438\u043f] [\u0438\u0433\u0440\u043e\u043a] [\u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c] [\u0434\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c]");
                    }
                } else {
                    local.sendTranslation(sender, "commands.booster.permission", false, new Variable[0]);
                }
            } else {
                sender.sendMessage(Reference.PREFIX + "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 \u00a7c>/pbooster [\u0442\u0438\u043f] [\u0438\u0433\u0440\u043e\u043a] [\u043c\u043d\u043e\u0436\u0438\u0442\u0435\u043b\u044c] [\u0434\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c]");
            }
        } else if (cmd.getName().equalsIgnoreCase("boosters")) {
            if (sender instanceof Player) {
                BoosterMenu.showBoosterOverview((Player)sender);
            } else {
                sender.sendMessage(Reference.CONSOLE);
            }
        } else if (cmd.getName().equalsIgnoreCase("quicksell")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("QuickSell.manage")) {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        this.reload();
                        local.sendTranslation(sender, "commands.reload.done", false, new Variable[0]);
                    } else if (args[0].equalsIgnoreCase("editor")) {
                        if (sender instanceof Player) {
                            this.editor.openEditor((Player)sender);
                        }
                    } else if (args[0].equalsIgnoreCase("edit")) {
                        sender.sendMessage(Reference.PREFIX + "\u00a7c\u0420\u0415\u041a\u041e\u041c\u0415\u041d\u0414\u0423\u0415\u0422\u0421\u042f! \u0412 \u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0438\u0439 \u0440\u0430\u0437 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 \u00a76/quicksell editor");
                        if (args.length == 4) {
                            if (Shop.getShop(args[1]) != null) {
                                boolean number = true;
                                try {
                                    Double.valueOf(args[3]);
                                }
                                catch (NumberFormatException x) {
                                    number = false;
                                }
                                if (number) {
                                    cfg.setValue("shops." + args[1] + ".price." + args[2].toUpperCase(), (Object)Double.valueOf(args[3]));
                                    cfg.save();
                                    this.reload();
                                    local.sendTranslation(sender, "commands.price-set", false, new Variable[]{new Variable("%item%", args[2]), new Variable("%shop%", args[1]), new Variable("%price%", args[3])});
                                } else {
                                    local.sendTranslation(sender, "commands.usage", false, new Variable[]{new Variable("%usage%", "/quicksell edit <ShopName> <Item> <Price>")});
                                }
                            } else {
                                local.sendTranslation(sender, "messages.unknown-shop", false, new Variable[0]);
                            }
                        } else {
                            local.sendTranslation(sender, "commands.usage", false, new Variable[]{new Variable("%usage%", "/quicksell edit <ShopName> <Item> <Price>")});
                        }
                    } else if (args[0].equalsIgnoreCase("create")) {
                        sender.sendMessage(Reference.PREFIX + "\u00a7c\u0420\u0415\u041a\u041e\u041c\u0415\u041d\u0414\u0423\u0415\u0422\u0421\u042f! \u0412 \u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0438\u0439 \u0440\u0430\u0437 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 \u00a76/quicksell editor");
                        if (args.length == 2) {
                            ArrayList<String> list = new ArrayList<String>();
                            for (Shop shop : Shop.list()) {
                                list.add(shop.getID());
                            }
                            list.add(args[1]);
                            cfg.setValue("list", list);
                            cfg.save();
                            this.reload();
                            local.sendTranslation(sender, "commands.shop-created", false, new Variable[]{new Variable("%shop%", args[1])});
                        } else {
                            local.sendTranslation(sender, "commands.usage", false, new Variable[]{new Variable("%usage%", "/quicksell create <ShopName>")});
                        }
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        sender.sendMessage(Reference.PREFIX + "\u00a7c\u0420\u0415\u041a\u041e\u041c\u0415\u041d\u0414\u0423\u0415\u0422\u0421\u042f! \u0412 \u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0438\u0439 \u0440\u0430\u0437 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0439\u0442\u0435 \u00a76/quicksell editor");
                        if (Shop.getShop(args[1]) == null) {
                            local.sendTranslation(sender, "messages.unknown-shop", false, new Variable[0]);
                        } else if (args.length == 2) {
                            ArrayList<String> list = new ArrayList<String>();
                            for (Shop shop : Shop.list()) {
                                list.add(shop.getID());
                            }
                            list.remove(args[1]);
                            cfg.setValue("list", list);
                            cfg.save();
                            this.reload();
                            local.sendTranslation(sender, "commands.shop-deleted", false, new Variable[]{new Variable("%shop%", args[1])});
                        } else {
                            local.sendTranslation(sender, "commands.usage", false, new Variable[]{new Variable("%usage%", "/quicksell delete <ShopName>")});
                        }
                    } else if (args[0].equalsIgnoreCase("linknpc")) {
                        if (!this.isCitizensInstalled()) {
                            sender.sendMessage(Reference.PREFIX + "\u00a7c\u0423 \u0412\u0430\u0441 \u043d\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d Citizens!");
                            return true;
                        }
                        if (args.length == 3) {
                            NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
                            if (npc == null) {
                                sender.sendMessage("\u00a7cYou must select an NPC before linking it!");
                            } else if (Shop.getShop(args[1]) == null) {
                                local.sendTranslation(sender, "messages.unknown-shop", false, new Variable[0]);
                            } else if (args[2].equalsIgnoreCase("sell") || args[2].equalsIgnoreCase("sellall")) {
                                this.npcs.setValue(String.valueOf(npc.getId()), (Object)(args[1] + " ; " + args[2].toUpperCase()));
                                this.npcs.save();
                                sender.sendMessage(npc.getName() + " \u00a77\u0442\u0435\u043f\u0435\u0440\u044c \u043f\u0435\u0440\u0435\u043f\u0440\u0438\u0432\u044f\u0437\u0430\u043d \u043a \u00a7r" + StringUtils.format((String)args[2]) + "\u00a77\u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0443 \u0434\u043b\u044f \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0430 \u00a7r" + args[1] + "\u00a77");
                            }
                        } else {
                            local.sendTranslation(sender, "commands.usage", false, new Variable[]{new Variable("%usage%", "/quicksell linknpc <ShopName> <sell/sellall>")});
                        }
                    } else if (args[0].equalsIgnoreCase("unlinknpc")) {
                        if (!this.isCitizensInstalled()) {
                            sender.sendMessage(Reference.PREFIX + "\u00a7c\u0423 \u0432\u0430\u0441 \u043d\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d Citizens!");
                            return true;
                        }
                        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
                        if (npc == null) {
                            sender.sendMessage(Reference.PREFIX + "\u00a76\u0412\u044b \u0434\u043e\u043b\u0436\u043d\u044b \u0432\u044b\u0431\u0440\u0430\u0442\u044c NPC, \u043f\u0440\u0435\u0436\u0434\u0435 \u0447\u0435\u043c \u043f\u0440\u0438\u0432\u044f\u0437\u0430\u0442\u044c \u0435\u0433\u043e \u043a \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0443!");
                        } else if (this.npcs.contains(String.valueOf(npc.getId()))) {
                            this.npcs.setValue(String.valueOf(npc.getId()), null);
                            this.npcs.save();
                            sender.sendMessage(npc.getName() + " \u00a77\u0442\u0435\u043f\u0435\u0440\u044c \u043f\u0435\u0440\u0435\u043f\u0440\u0438\u0432\u044f\u0437\u0430\u043d \u043a \u00a7r" + StringUtils.format((String)args[2]) + "\u00a77\u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0443 \u0434\u043b\u044f \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0430 \u00a7r" + args[1] + "\u00a77");
                        } else {
                            sender.sendMessage(npc.getName() + " \u00a76\u043d\u0435 \u043f\u0440\u0438\u0432\u044f\u0437\u0430\u043d \u043d\u0435 \u043a \u043e\u0434\u043d\u043e\u043c\u0443 \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0443!");
                        }
                    } else if (args[0].equalsIgnoreCase("stopboosters")) {
                        if (args.length == 2) {
                            Iterator<Booster> boosters = Booster.iterate();
                            while (boosters.hasNext()) {
                                Booster booster = boosters.next();
                                if (!booster.getAppliedPlayers().contains(args[1])) continue;
                                boosters.remove();
                                booster.deactivate();
                            }
                            local.sendTranslation(sender, "booster.reset", false, new Variable[]{new Variable("%player%", args[1])});
                        } else if (args.length == 1) {
                            Iterator<Booster> boosters = Booster.iterate();
                            while (boosters.hasNext()) {
                                Booster booster = boosters.next();
                                boosters.remove();
                                booster.deactivate();
                            }
                            local.sendTranslation(sender, "boosters.reset", false, new Variable[0]);
                        } else {
                            local.sendTranslation(sender, "commands.usage", false, new Variable[]{new Variable("%usage%", "/quicksell stopboosters <Player>")});
                        }
                    } else {
                        this.sendHelpMessager(sender);
                    }
                } else {
                    this.sendHelpMessager(sender);
                }
            } else {
                local.sendTranslation(sender, "commands.permission", false, new Variable[0]);
            }
        }
        return true;
    }

    private void sendHelpMessager(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("\u00a7e\u00a7lPrisonQuickSell v" + Main.getInstance().getDescription().getVersion() + " \u043e\u0442 \u00a76Shinigami_03");
        sender.sendMessage("");
        sender.sendMessage("\u00a77\u21e8 /quicksell: \u00a7b\u041e\u0442\u043e\u0431\u0440\u0430\u0436\u0430\u0435\u0442 \u044d\u0442\u043e \u041c\u0435\u043d\u044e \u043f\u043e\u0434\u0434\u0435\u0440\u0436\u043a\u0438");
        sender.sendMessage("\u00a77\u21e8 /quicksell reload: \u00a7b\u041f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0436\u0430\u0435\u0442 \u0432\u0441\u0435 \u0444\u0430\u0439\u043b\u044b \u0438 \u0441\u0430\u043c PrisonQuickSell");
        sender.sendMessage("\u00a77\u21e8 /quicksell editor: \u00a7b\u041e\u0442\u0440\u044b\u0432\u0430\u0435\u0442 \u0412\u043d\u0443\u0442\u0440\u0438\u0438\u0433\u0440\u043e\u0432\u043e\u0439 \u0420\u0435\u0434\u0430\u043a\u0442\u043e\u0440 \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0430");
        sender.sendMessage("\u00a77\u21e8 /quicksell stopboosters [\u0438\u0433\u0440\u043e\u043a]: \u00a7b\u041e\u0441\u0442\u0430\u043d\u0430\u0432\u043b\u0438\u0432\u0430\u0435\u0442 \u043d\u0435\u043a\u043e\u0442\u043e\u0440\u044b\u0435 \u0411\u0443\u0441\u0442\u0435\u0440\u044b");
        if (QuickSell.getInstance().isCitizensInstalled()) {
            sender.sendMessage("\u00a77\u21e8 /quicksell linknpc [\u043c\u0430\u0433\u0430\u0437\u0438\u043d] [sell/sellall]: \u00a7b\u041f\u0440\u0438\u0432\u044f\u0437\u044b\u0432\u0430\u0435\u0442 NPC \u043a \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0443");
            sender.sendMessage("\u00a77\u21e8 /quicksell unlinknpc: \u00a7b\u041e\u0442\u0432\u044f\u0437\u044b\u0432\u0430\u0435\u0442 \u0432\u044b\u0434\u0435\u043b\u0435\u043d\u043d\u043e\u0433\u043e NPC \u043e\u0442 \u041c\u0430\u0433\u0430\u0437\u0438\u043d\u0430");
        }
    }

    public boolean isCitizensInstalled() {
        return this.citizens;
    }

    public boolean isPrisonUtilsInstalled() {
        return this.backpacks;
    }

    public boolean isMCMMOInstalled() {
        return this.mcmmo;
    }

    public void reload() {
        cfg.reload();
        Shop.reset();
        for (String shop : cfg.getStringList("list")) {
            if (!shop.equalsIgnoreCase("")) {
                cfg.setDefaultValue("shops." + shop + ".name", (Object)("&9" + shop));
                cfg.setDefaultValue("shops." + shop + ".amount", (Object)1);
                cfg.setDefaultValue("shops." + shop + ".itemtype", (Object)"CHEST");
                cfg.setDefaultValue("shops." + shop + ".lore", new ArrayList());
                cfg.setDefaultValue("shops." + shop + ".permission", (Object)("QuickSell.shop." + shop));
                cfg.setDefaultValue("shops." + shop + ".inheritance", new ArrayList());
                if (cfg.getBoolean("options.pregenerate-all-item-prices")) {
                    for (Material m : Material.values()) {
                        if (m == Material.AIR) continue;
                        cfg.setDefaultValue("shops." + shop + ".price." + m.toString(), (Object)0.0);
                    }
                } else {
                    cfg.setDefaultValue("shops." + shop + ".price.COBBLESTONE", (Object)0.0);
                }
                new Shop(shop);
                continue;
            }
            new Shop();
        }
        cfg.save();
    }

    public static void registerSellEvent(SellEvent event) {
        events.add(event);
    }

    public static List<SellEvent> getSellEvents() {
        return events;
    }

    public static QuickSell getInstance() {
        return instance;
    }
}

