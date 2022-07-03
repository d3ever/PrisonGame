package sexy.criss.game.prison.prison_data;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AtomicDouble;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.google.common.collect.Maps;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.achievements.Achievement;
import sexy.criss.game.prison.achievements.Achievements;
import sexy.criss.game.prison.boosters.Booster;
import sexy.criss.game.prison.language.Reference;
import sexy.criss.game.prison.prison_data.data.factions.Faction;
import sexy.criss.game.prison.prison_data.data.levels.Levels;
import sexy.criss.game.prison.prison_data.data.passives.PassiveType;
import sexy.criss.game.prison.prison_data.data.passives.Passives;
import sexy.criss.game.prison.prison_data.data.quests.Quests;
import sexy.criss.gen.util.MultiUtils;

public class PrisonPlayer {
	public static Map<UUID, PrisonPlayer> players_map = Maps.newHashMap();
	public static HashMap<String, Integer> top_players = Maps.newHashMap();

	public UUID uuid;
	public String name;
	public String customName;
	public double money;
	public int level;
	public int exp;
	public int kills;
	public int deaths;
	public int totalblocks;
	public long lastkit;
	public int droppedKeys;
	public int usedKeys;
	Set<String> promo;
	Achievements achievements;
	Quests quests;
	boolean autosell;

	public String lastDamager;
	public long lastDamagerHitTime;

	public Set<Integer> access;
	public Map<Integer, BukkitTask> tasks = Maps.newHashMap();

	public Faction fraction;
	public Map<Material, Integer> block_log;
	public Map<EntityType, Integer> mob_log;
	public Map<String, Integer> passives;

	public PrisonPlayer(UUID uuid, double money, int level, int exp, int kills, int deaths, int totalblocks, String fraction, String block_log, String passives, String access, String name, String promo, int droppedKeys, int usedKeys, String mob_log, int autosell) {
		this.uuid = uuid;
		this.name = Bukkit.getOfflinePlayer(uuid).getName();
		this.customName = name;
		this.money = money;
		this.level = level;
		this.exp = exp;
		this.kills = kills;
		this.deaths = deaths;
		this.totalblocks = totalblocks;
		this.usedKeys = usedKeys;
		this.droppedKeys = droppedKeys;
		this.fraction = Faction.fromId(fraction);
		this.block_log = Maps.newHashMap();
		this.mob_log = Maps.newHashMap();
		this.passives = Maps.newHashMap();
		this.access = Sets.newHashSet();
		this.promo = Sets.newHashSet();
		this.achievements = new Achievements(this.name);
		this.quests = new Quests(this.name);
		if(promo.length() > 0) Arrays.asList(promo.split(",")).addAll(this.promo);
		if(!block_log.isEmpty()) Arrays.asList(block_log.split(",")).forEach(t -> this.block_log.put(Material.getMaterial(t.split("=")[0]), Integer.parseInt(t.split("=")[1])));
		if(!mob_log.isEmpty()) Arrays.asList(mob_log.split(",")).forEach(t -> this.mob_log.put(EntityType.valueOf(t.split("=")[0]), Integer.parseInt(t.split("=")[1])));
		if(!passives.isEmpty()) Arrays.asList(passives.split(",")).forEach(t -> this.passives.put(t.split("=")[0], Integer.parseInt(t.split("=")[1])));
		if(!access.isEmpty()) Arrays.asList(access.split(",")).forEach(t -> this.access.add(Integer.parseInt(t)));
		this.autosell = autosell==1;

		players_map.put(uuid, this);
	}

	public boolean hasAutoSell() {
		return this.autosell;
	}

	public void grantAutoSell() {
		this.autosell = true;
	}

	public void removeAutoSell() {
		this.autosell = false;
	}

	public boolean hasBlocks(Material m, int i) {
		return this.block_log.getOrDefault(m, 0) >= i;
	}

	public int getMob(EntityType type) {
		if(!this.mob_log.containsKey(type)) this.mob_log.put(type, 0);
		return this.mob_log.get(type);
	}

	public Map<EntityType, Integer> getMob_log() {
		return this.mob_log;
	}

	public boolean hasMob(EntityType type, int i) {
		return this.mob_log.getOrDefault(type, 0) >= i;
	}

	public boolean hasMob(EntityType type) {
		return hasMob(type, 1);
	}

	public void addMob(EntityType type) {
		this.mob_log.put(type, this.mob_log.getOrDefault(type, 0) + 1);
	}

	public Achievements getAchievements() {
		return this.achievements;
	}

	public boolean hasPromo(String id) {
		return this.promo.contains(id);
	}

	public void addPromo(String id) {
		this.promo.add(id);
	}

	public void takePromo(String id) {
		this.promo.remove(id);
	}

	public Set<String> getPromo() {
		return this.promo;
	}

	public String getCustomName() {
		return this.customName;
	}

	public void setCustomName(String name) {
		this.customName = name;
	}

	public void setTotalBlocks(int i) {
		this.totalblocks = i;
	}

	public int getTotalBlocks() {
		return this.totalblocks;
	}

	public void addTotalBlocks(int i) {
		this.totalblocks += i;
	}

	public int getDroppedKeys() {
		return this.droppedKeys;
	}

	public int getUsedKeys() {
		return this.usedKeys;
	}

	public Map<Material, Integer> getBlock_log() {
		return this.block_log;
	}

	public Map<String, Integer> getPassives() {
		return this.passives;
	}

	public int getPassive(PassiveType pas) {
		if(this.passives == null) this.passives = Maps.newHashMap();
		if(this.passives.containsKey(Passives.getByType(pas).getId())) {
			return this.passives.get(Passives.getByType(pas).getId());
		}
		this.passives.put(Passives.getByType(pas).getId(), 0);
		return 0;
	}

	public Set<Integer> getAccess() {
		return this.access;
	}

	public boolean hasAccess(int access) {
		return this.access.contains(access);
	}

	public void grantAccess(int ac) {
		this.access.add(ac);
	}

	public void takeAccess(int ac) {
		this.access.removeIf(this::hasAccess);
	}

	public void addBlocks(Collection<ItemStack> drops) {
		drops.forEach(t -> {
			this.block_log.put(t.getType(), this.block_log.getOrDefault(t.getType(), 0) + 1);
			this.totalblocks++;
		});
	}

	public void addBlock(Material mat, int i) {
		this.block_log.put(mat, this.block_log.getOrDefault(mat, 0) + i);
		this.totalblocks+=i;
	}

	public void addBlock(Material mat) {
		this.block_log.put(mat, this.block_log.getOrDefault(mat, 0) + 1);
		this.totalblocks++;
	}

	public void addPassive(Passives pas) {
		if(this.passives.containsKey(pas.getId())) {
			this.passives.put(pas.getId(), this.passives.get(pas.getId()) + 1);
			return;
		}
		this.passives.put(pas.getId(), 0);
	}

	public void addBlocks(int i) {
		this.totalblocks+=i;
	}

	public static PrisonPlayer getPlayer(UUID uuid) {
		if(!players_map.containsKey(uuid)) {
			Main.getInstance().getSource().loadPlayer(uuid);
			return getPlayer(uuid);
		}
		return players_map.get(uuid);
	}

	public Quests getQuests() {
		return quests;
	}

	public Levels getLevelClass() {
		return Levels.getByInt(this.level);
	}

	public boolean levelImprove() {
		Levels l = Levels.getByInt(Math.min(this.level, Levels.getMaxLevel()));
		return this.money >= l.getGold() && this.totalblocks >= l.getTotal_blocks();
	}

	public void levelUpper(){
		takeGold(getLevelClass().getGold());
		this.level++;
	}

	public boolean hasFraction() {
		return this.fraction != null;
	}

	public double getBalance() {
		return DoubleHandler.fixDouble(this.money);
	}

	public boolean hasMoney(double d) {
		return this.money >= d;
	}

	public void setMoney(double d) {
		this.money = d;
	}

	public boolean takeMoney(double d) {
		if(takeGold(d)) {
			Bukkit.getPlayer(name).sendMessage(Reference.PLAYER_MONEY_TAKE.get(d));
			return true;
		}
		return false;
	}

	public boolean takeGold(double d) {
		if((this.money-d) < 0) return false;
		this.money-=d; return true;
//		AtomicDouble val = new AtomicDouble(d);
//		tasks.put(2, new BukkitRunnable() {
//			double x = val.get();
//			@Override
//			public void run() {
//				if(x <= 0) {
//					this.cancel();
//					tasks.remove(2);
//					return;
//				}
//				double val = Math.sqrt(x);
//				x-=val;
//				money-=val;
//			}
//		}.runTaskTimer(Main.getInstance(), 0, 1));
	}

	public void addGold(double gold) {
		this.money+=gold;
//		tasks.put(3, new BukkitRunnable(){
//			double x = 0;
//			@Override
//			public void run() {
//				if(x >= gold) {
//					this.cancel();
//					tasks.remove(3);
//					return;
//				}
//				double val = gold >= 100 ? 5 : gold < 100 && gold >= 30 ? 1 : gold <30 && gold >= 5 ? 0.2 : 0.1;
//				x+=val;
//				money+=val;
//			}
//		}.runTaskTimer(Main.getInstance(), 0, 1));
	}

	public void setExp(int xp) {
		this.exp = xp;
	}

	public boolean hasAchievement(Achievement achievement) {
		return this.achievements.hasAchievement(achievement);
	}

	public void addAchievement(Achievement achievement) {
		this.achievements.addAchievement(achievement);
	}

	public void addExp(int xp) {
		this.exp+=xp;
//		tasks.put(4, new BukkitRunnable(){
//			int added = 0;
//			@Override
//			public void run() {
//
//				if(added >= xp) {
//					this.cancel();
//					tasks.remove(4);
//					return;
//				}
//				int val = speed(xp - added);
//				added += val;
//				exp += val;
//			}
//		}.runTaskTimer(Main.getInstance(), 0, 1));
	}

	public void takeExp(int xp) {
		this.exp-=xp;
//		tasks.put(6, new BukkitRunnable(){
//			int added = 0;
//			@Override
//			public void run() {
//				if(added >= xp) {
//					this.cancel();
//					tasks.remove(6);
//					return;
//				}
//				int val = speed(xp - added);
//				added += val;
//				exp += val;
//			}
//		}.runTaskTimer(Main.getInstance(), 0, 1));
	}

	private int speed(int i) {
		return i > 4000 && i < 2000 ? 1000 : i < 2000 && i > 1000 ? 700 : i < 1000 && i > 500 ? 200 : i < 500 && i > 50 ? 40 : 1;
	}

	public int getLevel() {
		return this.level;
	}

	public boolean hasMaxLevel() {
		return this.level >= Levels.getMaxLevel();
	}

	public int getKills() {
		return this.kills;
	}

	public int getDeaths() {
		return this.deaths;
	}

	public int getExp() {
		return this.exp;
	}

	public void save() {
		achievements.save();
		quests.save();
		if(tasks.isEmpty()) {
			Main.getInstance().getSource().updatePlayer(uuid);
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				if (tasks.isEmpty()) {
					Main.getInstance().getSource().updatePlayer(uuid);
					this.cancel();
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	public void reset() {
		this.money = 0;
		this.level = 1;
		this.exp = 0;
		this.kills = 0;
		this.deaths = 0;
		this.totalblocks = 0;
		this.usedKeys = 0;
		this.droppedKeys = 0;
		this.passives = Maps.newHashMap();
		this.block_log = Maps.newHashMap();
		this.access = Sets.newHashSet();
		this.promo = Sets.newHashSet();
		this.customName = this.name;
		this.fraction = null;
		this.autosell = false;
	}

	public Faction getFraction() {
		return this.fraction;
	}

	public void setFraction(Faction fraction) {
		this.fraction = fraction;
	}

	public void addLevel(int i) {
		this.level += i;
	}

	public void setKills(int i) {
		this.kills = i;
	}

	public void setDeaths(int i) {
		this.deaths = i;
	}

	public void setLevel(int val) {
		this.level = val;
	}

	public boolean hasPassive(PassiveType pas, int i) {
		return this.getPassive(pas) >= i;
	}

	public boolean hasPassive(PassiveType pas) {
		return this.passives.containsKey(Passives.getByType(pas).getId()) && this.passives.get(Passives.getByType(pas).getId()) > 0;
	}

	public int getTopNumber() {
		try {
			Map<String, Integer> map = getTopPlayers();
			Stream<Map.Entry<String,Integer>> sorted =
					map.entrySet().stream()
							.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
			AtomicInteger i = new AtomicInteger(0);
			AtomicInteger pose = new AtomicInteger(0);
			sorted.forEach(s -> {
				i.getAndIncrement();
				if(s.getKey().equals(this.name)) {
					pose.set(i.get());
				}
			});
			return pose.get();
		}catch (Exception ex) {
			System.out.println(ex.getMessage());
			return 0;
		}
	}

	public static Map<String, Integer> getTopByString(String s) {
		Map<String, Integer> map = Maps.newHashMap();
		ResultSet rs = MultiUtils.getGamesConnector().query("SELECT UUID, %s FROM prison_data;", s);
		try {
			while (rs.next()) {
				String name = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID"))).getName();
				int value = rs.getInt(s);
				map.put(name, value);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		Stream<Map.Entry<String,Integer>> sorted =
				map.entrySet().stream()
						.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
		return sorted.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static Map<String, Integer> getTopPlayers() {
		top_players = Maps.newHashMap();
		ResultSet rs = MultiUtils.getGamesConnector().query("SELECT UUID, MONEY, TOTAL_BLOCKS FROM prison_data;");
		try {
			while (rs.next()) {
				String name = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID"))).getName();
				int money = rs.getInt("MONEY");
				int blocks = rs.getInt("TOTAL_BLOCKS");
				top_players.put(name, money+blocks);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return top_players;
	}

    public void updateItem(ItemStack from, ItemStack to) {
		Player p = Bukkit.getPlayer(uuid);
		for(int i = 0; i < p.getInventory().getSize(); i++) {
			if(from.equals(p.getInventory().getItem(i))) {
				p.getInventory().remove(from);
				p.getInventory().setItem(i, to);
			}
		}
    }
}
