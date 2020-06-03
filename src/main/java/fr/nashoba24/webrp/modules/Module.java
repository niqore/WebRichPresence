package fr.nashoba24.webrp.modules;

import club.minnced.discord.rpc.DiscordRichPresence;
import org.json.simple.JSONObject;

public abstract class Module {

    private String moduleName;
    private String appId;
    private boolean enabled = true;

    public Module(String moduleName, String appId) {
        this.moduleName = moduleName;
        this.appId = appId;
    }

    public String getApplicationId() {
        return appId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract DiscordRichPresence formatRichPresence(DiscordRichPresence presence, JSONObject data);

    public abstract boolean needUpdate(JSONObject data);
}
