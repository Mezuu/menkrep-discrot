package id.mezuu.mcdiscrot.database;

import id.mezuu.mcdiscrot.config.ConfigManager;

import java.sql.*;

public class Database {
    private static Database instance = null;
    private Connection conn;

    private Database() throws SQLException {
        this.conn = DriverManager.getConnection(ConfigManager.getInstance().getConfig().databaseSqliteUrl);
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS session (id INTEGER PRIMARY KEY, username TEXT, playtime INTEGER)");
    };

    public void addPlaytime(String username, int playtime) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM session WHERE username = ?");
        pstmt.setString(1, username);

        // Check if the user already exists in the database
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            // If the user already exists, update the playtime
            pstmt = conn.prepareStatement("UPDATE session SET playtime = ? WHERE username = ?");
            pstmt.setInt(1, rs.getInt("playtime") + playtime);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } else {
            // If the user does not exist, insert the user
            pstmt = conn.prepareStatement("INSERT INTO session (username, playtime) VALUES (?, ?)");
            pstmt.setString(1, username);
            pstmt.setInt(2, playtime);
            pstmt.executeUpdate();
        }
    };

    public static Database getInstance() {
        if (instance == null) {
            try {
                instance = new Database();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        return instance;
    }
}
