/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package sexy.criss.game.prison.boosters;

import org.bukkit.entity.Player;

public interface SellEvent {
    public void onSell(Player var1, Type var2, int var3, double var4);

    public static enum Type {
        SELL,
        SELLALL,
        AUTOSELL,
        CITIZENS,
        UNKNOWN;

    }
}

