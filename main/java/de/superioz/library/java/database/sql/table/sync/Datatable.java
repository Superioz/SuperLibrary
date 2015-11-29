package de.superioz.library.java.database.sql.table.sync;


import de.superioz.library.java.database.sql.SQLRawColumn;
import de.superioz.library.java.database.sql.SQLUtils;
import de.superioz.library.java.database.sql.builder.SQLStatementBuilder;
import de.superioz.library.java.database.sql.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class created on April in 2015
 */
public class Datatable {

    protected Database database;
    protected Connection con;
    protected String name;
    protected SQLStatementBuilder builder;
    protected DatatableContent content;

    public Datatable(Database database, String name){
        this.database = database;
        this.con = database.getConnection();
        this.name = name;
        this.builder = new SQLStatementBuilder(name);
        this.content = new DatatableContent(this);
    }

    /**
     * Creates this table
     * @param ifNotExists Simple.
     * @param sqlRawColumns The columns for identifier
     * @return result from execute sql
     */
    public boolean create(boolean ifNotExists, SQLRawColumn... sqlRawColumns){
        try{
            PreparedStatement statement =
                    SQLUtils.getPreparedStatement(this.database,
                            this.builder.buildCreateTable(ifNotExists, sqlRawColumns));
            return this.database.executeSQL(statement);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes the table
     * @return The result from execute
     */
    public boolean delete(){
        try{
            PreparedStatement statement = SQLUtils.getPreparedStatement(this.database,
                    this.builder.buildDropTable());
            return this.database.executeSQL(statement);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the content from the table
     * @return The raw table content to edit content
     */
    public DatatableContent content(){
        return this.content;
    }

}
