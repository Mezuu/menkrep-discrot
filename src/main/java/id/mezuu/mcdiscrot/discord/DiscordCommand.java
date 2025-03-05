package id.mezuu.mcdiscrot.discord;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.minecraft.server.MinecraftServer;

public abstract class DiscordCommand {
    private String command;
    private String description;
    private String usage;
    private MinecraftServer server;

    public DiscordCommand(String command, String description, String usage, MinecraftServer server) {
        this.command = command;
        this.description = description;
        this.usage = usage;
        this.server = server;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public void action(SlashCommandInteractionEvent event) throws JsonProcessingException {

    }
}
