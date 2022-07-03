package sexy.criss.game.prison.connection;

import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.logger.PrisonLoggType;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.MultiUtils;

import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class DataSource {
    private Connection connection;

    public DataSource() {
    }

    public void createTable() {
        MultiUtils.getGamesConnector().query("CREATE TABLE IF NOT EXISTS prison_data (UUID VARCHAR(100), MONEY float(53), LEVEL int, EXP int, KILLS int, DEATHS int, TOTAL_BLOCKS int, FACTION VARCHAR(100), BLOCK_LOG VARCHAR(6666), PASSIVES VARCHAR(100), ACCESS VARCHAR(100), NAME VARCHAR(100), PROMO VARCHAR(100), DROP_KEYS int, USE_KEYS int, MOB_LOG VARCHAR(100), AUTOSELL int)");
        MultiUtils.getGamesConnector().query("CREATE TABLE IF NOT EXISTS prison_achievements (name VARCHAR(100), achievements VARCHAR(6666))");
        MultiUtils.getGamesConnector().query("CREATE TABLE IF NOT EXISTS prison_quests (name VARCHAR(100), quests VARCHAR(6666))");
        Main.getInstance().getLog().log(PrisonLoggType.SUCCESSFULLY, "MySQL table updated");
    }

    public void loadPlayer(UUID uuid) {
        try {
//            PreparedStatement pr = connection.prepareStatement(GET_DATA);
//            pr.setString(1, uuid.toString());
            ResultSet r = MultiUtils.getGamesConnector().query("SELECT MONEY, LEVEL, EXP, KILLS, DEATHS, TOTAL_BLOCKS, FACTION, BLOCK_LOG, PASSIVES, ACCESS, NAME, PROMO, DROP_KEYS, USE_KEYS, MOB_LOG, AUTOSELL FROM prison_data WHERE UUID='%s'", uuid.toString());
            if(r.next()) {
                double balance = DoubleHandler.fixDouble(Double.parseDouble(r.getString(1)));
                int level = r.getInt(2);
                int exp = r.getInt(3);
                int kills = r.getInt(4);
                int deaths = r.getInt(5);
                int total_blocks = r.getInt(6);
                String faction = r.getString(7);
                String block_log = r.getString(8);
                String passives = r.getString(9);
                String access = r.getString(10);
                String name = r.getString(11);
                String promo = r.getString(12);
                int dk = r.getInt(13);
                int uk = r.getInt(14);
                String mobs = r.getString(15);
                int as = r.getInt(16);
                new PrisonPlayer(uuid, balance, level, exp, kills, deaths, total_blocks, faction, block_log, passives, access, name, promo, dk, uk, mobs, as);
            } else {
                MultiUtils.getGamesConnector().query("INSERT INTO prison_data (UUID, MONEY, LEVEL, EXP, KILLS, DEATHS, TOTAL_BLOCKS, FACTION, BLOCK_LOG, PASSIVES, ACCESS, NAME, PROMO, DROP_KEYS, USE_KEYS, MOB_LOG, AUTOSELL) VALUES ('%s', '%s', '%d', '%d', '%d', '%d', '%d', '%s', '%s', '%s', '%s', '%s', '%s', '%d', '%d', '%s', '%d')",
                        uuid.toString(), "0", 1, 0, 0, 0, 0, "", "", "", "", Bukkit.getOfflinePlayer(uuid).getName(), "", 0, 0, "", 0);
                new PrisonPlayer(uuid, 0, 1, 0, 0, 0, 0, "", "", "", "", Bukkit.getOfflinePlayer(uuid).getName(), "", 0, 0, "", 0);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updatePlayer(UUID uuid) {
        PrisonPlayer p = PrisonPlayer.getPlayer(uuid);
        String block_log = "";
        String mob_log = "";
        String passives = "";
        String promo = "";
        StringBuilder access = new StringBuilder();

        if(p.getPromo().size() > 0) {
            for(String id : p.getPromo()) {
                promo = promo.concat(",").concat(id);
            }
            promo = promo.substring(1);
        }

        if(p.getBlock_log().size() > 0) {
            for (Map.Entry<Material, Integer> entry : p.getBlock_log().entrySet())
                block_log = block_log.concat(",").concat(entry.getKey().toString()).concat("=").concat(entry.getValue().toString());
            block_log = block_log.substring(1);
        }
        if(p.getPassives().size() > 0) {
            for(Map.Entry<String, Integer> entry : p.getPassives().entrySet()) {
                passives = passives.concat(",").concat(entry.getKey()).concat("=").concat(entry.getValue().toString());
            }
            passives = passives.substring(1);
        }

        if(p.getMob_log().size() > 0) {
            for(Map.Entry<EntityType, Integer> entry : p.getMob_log().entrySet()) {
                mob_log = mob_log.concat(",").concat(entry.getKey().name()).concat("=").concat(entry.getValue().toString());
            }
            mob_log = mob_log.substring(1);
        }

        if(p.getAccess().size() > 0) p.getAccess().forEach(x -> access.append(x).append(","));

        String faction = p.hasFraction() ? p.getFraction().getId() : "";
        MultiUtils.getGamesConnector().addToQueue("UPDATE prison_data SET MONEY='%s', LEVEL='%d', EXP='%d', KILLS='%d', DEATHS='%d', TOTAL_BLOCKS='%d', FACTION='%s', BLOCK_LOG='%s', PASSIVES='%s', ACCESS='%s', NAME='%s', PROMO='%s', DROP_KEYS='%d', USE_KEYS='%d', MOB_LOG='%s', AUTOSELL='%d' WHERE UUID='%s'",
        String.valueOf(DoubleHandler.fixDouble(p.getBalance())), p.getLevel(), p.getExp(), p.getKills(), p.getDeaths(), p.getTotalBlocks(), faction, block_log, passives, access.toString().length() > 0 ? access.toString().substring(0, access.length() - 1) : access.toString(), p.getCustomName(), promo, p.getDroppedKeys(), p.getUsedKeys(), mob_log, p.hasAutoSell() ? 1 : 0, uuid.toString());
    }

}
