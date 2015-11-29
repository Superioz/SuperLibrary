package de.superioz.library.java.database.sql.table.sync;


import de.superioz.library.java.database.sql.SQLUtils;
import de.superioz.library.java.database.sql.builder.SQLConditionBuilder;
import de.superioz.library.java.database.sql.builder.SQLStatementBuilder;
import de.superioz.library.java.database.sql.database.Database;
import de.superioz.library.java.database.sql.value.SQLPreparedSignedValue;
import de.superioz.library.java.database.sql.value.SQLUnsignedValue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class created on April in 2015
 */
public class DatatableContent {

    protected Datatable table;
    protected String tableName;
    protected Database database;
    protected SQLStatementBuilder builder;

    public DatatableContent(Datatable table){
        this.table = table;
        this.tableName = table.name;
        this.database = table.database;
        this.builder = table.builder;
    }

    /**
     * nsert a new row to the table
     * @param ident The identifier like name or language
     * @param values Signed values with index
     * @return The update sql result
     */
    public int insert(SQLUnsignedValue[] ident, SQLPreparedSignedValue[] values){
        String syntax = this.builder.buildInsertIntoFirst(ident);
        try{
            PreparedStatement statement = SQLUtils.getPreparedStatement(this.database, syntax, values);
            return this.database.updateSQL(statement);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Selects rows from table
     * @param all All rows?
     * @param cBuilder The where condition
     * @param values all or specific ROWS
     * @return The result as a resultset
     */
    public ResultSet select(boolean all, SQLConditionBuilder cBuilder, SQLUnsignedValue[] values){
        String syntax = this.builder.buildSelect(all, values, cBuilder);
        try{
            PreparedStatement statement = SQLUtils.getPreparedStatement(this.database, syntax);
            return this.database.querySQL(statement);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates this database table
     * @param ident The identifier for rows
     * @param values The values for the rows
     * @param cBuilder The where condition
     * @return The sql result as an int
     */
    public int update(SQLUnsignedValue[] ident, SQLPreparedSignedValue[] values, SQLConditionBuilder cBuilder){
        String syntax = this.builder.buildUpdate(ident, cBuilder);
        try{
            PreparedStatement statement = SQLUtils.getPreparedStatement(this.database, syntax, values);
            return this.database.updateSQL(statement);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Deletes content from table
     * @param values The values which will be replaces in where stmnt
     * @param cBuilder The where condition
     * @return The result of sql update
     */
    public int delete(SQLPreparedSignedValue[] values, SQLConditionBuilder cBuilder){
        String syntax = this.builder.buildDelete(cBuilder);
        try{
            PreparedStatement statement = SQLUtils.getPreparedStatement(this.database, syntax, values);
            return this.database.updateSQL(statement);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }


}
