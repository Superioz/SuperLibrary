package de.superioz.library.java.database.sql;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connects to and uses a MySQL database
 */
public class MySQL extends Database {

    private final String user;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;

    /**
     * Creates a new MySQL instance
     *
     * @param plugin   Plugin instance
     * @param hostname Name of the host
     * @param port     Port number
     * @param database Database name
     * @param username Username
     * @param password Password
     */
    public MySQL(Plugin plugin, String hostname, String port, String database,
                 String username, String password){
        super(plugin);
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
    }

    // -- Intern methods

    @Override
    public Connection openConnection(){
        if(checkConnection()){
            return connection;
        }

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" +
                    this.hostname + ":" + this.port + "/" + this.database + "?user=" + this.user
                    + "&password=" + this.password
                    + "&autoReconnect=true");
            return connection;
        }catch(SQLException | ClassNotFoundException e){
            //
        }

        return null;
    }

    public String getDatabase(){
        return database;
    }

    public String getHostname(){
        return hostname;
    }

    public String getPassword(){
        return password;
    }

    public String getPort(){
        return port;
    }

    public String getUser(){
        return user;
    }
}