package basecode.com.domain.usecase.base

import basecode.com.domain.exception.EmptyOutputException
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class UseCaseOutput<SuccessOutput, FailOutput> internal constructor(private val useCaseExecution: UseCaseExecution) {
    private var disposables: CompositeDisposable = CompositeDisposable()

    /**
     * Builds an [Observable] which will be used when executing the current [UseCase].
     */
    internal abstract fun buildUseCaseObservable(): Observable<SuccessOutput>

    internal abstract fun createFailOutput(throwable: Throwable): FailOutput

    fun executeAsync(resultListener: ResultListener<SuccessOutput, FailOutput>): UseCaseTask {
        val observer: DefaultObserver<SuccessOutput, FailOutput> = DefaultObserver(this::createFailOutput)

        observer.addRawResultListener(LogResultListener())
        observer.addRawResultListener(CrashlyticsResultListener())
        observer.addResultListener(resultListener)

        val observable = this.buildUseCaseObservable()
                .subscribeOn(useCaseExecution.execution)
                .observeOn(useCaseExecution.postExecution)

        val disposable = observable.subscribeWith(observer)
        addDisposable(disposable)
        return UseCaseTask(disposable)
    }

    inner class UseCaseResult(val isSuccess: Boolean, val successOutput: SuccessOutput?, val failOutput: FailOutput?)

    @Throws(Throwable::class)
    fun execute(): UseCaseResult {
        val outputObservable = DefaultObserver<SuccessOutput, FailOutput>(this::createFailOutput)
        val resultListener = ExecuteResultListener<SuccessOutput, FailOutput>()
        outputObservable.addRawResultListener(LogResultListener())
        outputObservable.addRawResultListener(CrashlyticsResultListener())
        outputObservable.addResultListener(resultListener)
        this.buildUseCaseObservable().subscribeWith(outputObservable)
        if (resultListener.successOutput == null && resultListener.failOutput == null) {
            throw EmptyOutputException()
        }
        return UseCaseResult(isSuccess = resultListener.isSuccess,
                successOutput = resultListener.successOutput,
                failOutput = resultListener.failOutput)
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    fun cancel() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
        disposables = CompositeDisposable()
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}