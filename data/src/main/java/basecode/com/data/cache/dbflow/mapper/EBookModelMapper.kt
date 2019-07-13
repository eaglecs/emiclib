package basecode.com.data.cache.dbflow.mapper

import basecode.com.data.cache.dbflow.model.BookDBModel
import basecode.com.domain.mapper.Mapper
import basecode.com.domain.model.dbflow.EBookModel

class EBookModelMapper : Mapper<BookDBModel, EBookModel>() {
    override fun map(input: BookDBModel): EBookModel {
        return EBookModel(id = input.id,
                author = input.author,
                photo = input.photo,
                title = input.title)
    }
}