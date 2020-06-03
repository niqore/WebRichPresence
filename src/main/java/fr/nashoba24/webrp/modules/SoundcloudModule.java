package fr.nashoba24.webrp.modules;

import club.minnced.discord.rpc.DiscordRichPresence;
import org.json.simple.JSONObject;

public class SoundcloudModule extends Module {

    private String title;
    private String author;
    private long progressTime;
    private long totalTime;
    private boolean playing;
    private long lastTime;

    public SoundcloudModule(String moduleName, String appId) {
        super(moduleName, appId);
    }

    public DiscordRichPresence formatRichPresence(DiscordRichPresence presence, JSONObject data) {
        title = getTitle(data);
        author = getAuthor(data);
        progressTime = getProgressTime(data);
        totalTime = getTotalTime(data);
        playing = isPlaying(data);

        presence.details = title;
        presence.state = "by " + author;
        if (!playing) {
            presence.smallImageKey = "pause";
        } else {
            presence.endTimestamp = System.currentTimeMillis() / 1000L + totalTime - progressTime;
        }
        presence.largeImageKey = "soundcloud_icon";
        return presence;
    }

    public boolean needUpdate(JSONObject data) {
        long deltaTime = Math.round((System.currentTimeMillis() - lastTime) / 1000.0);
        long tmpProgressTime = getProgressTime(data);
        boolean update = !getTitle(data).equals(title) || !getAuthor(data).equals(author) || getTotalTime(data) != totalTime || isPlaying(data) != playing || playing ? tmpProgressTime != progressTime + deltaTime : tmpProgressTime != progressTime;
        lastTime = System.currentTimeMillis();
        progressTime = tmpProgressTime;
        return update;
    }

    private String getTitle(JSONObject data) {
        return (String) data.get("title");
    }

    private String getAuthor(JSONObject data) {
        return (String) data.get("author");
    }

    private long getProgressTime(JSONObject data) {
        return (long) data.get("progressTime");
    }

    private long getTotalTime(JSONObject data) {
        return (long) data.get("totalTime");
    }

    private boolean isPlaying(JSONObject data) {
        return (boolean) data.get("playing");
    }
}
