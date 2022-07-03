package sexy.criss.game.prison.bosses;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import sexy.criss.game.prison.bosses.mobs.animals.poo.PooChicken;
import sexy.criss.game.prison.bosses.mobs.animals.poo.PooRabbit;
import sexy.criss.game.prison.bosses.mobs.animals.working.MeatPig;
import sexy.criss.game.prison.bosses.mobs.animals.poo.PooCow;
import sexy.criss.game.prison.bosses.mobs.animals.working.SheepMob;
import sexy.criss.game.prison.bosses.mobs.boss.*;
import sexy.criss.game.prison.bosses.updater.Spawner;

public enum EntityTypes {
    SPIDER("Паук", 52, SpiderBoss.class),
    RAT("Крыса", 60, Rat.class),
    COW("Корова", 92, PooCow.class),
    PIG("Поросёнок", 90, MeatPig.class),
    CHICKEN("Курица", 93, PooChicken.class),
    RABBIT("Заяц", 101, PooRabbit.class),
    SHEEP("Овца", 91, SheepMob.class),
    ZOMBIE("Zombie", 54, ZombieMob.class),
    WITCH("Witch", 66, WithBoss.class),
    SLIME("Slime", 55, SlimeBoss.class),
    GUARDIAN("Guardnian", 4, GuardianBoss.class),
    GIANT("Giant", 53, GiantBoss.class);

    private static final Map<Integer, UUID> assoc = new HashMap<>();
    String name;
    int id;
    Class<? extends Entity> custom;

    EntityTypes(String name, int id, Class<? extends Entity> custom) {
       this.name = name;
       this.id = id;
       this.custom = custom;
   }

    public static Entity spawnEntity(Entity entity, Location loc) {
       entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
       ((CraftWorld)loc.getWorld()).getHandle().addEntity(entity);
       return entity;
   }

    public static void spawnEntity(EntityTypes entityType, Location loc, Spawner spawner) {
       try {
          Entity entity = entityType.getEntityClass().getConstructor(Spawner.class).newInstance(spawner);
          entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
          ((CraftWorld)loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
       } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException ex) {
          ex.printStackTrace();
       }
    }

    public static void registerAll() {
       Arrays.asList(values()).forEach(EntityTypes::addToMaps);
    }

    private static void addToMaps(EntityTypes type) {
       ((Map)getPrivateField("d", net.minecraft.server.v1_16_R3.EntityTypes.class, null)).put(type.custom, type.name);
       ((Map)getPrivateField("f", net.minecraft.server.v1_16_R3.EntityTypes.class, null)).put(type.custom, type.id);
    }

    public static Object getPrivateField(String fieldName, Class<?> clazz, PathfinderGoalSelector object) {
       Object o = null;

       try {
          Field field = clazz.getDeclaredField(fieldName);
          field.setAccessible(true);
          o = field.get(object);
       } catch (NoSuchFieldException | IllegalAccessException ex) {
          ex.printStackTrace();
       }
       return o;
    }

    public String getName() {
      return this.name;
   }

    public Class<? extends Entity> getEntityClass() {
      return this.custom;
   }

    public static void associate(Entity entity, Spawner spawner) {
      assoc.put(entity.hashCode(), spawner.getUid());
   }

    public static Spawner getSpawnerByEntity(Entity entity) {
       return Spawner.spawners.get(assoc.get(entity.hashCode()));
    }
}