package basecode.com.ui.features.readbook

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.util.DoubleTouchPrevent
import kotlinx.android.synthetic.main.screen_read_book.view.*
import org.koin.standalone.inject

class ReadBookViewController(bundle: Bundle) : ViewController(bundle) {
    private val doubleTouchPrevent by inject<DoubleTouchPrevent>()
    private var url = ""
    private var bookName = ""

    object BundleOption {
        var Bundle.url by BundleExtraString("url")
        var Bundle.bookName by BundleExtraString("bookName")
        fun create(url: String, bookName: String) = Bundle().apply {
            this.url = url
            this.bookName = bookName
        }
    }

    companion object : BundleOptionsCompanion<BundleOption>(BundleOption)

    init {
        bundle.options { options ->
            url = options.url.valueOrEmpty()
            bookName = options.bookName.valueOrEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_read_book, container, false)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
        handleView(view)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        }
        view.tvBookName.text = bookName
        view.webView.settings.domStorageEnabled = true
        view.webView.settings.javaScriptEnabled = true
        view.webView.loadUrl(url)
        view.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                url?.let {
                }
            }

        }
        view.webView.webChromeClient = CustomChromeClient()
    }


    inner class CustomChromeClient : WebChromeClient() {
        override fun onProgressChanged(webView: WebView, progress: Int) {
            showLoading()
            if (progress == 100) {
                hideLoading()
            }
        }
    }

    private fun hideLoading() {
        view?.apply {
            vgLoading.hide()
        }
    }

    private fun showLoading() {
        view?.apply {
            vgLoading.show()
        }
    }

    private fun handleView(view: View) {
        view.ivBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivBack")) {
                activity?.finish()
            }
        }
    }
}