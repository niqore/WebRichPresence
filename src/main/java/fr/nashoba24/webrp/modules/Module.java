package fr.nashoba24.webrp.modules;

import club.minnced.discord.rpc.DiscordRichPresence;
import org.json.simple.JSONObject;

public interface Module {

    String getApplicationId();

    DiscordRichPresence formatRichPresence(DiscordRichPresence presence, JSONObject data);

    boolean needUpdate(JSONObject data);
}
