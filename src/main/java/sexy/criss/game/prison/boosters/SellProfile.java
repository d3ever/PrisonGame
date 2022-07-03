package sexy.criss.game.prison.boosters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.entity.Player;

public class SellProfile {
    public static Map<UUID, SellProfile> profiles = new HashMap<UUID, SellProfile>();
    UUID uuid;
    Config cfg;
    List<String> transactions;

    public SellProfile(Player p) {
        this.uuid = p.getUniqueId();
        this.transactions = new ArrayList<String>();
        this.cfg = new Config(new File("data-storage/QuickSell/transactions/" + p.getUniqueId() + ".log"));
        profiles.put(this.uuid, this);
        if (QuickSell.cfg.getBoolean("shop.enable-logging")) {
            for (String transaction : this.cfg.getKeys()) {
                this.transactions.add(this.cfg.getString(transaction));
            }
        }
    }

    public static SellProfile getProfile(Player p) {
        return profiles.containsKey(p.getUniqueId()) ? profiles.get(p.getUniqueId()) : new SellProfile(p);
    }

    public void unregister() {
        this.save();
        profiles.remove(this.uuid);
    }

    public void save() {
        this.cfg.save();
    }

    public void storeTransaction(SellEvent.Type type, int soldItems, double money) {
        long timestamp = System.currentTimeMillis();
        String string = String.valueOf(timestamp) + " __ " + type.toString() + " __ " + String.valueOf(soldItems) + " __ " + String.valueOf(money);
        this.cfg.setValue(String.valueOf(timestamp), (Object)string);
        this.transactions.add(string);
    }

    public List<String> getTransactions() {
        return this.transactions;
    }

    public Transaction getRecentTransactions(int amount) {
        int items = 0;
        double money = 0.0;
        for (int i = this.transactions.size() - amount; i < this.transactions.size(); ++i) {
            items += Transaction.getItemsSold(this.transactions.get(i));
            money += Transaction.getMoney(this.transactions.get(i));
        }
        return new Transaction(System.currentTimeMillis(), SellEvent.Type.UNKNOWN, items, money);
    }
}

