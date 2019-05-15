package basecode.com.domain.usecase.base

import io.reactivex.disposables.Disposable

class UseCaseTask(private var disposable: Disposable) {

    fun cancel() {
        disposable.dispose()
    }
}