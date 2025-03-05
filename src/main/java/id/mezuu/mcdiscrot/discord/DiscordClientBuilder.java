package id.mezuu.mcdiscrot.discord;

import id.mezuu.mcdiscrot.config.Config;
import id.mezuu.mcdiscrot.config.ConfigManager;
import id.mezuu.mcdiscrot.discord.listeners.DiscordSlashCommandListener;
import id.mezuu.mcdiscrot.utils.ModLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

public class DiscordClientBuilder {
    private JDABuilder builder;
    private final ArrayList<DiscordCommand> commands = new ArrayList<>();

    public DiscordClientBuilder() {
        ConfigManager configManager = ConfigManager.getInstance();
        Config config = configManager.getConfig();
        builder = JDABuilder.createLight(config.discordBotToken, EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT
                )
        );
    }

    public void addCommand(DiscordCommand command) {
        commands.add(command);
    }

    public void addListener(ListenerAdapter listener) {
        builder.addEventListeners(listener);
    }

    public TextChannel build() {
        try {
            builder.addEventListeners(new DiscordSlashCommandListener(commands));

            JDA jda = builder.build().awaitReady();

            Guild guild = jda.getGuildById(ConfigManager.getInstance().getConfig().discordServerId);
            if (guild == null) {
                throw new RuntimeException("Discord server not found : " + ConfigManager.getInstance().getConfig().discordServerId);
            }
            TextChannel channel = guild.getTextChannelById(ConfigManager.getInstance().getConfig().discordChannelId);
            if (channel == null) {
                throw new RuntimeException("Discord channel not found : " + ConfigManager.getInstance().getConfig().discordChannelId);
            }
            ModLogger.getInstance().info("Bot is ready");
//            channel.sendMessage("Bot is ready").queue();

            CommandListUpdateAction commands = jda.updateCommands();
            Collection<CommandData> commandDataCollection = new ArrayList<>();
            this.commands.forEach(command -> {
                ModLogger.getInstance().info("Registering command: {}", command.getCommand());
                commandDataCollection.add(Commands.slash(command.getCommand(), command.getDescription()));
            });
            commands.addCommands(commandDataCollection).queue();
            ConfigManager.getInstance().getConfig().serverIconUrl = guild.getIconUrl();

            return channel;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
