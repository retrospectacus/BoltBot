package net.ajpappas.discord.modules.audiostreamer.commands;

import net.ajpappas.discord.api.commands.BotCommand;
import net.ajpappas.discord.api.commands.CommandContext;
import net.ajpappas.discord.api.commands.exceptions.CommandException;
import net.ajpappas.discord.modules.audiostreamer.AudioStreamer;

/**
 * Created by Tony on 4/22/2017.
 */
public class SkipCommand {

    @BotCommand(command = "skip", module = "Audio Streamer Module", description = "Skip to the next song.", usage = "Skip <Force> <Count>", minArgs = 1, maxArgs = 3, allowedChannels = "music")
    public static void leaveCommand(CommandContext cc) {
        try {
            int count = 1;
            if (cc.getArgCount() >= 3) {
                try {
                    count = Integer.valueOf(cc.getArgument(2));
                } catch (NumberFormatException e) {
                }
            }
            AudioStreamer.getVoiceManager().skip(cc.getGuild(), cc.getUser(), cc.getArgCount() > 1, count);
        } catch (CommandException e) {
            cc.replyWith(e.getMessage());
            return;
        }
    }
}