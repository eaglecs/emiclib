package basecode.com.data.cache.dbflow.mapper

import basecode.com.data.cache.dbflow.model.BookDBModel
import basecode.com.domain.mapper.Mapper
import basecode.com.domain.model.dbflow.EBookModel

class BookDBModelMapper : Mapper<EBookModel, BookDBModel>() {
    override fun map(input: EBookModel): BookDBModel {
        return BookDBModel(id = input.id,
                title = input.title,
                photo = input.photo,
                author = input.author)
    }
}