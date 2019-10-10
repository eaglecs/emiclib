package basecode.com.ui.features.borrowbook

import basecode.com.ui.base.listview.model.ViewHolderModel

class BookBorrowViewHolderModel(val title: String, val copyNumber: String, val checkOutDate: String, val dueDate: String, var photo: String, val afterRenewDate: String): ViewHolderModel