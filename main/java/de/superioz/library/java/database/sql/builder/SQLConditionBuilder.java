package de.superioz.library.java.database.sql.builder;


import de.superioz.library.java.database.sql.value.SQLUnsignedValue;

/**
 * Class created on April in 2015
 */
public class SQLConditionBuilder {

    protected final String WHERE = "WHERE";
    protected StringBuilder syntax;

    public SQLConditionBuilder(){
        this.syntax = new StringBuilder(WHERE + " ");
    }

    /**
     * Appends a variable as question mark to syntax for prepared statement
     * @return This class
     */
    public SQLConditionBuilder variable(){
        syntax.append("?");
        return this;
    }

    /**
     * Appends a value to check condition like NAME='peter'
     * @param value The NAME in the example above as unsigned value
     * @return This class
     */
    public SQLConditionBuilder value(SQLUnsignedValue value){
        syntax.append(value.getIdentifier());
        return this;
    }

    /**
     * Appends AND to the syntax string
     * @return This class
     */
    public SQLConditionBuilder and(){
        syntax.append(" AND ");
        return this;
    }

    /**
     * Appends OR to the syntax builder
     * @return This class
     */
    public SQLConditionBuilder or(){
        syntax.append(" OR ");
        return this;
    }

    /**
     * Appends an operator for statement
     * @param operator The operator from operator-enum
     * @return This class
     */
    public SQLConditionBuilder operator(Operator operator){
        this.syntax.append(operator.toString());
        return this;
    }

    /**
     * Builds the syntax
     * @return The stringbuilder as normal string
     */
    public String build(){
        return syntax.toString();
    }

    /**
     * Enum for all operators allowed in WHERE statement
     */
    public enum Operator {

        EQUAL("="),
        NOT_EQUAL("<>"),
        GREATER_THAN(">"),
        NOT_GREATER("!>"),
        LESS_THEN("<"),
        NOT_LESS("!<"),
        GREATHER_OR_EQUAL(">="),
        LESS_OR_EQUAL("<="),
        BETWEEN("BETWEEN"),
        LIKE("LIKE"),
        IN("IN");

        private String s;

        Operator(String s){
            this.s = s;
        }

        @Override
        public String toString(){
            return s;
        }
    }

}
