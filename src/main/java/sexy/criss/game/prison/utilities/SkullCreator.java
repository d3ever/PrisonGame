package sexy.criss.game.prison.utilities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

public class SkullCreator {

	public static String GOAT_BASE64;
	public static String THIEVE_BASE64;
	public static String WARRIOR_BASE64;
	public static String ROOSTER_BASE64;
	public static String HECK_BASE64;
	public static String ARROW_LEFT_BASE64;
	public static String ARROW_RIGHT_BASE64;
	public static String DONATE_MINE_BASE64;
	public static String VIP_MINE_BASE64;
	public static String BACK_BUTTON;
	public static String MINE_VAULT_BASE64;
	public static String ACCESS_MINES_BASE64;

	public static String HAY_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY0NTliZTA5OTk4ZTUwYWJkMmNjZjRjZDM4M2U2YjM4YWI1YmM5MDVmYWNiNjZkY2UwZTE0ZTAzOGJhMTk2OCJ9fX0=";
	public static String STONE_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc0Mjg4NWRiNjgwM2FiMzY4MGNhNTBhYzJiOTdiMDhiZGRjMjRlMWFkMzA5NWRhOTE0Njc3ZjUyNGE2YjFhNiJ9fX0=";
	public static String SHEEP_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTVhZDkzZDU2NTQ2ZjEyZDUzNTZlZmZjYmM2ZWM0Yzg3YmEyNDVkODFlMTY2MmM0YjgzMGY3ZDI5OGU5In19fQ==";
	public static String FIRE_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZjMGVjMGU2OTBlMjg3ZDNlYmZiODk5YjliZWViZWM3NGVkYjE1YTNhZmIyYWY0MzE0MGM0YzU0ZjgyNzUxNiJ9fX0=";
	public static String EMERALD_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTEzNTBlMGE2MDY5NjVmMmVmMDk0OTFiYTU0ZWUxMzc5MTVkZTU4YThhMjhhYjEzMGE1M2ZhOGY0MTZiMjhkNSJ9fX0=";
	public static String ICE_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU2Njc1NjVkODhlMTI2ZTlmZWUxYmI0NjI3NDJmODhjOTFiNmY3YTA3NDgxNWJmY2Q5ZDEwZGY5ZDY5Y2FlMCJ9fX0=";
	public static String SPONGE_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVmNjEzMGYxY2Y4YmExNjhjNTdmYzAzNzMwYWUwYzA2ODAzMzMzODAzNTE3MWRhMmZlNTMwMWNkZTM2YTJhNCJ9fX0=";
	public static String WEB_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk2MGRhMjYxMjZiYzAyYWU4ODIzMTAxMDVjNzY1ZTEwNjk0NzkyZTY3NzE2ZGQ1MGJhZmVjMTkwZmViZWNmMyJ9fX0=";
	public static String LOG_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE4YzIwZjhjNTg3MmViMWQ4MDgwMmY2NzMxMjJmYmUzMDg5MTNkMWI2YmE0MTc2YTQ0ZTlkNjczZDQ5OWU0In19fQ==";
	public static String SAND_BLOCK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y0Y2Q2MWRmMDI3ODkyN2JiNWM5ZjRlMDU5YTMxNzkzM2NmZjZhMDAyNGNhMjMwMWQ5YzllNTE1MDIzNGU3MSJ9fX0=";

	private static boolean warningPosted = false;

	private static Field blockProfileField;
	private static Method metaSetProfileMethod;
	private static Field metaProfileField;

	public static Material getSkullMaterial() {
		checkLegacy();
		try {
			return Material.valueOf("PLAYER_HEAD");
		}catch (IllegalArgumentException e) {
			return Material.valueOf("SKULL_ITEM");
		}
	}

	public static ItemStack createSkull() {
		checkLegacy();

		try {
			return new ItemStack(Material.valueOf("PLAYER_HEAD"));
		} catch (IllegalArgumentException e) {
			return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
		}
	}

	public static ItemStack itemFromName(String name) {
		return itemWithName(createSkull(), name);
	}

	public static ItemStack itemFromUuid(UUID id) {
		return itemWithUuid(createSkull(), id);
	}

	public static ItemStack itemFromUrl(String url) {
		return itemWithUrl(createSkull(), url);
	}

	public static ItemStack itemFromBase64(String base64) {
		return itemWithBase64(createSkull(), base64);
	}

	public static ItemStack itemWithName(ItemStack item, String name) {
		notNull(item, "item");
		notNull(name, "name");

		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack itemWithUuid(ItemStack item, UUID id) {
		notNull(item, "item");
		notNull(id, "id");

		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack itemWithUrl(ItemStack item, String url) {
		notNull(item, "item");
		notNull(url, "url");

		return itemWithBase64(item, urlToBase64(url));
	}

	public static ItemStack itemWithBase64(ItemStack item, String base64) {
		notNull(item, "item");
		notNull(base64, "base64");

		if (!(item.getItemMeta() instanceof SkullMeta)) {
			return null;
		}
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		mutateItemMeta(meta, base64);
		item.setItemMeta(meta);

		return item;
	}

	public static void blockWithName(Block block, String name) {
		notNull(block, "block");
		notNull(name, "name");

		Skull state = (Skull) block.getState();
		state.setOwningPlayer(Bukkit.getOfflinePlayer(name));
		state.update(false, false);
	}

	public static void blockWithUuid(Block block, UUID id) {
		notNull(block, "block");
		notNull(id, "id");

		setToSkull(block);
		Skull state = (Skull) block.getState();
		state.setOwningPlayer(Bukkit.getOfflinePlayer(id));
		state.update(false, false);
	}

	public static void blockWithUrl(Block block, String url) {
		notNull(block, "block");
		notNull(url, "url");

		blockWithBase64(block, urlToBase64(url));
	}

	public static void blockWithBase64(Block block, String base64) {
		notNull(block, "block");
		notNull(base64, "base64");

		setToSkull(block);
		Skull state = (Skull) block.getState();
		mutateBlockState(state, base64);
		state.update(false, false);
	}

	private static void setToSkull(Block block) {
		checkLegacy();

		try {
			block.setType(Material.valueOf("PLAYER_HEAD"), false);
		} catch (IllegalArgumentException e) {
			block.setType(Material.valueOf("SKULL"), false);
			Skull state = (Skull) block.getState();
			state.setSkullType(SkullType.PLAYER);
			state.update(false, false);
		}
	}

	private static void notNull(Object o, String name) {
		if (o == null) {
			throw new NullPointerException(name + " should not be null!");
		}
	}

	private static String urlToBase64(String url) {

		URI actualUrl;
		try {
			actualUrl = new URI(url);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl.toString() + "\"}}}";
		return Base64.getEncoder().encodeToString(toEncode.getBytes());
	}

	private static GameProfile makeProfile(String b64) {
		UUID id = new UUID(
				b64.substring(b64.length() - 20).hashCode(),
				b64.substring(b64.length() - 10).hashCode());
		GameProfile profile = new GameProfile(id, "Player");
		profile.getProperties().put("textures", new Property("textures", b64));
		return profile;
	}

	private static void mutateBlockState(Skull block, String b64) {
		try {
			if (blockProfileField == null) {
				blockProfileField = block.getClass().getDeclaredField("profile");
				blockProfileField.setAccessible(true);
			}
			blockProfileField.set(block, makeProfile(b64));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private static void mutateItemMeta(SkullMeta meta, String b64) {
		try {
			if (metaSetProfileMethod == null) {
				metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
				metaSetProfileMethod.setAccessible(true);
			}
			metaSetProfileMethod.invoke(meta, makeProfile(b64));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {

			try {
				if (metaProfileField == null) {
					metaProfileField = meta.getClass().getDeclaredField("profile");
					metaProfileField.setAccessible(true);
				}
				metaProfileField.set(meta, makeProfile(b64));

			} catch (NoSuchFieldException | IllegalAccessException ex2) {
				ex2.printStackTrace();
			}
		}
	}

	private static void checkLegacy() {
		try {
			Material.class.getDeclaredField("PLAYER_HEAD");
			Material.valueOf("SKULL");

			if (!warningPosted) {
				Bukkit.getLogger().warning("SKULLCREATOR API - Using the legacy bukkit API with 1.13+ bukkit versions is not supported!");
				warningPosted = true;
			}
		} catch (NoSuchFieldException | IllegalArgumentException ignored) {}
	}

	static {
		GOAT_BASE64    = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDU3YTBkNTM4ZmEwOGE3YWZmZTMxMjkwMzQ2ODg2MTcyMGY5ZmEzNGU4NmQ0NGI4OWRjZWM1NjM5MjY1ZjAzIn19fQ==";
		THIEVE_BASE64  = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdkMDFjMDI5ZmZkODQ5YmI1NjE4MDU2MDk4MDRiMDE4MjcxNGZjYjYyOTA2M2I3ODc1MjM0MmFiZTIzMWFlNSJ9fX0=";
		WARRIOR_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTIwODgxYzM5NWU1NDgyM2U3ODBhOWEzNTVkNDNjM2I3NGZkZDkwMjEwYWI5MDI3M2ZhOWM5YmFjODU3YmU2YiJ9fX0=";
		ROOSTER_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJkNTEyMmE1YzE0NzU4ODI1YzRlMTkyODQ0Nzg2ZjMxNTI3NTE1OTBhNzcxYmEzMWM4ZDMyMjZhMmE1ZiJ9fX0=";
		HECK_BASE64    = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFiZjUyOGIzYzRiYjFmMmE3NjQxM2JlYmM1NzMyMzUwMzBlN2ZjYTRjNjkyM2QwNTZhYmVmY2ExNTA3NGE0NiJ9fX0=";
		ARROW_LEFT_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=";
		ARROW_RIGHT_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19";
		DONATE_MINE_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY3NDY3YzVmNzM4YzY0MTI0NmMwOWY4Y2U3OTFlMzM5YTg2ZTgxZGU2MjA0OWI0MWY0OTI4ODgxNzJmYTcyNiJ9fX0=";
		VIP_MINE_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNlMTM3ZmYwNzYzNzg4ZDU1M2UwNTZiNjk4MzU5MDI5NGIzZTM3ZDU3ZjAzYjUxODc4NTBkMDMzNWIyOTc5OSJ9fX0=";
		BACK_BUTTON = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg4N2NjMzg4YzhkY2ZjZjFiYThhYTVjM2MxMDJkY2U5Y2Y3YjFiNjNlNzg2YjM0ZDRmMWMzNzk2ZDNlOWQ2MSJ9fX0=";
		MINE_VAULT_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFlYWEyZmU5MjAyNzRkYWEzYWZhNDEwYTdjMjQ4NzcxZGE4ZGZkZDQ3ZWJiZTJlZDI2YmI5YzM2Y2EyYTczZiJ9fX0=";
		ACCESS_MINES_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE5OWI1ZWUzMjBlNzk5N2Q5MWJiNWY4NjY1ZjNkMzJhZTQ5MjBlMDNjNmIzZDliN2VlY2E2OTcxMTk5OTcifX19";
	}
}