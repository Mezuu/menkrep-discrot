package id.mezuu.mcdiscrot.game.listeners;

import id.mezuu.mcdiscrot.config.ConfigManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import java.time.OffsetDateTime;

public class PlayerDeathListener {
    public static void action(LivingEntity entity, DamageSource source, TextChannel channel) {
        if (channel != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            MessageEmbed embed = embedBuilder.setTitle(ConfigManager.getInstance().getConfig().messagePlayerDeathDiscord
                    .replace("${player}", entity.getName().getString()))
                    .setDescription(source.getDeathMessage(entity).getString())
                    .setTimestamp(OffsetDateTime.now())
                    .setColor(ConfigManager.getInstance().getConfig().embedPlayerDeathColor)
                    .setThumbnail("https://minotar.net/cube/" + entity.getName().getString())
                    .setFooter(ConfigManager.getInstance().getConfig().serverName, ConfigManager.getInstance().getConfig().serverIconUrl)
                    .build();

            channel.sendMessageEmbeds(embed).queue();
        }
    }
}
