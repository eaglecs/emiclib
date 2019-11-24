package basecode.com.ui.features.home.renderer

import basecode.com.domain.model.network.response.NewNewsModel
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.base.slider.AppSliderAdapter
import basecode.com.ui.base.slider.GlideImageLoadingService
import basecode.com.ui.features.home.viewholder.NewNewsViewHolderModel
import basecode.com.ui.util.DoubleTouchPrevent
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder
import org.koin.standalone.inject
import ss.com.bannerslider.Slider

class NewNewsRenderer(private val onActionClickNewNews: (NewNewsModel) -> Unit) : ViewHolderRenderer<NewNewsViewHolderModel>() {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    override fun getLayoutId(): Int = R.layout.item_new_news

    override fun getModelClass(): Class<NewNewsViewHolderModel> = NewNewsViewHolderModel::class.java

    override fun bindView(model: NewNewsViewHolderModel, viewFinder: ViewFinder) {
        Slider.init(GlideImageLoadingService())
        val slider = viewFinder.find<Slider>(R.id.bannerSlider)
        val lstPhoto = mutableListOf<String>()
        val lstNewNews = model.lstNewNews
        lstNewNews.forEach { newNews ->
//            lstPhoto.add("${newNews.picture.split(".PNG")[0]}.PNG")
            lstPhoto.add(newNews.picture)
        }
        slider.setAdapter(AppSliderAdapter(lstData = lstPhoto))
        slider.setOnSlideClickListener { position ->
            if (doubleTouchPrevent.check("Slide$position")) {
                val newNewsModel = lstNewNews[position]
                onActionClickNewNews.invoke(newNewsModel)
            }
        }

    }
}