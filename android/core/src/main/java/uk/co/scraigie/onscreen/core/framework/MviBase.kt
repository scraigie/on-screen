package uk.co.scraigie.onscreen.core.framework

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import uk.co.scraigie.onscreen.core.behaviors.IPresenter
import uk.co.scraigie.onscreen.core.behaviors.IView

interface MviIntent

interface MviAction

interface MviResult

interface MviState

interface MviPresenter<V : MviView<I,S>, S: MviState, I: MviIntent, A: MviAction, R: MviResult>: IPresenter<V> {
    val initialState: S
    val intentActionResolver: Map<I,A>
}

abstract class BaseMviPresenter<V: MviView<I,S>, S: MviState, I: MviIntent, A: MviAction, R: MviResult> : MviPresenter<V, S, I, A, R> {

    abstract val reducer: MviReducer
    abstract val actionsProcessor: ActionsProcessor

    private var disposables = CompositeDisposable()

    private fun <T> Observable<T>.subscribeUntilDetached(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        subscribe(onSuccess,onError).clearOnDetached()
    }

    private fun <T> Observable<T>.subscribeUntilDetached(onSuccess: (T) -> Unit) {
        subscribeUntilDetached(onSuccess, {})
    }

    private fun Disposable.clearOnDetached() {
        disposables.add(this)
    }

    override fun onDetach() {
        disposables.clear()
    }

    override fun onAttach(view: V) {
        view.intentObservable
//                .doOnNext { println("mviPresenter - intent = $it") }
            .map{ intentActionResolver[it] ?: error("Unable to resolve intent to action $it") }
//                .doOnNext { println("mviPresenter - action = $it") }
            .compose(actionsProcessor)
//                .doOnNext { println("mviPresenter - result = $it") }
            .scan(initialState, reducer)
//                .doOnNext { println("mviPresenter - state = $it") }
            .distinctUntilChanged()
            .subscribeUntilDetached {
                view.render(it)
            }
    }

    inner class MviReducer(val reducer: (previousState: S, result: R) -> S) : BiFunction<S, R, S> {
        override fun apply(previousState: S, result: R): S {
            return reducer(previousState, result)
        }
    }

    inner class ActionsProcessor
        (val processorsProvider: (Observable<A>) -> List<Observable<R>>) : ObservableTransformer<A, R> {
        override fun apply(upstream: Observable<A>): ObservableSource<R> {
            return upstream.publish { combined ->
                Observable.merge(processorsProvider(combined))
            }
        }
    }

    protected inline fun <reified AC: A,RC: R> Observable<A>.addProcessor(processor: Processor<AC,RC>): Observable<R> {
        return ofType(AC::class.java).compose(processor)
    }

    abstract inner class Processor<AC,RC> : ObservableTransformer<AC, RC>

    inner class RxProcessor<AC: A,RC: R>(val func: (AC) -> Observable<RC>) : Processor<AC,RC>() {
        override fun apply(upstream: Observable<AC>): ObservableSource<RC> {
            return upstream.switchMap { func(it) }
        }
    }

    inner class SequentialProcessor<AC: A, RC: R>(val func: (AC) -> RC) : Processor<AC,RC>() {
        override fun apply(upstream: Observable<AC>): ObservableSource<RC> {
            return upstream.switchMap { Observable.just(func(it)) }
        }
    }
}

interface MviView<I : MviIntent, in S: MviState>: IView {
    val intentObservable: Observable<I>
    fun render(state: S)
}
