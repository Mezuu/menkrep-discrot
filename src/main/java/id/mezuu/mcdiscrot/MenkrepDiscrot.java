package id.mezuu.mcdiscrot;

import id.mezuu.mcdiscrot.database.Database;
import id.mezuu.mcdiscrot.discord.listeners.DiscordChatListener;
import id.mezuu.mcdiscrot.discord.DiscordClientBuilder;
import id.mezuu.mcdiscrot.discord.commands.ListAllPlayer;
import id.mezuu.mcdiscrot.game.listeners.*;
import id.mezuu.mcdiscrot.utils.ModLogger;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenkrepDiscrot implements ModInitializer {
    private MinecraftServer server;
    private TextChannel channel;
    private DiscordClientBuilder clientBuilder;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.server = server;

            // Initialize database
            Database.getInstance();

            synchronized (this) {
                if (clientBuilder == null) {
                    initializeDiscord();
                }
            }
            ServerOnlineListener.action(channel);

            // Heartbeat every x seconds
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
                    if (players.isEmpty()) {
                        return;
                    }

                    players.forEach(player -> {
                        try {
                            Database.getInstance().addPlaytime(player.getName().getString(), 5);
                            ModLogger.getInstance().info("Added 5 seconds to {}", player.getName().getString());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }, 0, 5000);
        });

        // Send chat message from game to discord

        PlayerChatListener playerChatListener = new PlayerChatListener();
        ServerMessageEvents.CHAT_MESSAGE.register((message, player, p) -> {
            playerChatListener.action(message, player, channel);
        });

        // Send join message from game to discord
        ServerPlayConnectionEvents.JOIN.register((handler, sender, s) -> {
            PlayerJoinListener.action(handler, channel);
        });

        // Send leave message from game to discord
        ServerPlayConnectionEvents.DISCONNECT.register((handler, s) -> {
            PlayerLeaveListener.action(handler, channel);
        });

        // send player death message from game to discord
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity.isPlayer()) {
                PlayerDeathListener.action(entity, source, channel);
            }
        });

        // Send message when the server is offline
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            ServerOfflineListener.action(channel);
        });

        // Send message when a player achieves advancements
        ServerMessageEvents.GAME_MESSAGE.register((server, message, p) -> {
            String regex = "^(.+) has made the advancement \\[(.+)]$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(message.getString());
            if (matcher.matches()) {
                PlayerAdvancementListener.action(matcher.group(1), message.getString(), channel);
            }
        });
    }

    private void initializeDiscord() {
        clientBuilder = new DiscordClientBuilder();

        // Relay discord chat message to game
        clientBuilder.addListener(new DiscordChatListener(server));

        // /list - list all players currently in game
        clientBuilder.addCommand(new ListAllPlayer(server));

        channel = clientBuilder.build();
    }
}
