package de.superioz.library.java.database.sql;

/**
 * Class created on April in 2015
 */
public class SQLRawColumn {

    protected String name;
    protected DataType type;
    protected int dataTypeSize;
    protected Constraint[] constraints;

    public SQLRawColumn(String name, DataType type, int dataTypeSize, Constraint... constraints){
        this.name = name;
        this.type = type;
        this.dataTypeSize = dataTypeSize;
        this.constraints = constraints;

    }

    public String getName(){
        return name;
    }

    public DataType getType(){
        return type;
    }

    public Constraint[] getConstraints(){
        return constraints;
    }

    @Override
    public String toString(){
        return getName()
                + " "
                + type.getName()
                + (type.hasSize ? "("+dataTypeSize+")" : "") + " "
                + (constraints.length > 0 ? SQLUtils.toString(constraints) : "");
    }

    /**
     * Helper enum to store all important data types
     */
    public enum DataType {

        BIG_INT("bigint", true),
        INT("int", true),
        CHAR("char", false),
        VARCHAR("varchar", true),
        TEXT("text", false),
        TIMESTAMP("timestamp", false);

        private String name;
        private boolean hasSize;

        DataType(String name, boolean hasSize){
            this.name = name;
            this.hasSize = hasSize;
        }

        public boolean hasSize(){
            return hasSize;
        }

        public String getName(){
            return name.toUpperCase();
        }


    }

    /**
     * Helper enum for constraints
     */
    public enum Constraint {

        NOT_NULL("not null"),
        DEFAULT("default"),
        UNIQUE("unique"),
        PRIMARY_KEY("primary key"),
        FOREIGN_KEY("foreign key"),
        NONE("");

        private String name;

        Constraint(String name){
            this.name = name;
        }

        public String getName(){
            return name.toUpperCase();
        }

        @Override
        public String toString(){
            return name();
        }
    }

}
