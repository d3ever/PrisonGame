package sexy.criss.game.prison.listener.players;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import sexy.criss.game.prison.Main;
import sexy.criss.game.prison.achievements.Achievement;
import sexy.criss.game.prison.board.manager.BoardManager;
import sexy.criss.game.prison.boosters.Booster;
import sexy.criss.game.prison.language.Reference;
import sexy.criss.game.prison.listener.manager.SexyListener;
import sexy.criss.game.prison.listener.players.tags.PlayerTag;
import sexy.criss.game.prison.prison_data.PrisonItem;
import sexy.criss.game.prison.prison_data.PrisonPlayer;
import sexy.criss.game.prison.prison_data.data.passives.PassiveType;
import sexy.criss.gen.util.Task;
import sexy.criss.gen.util.Util;
import sexy.criss.gen.util.UtilItem;
import sexy.criss.gen.util.UtilPlayer;
import worldguard_preset.util.Regions;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import static sexy.criss.game.prison.listener.players.StatisticBookHandler.STATISTIC_BOOK;

public class PlayerHandler extends SexyListener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		e.setJoinMessage(null);
		Main.getInstance().getSource().loadPlayer(p.getUniqueId());
		PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
		if(p.getInventory().getItem(8) == null) p.getInventory().setItem(8, STATISTIC_BOOK);

		BoardManager.createBoard(p, PrisonPlayer.getPlayer(p.getUniqueId()));

		p.sendMessage(Reference.PLAYER_JOIN_MESSAGE.get());

		new PlayerTag(p) {
			@Override
			public void run() {
				this.update();
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 20 * 10);

		new BukkitRunnable(){
			@Override
			public void run() {
				Set<ProtectedRegion> regs = Regions.getRegions(p.getLocation());
				ProtectedRegion reg = regs.stream().filter(cs -> !cs.getId().equalsIgnoreCase("__global__")).findFirst().orElse(null);
				if(reg != null && reg.getId().contains("_")) {
					int level = StringUtils.isNumeric(reg.getId().split("_")[1]) ? Integer.parseInt(reg.getId().split("_")[1]) : 1;
					if(PrisonPlayer.getPlayer(p.getUniqueId()).getLevel() < level) {
						BlockVector3 v1 = reg.getMinimumPoint();
						BlockVector3 v2 = reg.getMaximumPoint();

						Vector vec1 = new Vector(v1.getX(), v1.getY(), v1.getZ());
						Vector vec2 = new Vector(v2.getX(), v2.getY(), v2.getZ());

						Vector center = vec1.add(vec2.clone().subtract(vec1).multiply(0.5));
						Vector v = p.getLocation().toVector().subtract(center).normalize().multiply(-0.3);
						p.setVelocity(v);
						p.sendTitle("", Util.f("&6Нужен "+ level + " уровень"), 20, 20, 20);
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 2);
		Task.schedule(() -> {
			if(!pp.hasAchievement(Achievement.START)) {
				pp.addAchievement(Achievement.START);
				Util.s(p, "&6+10 золота за выполнение задания");
				pp.addGold(10);
			}
		}, 21);

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage(null);

		BoardManager.boards_map.remove(p);

		PrisonPlayer.getPlayer(p.getUniqueId()).save();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled()
				|| e.getBlock().getType() == Material.TRIPWIRE
				|| e.getBlock().getType().name().contains("SAPLING")
				|| e.getBlock().getType() == Material.TRIPWIRE_HOOK) return;

		Material m = e.getBlock().getType();
		Player p = e.getPlayer();
		PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
		Set<ProtectedRegion> regs = Regions.getRegions(e.getBlock().getLocation());
		ProtectedRegion reg = regs.stream().filter(pr -> !pr.getId().equalsIgnoreCase("__global__")).findFirst().orElse(null);
		if(reg != null) {
			if(reg.getId().contains("_") && reg.getId().split("_")[1] != null) {
				String[] a = reg.getId().split("_");
				int level = StringUtils.isNumeric(a[1]) ? Integer.parseInt(a[1]) : 1;
				if(pp.getLevel() <= level) {
					e.setCancelled(true);
					Util.ps("&c!", p, "&fИзвините, но вы не можете копать тут блоки");
					Util.s(p, "&f      Минимальный уровень: &c" + level);
					return;
				}
			}
		}
		if(!Util.inventoryIsFull(p)) {
			e.setExpToDrop(0);
			double multiplier = 1;
			for(Booster booster : Booster.getBoosters(p.getName())) {
				if(booster.getType().equals(Booster.BoosterType.BLOCKS)) {
					multiplier = booster.getMultiplier();
					break;
				}
			}
			pp.addBlock(m, Math.toIntExact(Math.round(multiplier)));
			for (Booster booster : Booster.getBoosters(p.getName())) {
				if(booster.getType().equals(Booster.BoosterType.KEYS)) {
					multiplier = booster.getMultiplier();
					break;
				}
			}
			int mod = !pp.hasPassive(PassiveType.FORTUNE, 1) ? 1 : pp.getPassive(PassiveType.FORTUNE) + 1;
			if (new Random().nextFloat() <= 0.001 * ((double)mod + multiplier)) {
				pp.droppedKeys++;
				p.getInventory().addItem(Util.update(PrisonItem.getItem("key"), "&6Обычный ключ", null));
				PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(Util.f("&6Кажись я нашёл ключ")), ChatMessageType.GAME_INFO, p.getUniqueId());
				UtilPlayer.getNMSPlayer(p).playerConnection.sendPacket(packet);
			}
		}
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		Arrays.stream(event.getChunk().getEntities()).filter(entity -> entity.getType() != EntityType.PLAYER).forEach(e -> event.setSaveChunk(true));
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		e.setDeathMessage(null);
		PrisonPlayer pp = PrisonPlayer.getPlayer(p.getUniqueId());
		Player killer = p.getKiller();
		EntityEquipment eq = p.getEquipment();
		p.closeInventory();
		Location l = p.getLocation();
		e.setKeepInventory(true);
		for(int i = 0; i < 36; ++i) {
			ItemStack item = p.getInventory().getItem(i);
			if (item != null) {
				if (this.willDrop(item)) {
					p.getWorld().dropItem(p.getLocation(), item);
					p.getInventory().remove(item);
				}

				if(!(item.getType().equals(Material.BOOK) && item.hasItemMeta() && item.getItemMeta().hasDisplayName())) {
					p.getWorld().dropItem(p.getLocation(), item);
					p.getInventory().remove(item);
				}

				if (p.getInventory().getHelmet() != null && this.willDrop(p.getInventory().getHelmet())) {
					p.getWorld().dropItem(p.getLocation(), p.getInventory().getHelmet());
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
				}

				if (p.getInventory().getChestplate() != null && this.willDrop(p.getInventory().getChestplate())) {
					p.getWorld().dropItem(p.getLocation(), p.getInventory().getChestplate());
					p.getInventory().setChestplate(new ItemStack(Material.AIR));
				}

				if (p.getInventory().getLeggings() != null && this.willDrop(p.getInventory().getLeggings())) {
					p.getWorld().dropItem(p.getLocation(), p.getInventory().getLeggings());
					p.getInventory().setLeggings(new ItemStack(Material.AIR));
				}

				if (p.getInventory().getBoots() != null && this.willDrop(p.getInventory().getBoots())) {
					p.getWorld().dropItem(p.getLocation(), p.getInventory().getBoots());
					p.getInventory().setBoots(new ItemStack(Material.AIR));
				}
			}
		}
		p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300 * pp.getLevel(), 2, false, false, false));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2, false, false, false));
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 4, false, false, false));
		p.setRemainingAir(p.getMaximumAir());
		p.setFireTicks(0);
		p.setFallDistance(0.0f);
		p.spigot().respawn();
		p.teleport(Util.getLoc(Main.getDatFile().get("clinic")));
		p.setRemainingAir(p.getMaximumAir());
		p.setFireTicks(0);
		boolean taked = pp.takeGold(pp.getLevel());
		pp.deaths++;
		if(killer != null) {
			PrisonPlayer ppk = PrisonPlayer.getPlayer(killer.getUniqueId());
			ppk.kills++;
			if(taked) ppk.addGold(pp.getLevel());
		}
		Util.s(p, killer != null ? "&4Вы были убиты игроком "+killer.getName() + "." : "&4Вы погибли");
	}

	private boolean willDrop(ItemStack it) {
		return !PrisonItem.isCustomItem(it) || PrisonItem.getPrisonItem(it).willDrop();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onDropItem(PlayerDropItemEvent e) {
		Item item = e.getItemDrop();
		ItemStack s = item.getItemStack();
		if(UtilItem.isArmor(s)) e.setCancelled(true);
		String id = s.hasItemMeta() && s.getItemMeta().hasLore() ? Util.strip(s.getItemMeta().getLore().get(0)) : "";
		if(id.equalsIgnoreCase("")) return;
		if(!willDrop(s)) e.setCancelled(true);
	}

	@EventHandler
	public void onCraft(CraftItemEvent e) {
		if(!(e.getWhoClicked().isOp())) {
			e.setResult(Event.Result.DENY);
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerFish(PlayerFishEvent event) {
		Item item;
		ItemStack is;
		if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught().getType() == EntityType.DROPPED_ITEM && ((is = (item = (Item)event.getCaught()).getItemStack()).getType() == Material.ENCHANTED_BOOK || is.getType() == Material.LEATHER_BOOTS || is.getType() == Material.FISHING_ROD || is.getType() == Material.BOW)) {
			item.setItemStack(new ItemStack(Material.AIR));
			item.remove();
			PrisonPlayer pp = PrisonPlayer.getPlayer(event.getPlayer().getUniqueId());
			pp.addGold((1.0 + (double)new Random().nextInt(9)) / 10.0);
			pp.setExp(pp.getExp() + (1 + new Random().nextInt(9)) / 10);
		}
	}

	@Override
	public String getName() {
		return "Player listener";
	}

	@Override
	public String getType() {
		return "PLAYERS";
	}

}
