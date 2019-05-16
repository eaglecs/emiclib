package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.presentation.features.home.HomeContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import kotlinx.android.synthetic.main.layout_tab_home.view.*
import org.koin.standalone.inject

class HomeViewController() : ViewController(bundle = null), HomeContract.View {

    private val presenter: HomeContract.Presenter by inject()

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun initPostCreateView(view: View) {
        loadData()
    }

    private fun loadData() {
        presenter.attachView(this)
        presenter.getListNewBook(NewEBookRequest(numberItem = 10, pageIndex = 1, pageSize = 10))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_home, container, false)
    }

    override fun getListNewEBookSuccess() {
    }

    override fun showErrorGetListNewEbook() {
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgLoading.show()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.vgLoading.show()
        }
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
        super.onDestroyView(view)
    }
}