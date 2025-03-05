package id.mezuu.mcdiscrot.config;

import org.jetbrains.annotations.Nullable;

public class Config {
    public String serverName;
    public String discordBotToken;
    public String discordServerId;
    public String discordChannelId;
    public String messagePlayerJoinDiscord;
    public String messagePlayerLeaveDiscord;
    public String messageServerOnlineDiscord;
    public String messageServerOnlineDescriptionDiscord;
    public String messageServerOfflineDiscord;
    public String messageServerOfflineDescriptionDiscord;
    public String messagePlayerListDiscord;
    public String messagePlayerDeathDiscord;
    public String messagePlayerAdvancementDiscord;

    public int embedPlayerJoinColor;
    public int embedPlayerLeaveColor;
    public int embedServerOnlineColor;
    public int embedServerOfflineColor;
    public int embedPlayerListColor;
    public int embedPlayerDeathColor;
    public int embedPlayerAdvancementColor;

    public int sessionHeartbeatInterval;
    public int sessionDisconnectTolerance;

    public String databaseSqliteUrl;

    @Nullable
    public String serverIconUrl;
}

