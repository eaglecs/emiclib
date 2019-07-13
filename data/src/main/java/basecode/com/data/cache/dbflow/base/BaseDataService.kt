package basecode.com.data.cache.dbflow.base

interface BaseDataService<T> {
    fun insert(data: T)
    fun insert(lstData: List<T>)
    fun update(data: T)
    fun update(lstData: List<T>)
    fun save(data: T)
    fun save(lstData: List<T>)
    fun getAll(): List<T>
    fun deleteAll()
    fun delete(data: T)
    fun delete(lstData: List<T>)
}