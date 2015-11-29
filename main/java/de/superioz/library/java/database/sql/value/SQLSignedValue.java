package de.superioz.library.java.database.sql.value;

/**
 * Class created on April in 2015
 */
public class SQLSignedValue extends SQLUnsignedValue {

    protected Object value;

    public SQLSignedValue(String name, Object value){
        super(name);
        this.value = value;
    }

    public Object getValue(){
        return value;
    }

    @Override
    public String toString(){
        return "SQLSignedValue{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof SQLSignedValue)) return false;
        if(!super.equals(o)) return false;

        SQLSignedValue that = (SQLSignedValue) o;

        return !(getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null);

    }

    @Override
    public int hashCode(){
        int result = super.hashCode();
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }

}
