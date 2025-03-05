package id.mezuu.mcdiscrot.game.listeners;

import id.mezuu.mcdiscrot.config.ConfigManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.time.OffsetDateTime;

public class PlayerJoinListener {
    public static void action(ServerPlayNetworkHandler handler, TextChannel channel) {
        if (channel != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            MessageEmbed embed = embedBuilder.setTitle(ConfigManager.getInstance().getConfig().messagePlayerJoinDiscord
                    .replace("${player}", handler.getPlayer().getName().getString()))
                    .setTimestamp(OffsetDateTime.now())
                    .setColor(ConfigManager.getInstance().getConfig().embedPlayerJoinColor)
                    .setThumbnail("https://minotar.net/armor/body/" + handler.getPlayer().getName().getString())
                    .setFooter(ConfigManager.getInstance().getConfig().serverName, ConfigManager.getInstance().getConfig().serverIconUrl)
                    .build();

            channel.sendMessageEmbeds(embed).queue();
        }
    }
}
