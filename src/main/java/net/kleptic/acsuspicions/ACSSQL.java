package net.kleptic.acsuspicions;

import net.md_5.bungee.api.ProxyServer;

import java.sql.*;
import java.util.UUID;

public class ACSSQL extends ACSDatabase{
    
    private Connection connection;
    private int refreshDelay;
    private int cleanupDelay;
    
    public static ACSSQL connect(ACSuspicions plugin, String host, int port, String user, String password, String database, int refreshDelay, int cleanupDelay) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
    
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        plugin.getLogger().info("Connecting to: "+"jdbc:mysql://"+host+":"+port+"/"+database);
        Connection con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database,user,password);
        con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS acsuspicions (player CHAR(36) NOT NULL, time TIMESTAMP, hacktype VARCHAR(32), server VARCHAR(32))");
        return new ACSSQL(plugin,con, refreshDelay, cleanupDelay);
    }
    
    private ACSSQL(ACSuspicions plugin, Connection connection, int refreshDelay, int cleanupDelay){
        
        super(plugin);
        this.connection = connection;
        this.refreshDelay = refreshDelay;
        this.cleanupDelay = cleanupDelay;
    
    }
    
    
    public int countSuspicions(UUID player) {
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM acsuspicions WHERE player = ?");
            statement.setString(1, player.toString());
            ResultSet result = statement.executeQuery();
            if(result.next()){
                return result.getInt(1);}
            else return 0;
            
        }
        catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    
    public boolean addSuspicion(Suspicion suspicion) {
        
        try{
            PreparedStatement statement = connection.prepareStatement("INSERT INTO acsuspicions VALUES (?, ?, ?, ?)");
            statement.setString(1, suspicion.player.toString());
            statement.setTimestamp(2, suspicion.time);
            statement.setString(3, suspicion.hacktype);
            statement.setString(4, suspicion.server);
        return statement.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
