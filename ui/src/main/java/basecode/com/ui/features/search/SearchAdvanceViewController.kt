package basecode.com.ui.features.search

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.extension.view.hideKeyboard
import basecode.com.ui.extension.view.showKeyboard
import basecode.com.ui.util.DoubleTouchPrevent
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_search_advance.view.*
import org.koin.standalone.inject

class SearchAdvanceViewController<T>() : ViewController(bundle = null)
        where T : ViewController, T : SearchAdvanceViewController.Action {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()

    constructor(targetController: T) : this() {
        setTargetController(targetController)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_search_advance, container, false)
    }

    override fun initPostCreateView(view: View) {
        view.edtTitle.requestFocus()
        view.context.showKeyboard()
        handleOnClick(view)
    }

    private fun handleOnClick(view: View) {
        view.btnSearch.setOnClickListener {
            if (doubleTouchPrevent.check("btnSearch")) {
                searchAdvanceBook(view)
            }
        }
        view.vgClose.setOnClickListener {
            if (doubleTouchPrevent.check("vgClose")) {
                router.popController(this)
            }
        }
        view.edtAuthor.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchAdvanceBook(view)
                true
            }
            false
        }
    }

    private fun searchAdvanceBook(view: View) {
        val title = view.edtTitle.text.toString()
        val author = view.edtAuthor.text.toString()
        val keyword = view.edtKeyword.text.toString()
        val language = view.edtLanguage.text.toString()
        if (title.isEmpty() && author.isEmpty()) {
            Toasty.warning(view.context, view.context.getString(R.string.text_title_author_empty)).show()
        } else {
            targetController?.let {
                if (it is Action) {
                    it.searchAdvanceBook(title, author, keyword, language)
                }
            }
            router.popController(this)
        }
    }

    override fun handleBack(): Boolean {
        router.popController(this)
        return true
    }

    override fun onDestroyView(view: View) {
        view.context.hideKeyboard()
        super.onDestroyView(view)
    }

    interface Action {
        fun searchAdvanceBook(title: String, author: String, keyword: String, language: String)
    }
}