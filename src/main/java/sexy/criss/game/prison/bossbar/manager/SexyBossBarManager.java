package sexy.criss.game.prison.bossbar.manager;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class SexyBossBarManager {
    public static BossBar BOSS_BAR;

    public static void CREATE_BOSS_BAR() {
        BOSS_BAR = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
    }
}
