package sexy.criss.game.prison.bosses.mobs.animals.poo;

import net.minecraft.server.v1_16_R3.*;
import sexy.criss.game.prison.bosses.interfaces.IBossParameters;
import sexy.criss.game.prison.bosses.mobs.util.BossUtil;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.gen.util.Component;
import sexy.criss.gen.util.UNMS;
import sexy.criss.gen.util.Util;

import java.util.Random;

public class PooCow extends EntityCow {
    Spawner spawner;
    Random rand = new Random();
    private int t;

    public PooCow(Spawner spawner) {
        super(EntityTypes.COW, UNMS.getWorldServer(spawner.getSpawnLocation()));
        this.setCustomName(Component.of(getName()));
        this.setCustomNameVisible(true);

        t = 1200 + (rand.nextInt(250));

        this.spawner = spawner;
        spawner.register(this);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public void tick() {
        if(t-- <= 0) {
            BossUtil.DROP_POO(getBukkitEntity());
            t = 1200 + (rand.nextInt(250));
        }
        super.tick();
    }

    public ChatComponentText getCustomerName() {
        return Component.of("&6Корова");
    }

    public double getSpeed() {
        return .1;
    }

    public double getAttackDamage() {
        return 0;
    }

    public double getGeneralHealth() {
        return 1;
    }
}
