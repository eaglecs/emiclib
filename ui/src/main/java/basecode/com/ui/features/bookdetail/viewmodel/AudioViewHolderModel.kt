package basecode.com.ui.features.bookdetail.viewmodel

import basecode.com.ui.base.listview.model.ViewHolderModel

class AudioViewHolderModel(
    val id: Int,
    val title: String,
    val url: String,
    var isSelected: Boolean
) : ViewHolderModel