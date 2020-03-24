package basecode.com.ui.features.about

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.BuildConfig
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.util.DoubleTouchPrevent
import kotlinx.android.synthetic.main.layout_about.view.*
import org.koin.standalone.inject

class AboutViewController : ViewController(null) {
    private val textAbout = "<h2> DGSoft Technologies JSC © Emiclib 2017. All Rights Reserved</h2>\n" +
            "        <br/>\n" +
            "        <p>Tầng M Tòa nhà VTC, Số 132 Cộng Hòa - Phường 4 - Quận Tân Bình - Tp. Hồ Chí Minh.</p>\n" +
            "        <p>Văn phòng: 138/7 Duy Tân, Phường 15, Quận Phú Nhuận, TP Hồ Chí Minh.</p>\n" +
            "        <p>(84-8 3 847 5821)</p>\n" +
            "        <p>info@dgsoft.vn</p>"

    private val textAboutTND = "<h2>Thư viện số Trần Đại Nghĩa </h2>\n" +
            "        <br/>\n" +
            "        <p>53 Nguyễn Du, P. Bến Nghé, Quận 1, TP. HCM</p>\n" +
            "        <p>20 Lý Tự Trọng, P. Bến Nghé, Q. 1, TP. HCM</p>\n" +
            "        <p>(028 38 229 040) (08 38 258 368)</p>\n" +
            "        <p>Website: www.trandainghia.edu.vn</p>" +
            "        <p>Email:  info@trandainghia.edu.vn</p>" +
            "        <br/>\n" +
            "Powered by Greenhouse. All rights Reserved."

    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_about, container, false)
    }

    override fun initPostCreateView(view: View) {
        val textAbountResult = if(BuildConfig.USE_DATA_TDN){
            textAboutTND
        } else {
            textAbout
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.tvAbout.text = Html.fromHtml(textAbountResult, Html.FROM_HTML_MODE_COMPACT)
        } else {
            view.tvAbout.text = Html.fromHtml(textAbountResult)
        }
        view.ivBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivBack")) {
                router.popController(this)
            }
        }
    }
}