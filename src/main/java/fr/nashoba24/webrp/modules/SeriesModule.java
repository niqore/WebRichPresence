package fr.nashoba24.webrp.modules;

import club.minnced.discord.rpc.DiscordRichPresence;
import org.json.simple.JSONObject;

public abstract class SeriesModule implements Module {

    private String title = "";
    private String episode;
    private long progressTime;
    private long totalTime;
    private boolean playing;
    private long lastTime;

    public DiscordRichPresence formatRichPresence(DiscordRichPresence presence, JSONObject data) {

        title = getTitle(data);
        if (getTitle(data).equals("none")) {
            presence.details = "Browsing";
        } else {
            episode = getEpisode(data);

            progressTime = getProgressTime(data);
            totalTime = getTotalTime(data);
            playing = isPlaying(data);

            presence.details = title;
            presence.state = episode;

            if (playing) {
                presence.endTimestamp = System.currentTimeMillis() / 1000L + totalTime - progressTime;
            } else {
                presence.smallImageKey = "pause";
            }
        }
        presence.largeImageKey = "logo";
        return presence;
    }

    public boolean needUpdate(JSONObject data) {
        long deltaTime = Math.round((System.currentTimeMillis() - lastTime) / 1000.0);
        boolean update;
        if (getTitle(data).equals("none") || title.equals("none")) {
            update = !getTitle(data).equals(title);
        } else {
            long tmpProgressTime = getProgressTime(data);
            update = !getTitle(data).equals(title) || !getEpisode(data).equals(episode) || getTotalTime(data) != totalTime || isPlaying(data) != playing || playing ? tmpProgressTime != progressTime + deltaTime : tmpProgressTime != progressTime;
            progressTime = tmpProgressTime;
        }
        lastTime = System.currentTimeMillis();
        return update;
    }

    private String getTitle(JSONObject data) {
        return (String) data.get("title");
    }

    private String getEpisode(JSONObject data) {
        return (String) data.get("episode");
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
