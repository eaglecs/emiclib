package basecode.com.presentation.features.renew

import basecode.com.domain.features.GetLoanHoldingRenewUseCase
import basecode.com.domain.features.LoanRenewUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.usecase.base.ResultListener

class RenewPresenter(private val getLoanHoldingRenewUseCase: GetLoanHoldingRenewUseCase,
                     private val loanRenewUseCase: LoanRenewUseCase) : RenewContract.Presenter() {
    override fun getListLoanRenew() {
        view?.let { view ->
            view.showLoading()
            getLoanHoldingRenewUseCase.cancel()
            getLoanHoldingRenewUseCase.executeAsync(object : ResultListener<List<NewNewsResponse>, ErrorResponse> {
                override fun success(data: List<NewNewsResponse>) {
                    view.getListLoanRenewSuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                    view.getListLoanRenewFail(error.msgError)
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun renew(coppynumber: String) {
        view?.let { view ->
            view.showLoading()
            loanRenewUseCase.cancel()
            loanRenewUseCase.executeAsync(object : ResultListener<Any, ErrorResponse> {
                override fun success(data: Any) {
                    view.renewSuccess()
//                    if (data == 1) {
//                    } else {
//                        view.renewfail()
//                    }
                }

                override fun fail(error: ErrorResponse) {
                    view.renewfail()
                }

                override fun done() {
                    view.hideLoading()
                }

            }, coppynumber)
        }
    }

    override fun onDetachView() {
        getLoanHoldingRenewUseCase.cancel()
        loanRenewUseCase.cancel()
        super.onDetachView()
    }
}