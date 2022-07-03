package sexy.criss.game.prison.board.manager;

import java.util.*;

import com.google.common.collect.Sets;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import com.google.common.collect.Maps;

import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.board.PrisonBoard;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.factions.Faction;
import sexy.criss.gen.player.RPlayer;
import sexy.criss.gen.scoreboard.SCTeam;
import sexy.criss.gen.util.Util;
import sexy.criss.gen.util.UtilAlgo;
import sexy.criss.gen.util.UtilPlayer;

public class BoardManager {
	public static Map<Player, Scoreboard> boards_map = Maps.newHashMap();
	private static final Map<Player, SCTeam> scteams = Maps.newHashMap();
	private static final Set<Integer> used = Sets.newHashSet();

	public static void createBoard(Player p, PrisonPlayer pp) {
		ScoreboardManager sm = Bukkit.getScoreboardManager();
		Scoreboard board = sm.getNewScoreboard();
		Objective obj = board.registerNewObjective("stats", "stats", "rewforce");

		obj.setDisplayName(Util.f("&cPrison"));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		boolean b = new Random().nextBoolean();
		runUpdate();
		new PrisonBoard(p.getName(), board).runTaskTimer(Main.getInstance(), 0, 2);
		p.setScoreboard(board);
	}

	public static void runUpdate() {
		new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(p -> {
					RPlayer rp = RPlayer.get(p);
					PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
					String l_f = pp.hasFraction() ? pp.getFraction().getName() : "&cPrison";
					Scoreboard b = p.getScoreboard();
					Objective objective = b.getObjective("stats");
//					objective.setDisplayName(Util.f(l_f));
					String f = pp.hasFraction() ? pp.getFraction().getName() : "&cнет";
					setScore(b, objective, Util.f("&fФракция: " + f), 13);
//					setScore(b, objective, setPlaceholders(p, "%img_exp%")+Util.f(" &6%d", pp.getExp()) + setPlaceholders(p, " &r%img_mc_experience_bottle%"), 13);
					setScore(b, objective, Util.f("&r&r&r"), 12);
					setScore(b, objective, Util.f("&fБаланс"), 11);
					setScore(b, objective, Util.f(Util.f("&6"+ DoubleHandler.getFancyDouble(DoubleHandler.fixDouble(pp.getBalance())))), 10);
					setScore(b, objective, Util.f("&r&r"), 9);
					setScore(b, objective, Util.f("&fВсего блоков"), 8);
					setScore(b, objective, Util.f("&r&6"+pp.getTotalBlocks()), 7);
					setScore(b, objective, Util.f("&r&r"), 6);
					setScore(b, objective, Util.f("&fУровень"), 5);
					setScore(b, objective, Util.f("&6%d&f", pp.getLevel()), 4);
					setScore(b, objective, "", 3);
					setScore(b, objective, Util.f("&fУбийств: &6"+pp.getKills()), 2);
					setScore(b, objective, Util.f("&fСмертей: &6"+pp.getDeaths()), 1);
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);

		new BukkitRunnable(){
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(BoardManager::onVisibleNameUpdate);
			}
		}.runTaskTimer(Main.getInstance(), 0, 400);
	}

	public static void onVisibleNameUpdate(Player p) {
		PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
		SCTeam scteam = scteams.remove(p);
		if (scteam != null) {
			used.remove(Integer.parseInt(Util.s(scteam.getName())));
			scteam.delete(UtilPlayer.getOnlinePlayers());
		}

		RPlayer rp = RPlayer.getIfPresent(p);
		if (rp != null) {
			int id;
			id = UtilAlgo.r(1000000000);
			while (used.contains(id)) {
				id = UtilAlgo.r(1000000000);
			}

			used.add(id);
			SCTeam myTeam = new SCTeam(Util.c("&r%d", id), rp.getShortPrefix(), Util.c(" &6"+pp.getLevel()), Collections.singletonList(Util.c((pp.hasFraction() ? pp.getFraction().getColor() : "&7")+p.getName())));
			myTeam.create(UtilPlayer.getOnlinePlayers());
			scteams.put(p, myTeam);
		}
	}

	private static char getPriority(Player player) {
		PrisonPlayer pp = PrisonPlayer.getPlayer(player.getUniqueId());
		RPlayer rp = RPlayer.get(player);
		Faction f = pp.getFraction();
		switch (rp.getMainPermissionGroup()) {
			case OWNER -> {return '4';}
			case ADMINISTRATOR -> {return 'c';}
		}
		if(f == null) return '7';
		switch (f) {
			case ASIAN ->  {return 'e';}
			case BLACK ->  {return '8';}
			case WHITE ->  {return 'f';}
			default -> {return '7';}
		}
	}

	public static void setScore(Scoreboard board, Objective objective, String name, int index) {
		String string = build(index);
		Team team = board.getTeam(string);
		if (team == null) {
			team = board.registerNewTeam(string);
			team.addEntry(string);
			Score score = objective.getScore(string);
			score.setScore(index);
		}
		team.setPrefix(name);
	}

	public static void resetScores(Scoreboard board, int index) {
		String string = build(index);
		board.getTeam(string).unregister();
		board.resetScores(string);
	}

	public static String build(int index) {
		String hex = Integer.toHexString(index);
		StringBuilder sb = new StringBuilder();
		for (char c : hex.toCharArray()) {
			sb.append("§").append(c);
		}
		return sb.toString();
	}

}
