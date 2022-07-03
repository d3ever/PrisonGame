package sexy.criss.game.prison.bossbar;

import com.google.common.collect.Lists;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.gen.util.colors.compare.AdvancedColor;
import sexy.criss.gen.util.colors.compare.builders.GradientTextBuilder;

import java.util.List;

public class SexyBossBar extends BukkitRunnable {
    BossBar bar;

    public SexyBossBar(BossBar bar) {
        this.bar = bar;
    }

    List<String> ani = Lists.newArrayList(
            "> > > REWFORCE NETWORK < < <"
    );

    int i = 0;
    int max = Integer.MAX_VALUE;
    float[] f = new float[] {0f, 0.14f, 0.3f, 0.4f};

    @Override
    public void run() {
        if(i >= max) i = 0;
        bar.setTitle(new GradientTextBuilder().text("REWFORCE Network")
                .addColor(new AdvancedColor(AdvancedColor.hsl2rgb(f[0], 1, 1)))
                .addColor(new AdvancedColor(AdvancedColor.hsl2rgb(f[1], 1, 1)))
                .addColor(new AdvancedColor(AdvancedColor.hsl2rgb(f[2], 1, 1)))
                .addColor(new AdvancedColor(AdvancedColor.hsl2rgb(f[3], 1, 1)))
                .blur(0.3).build().getRenderedText());
        f[0] += 0.01f;
        f[1] += 0.01f;
        f[2] += 0.01f;
        f[3] += 0.01f;
        i++;
    }
}
