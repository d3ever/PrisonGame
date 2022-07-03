package sexy.criss.game.prison.boosters.commands;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import sexy.criss.game.prison.boosters.Booster;
import sexy.criss.game.prison.boosters.PrivateBooster;
import sexy.criss.gen.commands.SpigotCommand;
import sexy.criss.gen.player.permission.PermissionGroup;
import sexy.criss.gen.util.Util;

import java.util.Arrays;
import java.util.List;

public class PrivateBoosterCommand extends SpigotCommand {

    public PrivateBoosterCommand() {
        super("pbooster", PermissionGroup.ADMINISTRATOR, "/pbooster [тип] [владелец] [множитель] [время]");
    }

    @Override
    public void handle(CommandSender sender, String cmd, String[] args) {
        if(args.length != 4) Util.ps("Prison", sender, this.usageMessage);
        else {
            List<String> types = Lists.newArrayList();
            Arrays.asList(Booster.BoosterType.values()).forEach(boosterType -> types.add(boosterType.name()));
            String id = args[0];
            if(!id.equalsIgnoreCase("all") && !types.contains(id.toUpperCase())) {
                Util.ps("Prison", sender, "&fВозможные типы:");
                Util.ps("Prison", sender, "&6"+ Arrays.toString(types.toArray()));
                return;
            }
            final Booster.BoosterType type = Booster.BoosterType.valueOf(args[0].toUpperCase()) == null ? Booster.BoosterType.MONETARY : Booster.BoosterType.valueOf(args[0].toUpperCase());
            final boolean all = args[0].toLowerCase().equals("all");
            Object[] o = new Object[]{0.0, 0};
            try { o[0] = Double.parseDouble(args[2]); } catch (Exception ex) { o[0] = 1.1; }
            try { o[1] = Integer.parseInt(args[3]); } catch (Exception ex) { o[1] = 60; }
            final String owner = args[1];
            if(all) Arrays.asList(Booster.BoosterType.values()).forEach(boosterType -> new PrivateBooster(boosterType, owner, (double) o[0], (int) o[1]).activate());
            else new PrivateBooster(type, owner, (double) o[0], (int) o[1]).activate();
        }
    }
}
