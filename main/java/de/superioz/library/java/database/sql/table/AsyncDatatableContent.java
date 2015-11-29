package de.superioz.library.java.database.sql.table;

import de.superioz.library.java.database.sql.builder.SQLConditionBuilder;
import de.superioz.library.java.database.sql.table.sync.DatatableContent;
import de.superioz.library.java.database.sql.value.SQLPreparedSignedValue;
import de.superioz.library.java.database.sql.value.SQLUnsignedValue;

import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Class created on April in 2015
 */
public class AsyncDatatableContent extends DatatableContent {

    public AsyncDatatable asyncDatatable;
    public ExecutorService executorService;

    public AsyncDatatableContent(AsyncDatatable table){
        super(table);
        this.asyncDatatable = table;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public int insert(SQLUnsignedValue[] ident, SQLPreparedSignedValue[] values){
        this.asyncDatatable.executorService.execute(() -> super.insert(ident, values));
        return -1;
    }

    @Override
    public int update(SQLUnsignedValue[] ident, SQLPreparedSignedValue[] values, SQLConditionBuilder cBuilder){
        this.asyncDatatable.executorService.execute(() -> super.update(ident, values, cBuilder));
        return -1;
    }

    @Override
    public int delete(SQLPreparedSignedValue[] values, SQLConditionBuilder cBuilder){
        this.asyncDatatable.executorService.execute(() -> super.delete(values, cBuilder));
        return -1;
    }

    /**
     * Better select method (async)
     */
    public void select(boolean all, SQLConditionBuilder cBuilder,
                       SQLUnsignedValue[] values, Consumer<ResultSet> consumer){
        this.asyncDatatable.executorService.execute(() -> {
            ResultSet result = super.select(all, cBuilder, values);
            executorService.execute(() -> consumer.accept(result));
        });
    }

}
