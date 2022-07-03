package sexy.criss.game.prison.bosses.updater;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Maps;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Entity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.bosses.EntityTypes;
import sexy.criss.gen.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Spawner {

   private Hologram holo;
   public static Map<UUID, Spawner> spawners = Maps.newHashMap();
   private Location location;
   private EntityTypes type;
   private Entity current;
   private long deathTime;
   private int interval;
   private UUID uid;


   public Spawner(Location location, EntityTypes type, int interval) {
      this.location = location;
      this.current = null;
      this.deathTime = -1L;
      this.uid = UUID.randomUUID();
      this.type = type;
      this.interval = interval;
      spawners.put(this.uid, this);
      this.spawn();
   }

   public void spawn() {
      if(holo != null) {
         holo.clearLines();
         holo.delete();
         holo = null;
      }
      if(!this.location.getChunk().isLoaded()) this.location.getChunk().load();
      if(this.location.getChunk().isLoaded()) {
         EntityTypes.spawnEntity(this.type, this.location.clone().add(0.0D, 1.0D, 0.0D), this);
      }

   }

   public void iDead() {
      this.current = null;
      this.deathTime = System.currentTimeMillis() / 1000L;
      holo = HologramsAPI.createHologram(Main.getInstance(), location.clone().add(0, 2, 0));
      Calendar cal = Util.getCalendar(System.currentTimeMillis());
      cal.add(Calendar.SECOND, interval);

      final long time = Util.getTime(cal);
      new BukkitRunnable(){
         @Override
         public void run() {
            long l1 = time/1000;
            long l2 = System.currentTimeMillis()/1000;
            long res = l1-l2;
            if(res <= 0) {
               holo.clearLines();
               holo.delete();
               update();
               this.cancel();
               return;
            }
            holo.clearLines();
            holo.appendTextLine(Util.f("&6Появится через " + (l1-l2)));
         }
      }.runTaskTimer(Main.getInstance(), 0, 20);
//      Calendar cal = Util.getCalendar(System.currentTimeMillis());
//      SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//      holo.appendTextLine(Util.f("&6Был убит в " + df.format(cal.getTime())));
//      cal.add(Calendar.SECOND, interval);
//      holo.appendTextLine(Util.f("&6Будет новый в "+df.format(cal.getTime())));
   }

   public Location getSpawnLocation() {
      return this.location;
   }

   public void update() {
      if(this.current == null) {
         if (System.currentTimeMillis() / 1000L - this.deathTime >= (long) this.interval) this.spawn();
      }else if(this.current.getBukkitEntity().getLocation().distance(this.location) > 50) this.reset();
   }

   public void register(Entity me) {
      this.current = me;
   }

   public void reset() {
      if(holo != null) {
         holo.clearLines();
         holo.delete();
         holo = null;
      }
      if(this.current != null) {
         this.current.getBukkitEntity().remove();
      }

      this.deathTime = -1L;
      this.getCurrent().getBukkitEntity().teleport(getSpawnLocation());
//      this.getCurrent().teleportTo(((CraftWorld) getSpawnLocation().getWorld()).getHandle(), new BlockPosition(getSpawnLocation().getBlockX(), getSpawnLocation().getY(), getSpawnLocation().getZ()));
   }

   public UUID getUid() {
      return this.uid;
   }

   public Entity getCurrent() {
      return this.current;
   }

   public EntityTypes getType() {
      return this.type;
   }
}