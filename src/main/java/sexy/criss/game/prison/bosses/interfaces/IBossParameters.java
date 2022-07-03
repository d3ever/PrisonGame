package sexy.criss.game.prison.bosses.interfaces;

import net.minecraft.server.v1_16_R3.ChatComponentText;

public interface IBossParameters {

    ChatComponentText getCustomerName();
    double getSpeed();
    double getAttackDamage();
    float getGeneralHealth();

}
