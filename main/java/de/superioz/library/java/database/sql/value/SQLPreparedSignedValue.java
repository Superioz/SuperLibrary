package de.superioz.library.java.database.sql.value;

/**
 * Class created on April in 2015
 */
public class SQLPreparedSignedValue extends SQLSignedValue {

    protected int index;

    public SQLPreparedSignedValue(String name, Object value, int index){
        super(name, value);
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    @Override
    public String toString(){
        return "SQLPreparedSignedValue{" +
                "index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof SQLPreparedSignedValue)) return false;
        if(!super.equals(o)) return false;

        SQLPreparedSignedValue that = (SQLPreparedSignedValue) o;

        return getIndex() == that.getIndex();

    }

    @Override
    public int hashCode(){
        int result = super.hashCode();
        result = 31 * result + getIndex();
        return result;
    }
}
