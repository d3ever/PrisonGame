package sexy.criss.game.prison.bosses.mobs.animals.poo;

import net.minecraft.server.v1_16_R3.*;
import sexy.criss.game.prison.bosses.interfaces.IBossParameters;
import sexy.criss.game.prison.bosses.mobs.util.BossUtil;
import sexy.criss.game.prison.bosses.updater.Spawner;
import sexy.criss.gen.util.Component;
import sexy.criss.gen.util.UNMS;

public class PooChicken extends EntityChicken {
    Spawner spawner;
    private int t;

    public PooChicken(Spawner spawner) {
        super(EntityTypes.CHICKEN, UNMS.getWorldServer(spawner.getSpawnLocation()));

        this.setCustomName(getCustomerName());
        this.setCustomNameVisible(true);

        t = 1200 + (getRandom().nextInt(200));

        this.spawner = spawner;
        spawner.register(this);
    }

    @Override
    public void tick() {
        if(t <= 0) {
            BossUtil.DROP_POO(getBukkitEntity());
            t = 1200 + getRandom().nextInt(200);
        }
        t--;
        super.tick();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    public ChatComponentText getCustomerName() {
        return Component.of("&6Курица");
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
