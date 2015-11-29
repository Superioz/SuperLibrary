package de.superioz.library.java.database.sql.table;


import de.superioz.library.java.database.sql.SQLRawColumn;
import de.superioz.library.java.database.sql.database.Database;
import de.superioz.library.java.database.sql.table.sync.Datatable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class created on April in 2015
 */
public class AsyncDatatable extends Datatable {

    public ExecutorService executorService;

    public AsyncDatatable(Database database, String name){
        super(database, name);
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public boolean create(final boolean ifNotExists, final SQLRawColumn... sqlRawColumns){
        executorService.execute(() -> super.create(ifNotExists, sqlRawColumns));
        return true;
    }

    @Override
    public boolean delete(){
        executorService.execute(super::delete);
        return true;
    }

    public ExecutorService getExecutorService(){
        return executorService;
    }
}
