package de.superioz.library.java.database.sql;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract Database class, serves as a base for any connection method (MySQL, SQLite, etc.) Inspired by -_Husky_- and tips48
 */
public abstract class Database {

    protected Connection connection;
    protected Plugin plugin;

    /**
     * Creates a new Database
     *
     * @param plugin Plugin instance
     */
    protected Database(Plugin plugin){
        this.plugin = plugin;
        this.connection = null;
    }

    /**
     * Opens a connection with the database
     *
     * @return Opened connection
     */
    public abstract Connection openConnection();

    /**
     * Checks if a connection is open with the database
     *
     * @return true if the connection is open
     */
    public boolean checkConnection(){
        try{
            return connection != null && !connection.isClosed();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the connection with the database
     *
     * @return Connection with the database, null if none
     */
    public Connection getConnection(){
        return connection;
    }

    /**
     * Closes the connection with the database
     *
     * @return true if successful
     */
    public boolean closeConnection(){
        if(connection == null){
            return false;
        }
        try{
            connection.close();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Executes a SQL Query
     *
     * @return the results of the query
     */
    public ResultSet querySQL(PreparedStatement statement) throws SQLException{
        try{
            if(!checkConnection()){
                openConnection();
            }

            return statement.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(statement != null)
                statement.close();
        }

        return null;
    }

    /**
     * Executes an Update SQL Query
     *
     * @return Result code
     */
    public int updateSQL(PreparedStatement statement) throws SQLException{
        try{
            if(!checkConnection()){
                openConnection();
            }

            return statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(statement != null)
                statement.close();
        }

        return -1;
    }

    /**
     * Executes a sql query
     */
    public boolean executeSQL(PreparedStatement statement) throws SQLException{
        try{
            if(!checkConnection()){
                openConnection();
            }

            return statement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(statement != null)
                statement.close();
        }

        return false;
    }

}