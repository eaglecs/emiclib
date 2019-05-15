package basecode.com.domain.eventbus

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

object KBus {

    val disposables = mutableMapOf<Any, CompositeDisposable>()
    val publishSubject = PublishSubject.create<Any>()

    inline fun <reified T : Any> subscribe(subscriber: Any, noinline consumer: (T) -> Unit) {
        val observer = publishSubject.ofType(T::class.java).subscribe(consumer)
        val disposable = disposables[subscriber] ?: CompositeDisposable().apply { disposables[subscriber] = this }
        disposable.add(observer)
    }

    fun unsubscribe(subscriber: Any) {
        disposables[subscriber]?.clear()
        disposables.remove(subscriber)
    }

    fun post(event: Any) = publishSubject.onNext(event)
}