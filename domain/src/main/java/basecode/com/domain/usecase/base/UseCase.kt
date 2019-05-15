/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package basecode.com.domain.usecase.base

import basecode.com.domain.exception.EmptyOutputException
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a [DisposableObserver]
 * that will executeAsync its job in a background thread and will post the result in the UI thread.
 */
abstract class UseCase<in Input, SuccessOutput, FailOutput> internal constructor(private val useCaseExecution: UseCaseExecution) {
    private var disposables: CompositeDisposable = CompositeDisposable()

    /**
     * Builds an [Observable] which will be used when executing the current [UseCase].
     */
    internal abstract fun buildUseCaseObservable(input: Input): Observable<SuccessOutput>

    internal abstract fun createFailOutput(throwable: Throwable): FailOutput

    fun executeAsync(resultListener: ResultListener<SuccessOutput, FailOutput>, input: Input): UseCaseTask {
        val observer: DefaultObserver<SuccessOutput, FailOutput> = DefaultObserver(this::createFailOutput)

        observer.addRawResultListener(LogResultListener())
        observer.addRawResultListener(CrashlyticsResultListener())
        observer.addResultListener(resultListener)

        val observable = this.buildUseCaseObservable(input)
                .subscribeOn(useCaseExecution.execution)
                .observeOn(useCaseExecution.postExecution)

        val disposable = observable.subscribeWith(observer)
        addDisposable(disposable)
        return UseCaseTask(disposable)
    }

    inner class UseCaseResult(val isSuccess: Boolean, val successOutput: SuccessOutput?, val failOutput: FailOutput?)

    @Throws(Throwable::class)
    fun execute(input: Input): UseCaseResult {
        val outputObservable = DefaultObserver<SuccessOutput, FailOutput>(this::createFailOutput)
        val resultListener = ExecuteResultListener<SuccessOutput, FailOutput>()
        outputObservable.addRawResultListener(LogResultListener())
        outputObservable.addRawResultListener(CrashlyticsResultListener())
        outputObservable.addResultListener(resultListener)
        this.buildUseCaseObservable(input).subscribeWith(outputObservable)
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
