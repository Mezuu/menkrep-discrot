package id.mezuu.mcdiscrot.discord.listeners;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import id.mezuu.mcdiscrot.utils.ModLogger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class DiscordChatListener extends ListenerAdapter {
    MinecraftServer server;

    public DiscordChatListener(@NotNull MinecraftServer server) {
        this.server = server;
        ModLogger.getInstance().info(server.getServerMotd());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        // Check for bot messages to avoid infinite loops
        if (event.getAuthor().isBot() || server.getCurrentPlayerCount() == 0) {
            return;
        }

        JsonObject json = new JsonObject();
        json.addProperty("text", event.getAuthor().getName() + ": " + event.getMessage().getContentRaw());
        json.addProperty("color", "blue");
        String command = "tellraw @a " + json;
        try {
            ModLogger.getInstance().info(event.getAuthor().getName() + ": " + event.getMessage().getContentRaw());
            server.getCommandManager().getDispatcher().execute(
                    command,
                    server.getCommandSource()
            );
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
