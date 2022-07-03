package sexy.criss.game.prison.prison_data.data.quests;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import sexy.criss.game.prison.achievements.Achievement;
import sexy.criss.game.prison.quests.Quest;
import sexy.criss.gen.util.MultiUtils;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Quests {

    String owner;
    Map<String, List<Integer>> quests;

    public Quests(String owner) {
        this.owner = owner;
        this.quests = Maps.newHashMap();

        try {
            ResultSet rs = MultiUtils.getGamesConnector().query("SELECT quests FROM prison_quests WHERE name='"+owner+"'");
            if (rs.next()) {
                if(rs.getString("quests").length() > 0) {
                    if(rs.getString("quests").split(",").length > 0) {
                        Arrays.asList(rs.getString("quests").split(",")).forEach(s -> {
                            String[] arg = s.split(":");
                            List<Integer> ints = Lists.newArrayList();
                            Arrays.asList(arg[1].split("/")).forEach(x -> ints.add(Integer.parseInt(x)));
                            this.quests.put(arg[0], ints);
                        });
                    }
                }
            } else {
                MultiUtils.getGamesConnector().query("INSERT INTO prison_quests (name, quests) VALUES ('%s', '%s')", owner, "");
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        if(this.quests.size() <= 0) return;
        StringBuilder str = new StringBuilder();
        this.quests.forEach((s, integers) -> {
            StringBuilder ints = new StringBuilder();
            integers.forEach(integer -> ints.append("/").append(integer));
            str.append(",").append(s).append(":").append(ints.toString().substring(1));
        });
        try {
            ResultSet rs = MultiUtils.getGamesConnector().query("SELECT quests FROM prison_quests WHERE name='%s'", owner);
            if (rs.next()) {
                MultiUtils.getGamesConnector().query("UPDATE prison_quests SET quests='%s' WHERE name='%s'", str.toString().substring(1), owner);
            } else {
                MultiUtils.getGamesConnector().query("INSERT INTO prison_quests (name, quests) VALUES ('%s', '%s')", owner, str.toString().substring(1));
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void grant(Quest q) {
        this.grant(q, 1);
    }

    public void grant(Quest q, int level) {
        List<Integer> l = this.quests.getOrDefault(q.getId(), Lists.newArrayList());
        l.add(level);
        this.quests.put(q.getId(), l);
    }

    public boolean has(Quest q) {
        return quests.containsKey(q.getId());
    }

    public boolean has(Quest q, int level) {
        return has(q) && this.quests.get(q.getId()).contains(level);
    }

    public Map<String, List<Integer>> getQuests() {
        return quests;
    }
}
