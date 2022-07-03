package sexy.criss.game.prison.timer;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.language.Reference;

public abstract class SexyTimer {

	Player p;
	int s;
	BukkitTask run;
	int time;

	public SexyTimer(int s) {
		this.s = s;
	}

	public SexyTimer(Player p, int s) {
		this.p = p;
		this.s = s;
	}

	public void run() {
		time = s;
		this.run = new BukkitRunnable() {

			@Override
			public void run() {
				if (time <= 0) {
					if(p != null) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
					handle();
					this.cancel();
				}
				if (time != 0) {
					if(p != null) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Reference.PLAYER_TIME.get(time)));
				}
				time--;
			}

		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

	public int getTime() {
		return this.time;
	}

	protected abstract void handle();

}
