package sexy.criss.game.prison.commands.manager;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import sexy.criss.game.prison.language.Reference;

public abstract class AbstractCommand implements CommandExecutor, TabExecutor {
    
    protected final String command;
    protected final String description;
    protected final List<String> alias;
    protected final String usage;
    protected final String permMessage;
    protected boolean consoleOnly = false;

    protected static CommandMap cmap;
    
    public AbstractCommand(String command) {
        this(command, null, null, null, null);
    }

    public AbstractCommand(String command, List<String> alias) {
        this(command, null, null, alias);
    }
    
    public AbstractCommand(String command, String usage) {
        this(command, usage, null, null, null);
    }
    
    public AbstractCommand(String command, String usage, String description) {
        this(command, usage, description, null, null);
    }
    
    public AbstractCommand(String command, String usage, String description, String permissionMessage) {
        this(command, usage, description, permissionMessage, null);
    }
    
    public AbstractCommand(String command, String usage, String description, List<String> aliases) {
        this(command, usage, description, null, aliases);
    }
    
    public AbstractCommand(String command, String usage, String description, String permissionMessage, List<String> aliases) {
        this.command = command.toLowerCase();
        this.usage = usage;
        this.description = description;
        this.permMessage = permissionMessage;
        this.alias = aliases;
    }
    
    public void register() {
        ReflectCommand cmd = new ReflectCommand(this.command);
        if (this.alias != null) cmd.setAliases(this.alias);
        if (this.description != null) cmd.setDescription(this.description);
        if (this.usage != null) cmd.setUsage(this.usage);
        if (this.permMessage != null) cmd.setPermissionMessage(this.permMessage);
        getCommandMap().register("", cmd);
        cmd.setExecutor(this);
    }
    
    final CommandMap getCommandMap() {
        if (cmap == null) {
            try {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            } catch (Exception e) { e.printStackTrace(); }
        } else if (cmap != null) { return cmap; }
        return getCommandMap();
    }

    protected void unavailableFromConsole() {
        this.consoleOnly = false;
    }

    protected void unavailableFromPlayer() {
        this.consoleOnly = true;
    }

    private final class ReflectCommand extends Command {
        private AbstractCommand exe = null;
        protected ReflectCommand(String command) { super(command); }
        public void setExecutor(AbstractCommand exe) { this.exe = exe; }
        @Override public boolean execute(CommandSender sender, String cmd, String[] args) {
            if (exe != null) {
                if(consoleOnly && !(sender instanceof ConsoleCommandSender)) {
                    sender.sendMessage(Reference.COMMAND_CONSOLE_ONLY.get());
                    return false;
                }
                if(!consoleOnly && !(sender instanceof Player)) return false;

                exe.handle(sender, cmd, args);
                return false;
            }
            return false;
        }
        
        @Override  public List<String> tabComplete(CommandSender sender, String cmd, String[] args) {
            if (exe != null) { return exe.onTabComplete(sender, this, cmd, args); }
            return null;
        }
    }

    public abstract void handle(CommandSender sender, String cmd, String[] args);
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { return false; }
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}