package basecode.com.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.ui.features.readbook.ReadBookViewController
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.layout_main.*

class ReadBookActivity : AppCompatActivity() {
    private lateinit var router: Router
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        router = Conductor.attachRouter(this, this.controllerContainer, savedInstanceState)
        if (!router.hasRootController()) {
            val url = intent.getStringExtra("LinkBook").valueOrEmpty()
            val bookName = intent.getStringExtra("BookName").valueOrEmpty()
            val bundle = ReadBookViewController.BundleOption.create(url = url, bookName = bookName)
            router.setRoot(RouterTransaction.with(ReadBookViewController(bundle)))
        }
    }
}