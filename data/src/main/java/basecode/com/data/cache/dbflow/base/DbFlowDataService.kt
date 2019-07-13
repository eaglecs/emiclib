package basecode.com.data.cache.dbflow.base

import basecode.com.data.cache.dbflow.AppDatabase
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.Model
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction

abstract class DbFlowDataService<T : Model> : BaseDataService<T> {
    protected abstract fun getClassModel(): Class<T>
    override fun insert(data: T) {
        data.insert()
    }

    override fun insert(lstData: List<T>) {
        val storeModelTransaction: FastStoreModelTransaction<T> = FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(getClassModel()))
                .addAll(lstData)
                .build()
        AppDatabase.executeTransaction(storeModelTransaction)
    }

    override fun update(data: T) {
        data.update()
    }

    override fun update(lstData: List<T>) {
        val storeModelTransaction: FastStoreModelTransaction<T> = FastStoreModelTransaction
                .updateBuilder(FlowManager.getModelAdapter(getClassModel()))
                .addAll(lstData)
                .build()
        AppDatabase.executeTransaction(storeModelTransaction)
    }

    override fun save(data: T) {
        data.save()
    }

    override fun save(lstData: List<T>) {
        val storeModelTransaction: FastStoreModelTransaction<T> = FastStoreModelTransaction
                .saveBuilder(FlowManager.getModelAdapter(getClassModel()))
                .addAll(lstData)
                .build()
        AppDatabase.executeTransaction(storeModelTransaction)
    }

    override fun getAll(): List<T> {
        return SQLite.select().from(getClassModel()).queryList()
    }

    override fun deleteAll() {
        SQLite.delete(getClassModel()).execute()
    }

    override fun delete(data: T) {
        data.delete()
    }

    override fun delete(lstData: List<T>) {
        val storeModelTransaction: FastStoreModelTransaction<T> = FastStoreModelTransaction
                .deleteBuilder(FlowManager.getModelAdapter(getClassModel()))
                .addAll(lstData)
                .build()
        AppDatabase.executeTransaction(storeModelTransaction)
    }


}