package sexy.criss.game.prison.boosters;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.PluginUtils;
import org.bukkit.plugin.Plugin;
import sexy.criss.game.prison.Main;

public class Reference {
    private static final Config config;
    public static final String ACCESS_DENIED;
    public static final String PREFIX = "§8[§6Prison§8] §f» ";
    public static String SUBPREFIX;
    public static final String SERVER;
    public static final String CONSOLE = "§7Prison §c» §f\u0422\u043e\u043b\u044c\u043a\u043e \u0438\u0433\u0440\u043e\u043a\u0438 \u043c\u043e\u0433\u0443\u0442 \u0432\u044b\u043f\u043e\u043b\u043d\u044f\u0442\u044c \u044d\u0442\u0443 \u043a\u043e\u043c\u0430\u043d\u0434\u0443!";

    static {
        config = new PluginUtils((Plugin)QuickSell.getInstance()).getConfig();
        ACCESS_DENIED = Reference.config.getString("options.access_denied").replace('&', '§');
        Reference.SUBPREFIX = "%s§f» ";
        SERVER = "§c| §f";
    }
}

