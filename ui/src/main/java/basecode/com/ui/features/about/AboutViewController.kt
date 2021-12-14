package basecode.com.ui.features.about

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
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
    private val textAbout = "<p>Tên gọi chính thức: <h3>THƯ VIỆN KHOA HỌC TỔNG HỢP THÀNH PHỐ HỒ CHÍ MINH(TVKHTH TPHCM)</h3></p>\n" +
            "        <p>Tên giao dịch quốc tế: <h3>GENERAL SCIENCES LIBRARY OF HOCHIMINH CITY(GSLHCMC)</h3></p>\n" +
            "        <br/>\n" +
            "        <p>Địa chỉ: 69 Lý Tự Trọng, Phường Bến Thành, Quận 1, Thành phố Hồ Chí Minh</p>\n" +
            "        <p>Điện thoại: <a href=\"tel:842838225055\">(84) 028 -  38 225 055</a></p>\n" +
            "        <p>Fax: (84) 028 -  38 299 318</p>\n" +
            "        <p>Website: <a href=\"http://www.thuvientphcm.gov.vn\">http://www.thuvientphcm.gov.vn</a> và <a href=\"http://www.gslhcm.org.vn\">http://www.gslhcm.org.vn</a></p>\n" +
            "        <p>Email: <a href=\"mailto:thuvientphcm@thuvientphcm.gov.vn\">thuvientphcm@thuvientphcm.gov.vn</a></p>"

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
        view.tvAbout.movementMethod = LinkMovementMethod.getInstance()
    }
}