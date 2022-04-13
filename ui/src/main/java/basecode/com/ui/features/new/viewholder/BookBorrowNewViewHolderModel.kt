package basecode.com.ui.features.new.viewholder

import basecode.com.ui.base.listview.model.ViewHolderModel

class BookBorrowNewViewHolderModel(val code: String,
                                   val bookName: String,
                                   val returnDate: String,
                                   val borrowDate: String) : ViewHolderModel
