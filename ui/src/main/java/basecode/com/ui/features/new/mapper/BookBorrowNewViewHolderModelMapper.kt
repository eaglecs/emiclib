package basecode.com.ui.features.new.mapper

import basecode.com.domain.mapper.Mapper
import basecode.com.presentation.features.borrowreturn.model.BookBorrowNewViewModel
import basecode.com.ui.features.new.viewholder.BookBorrowNewViewHolderModel

class BookBorrowNewViewHolderModelMapper :
    Mapper<BookBorrowNewViewModel, BookBorrowNewViewHolderModel>() {
    override fun map(input: BookBorrowNewViewModel): BookBorrowNewViewHolderModel {
        return BookBorrowNewViewHolderModel(
            code = input.code,
            bookName = input.bookName,
            borrowDate = input.borrowDate,
            returnDate = input.returnDate
        )
    }
}