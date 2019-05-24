package basecode.com.ui.features.searchbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraInt
import basecode.com.ui.base.extra.BundleOptionsCompanion

class TabBookCategoryViewController(bundle: Bundle) : ViewController(bundle) {
    private var categoryId = 0

    object BundleOptions {
        var Bundle.categoryId by BundleExtraInt("TabBookCategoryViewController.categoryId")
        fun create(categoryId: Int) = Bundle().apply {
            this.categoryId = categoryId
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            categoryId = options.categoryId.valueOrZero()
        }
    }

    override fun initPostCreateView(view: View) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_category_book, container, false)
    }
}