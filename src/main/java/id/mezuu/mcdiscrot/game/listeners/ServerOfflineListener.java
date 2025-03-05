package id.mezuu.mcdiscrot.game.listeners;

import id.mezuu.mcdiscrot.config.ConfigManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.OffsetDateTime;

public class ServerOfflineListener {
    public static void action(TextChannel channel) {
        if (channel != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            MessageEmbed embed = embedBuilder.setTitle(ConfigManager.getInstance().getConfig().messageServerOfflineDiscord)
                    .setDescription(ConfigManager.getInstance().getConfig().messageServerOfflineDescriptionDiscord)
                    .setTimestamp(OffsetDateTime.now())
                    .setColor(ConfigManager.getInstance().getConfig().embedServerOfflineColor)
                    .setFooter(ConfigManager.getInstance().getConfig().serverName, ConfigManager.getInstance().getConfig().serverIconUrl)
                    .build();

            channel.sendMessageEmbeds(embed).queue();
        }
    }
}
