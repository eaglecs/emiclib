package basecode.com.data.cache.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.io.File;

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    static final String NAME = "AppDatabase_v1.0.0";
    static final int VERSION = 2;

    private static DatabaseDefinition getDatabase() {
        return FlowManager.getDatabase(AppDatabase.class);
    }

    public static void executeTransaction(ITransaction iTransaction) {
        DatabaseDefinition databaseDefinition = getDatabase();
        databaseDefinition.executeTransaction(iTransaction);
    }

    public static File getDatabaseFile() {
        return FlowManager.getContext().getDatabasePath(AppDatabase.NAME);
    }
}
