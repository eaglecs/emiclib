package basecode.com.domain.repository.dbflow

import basecode.com.domain.model.dbflow.EBookModel

interface BookDataService {
    fun saveDB(data: EBookModel)
    fun getAllDB(): List<EBookModel>
}