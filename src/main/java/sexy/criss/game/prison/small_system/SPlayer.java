package sexy.criss.game.prison.small_system;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import sexy.criss.game.prison.configuration.Configs;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.gen.util.Util;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class SPlayer {
    private static final Map<String, SPlayer> players = Maps.newHashMap();
    FileConfiguration conf = Configs.PLAYERS.getConfig();
    String name;
    ConfigurationSection s;
    private Player p;
    private Map<String, Integer> war_list;
    private Set<String> coalition;
    private int age;
    private int time;
    private Gender gender;
    private String realName;
    private String partner;
    private long realDate;
    private boolean change;
    PrisonPlayer pp;
    BukkitTask task;

    private SPlayer(String name) {
        this.name = name;
        s = conf.contains(name) ? conf.getConfigurationSection(name) : conf.createSection(name);
        this.p = Bukkit.getPlayerExact(name);

        this.coalition = Sets.newHashSet();
        if(s.contains("coalition")) coalition.addAll(s.getConfigurationSection("coalition").getKeys(false));

        this.war_list = Maps.newHashMap();
        if(s.contains("war-list")) {
            ConfigurationSection war = s.getConfigurationSection("war-list");
            war.getKeys(false).forEach(s1 -> war_list.put(s1, war.getInt(s1)));
        }
        this.age = s.getInt("age", 10);
        this.time = s.getInt("time", 0);
        this.gender = Gender.valueOf(s.getString("gender", "MAN"));
        this.realName = s.getString("real-name", "");
        this.partner = s.getString("partner", "");
        Calendar d_cal = Calendar.getInstance();
        d_cal.set(Calendar.YEAR, 2005);
        d_cal.set(Calendar.MONTH, 1);
        this.realDate = s.getLong("real-date", d_cal.getTime().getTime());
        this.change = s.getBoolean("change", false);

        players.put(name, this);
    }

    public static SPlayer get(String s) {
        if(players.containsKey(s)) return players.get(s);
        else return players.put(s, new SPlayer(s));
    }

    public static SPlayer get(Player p) {
        return get(p.getName());
    }

    public void addWar(String target, int price) {
        if(Bukkit.getPlayerExact(target) == null) Util.ps("Prison", p, "&fНельзя объявить игроку, который не в сети.");
        else if(this.coalition.contains(target)) Util.ps("Prison", p, "&fИгрок &6%s&f уже выплатил деньги за вражду.", target);
        else if(!pp.hasMoney(price)) Util.ps("Prison", p, "&fВы должны иметь ту сумму, которую запрашиваете.");
        else {
            Player t = Bukkit.getPlayerExact(target);
            boolean c = PrisonPlayer.getPlayer(t.getUniqueId()).hasMoney(price);
            Util.ps("Prison", t, "&fИгрок &c%s&f объявил вам войну!", p.getName());
            Util.ps("Prison", t, "&fСумма выкупа составляет: "+ (c ? "&a":"&c") + price +"$&f.");
            Util.ps("Prison", p, "&fВы объявили игроку &c%s&f войну с выкупом &6%d&f.", price);
            this.war_list.put(target, this.war_list.getOrDefault(target, price));
        }
    }

    public void unWar(String target) {
        if(this.war_list.containsKey(target)) {
            PrisonPlayer tpp = PrisonPlayer.getPlayer(Bukkit.getOfflinePlayer(target).getUniqueId());
            if(tpp.hasMoney(this.war_list.get(target))) {
                tpp.takeGold(this.war_list.get(target));
                Util.ps("Prison", Bukkit.getPlayerExact(target), "&aВы больше не враждуете с игроком &f&l"+p.getName()+"&a.");
                Util.ps("Prison", Bukkit.getPlayerExact(target), "&fС вашего счёта было списанно &6"+this.war_list.get(target)+"$&f.");
                this.war_list.remove(target);
                this.coalition.add(target);
            } else Util.ps("Prison", Bukkit.getPlayerExact(target), "&fУ вас недостаточно средств, чтобы совершить выкуп.");
        } else Util.ps("Prison", Bukkit.getPlayerExact(target), "&fВы не враждуете с игроком &6%s&f.", p.getName());
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public boolean isChange() {
        return change;
    }

//    public BukkitTask glow_updater() {
//        return new BukkitRunnable(){
//            @Override
//            public void run() {
//                Bukkit.getOnlinePlayers().forEach(t -> {
//                    PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
//                    PrisonPlayer tp = PrisonPlayer.getPlayer(t.getUniqueId());
//                    if(t.equals(p)) GlowAPI.setGlowing(t, GlowAPI.Color.GRAY, p);
//                    else if(war_list.containsKey(t.getName())) GlowAPI.setGlowing(p, GlowAPI.Color.RED, t);
//                    else if((pp.hasFraction() && tp.hasFraction()) && pp.getFraction().equals(tp.getFraction())) GlowAPI.setGlowing(p, GlowAPI.Color.GREEN, t);
//                    else if(!tp.hasFraction() && !pp.hasFraction()) GlowAPI.setGlowing(p, GlowAPI.Color.WHITE, t);
//                    else if(t.getName().equalsIgnoreCase(partner)) GlowAPI.setGlowing(p, GlowAPI.Color.PURPLE, t);
//                    else if((pp.hasFraction() && tp.hasFraction()) && !pp.getFraction().equals(tp.getFraction())) GlowAPI.setGlowing(p, GlowAPI.Color.RED, t);
//                    else GlowAPI.setGlowing(p, GlowAPI.Color.WHITE, t);
//                });
//            }
//        }.runTaskTimer(Main.getInstance(), 0, 10);
//    }

    public void save() {
        if(conf.contains(name)) conf.createSection(name);
        s.set("age", this.age);
        s.set("time", this.time);
        s.set("gender", this.gender.name());
        s.set("real-name", realName);
        s.set("partner", partner);
        s.set("real-date", this.realDate);
        s.set("change", this.change);
        s.set("coalition", this.coalition);
        if(!s.contains("war-list")) s.createSection("war-list");
        ConfigurationSection wl = s.getConfigurationSection("war-list");
        this.war_list.forEach(wl::set);
        if(task!=null)task.cancel();
        Configs.PLAYERS.save();
    }

}
