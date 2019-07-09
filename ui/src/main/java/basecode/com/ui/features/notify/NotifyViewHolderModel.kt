package basecode.com.ui.features.notify

import basecode.com.ui.base.listview.model.ViewHolderModel

class NotifyViewHolderModel(var id: Long,
                            var title: String,
                            var message: String,
                            var status: Int,
                            var createDate: String) : ViewHolderModel