package de.superioz.library.java.database.sql.builder;

import de.superioz.library.java.database.sql.SQLRawColumn;
import de.superioz.library.java.database.sql.SQLUtils;
import de.superioz.library.java.database.sql.value.SQLUnsignedValue;

/**
 * Class created on April in 2015
 */
public class SQLStatementBuilder {

    protected String tablename;
    protected String query;

    public SQLStatementBuilder(String tablename){
        this.tablename = tablename;
    }

    public String buildDelete(SQLConditionBuilder condition){
        String syntax = Statement.DELETE.getPattern()
                .replace("%t", tablename);

        // Where cond
        if(condition != null){
            syntax += " " + condition.build();
        }

        return syntax;
    }

    public String buildUpdate(SQLUnsignedValue[] v, SQLConditionBuilder condition){
        String syntax = Statement.UPDATE.getPattern()
                .replace("%t", tablename)
                .replace("%cv", SQLUtils.toUpdateString(v));

        // Where cond
        if(condition != null){
            syntax += " " + condition.build();
        }

        return syntax;
    }

    public String buildInsertIntoSecond(SQLUnsignedValue[] values){
        String[] sv = SQLUtils.toInsertString(values);
        return Statement.INSERT_INTO_SECOND.getPattern()
                .replace("%t", tablename)
                .replace("%v", sv[1]);
    }

    public String buildInsertIntoFirst(SQLUnsignedValue[] values){
        String[] sv = SQLUtils.toInsertString(values);
        return Statement.INSERT_INTO_FIRST.getPattern()
                .replace("%t", tablename)
                .replace("%c", sv[0])
                .replace("%v", sv[1]);
    }

    /**
     * Builds a select statement for sql
     *
     * @param all       Select all?
     * @param columns   The to-select-columns
     * @param condition The where condition
     *
     * @return The builded string
     */
    public String buildSelect(boolean all, SQLUnsignedValue[] columns, SQLConditionBuilder condition){
        String syntax = Statement.SELECT.getPattern()
                .replace("%t", tablename)
                .replace("%c", all ? "*" : SQLUtils.toString(columns));

        // Where cond
        if(condition != null){
            syntax += " " + condition.build();
        }

        return syntax;
    }

    /**
     * Builds the create table statement to create a table
     *
     * @param ifNotExists   If not exists condition
     * @param sqlRawColumns The columns in the table
     *
     * @return The query syntax
     */
    public String buildCreateTable(boolean ifNotExists, SQLRawColumn... sqlRawColumns){
        this.query = Statement.CREATE_TABLE.getPattern()
                .replace("%f", ifNotExists ? "IF NOT EXISTS " : "")
                .replace("%t", tablename + " ");

        if(sqlRawColumns.length != 0){
            String s = SQLUtils.toString(sqlRawColumns);
            this.query += "(" + s + ")";
        }

        return this.query;
    }

    /**
     * Builds the DROP TABLE statement to delete a table
     *
     * @return The sql syntax query
     */
    public String buildDropTable(){
        this.query = Statement.DROP_TABLE.getPattern().replace("%t", this.tablename);
        return this.query;
    }

    /**
     * Enum for all important sql actions
     */
    public enum Statement {

        DROP_TABLE("DROP TABLE %t"),
        CREATE_TABLE("CREATE TABLE %f%t"),
        SELECT("SELECT %c FROM %t"),
        INSERT_INTO_FIRST("INSERT INTO %t (%c) VALUES (%v)"),
        INSERT_INTO_SECOND("INSERT INTO %t (%v)"),
        UPDATE("UPDATE %t SET %cv"),
        DELETE("DELETE FROM %t");

        private String pattern;

        Statement(String pattern){
            this.pattern = pattern;
        }

        public String getPattern(){
            return pattern;
        }
    }

}
