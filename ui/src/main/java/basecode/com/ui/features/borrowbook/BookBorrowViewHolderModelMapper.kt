package basecode.com.ui.features.borrowbook

import basecode.com.domain.mapper.Mapper
import basecode.com.presentation.features.books.BookBorrowViewModel

class BookBorrowViewHolderModelMapper : Mapper<BookBorrowViewModel, BookBorrowViewHolderModel>() {
    override fun map(input: BookBorrowViewModel): BookBorrowViewHolderModel {
        return BookBorrowViewHolderModel(title = input.title,
                copyNumber = input.coppyNumber,
                dueDate = input.dueDate,
                checkOutDate = input.checkOutDate,
                photo = input.photo,
                afterRenewDate = input.afterRenewDate)
    }
}