package de.superioz.library.java.database.sql;

import de.superioz.library.java.database.sql.database.Database;
import de.superioz.library.java.database.sql.value.SQLPreparedSignedValue;
import de.superioz.library.java.database.sql.value.SQLUnsignedValue;
import de.superioz.library.java.util.list.ListUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Class created on April in 2015
 */
public class SQLUtils {

    /**
     * Gets the unsignes values with commata between
     *
     * @param values The values which will be put into
     * @return The value syntax for queries
     */
    public static String toString(SQLUnsignedValue[] values){
        return Arrays.toString(values)
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");
    }

    /**
     * Gets the sql raw column as a string
     *
     * @param columns The columns
     * @return The string
     */
    public static String toString(SQLRawColumn[] columns){
        String[] strings = new String[columns.length];

        for(int i = 0; i < columns.length; i++){
            strings[i] = columns[i].toString();
        }

        return ListUtil.insert(strings, ", ");
    }

    /**
     * Gets the string of all constraints
     *
     * @param constraints Array of constaints to convert
     * @return The string
     */
    public static String toString(SQLRawColumn.Constraint[] constraints){
        String[] arr = new String[constraints.length];
        for(int i = 0; i < constraints.length; i++){
            arr[i] = constraints[i].getName();
        }
        return ListUtil.insert(arr, ", ").replace(",", "");
    }

    /**
     * Gets the sql syntax for insert first
     *
     * @param sValues The signed values
     * @return The string
     */
    public static String[] toInsertString(SQLUnsignedValue[] sValues){
        String[] columns = new String[sValues.length];
        String[] values = new String[sValues.length];

        for(int i = 0; i < sValues.length; i++){
            columns[i] = sValues[i].getIdentifier();
            values[i] = "?";
        }

        return new String[]{
                ListUtil.insert(columns, ", "),
                ListUtil.insert(values, ", ")
        };
    }

    /**
     * Gets update string from unsigned values
     *
     * @param sValues The values
     * @return The syntax string
     */
    public static String toUpdateString(SQLUnsignedValue[] sValues){
        String[] columns = new String[sValues.length];
        String[] values = new String[sValues.length];
        String[] end = new String[sValues.length];

        for(int i = 0; i < sValues.length; i++){
            columns[i] = sValues[i].getIdentifier();
            values[i] = "?";
        }

        for(int i = 0; i < sValues.length; i++){
            end[i] = columns[i] + "=" + values[i];
        }

        return ListUtil.insert(end, ", ");
    }

    /**
     * Returns the prepared statement with values
     * @param syntax The query syntax
     * @return The prepared statement
     */
    public static PreparedStatement getPreparedStatement(Database db, String syntax, SQLPreparedSignedValue... values) throws SQLException{
        PreparedStatement statement = null;

        if(db.checkConnection()){
            statement = db.getConnection().prepareStatement(syntax);

            for(SQLPreparedSignedValue v : values){
                statement.setObject(v.getIndex(), v.getValue());
            }
        }

        return statement;
    }


}
