package id.mezuu.mcdiscrot.discord.commands;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.mezuu.mcdiscrot.config.Config;
import id.mezuu.mcdiscrot.config.ConfigManager;
import id.mezuu.mcdiscrot.discord.DiscordCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ListAllPlayer extends DiscordCommand {
    private MinecraftServer server;
    private final Config config = ConfigManager.getInstance().getConfig();

    public ListAllPlayer(MinecraftServer server) {
        super("list", "List all players currently in game", "list", server);
        this.server = server;
    }

    @Override
    public void action(SlashCommandInteractionEvent event) throws JsonProcessingException {
        super.action(event);
        Collection<ServerPlayerEntity> players = PlayerLookup.all(server);
        int maxPlayers = server.getMaxPlayerCount();

        final EmbedBuilder[] embedBuilder = {new EmbedBuilder()};
        embedBuilder[0] = embedBuilder[0].setTitle(config.messagePlayerListDiscord
                .replace("${online}", "**" + players.size() + "**")
                .replace("${max}", "**" + maxPlayers + "**"))
                .setTimestamp(OffsetDateTime.now())
                .setColor(ConfigManager.getInstance().getConfig().embedPlayerListColor)
                .setFooter(config.serverName, config.serverIconUrl);

        players.forEach(player -> {
            embedBuilder[0] = embedBuilder[0].addField(new MessageEmbed.Field(player.getName().getString(), player.getUuid().toString(), false));
        });

        MessageEmbed embed = embedBuilder[0].build();

        event.replyEmbeds(embed).queue();
    }
}
