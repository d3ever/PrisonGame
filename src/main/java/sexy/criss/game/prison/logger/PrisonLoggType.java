package sexy.criss.game.prison.logger;

public enum PrisonLoggType {

	INFO("INFO", "&9", "&b"), SUCCESSFULLY("SUCCESS", "&2&l", "&a"), ERROR("ERROR", "&c", "&c"),
	CRITICAL("CRITICAL-ERROR", "&4&l", "&4");

	String prefix;
	String color;
	String suffix;

	PrisonLoggType(String prefix, String color, String suffix) {
		this.prefix = prefix;
		this.color = color;
		this.suffix = suffix;
	}

	public String getPrefix() {
		return "&8[".concat(this.color).concat(this.prefix).concat("&8] ").concat(suffix);
	}

}
