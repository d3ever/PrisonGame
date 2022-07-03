package sexy.criss.game.prison.bosses.mobs.util;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.gen.util.Util;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BossUtil {

    public static void DROP_POO(CraftEntity e) {
        e.getWorld().dropItemNaturally(e.getLocation(), PrisonItem.getItem("poo"));
    }

    public static void superJump(CraftEntity ent) {
        ent.getWorld().playEffect(ent.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
        ent.setVelocity(new Vector(0, 1, 0).multiply(1));
        List<Entity> near = ent.getNearbyEntities(7, 4, 7);
        near.stream().filter(e -> e instanceof Player).forEach(t -> Util.s(t, ent.getCustomName() + " -> &fПолетаем?"));

        new BukkitRunnable(){
            @Override
            public void run() {
                AtomicReference<EntityDamageEvent> event = new AtomicReference<>();
                if(near.size() > 0) {
                    near.stream().filter(e -> e instanceof Player).forEach(p -> {
                        p.setVelocity(p.getVelocity().add(p.getLocation().getDirection()).multiply(-3));
                        event.set(new EntityDamageEvent(p, EntityDamageEvent.DamageCause.CUSTOM, 5));
                        Bukkit.getPluginManager().callEvent(event.get());
                        if(!event.get().isCancelled()) ((Player) p).damage(5, ent);
                    });
                }
            }
        }.runTaskLater(Main.getInstance(), 25);
    }

}
