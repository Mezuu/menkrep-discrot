package id.mezuu.mcdiscrot.game.listeners;

import id.mezuu.mcdiscrot.config.ConfigManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.time.OffsetDateTime;

public class PlayerAdvancementListener {
    public static void action(String player, String message, TextChannel channel) {
        if (channel != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            MessageEmbed embed = embedBuilder.setTitle(ConfigManager.getInstance().getConfig().messagePlayerAdvancementDiscord
                    .replace("${player}", player))
                    .setDescription(message)
                    .setTimestamp(OffsetDateTime.now())
                    .setColor(ConfigManager.getInstance().getConfig().embedPlayerAdvancementColor)
                    .setThumbnail("https://minotar.net/helm/" + player)
                    .setFooter(ConfigManager.getInstance().getConfig().serverName, ConfigManager.getInstance().getConfig().serverIconUrl)
                    .build();

            channel.sendMessageEmbeds(embed).queue();
        }
    }
}
