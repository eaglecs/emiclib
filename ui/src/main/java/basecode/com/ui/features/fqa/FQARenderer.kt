package basecode.com.ui.features.fqa

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class FQARenderer : ViewHolderRenderer<FQAViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_fqa

    override fun getModelClass(): Class<FQAViewHolderModel> = FQAViewHolderModel::class.java

    override fun bindView(model: FQAViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setText(R.id.tvQuestion, model.question)
        viewFinder.setText(R.id.tvAnswer, model.anwser)
    }
}