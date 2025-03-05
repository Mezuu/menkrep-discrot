package id.mezuu.mcdiscrot.game.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.time.OffsetDateTime;

public class PlayerChatListener {
    private SignedMessage lastMessage;
    private MessageEmbed lastMessageEmbed;

    public PlayerChatListener() {}

    public void action(SignedMessage message, ServerPlayerEntity player, TextChannel channel) {
        if (channel == null) {
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder = embedBuilder
                .setTimestamp(OffsetDateTime.now())
                .setColor(12566463)
                .setAuthor(player.getName().getString(), null, "https://minotar.net/avatar/" + player.getName().getString());

        if (lastMessage == null || !lastMessage.getSender().equals(player.getUuid()) || lastMessage.getTimestamp().toEpochMilli() + 10000 < System.currentTimeMillis()) {
            MessageEmbed messageEmbed = embedBuilder.setDescription(message.getContent().getString()).build();
            channel.sendMessageEmbeds(messageEmbed).queue();
            lastMessageEmbed = messageEmbed;
        } else {
            MessageEmbed messageEmbed = embedBuilder.setDescription(lastMessageEmbed.getDescription() + "\n" + message.getContent().getString()).build();
            channel.editMessageEmbedsById(channel.getLatestMessageId(), messageEmbed).queue();
            lastMessageEmbed = messageEmbed;
        }

        lastMessage = message;
    }
}
