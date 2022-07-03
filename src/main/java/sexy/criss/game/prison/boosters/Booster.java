/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage$ClickAction
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage$HoverAction
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Clock
 *  me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package sexy.criss.game.prison.boosters;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.common.collect.Sets;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Clock;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Booster {
    public static List<Booster> active = new ArrayList<Booster>();
    BoosterType type;
    int id;
    int minutes;
    public String owner;
    double multiplier;
    Date timeout;
    Config cfg;
    boolean silent;
    boolean infinite;
    Map<String, Integer> contributors = new HashMap<String, Integer>();
    Set<String> thansk = Sets.newHashSet();

    public Booster(double multiplier, boolean silent, boolean infinite) {
        this(BoosterType.MONETARY, multiplier, silent, infinite);
    }

    public Booster(BoosterType type, double multiplier, boolean silent, boolean infinite) {
        this.type = type;
        this.multiplier = multiplier;
        this.silent = silent;
        this.infinite = infinite;
        if (infinite) {
            this.minutes = Integer.MAX_VALUE;
            this.timeout = new Date(System.currentTimeMillis() + 1471228928L);
        }
        this.owner = "INTERNAL";
        active.add(this);
    }

    public Booster(String owner, double multiplier, int minutes) {
        this(BoosterType.MONETARY, owner, multiplier, minutes);
    }

    public Booster(BoosterType type, String owner, double multiplier, int minutes) {
        this.type = type;
        this.minutes = minutes;
        this.multiplier = multiplier;
        this.owner = owner;
        this.timeout = new Date(System.currentTimeMillis() + (long)(minutes * 60 * 1000));
        this.silent = false;
        this.infinite = false;
        this.contributors.put(owner, minutes);
    }

    public Booster(int id) throws ParseException {
        active.add(this);
        this.id = id;
        this.cfg = new Config(new File("data-storage/QuickSell/boosters/" + id + ".booster"));
        if (this.cfg.contains("type")) {
            this.type = BoosterType.valueOf(this.cfg.getString("type"));
        } else {
            this.cfg.setValue("type", (Object)BoosterType.MONETARY.toString());
            this.cfg.save();
            this.type = BoosterType.MONETARY;
        }
        this.minutes = this.cfg.getInt("minutes");
        this.multiplier = (Double)this.cfg.getValue("multiplier");
        this.owner = this.cfg.getString("owner");
        this.timeout = new SimpleDateFormat("yyyy-MM-dd-HH-mm").parse(this.cfg.getString("timeout"));
        this.silent = false;
        this.infinite = false;
        if (this.cfg.contains("contributors." + this.owner)) {
            for (String key : this.cfg.getKeys("contributors")) {
                this.contributors.put(key, this.cfg.getInt("contributors." + key));
            }
        } else {
            this.contributors.put(this.owner, this.minutes);
            this.writeContributors();
        }
    }

    private void writeContributors() {
        for (Map.Entry<String, Integer> entry : this.contributors.entrySet()) {
            this.cfg.setValue("contributors." + entry.getKey(), (Object)entry.getValue());
        }
        this.cfg.save();
    }

    public void activate() {
        if (QuickSell.cfg.getBoolean("boosters.extension-mode")) {
            for (Booster booster : active) {
                if (!booster.getType().equals((Object)this.type) || Double.compare(booster.getMultiplier(), this.getMultiplier()) != 0 || (!(this instanceof PrivateBooster) || !(booster instanceof PrivateBooster)) && (this instanceof PrivateBooster || booster instanceof PrivateBooster)) continue;
                booster.extend(this);
                if (!this.silent) {
                    if (this instanceof PrivateBooster && Bukkit.getPlayer((String)this.getOwner()) != null) {
                        QuickSell.local.sendTranslation((CommandSender)Bukkit.getPlayer((String)this.getOwner()), "pbooster.extended." + this.type.toString(), false, new Variable[]{new Variable("%time%", String.valueOf(this.getDuration())), new Variable("%multiplier%", String.valueOf(this.getMultiplier()))});
                    } else {
                        for (String message : QuickSell.local.getTranslation("booster.extended." + this.type.toString())) {
                            Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)message.replace("%player%", this.getOwner()).replace("%time%", String.valueOf(this.getDuration())).replace("%multiplier%", String.valueOf(this.getMultiplier()))));
                        }
                    }
                }
                return;
            }
        }
        if (!this.infinite) {
            for (int i = 0; i < 1000; ++i) {
                if (new File("data-storage/QuickSell/boosters/" + i + ".booster").exists()) continue;
                this.id = i;
                break;
            }
            this.cfg = new Config(new File("data-storage/QuickSell/boosters/" + this.id + ".booster"));
            this.cfg.setValue("type", this.type.toString());
            this.cfg.setValue("owner", this.getOwner());
            this.cfg.setValue("multiplier", this.multiplier);
            this.cfg.setValue("minutes", this.minutes);
            this.cfg.setValue("timeout", new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(this.timeout));
            this.cfg.setValue("private", (this instanceof PrivateBooster ? 1 : 0));
            this.writeContributors();
        }
        active.add(this);
        if (!this.silent) {
            if (this instanceof PrivateBooster && Bukkit.getPlayer((String)this.getOwner()) != null) {
                QuickSell.local.sendTranslation((CommandSender)Bukkit.getPlayer((String)this.getOwner()), "pbooster.activate." + this.type.toString(), false, new Variable[]{new Variable("%time%", String.valueOf(this.getDuration())), new Variable("%multiplier%", String.valueOf(this.getMultiplier()))});
            } else {
                for (String message : QuickSell.local.getTranslation("booster.activate." + this.type.toString())) {
                    Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)message.replace("%player%", this.getOwner()).replace("%time%", String.valueOf(this.getDuration())).replace("%multiplier%", String.valueOf(this.getMultiplier()))));
                }
            }
        }
    }

    public void extend(Booster booster) {
        this.addTime(booster.getDuration());
        int minutes = this.contributors.containsKey(booster.getOwner()) ? this.contributors.get(booster.getOwner()) : 0;
        this.contributors.put(booster.getOwner(), minutes += booster.getDuration());
        this.writeContributors();
    }

    public void deactivate() {
        if (!this.silent) {
            if (this instanceof PrivateBooster) {
                if (Bukkit.getPlayer((String)this.getOwner()) != null) {
                    QuickSell.local.sendTranslation((CommandSender)Bukkit.getPlayer((String)this.getOwner()), "pbooster.deactivate." + this.type.toString(), false, new Variable[]{new Variable("%time%", String.valueOf(this.getDuration())), new Variable("%multiplier%", String.valueOf(this.getMultiplier()))});
                }
            } else {
                for (String message : QuickSell.local.getTranslation("booster.deactivate." + this.type.toString())) {
                    Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)message.replace("%player%", this.getOwner()).replace("%time%", String.valueOf(this.getDuration())).replace("%multiplier%", String.valueOf(this.getMultiplier()))));
                }
            }
        }
        if (!this.infinite) {
            new File("data-storage/QuickSell/boosters/" + this.getID() + ".booster").delete();
        }
        active.remove(this);
    }

    public static Iterator<Booster> iterate() {
        return active.iterator();
    }

    public String getOwner() {
        return this.owner;
    }

    public Double getMultiplier() {
        return this.multiplier;
    }

    public int getDuration() {
        return this.minutes;
    }

    public Date getDeadLine() {
        return this.timeout;
    }

    public int getID() {
        return this.id;
    }

    public long formatTime() {
        return (this.getDeadLine().getTime() - Clock.getCurrentDate().getTime()) / 60000L;
    }

    public void addTime(int minutes) {
        this.timeout = new Date(this.timeout.getTime() + (long)(minutes * 60 * 1000));
        this.cfg.setValue("timeout", (Object)new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(this.timeout));
        this.cfg.save();
    }

    public static void update() {
        Iterator<Booster> boosters = Booster.iterate();
        while (boosters.hasNext()) {
            Booster booster = boosters.next();
            if (!new Date().after(booster.getDeadLine())) continue;
            boosters.remove();
            booster.deactivate();
        }
    }

    @Deprecated
    public static Double getMultiplier(String p) {
        return Booster.getMultiplier(p, BoosterType.MONETARY);
    }

    public static List<Booster> getBoosters(String player) {
        Booster.update();
        ArrayList<Booster> boosters = new ArrayList<Booster>();
        for (Booster booster : active) {
            if (!booster.getAppliedPlayers().contains(player)) continue;
            boosters.add(booster);
        }
        return boosters;
    }

    public static List<Booster> getBoosters(String player, BoosterType type) {
        Booster.update();
        ArrayList<Booster> boosters = new ArrayList<Booster>();
        for (Booster booster : active) {
            if (!booster.getAppliedPlayers().contains(player) || !booster.getType().equals((Object)type)) continue;
            boosters.add(booster);
        }
        return boosters;
    }

    public List<String> getAppliedPlayers() {
        ArrayList<String> players = new ArrayList<String>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            players.add(p.getName());
        }
        return players;
    }

    public Set<String> getThansk() {
        return this.thansk;
    }

    public String getMessage() {
        return "messages.booster-use." + this.type.toString();
    }

    @Deprecated
    public static long getTimeLeft(String player) {
        long timeleft = 0L;
        for (Booster booster : Booster.getBoosters(player)) {
            timeleft += booster.formatTime();
        }
        return timeleft;
    }

    public BoosterType getType() {
        return this.type;
    }

    public boolean isSilent() {
        return this.silent;
    }

    public String getUniqueName() {
        switch (this.type) {
            case MONETARY: {
                return "\u0414\u0435\u043d\u0435\u0436\u043d\u044b\u0439 \u0411\u0443\u0441\u0442\u0435\u0440";
            }
            case BLOCKS: {
                return "\u0411\u0443\u0441\u0442\u0435\u0440 \u0411\u043b\u043e\u043a\u043e\u0432";
            }
            case KEYS: {
                return "\u0411\u0443\u0441\u0442\u0435\u0440 \u041a\u043b\u044e\u0447\u0435\u0439";
            }
        }
        return "\u0411\u0443\u0441\u0442\u0435\u0440";
    }

    public static double getMultiplier(String name, BoosterType type) {
        double multiplier = 1.0;
        for (Booster booster : Booster.getBoosters(name, type)) {
            multiplier *= booster.getMultiplier().doubleValue();
        }
        return DoubleHandler.fixDouble((double)multiplier, (int)2);
    }

    public boolean isPrivate() {
        return this instanceof PrivateBooster;
    }

    public boolean isInfinite() {
        return this.infinite;
    }

    public Map<String, Integer> getContributors() {
        return this.contributors;
    }

    public void sendMessage(Player p, Variable ... variables) {
        List messages = QuickSell.local.getTranslation(this.getMessage());
        if (messages.isEmpty()) {
            return;
        }
        try {
            String message = ChatColor.translateAlternateColorCodes((char)'&', (String)((String)messages.get(0)).replace("%multiplier%", String.valueOf(this.multiplier)).replace("%minutes%", String.valueOf(this.formatTime())));
            for (Variable v : variables) {
                message = v.apply(message);
            }
            new TellRawMessage().addText(message).addClickEvent(TellRawMessage.ClickAction.RUN_COMMAND, "/boosters").addHoverEvent(TellRawMessage.HoverAction.SHOW_TEXT, BoosterMenu.getTellRawMessage(this)).send(new Player[]{p});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static enum BoosterType {
        MONETARY,
        BLOCKS,
        KEYS;

    }
}

