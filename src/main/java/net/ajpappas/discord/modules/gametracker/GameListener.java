package net.ajpappas.discord.modules.gametracker;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.user.PresenceUpdateEvent;
import sx.blah.discord.handle.impl.events.user.UserUpdateEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;

import java.util.HashMap;

/**
 * Created by Tony on 6/13/2017.
 */
public class GameListener {

    private HashMap<Long, UserInfo> currentUsers = new HashMap<>();

    @EventSubscriber
    public void onGameChange(PresenceUpdateEvent e) {
        if (currentUsers.containsKey(e.getUser().getLongID())) { // User was already in a game
            UserInfo ui = currentUsers.get(e.getUser().getLongID());

            if (e.getNewPresence().getPlayingText().isPresent() && ui.getGame().equals(e.getNewPresence().getPlayingText().get())) { // Event was fired for same game they are already playing
                return;
            }
            // User is finished playing their game
            if (System.currentTimeMillis() - ui.getStartTime() >= GameTrackerModule.MIN_GAME_TIME) {
                GameLog.addGameLog(e.getUser(), currentUsers.get(e.getUser().getLongID()).getGame(), ui.getStartTime(), System.currentTimeMillis());
            }
            currentUsers.remove(e.getUser().getLongID());
        }

        if (e.getNewPresence().getPlayingText().isPresent() && !e.getNewPresence().getStatus().equals(StatusType.STREAMING)) {
            currentUsers.put(e.getUser().getLongID(), new UserInfo(e.getUser(), e.getNewPresence().getPlayingText().get(), System.currentTimeMillis()));
        }
    }

    @EventSubscriber
    public void updateUsername(UserUpdateEvent e) {
        if (!e.getOldUser().getName().equals(e.getNewUser().getName()))
            GameLog.addUser(e.getNewUser());
    }

    @EventSubscriber
    public void joinGuild(GuildCreateEvent e) {
        GameLog.addUsers(e.getGuild().getUsers());

        for (IUser user : e.getGuild().getUsers()) {
            if (user.isBot())
                continue;
            if (user.getPresence().getPlayingText().isPresent() && !user.getPresence().getStatus().equals(StatusType.STREAMING))
                currentUsers.put(user.getLongID(), new UserInfo(user, user.getPresence().getPlayingText().get(), System.currentTimeMillis()));
        }

    }

    @EventSubscriber
    public void userJoin(UserJoinEvent e) {
        if (e.getUser().isBot())
            return;
        GameLog.addUser(e.getUser());

        if (e.getUser().getPresence().getPlayingText().isPresent() && !e.getUser().getPresence().getStatus().equals(StatusType.STREAMING))
            currentUsers.put(e.getUser().getLongID(), new UserInfo(e.getUser(), e.getUser().getPresence().getPlayingText().get(), System.currentTimeMillis()));
    }
}