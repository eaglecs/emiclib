package basecode.com.ui.features.bookdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraObject
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import basecode.com.ui.util.DoubleTouchPrevent
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.models.SlideModel
import kotlinx.android.synthetic.main.screen_show_full_image.view.*
import org.koin.standalone.inject
import java.io.Serializable

class ShowFullImageViewController(bundle: Bundle) : ViewController(bundle) {
    private val doubleTouchPrevent by inject<DoubleTouchPrevent>()
    private var images = mutableListOf<String>()
    private var positionCurrent = 0

    object BundleOptions {
        var Bundle.input by BundleExtraObject<Input>("ShowFullImageViewController.input")
        fun create(
            input: Input
        ) =
            Bundle().apply {
                this.input = input
            }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            options.input?.let {
                images.addAll(it.lstImage)
                positionCurrent = it.positonSelected
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_show_full_image, container, false)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
        handleView(view)
    }

    private fun initView(view: View) {
        if (images.size > 1) {
            view.ivNextPhoto.visible()
        }
        val lstData = mutableListOf<SlideModel>()
        images.forEach { image ->
            lstData.add(SlideModel(imageUrl = image, scaleType = ScaleTypes.CENTER_INSIDE))
        }
        view.imageSlider.setImageList(lstData)
        view.imageSlider.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChanged(position: Int) {
                positionCurrent = position
                when (position) {
                    0 -> {
                        view.ivBackPhoto.gone()
                        if (images.size > 1){
                            view.ivNextPhoto.visible()
                        }
                    }
                    images.size - 1 -> {
                        if (images.size > 1){
                            view.ivBackPhoto.visible()
                        }
                        view.ivNextPhoto.gone()
                    }
                    else -> {
                        view.ivBackPhoto.visible()
                        view.ivNextPhoto.visible()
                    }
                }
            }
        })
        view.imageSlider.setCurrentItem(positionCurrent)

    }

    private fun handleView(view: View) {
        view.ivBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivBack")) {
                router.popController(this)
            }
        }
        view.ivBackPhoto.setOnClickListener {
            if (doubleTouchPrevent.check("ivBackPhoto")) {
                if (positionCurrent > 0) {
                    view.imageSlider.setCurrentItem(positionCurrent - 1)
                }
            }
        }

        view.ivNextPhoto.setOnClickListener {
            if (doubleTouchPrevent.check("ivNextPhoto")) {
                if (positionCurrent < images.size - 1) {
                    view.imageSlider.setCurrentItem(positionCurrent + 1)
                }
            }
        }
    }

    class Input(val lstImage: MutableList<String>, val positonSelected: Int) : Serializable
}