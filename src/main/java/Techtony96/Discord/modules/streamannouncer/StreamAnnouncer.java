package Techtony96.Discord.modules.streamannouncer;

import Techtony96.Discord.api.CustomModule;
import Techtony96.Discord.utils.ChannelUtil;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.user.PresenceUpdateEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.modules.IModule;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tony on 12/24/2016.
 */
public class StreamAnnouncer extends CustomModule implements IModule {

    private ArrayList<Streamer> streamers = new ArrayList<>();

    public StreamAnnouncer() {
        super("Stream Announcer", "1.0");
    }

    @EventSubscriber
    public void onUserGameUpdate(PresenceUpdateEvent e) {
        if (e.getNewPresence().getStatus() == StatusType.STREAMING) {
            for (Streamer s : streamers) {
                if (s.getStreamer().equals(e.getUser())) {
                    if (s.isTimePassed(2, TimeUnit.HOURS) || s.isTimeAfterElapsed(1, TimeUnit.HOURS)) {
                        sendAnnouncement(e);
                        streamers.remove(s);
                        streamers.add(new Streamer(e.getUser()));
                        return;
                    } else
                        return;
                }
            }
            sendAnnouncement(e);
            streamers.add(new Streamer(e.getUser()));
        }
    }

    private void sendAnnouncement(PresenceUpdateEvent e) {
        for (IGuild guild : client.getGuilds()) {
            if (!guild.getUsers().contains(e.getUser()))
                continue;

            ChannelUtil.sendMessage(guild.getGeneralChannel(), e.getUser().mention() + " just started streaming " + e.getNewPresence().getPlayingText().get());
            ChannelUtil.sendMessage(guild.getGeneralChannel(), "Come join in on the fun! <" + e.getNewPresence().getStreamingUrl().orElseGet(null) + ">");
        }
    }
}
