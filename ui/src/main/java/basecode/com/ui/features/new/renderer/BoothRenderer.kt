package basecode.com.ui.features.new.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.new.viewholder.BoothViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BoothRenderer(private val onActionChoose: (model: BoothViewHolderModel) -> Unit) :
    ViewHolderRenderer<BoothViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_booth

    override fun getModelClass(): Class<BoothViewHolderModel> = BoothViewHolderModel::class.java

    override fun bindView(model: BoothViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setText(R.id.tvBoothName, model.symbol)
        viewFinder.setText(R.id.tvAddress, model.codeLoc)
        viewFinder.setText(R.id.tvDistance, "Khoảng cách ${model.distance}")
        viewFinder.setOnClickListener {
            runWithCheckMultiTouch("root") {
                onActionChoose.invoke(model)
            }
        }
    }
}