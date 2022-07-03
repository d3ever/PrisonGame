package sexy.criss.game.prison.board;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import sexy.criss.gen.util.Util;

public class PrisonBoard extends BukkitRunnable {
	public static Map<String, PrisonBoard> players_boards = Maps.newHashMap();

	public static List<String> ani = Lists.newArrayList(
			"&9                w &r",
			"&9               w  &r",
			"&9              w   &r",
			"&9             w    &r",
			"&9            w     &r",
			"&9           w      &r",
			"&9          w       &r",
			"&9         w        &r",
			"&9        w         &r",
			"&9       w          &r",
			"&9      w           &r",
			"&9     w            &r",
			"&9    w             &r",
			"&9   w              &r",
			"&9  w               &r",
			"&9 w                &r",
			"&9w                 &r",
			"&9w              w  &r",
			"&9w             w   &r",
			"&9w            w    &r",
			"&9w           w     &r",
			"&9w          w      &r",
			"&9w         w       &r",
			"&9w        w        &r",
			"&9w       w         &r",
			"&9w      w          &r",
			"&9w     w           &r",
			"&9w    w            &r",
			"&9w   w             &r",
			"&9w  w              &r",
			"&9w w               &r",
			"&9ww                &r",
			"&9ww             w  &r",
			"&9ww            w   &r",
			"&9ww           w    &r",
			"&9ww          w     &r",
			"&9ww         w      &r",
			"&9ww        w       &r",
			"&9ww       w        &r",
			"&9ww      w         &r",
			"&9ww     w          &r",
			"&9ww    w           &r",
			"&9ww   w            &r",
			"&9ww  w             &r",
			"&9ww w              &r",
			"&9www               &r",
			"&9www            .  &r",
			"&9www           .   &r",
			"&9www          .    &r",
			"&9www         .     &r",
			"&9www        .      &r",
			"&9www       .       &r",
			"&9www      .        &r",
			"&9www     .         &r",
			"&9www    .          &r",
			"&9www   .           &r",
			"&9www  .            &r",
			"&9www .             &r",
			"&9www.              &r",
			"&9www.            r &r",
			"&9www.           r  &r",
			"&9www.          r   &r",
			"&9www.         r    &r",
			"&9www.        r     &r",
			"&9www.       r      &r",
			"&9www.      r       &r",
			"&9www.     r        &r",
			"&9www.    r         &r",
			"&9www.   r          &r",
			"&9www.  r           &r",
			"&9www. r            &r",
			"&9www.r             &r",
			"&9www.r           e &r",
			"&9www.r          e  &r",
			"&9www.r         e   &r",
			"&9www.r        e    &r",
			"&9www.r       e     &r",
			"&9www.r      e      &r",
			"&9www.r     e       &r",
			"&9www.r    e        &r",
			"&9www.r   e         &r",
			"&9www.r  e          &r",
			"&9www.r e           &r",
			"&9www.re            &r",
			"&9www.re          w &r",
			"&9www.re         w  &r",
			"&9www.re        w   &r",
			"&9www.re       w    &r",
			"&9www.re      w     &r",
			"&9www.re     w      &r",
			"&9www.re    w       &r",
			"&9www.re   w        &r",
			"&9www.re  w         &r",
			"&9www.re w          &r",
			"&9www.rew           &r",
			"&9www.rew         f &r",
			"&9www.rew        f  &r",
			"&9www.rew       f   &r",
			"&9www.rew      f    &r",
			"&9www.rew     f     &r",
			"&9www.rew    f      &r",
			"&9www.rew   f       &r",
			"&9www.rew  f        &r",
			"&9www.rew f         &r",
			"&9www.rewf          &r",
			"&9www.rewf        o &r",
			"&9www.rewf       o  &r",
			"&9www.rewf      o   &r",
			"&9www.rewf     o    &r",
			"&9www.rewf    o     &r",
			"&9www.rewf   o      &r",
			"&9www.rewf  o       &r",
			"&9www.rewf o        &r",
			"&9www.rewfo         &r",
			"&9www.rewfo       r &r",
			"&9www.rewfo      r  &r",
			"&9www.rewfo     r   &r",
			"&9www.rewfo    r    &r",
			"&9www.rewfo   r     &r",
			"&9www.rewfo  r      &r",
			"&9www.rewfo r       &r",
			"&9www.rewfor        &r",
			"&9www.rewfor      c &r",
			"&9www.rewfor     c  &r",
			"&9www.rewfor    c   &r",
			"&9www.rewfor   c    &r",
			"&9www.rewfor  c     &r",
			"&9www.rewfor c      &r",
			"&9www.rewforc       &r",
			"&9www.rewforc     e &r",
			"&9www.rewforc    e  &r",
			"&9www.rewforc   e   &r",
			"&9www.rewforc  e    &r",
			"&9www.rewforc e     &r",
			"&9www.rewforce      &r",
			"&9www.rewforce    . &r",
			"&9www.rewforce   .  &r",
			"&9www.rewforce  .   &r",
			"&9www.rewforce .    &r",
			"&9www.rewforce.     &r",
			"&9www.rewforce.   x &r",
			"&9www.rewforce.  x  &r",
			"&9www.rewforce. x   &r",
			"&9www.rewforce.x    &r",
			"&9www.rewforce.x  y &r",
			"&9www.rewforce.x y  &r",
			"&9www.rewforce.xy   &r",
			"&9www.rewforce.xy z &r",
			"&9www.rewforce.xyz&r&r",
			"&9www.rewforce.xyz&r&r",
			"&9www.rewforce.xyz&r&r",
			"&bw&9ww.rewforce.xyz&r",
			"w&bw&9w.rewforce.xyz&r",
			"ww&bw&9.rewforce.xyz&r",
			"www&b.&9rewforce.xyz&r",
			"www.&br&9ewforce.xyz&r",
			"www.r&be&9wforce.xyz&r",
			"www.re&bw&9force.xyz&r",
			"www.rew&bf&9orce.xyz&r",
			"www.rewf&bo&9rce.xyz&r",
			"www.rewfo&br&9ce.xyz&r",
			"www.rewfor&bc&9e.xyz&r",
			"www.rewforc&be&9.xyz&r",
			"www.rewforce&b.&9xyz&r",
			"www.rewforce.&bx&9yz&r",
			"www.rewforce.x&by&9z&r",
			"www.rewforce.xy&bz&r&r",
			"www.rewforce.x&9y&bz&r",
			"www.rewforce.&9x&by&rz",
			"www.rewforce&9.&bx&ryz",
			"www.rewforc&9e&b.&rxyz",
			"www.rewfor&9c&be&r.xyz",
			"www.rewfo&9r&bc&re.xyz",
			"www.rewf&9o&br&rce.xyz",
			"www.rew&9f&bo&rrce.xyz",
			"www.re&9w&bf&rorce.xyz",
			"www.r&9e&bw&rforce.xyz",
			"www.&9r&be&rwforce.xyz",
			"www&9.&br&rewforce.xyz",
			"ww&9w&b.&rrewforce.xyz",
			"w&9w&bw&r.rewforce.xyz",
			"&9w&bw&rw.rewforce.xyz",
			"&bw&rww.rewforce.xyz&r",
			"&b&rwww.rewforce.xyz&r");

	int i = 0;
	int last = ani.size();

	Scoreboard board;
	String s;

	public PrisonBoard(String s, Scoreboard b) {
		this.board = b;
		this.s = s;
		players_boards.put(s, this);
	}

	@Override
	public void run() {
		i++;
		if(i >= last) i = 0;

		if(Bukkit.getPlayer(this.s) == null) {
			this.cancel();
			return;
		}
		board.getObjective(DisplaySlot.SIDEBAR).setDisplayName(Util.f(ani.get(i)));
	}

}
