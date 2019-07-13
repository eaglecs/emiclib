package basecode.com.data.cache.dbflow

import basecode.com.data.cache.dbflow.base.DbFlowDataService
import basecode.com.data.cache.dbflow.mapper.BookDBModelMapper
import basecode.com.data.cache.dbflow.mapper.EBookModelMapper
import basecode.com.data.cache.dbflow.model.BookDBModel
import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.domain.repository.dbflow.BookDataService

class BookDataServiceImpl : DbFlowDataService<BookDBModel>(), BookDataService {
    override fun getClassModel(): Class<BookDBModel> = BookDBModel::class.java
    override fun saveDB(data: EBookModel) {
        save(BookDBModelMapper().map(data))
    }

    override fun getAllDB(): List<EBookModel> {
        return EBookModelMapper().mapList(getAll())
    }
}