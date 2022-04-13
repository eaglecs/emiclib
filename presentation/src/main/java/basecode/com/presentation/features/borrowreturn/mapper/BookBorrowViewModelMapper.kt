package basecode.com.presentation.features.borrowreturn.mapper

import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.mapper.Mapper
import basecode.com.domain.model.network.response.GetPatronOnLoanCopiesResponse
import basecode.com.presentation.features.borrowreturn.model.BookBorrowNewViewModel

class BookBorrowViewModelMapper:
    Mapper<GetPatronOnLoanCopiesResponse, List<BookBorrowNewViewModel>>() {
    override fun map(input: GetPatronOnLoanCopiesResponse): List<BookBorrowNewViewModel> {
        val lstBook = mutableListOf<BookBorrowNewViewModel>()
        input.forEach { book ->
            lstBook.add(BookBorrowNewViewModel(code = book.copyNumber.valueOrEmpty(),
            bookName = book.title.valueOrEmpty(), returnDate = book.checkInDate.valueOrEmpty(), borrowDate = book.checkOutDate.valueOrEmpty()))
        }
        return lstBook
    }
}