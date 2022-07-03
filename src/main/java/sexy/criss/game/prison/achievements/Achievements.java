package sexy.criss.game.prison.achievements;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import sexy.criss.gen.util.MultiUtils;
import sexy.criss.gen.util.Util;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Achievements {

    String owner;
    List<Achievement> achievementList;

    public Achievements(String owner) {
        this.owner = owner;
        achievementList = Lists.newArrayList();

        try {
            ResultSet rs = MultiUtils.getGamesConnector().query("SELECT achievements FROM prison_achievements WHERE name='"+owner+"'");
            if (rs.next())
                if(rs.getString("achievements").length() > 0) if(rs.getString("achievements").split(",").length > 0) Arrays.asList(rs.getString("achievements").split(",")).forEach(s -> this.achievementList.add(Achievement.valueOf(s)));
                 else achievementList = Lists.newArrayList();
             else MultiUtils.getGamesConnector().query("INSERT INTO prison_achievements (name, achievements) VALUES ('%s', '%s')", owner, "");

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        StringBuilder str = new StringBuilder();
        achievementList.forEach(achievement -> str.append(",").append(achievement.name()));
        if(achievementList.size() <= 0) return;
        try {
            ResultSet rs = MultiUtils.getGamesConnector().query("SELECT achievements FROM prison_achievements WHERE name='%s'", owner);
            if (rs.next()) MultiUtils.getGamesConnector().query("UPDATE prison_achievements SET achievements='%s' WHERE name='%s'", str.toString().substring(1), owner);
            else MultiUtils.getGamesConnector().query("INSERT INTO prison_achievements (name, achievements) VALUES ('%s', '%s')", owner, str.toString().substring(1));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addAchievement(Achievement achievement) {
        this.achievementList.add(achievement);
        List<String> message = Lists.newArrayList();
        message.add("");
        message.add("&b&k****************************************");
        message.add("&3     " + achievement.getName());
        message.addAll(Arrays.asList(achievement.getMessages()));
        message.add("&b&k****************************************");
        message.add("");
        if(Bukkit.getPlayerExact(owner) == null) return;
        Util.f(message).forEach(s -> Util.s(Bukkit.getPlayerExact(owner), s));
    }

    public boolean hasAchievement(Achievement achievement) {
        return this.achievementList.contains(achievement);
    }

}
