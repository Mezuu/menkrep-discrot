package id.mezuu.mcdiscrot.discord.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.mezuu.mcdiscrot.discord.DiscordCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DiscordSlashCommandListener extends ListenerAdapter {
    private final ArrayList<DiscordCommand> commands;

    public DiscordSlashCommandListener(ArrayList<DiscordCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);
        commands.forEach(command -> {
            if (event.getName().equals(command.getCommand())) {
                try {
                    command.action(event);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
