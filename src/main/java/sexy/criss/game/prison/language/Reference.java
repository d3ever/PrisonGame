package sexy.criss.game.prison.language;

import sexy.criss.gen.util.Util;

import java.util.List;

public enum Reference {
	CLEAR_INVENTORY_NOTIFY("$pr$&fОсвободите инвентарь, чтобы продолжить добычу блоков."),
	NUMBER_ERROR("$pr$&fВы неверно ввели число &c%s&7."),
	PERMISSION("$pr$&fУ вас недостаточно прав, чтобы использовать данное действие. %n&7Если вы считаете, что это не так, то свяжитесь с &bадминистратором&7 для получения подробной информации."),
	COMMAND_USAGE("$pr$&fИспользование: &6✎ /%s &8- &6%s"),
	COMMAND_CONSOLE_ONLY("$pr$&cДанную команду можно выполнить только из консоли. %nЕсли вы считаете, что это не так, то свяжитесь с администратором для получения подробной информации."),
	PLAYER_JOIN_MESSAGE("$pr$&fС возвращением"),
	PLAYER_ADMIN_JOIN("&8[&a+&8] &4&l%s"),
	PLAYER_ADMIN_QUIT("&8[&c-&8] &4&l%s"),
	PLAYER_DONATER_JOIN("&6%s&f присоединился к игре"),
	PLAYER_DONATER_QUIT("&6%s&f отключился от игры"),
	PLAYER_MONEY_GIVE("$pr$&fНа ваш счёт было начисленно &6%.2f&f."),
	PLAYER_MONEY_TAKE("$pr$&fС вашего счёта было списанно &c%.2f"),
	PLAYER_TIME("&fБудет выполнено через &6%d"),
	FRACTION_QUIT("$pr$&fВы покинули фракцию %s&f."),
	LOCATION_SPAWN_FAIL("$pr$&fТочка спавна не установлена, свяжитесь с администратором."),
	LOCATION_SPAWN_SET("$pr$&fточка справна была установлена."),
	FRACTION_NOT_NULL("$pr$&fУ вас уже есть фракция, чтобы сменить:\n  &6✎ Напишите /faction quit&f - чтобы покинуть фракцию."),
	FRACTION_GUI_TITLE("&0выберите фракцию"),
	FRACTION_JOIN("$pr$&fВы вступили во фракцию %s&f.\n  &fДля того, чтобы покинуть фракцию:\n  &6✎ Напишите /faction quit&f."),
	BALANCE_ENOUGHT("$pr$&fУ вас недостаточно золота. Требуется ещё &6%.2f&f."),
	PROMO_NOT_EXIST("$pr$&fПромокода &6%s&f не существует, проверьте название промокода."),
	PROMO_GET("$pr$&fПромокод &6%s&f был активирован. Вы получили &b%.2f&7 золота."),
	PROMO_CREATE("$pr$&fПромокод &6%s&f создан. Игроки которые используют данный промокод, получат &610$&f вам будет начислен бонус за каждого игрока."),
	PROMO_YOU_IS_OWN("$pr$&fВы не можете поблагодарить самого себя за веденный промокод."),
	PROMO_USED("$pr$&fВы уже использовали промокод &6%s&f."),
	PROMO_USE("$pr$&fВы использовали промокод &6%s&7 и получили за него &6%.2f&f золота."),
	PROMO_CREATE_FAILURE("$pr$&fПромокод &c%s&f уже существует, введите другое название."),
	PROMO_ID_FAIL("$pr$&fВы не можете создать промокод с названием &c%s&f.");

	String msg;
	List<String> lore;

	Reference(String msg) {
		this.msg = msg;
	}

	public List<String> getList() {
		return Util.f(lore);
	}

	public String get() {
		return Util.f(msg.replace("$pr$", "&cＲＦ ➣ &f"));
	}

	public String get(Object... args) {
		return Util.f(msg.replace("$pr$", "&cＲＦ ➣ &f"), args);
	}

}
