package basecode.com.ui.features.borrowbook

import basecode.com.domain.mapper.Mapper
import basecode.com.presentation.features.books.BookBorrowViewModel

class BookBorrowViewHolderModelMapper : Mapper<BookBorrowViewHolderModelMapper.Input, BookBorrowViewHolderModel>() {
    override fun map(input: Input): BookBorrowViewHolderModel {
        val bookBorrowViewModel = input.bookBorrowViewModel
        return BookBorrowViewHolderModel(title = bookBorrowViewModel.title,
                copyNumber = bookBorrowViewModel.coppyNumber,
                dueDate = bookBorrowViewModel.dueDate,
                checkOutDate = bookBorrowViewModel.checkOutDate,
                photo = bookBorrowViewModel.photo,
                afterRenewDate = bookBorrowViewModel.afterRenewDate,
                isBorrowing = input.isBorrowing,
                timeDueDate = bookBorrowViewModel.timeDueDate
                )
    }

    class Input(val isBorrowing: Boolean, val bookBorrowViewModel: BookBorrowViewModel)
}