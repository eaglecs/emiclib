package basecode.com.ui.features.user.tab

import basecode.com.ui.base.listview.model.ViewHolderModel

class ReserveBookViewHolderModel(
        val title: String,
        val copyNumber: String,
        val createDate: String,
        val expiredDate: String
) : ViewHolderModel