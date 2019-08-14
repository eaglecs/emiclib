package basecode.com.ui.features.notify

import basecode.com.domain.extention.formatTime
import basecode.com.domain.util.DateTimeFormat
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class NotifyRenderer(private val onItemClick: (NotifyViewHolderModel) -> Unit) : ViewHolderRenderer<NotifyViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_notify

    override fun getModelClass(): Class<NotifyViewHolderModel> = NotifyViewHolderModel::class.java

    override fun bindView(model: NotifyViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setText(R.id.tvTitle, model.title)
        viewFinder.setText(R.id.tvMessage, model.message)
        val createDate = model.createDate.formatTime(DateTimeFormat.YY_MM_DD_T_HH_MM_SS, DateTimeFormat.DDMMYYFORMAT)
        viewFinder.setText(R.id.tvCreateDate, createDate)
        if (model.status == 0) {
            viewFinder.setBackgroundResource(R.id.vgNotify, R.color.md_white)
        } else {
            viewFinder.setBackgroundResource(R.id.vgNotify, R.color.md_blue100)
        }

        viewFinder.setOnClickListener(R.id.vgRoot) {
            runWithCheckMultiTouch("vgRoot") {
                onItemClick.invoke(model)
            }
        }
    }
}