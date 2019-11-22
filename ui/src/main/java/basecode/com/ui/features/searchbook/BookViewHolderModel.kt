package basecode.com.ui.features.searchbook

import basecode.com.ui.base.listview.model.ViewHolderModel

class BookViewHolderModel(
        val id: Long,
        val copyNumber: String = "",
        val name: String,
        var publisher: String,
        val photo: String,
        val author: String,
        var publishedYear: String
): ViewHolderModel