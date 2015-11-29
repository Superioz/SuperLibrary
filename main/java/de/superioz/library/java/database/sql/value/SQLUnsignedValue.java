package de.superioz.library.java.database.sql.value;

/**
 * Class created on April in 2015
 */
public class SQLUnsignedValue {

    protected String name;

    public SQLUnsignedValue(String name){
        this.name = name;
    }

    public String getIdentifier(){
        return name;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof SQLUnsignedValue)) return false;

        SQLUnsignedValue that = (SQLUnsignedValue) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode(){
        return name != null ? name.hashCode() : 0;
    }

}
