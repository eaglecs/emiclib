package basecode.com.presentation.features.new

import basecode.com.domain.features.GetBoothsUseCase
import basecode.com.domain.model.network.response.BoothsResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.presentation.features.new.mapper.BoothViewModelMapper

class SearchBoothPresenter(private val getBoothsUseCase: GetBoothsUseCase) : SearchBoothContract.Presenter() {
    override fun getBooths() {
       view?.let { view ->
           view.showLoading()
           getBoothsUseCase.cancel()
           getBoothsUseCase.executeAsync(object : ResultListener<BoothsResponse, ErrorResponse>{
               override fun success(data: BoothsResponse) {
                   val lstBooth = BoothViewModelMapper().map(data)
                   view.showBooths(lstBooth = lstBooth)
               }

               override fun fail(error: ErrorResponse) {
                   view.showErrorGetBooths()
               }

               override fun done() {
                   view.hideLoading()
               }

           })
       }
    }

    override fun onDetachView() {
        getBoothsUseCase.cancel()
        super.onDetachView()
    }
}