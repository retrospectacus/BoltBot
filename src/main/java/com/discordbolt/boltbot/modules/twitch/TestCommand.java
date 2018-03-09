package com.discordbolt.boltbot.modules.twitch;

import com.discordbolt.api.command.BotCommand;
import com.discordbolt.api.command.CommandContext;
import com.discordbolt.boltbot.system.twitch.TwitchRequests;

public class TestCommand {

    @BotCommand(command = "tt", description = "test", usage = "?", module = "Twitch Module")
    public static void testCommand(CommandContext cc) {
        try {
            cc.replyWith(TwitchRequests.getUserID(cc.getArgument(1)));
        } catch (Exception e) {
            e.printStackTrace();
            cc.replyWith(e.getMessage());
        }
    }
}
