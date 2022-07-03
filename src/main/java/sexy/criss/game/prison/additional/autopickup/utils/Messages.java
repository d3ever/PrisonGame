package sexy.criss.game.prison.additional.autopickup.utils;

import sexy.criss.game.prison.Main;

public class Messages {

    private String prefix;
    private String autoPickupEnable;
    private String autoPickupDisable;
    private String autoEnabled;
    private String autoReenabled;
    private String reload;
    private String fullInventory;
    private String noPerms;

    public String getMsgAutoDropsEnable() {
        return msgAutoDropsEnable;
    }

    public String getMsgAutoDropsDisable() {
        return msgAutoDropsDisable;
    }

    public String getMsgAutoSmeltEnable() {
        return msgAutoSmeltEnable;
    }

    public String getMsgAutoSmeltDisable() {
        return msgAutoSmeltDisable;
    }

    private String msgAutoDropsEnable;
    private String msgAutoDropsDisable;
    private String msgAutoSmeltEnable;
    private String msgAutoSmeltDisable;

    public String getPrefix() {
        return prefix;
    }

    public String getAutoPickupEnable() {
        return autoPickupEnable;
    }

    public String getAutoPickupDisable() {
        return autoPickupDisable;
    }

    public String getAutoEnabled() {
        return autoEnabled;
    }

    public String getAutoReenabled() {
        return autoReenabled;
    }

    public String getReload() {
        return reload;
    }

    public String getFullInventory() {
        return fullInventory;
    }

    public String getNoPerms() {
        return noPerms;
    }

    public Messages() {
        this.prefix = HexFormat.format("&8[&5Auto&d&oPickup&8]");
        this.autoPickupEnable = HexFormat.format("&7You have&a enabled &7auto pickup.");
        this.autoPickupDisable = HexFormat.format("&7You have&c disabled &7auto pickup.");
        this.autoEnabled = HexFormat.format("&7Auto pickup has been automatically&a enabled&7.");
        this.autoReenabled = HexFormat.format("&7Auto pickup has been automatically&a enabled&7.");
        this.reload = HexFormat.format("&cReloaded Config");
        this.fullInventory = HexFormat.format("&7Your inventory is full!");
        this.noPerms = HexFormat.format("&cYou don't have permission to use this command");
        this.msgAutoSmeltEnable = HexFormat.format("&7You have&a enabled &7auto smelt.");
        this.msgAutoSmeltDisable = HexFormat.format("&7You have&c disabled &7auto smelt.");
    }

}