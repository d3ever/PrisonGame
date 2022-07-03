package sexy.criss.game.prison.donate_part.util;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_16_R3.SoundCategory;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.CraftSound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.api.inventory.DynamicInventory;
import sexy.criss.gen.api.inventory.DynamicItem;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.util.LoreCreator;
import sexy.criss.gen.util.Util;
import sexy.criss.gen.util.UtilPlayer;

import java.util.List;

public abstract class DonateButton {
    private boolean check;
    private final String name;
    private final ItemStack icon;
    private final int price;
    private final List<String> lore;

    public DonateButton(String name, ItemStack icon, List<String> lore, int price) {
        this.check = false;
        this.name = name;
        this.icon = icon;
        this.price = price;
        this.lore = lore;
    }

    public DonateButton(boolean check, String name, ItemStack icon, List<String> lore, int price) {
        this.check = check;
        this.name = name;
        this.icon = icon;
        this.price = price;
        this.lore = lore;
    }

    public DonateButton(ItemStack icon, int price) {
        this("", icon, Lists.newArrayList(), price);
    }

    public DynamicItem build(DynamicInventory gui, int row, int slot) {
        return new DynamicItem(Util.format(this.icon, this.name, new LoreCreator()
                .addLines(this.lore)
                .addEmpty()
                .addLineIf(this.check, "&6недоступно")
                .addLineIf(!this.check, "&fСтоимость: &6"+this.price).build(), true), (p, clickType, slot1) -> {
            if(this.check) return;
            p.closeInventory();
            PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
            RPlayer rp = RPlayer.get(p);
            int balance = rp.getCoins().getCoins();
            if(balance < this.price && !rp.isDeveloper()) {
                Util.ps("Prison", p, "&fУ вас недостаточно монет. Получите подробную информацию");
                Util.s(p, "        &fна сайте &6www.rewforce.xyz&f.");
                return;
            }
            if(!rp.isDeveloper()) rp.getCoins().takeCoins(this.price);
            Util.ps("Prison", p, "&fВы приобрели &6%s&f стоимостью &6%d&f.", this.name, price);
            Util.s(p, "&fМы выражаем свою благодарность за вложение.");
            UtilPlayer.getNMSPlayer(p).playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(CraftSound.getSoundEffect(Sound.ENTITY_CAT_PURREOW), SoundCategory.AMBIENT, p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 1f, 1f));
            sendParticleEffect(p);
            reward(p, pp, rp);
            gui.addItem(row, slot, build(gui, row, slot));
        });
    }

    private void sendParticleEffect(Player p) {
        new BukkitRunnable() {
            double t = 0;
            @Override
            public void run() {
                t += Math.PI/16;
                Location loc = p.getLocation();
                for(double q = 0; q <= 2 * Math.PI; q += Math.PI/2) {
                    double x = 0.3 * (4 * Math.PI - t) * Math.cos(t + q),
                    y = 0.2 * t,
                    z = 0.3 * (4 * Math.PI - t) * Math.sin(t + q);
                    loc.add(x, y, z);
                    Util.sendParticle(Particle.FIREWORKS_SPARK, loc, 0, 0, 0, 0, 1);
                    loc.subtract(x, y, z);

                    if(t >= 4 * Math.PI) {
                        loc.add(x, y, z);
                        Util.sendParticle(Particle.SOUL_FIRE_FLAME, loc, 0, 0, 0, .01f, 50);
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 100);
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }

    public abstract void reward(Player p, PrisonPlayer pp, RPlayer rp);

}
