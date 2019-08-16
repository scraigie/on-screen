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

class MviReducer<S : MviState, R : MviResult>(val reducer: (previousState: S, result: R) -> S) : BiFunction<S, R, S> {
    override fun apply(previousState: S, result: R): S {
        return reducer(previousState, result)
    }
}

class IntentActionResolver<I : MviIntent, A : MviAction>(val resolver: (I) -> A) : ObservableTransformer<I,A> {
    override fun apply(upstream: Observable<I>): ObservableSource<A> =
        upstream.map { resolver(it) }

}

class ActionsProcessor<A: MviAction, R: MviResult>(val processorsProvider: (Observable<A>) -> List<Observable<R>>) : ObservableTransformer<A,R> {
    override fun apply(upstream: Observable<A>): ObservableSource<R> {
        return upstream.publish { combined ->
            Observable.merge(processorsProvider(combined))
        }
    }
}

interface MviPresenter<V : MviView<I, S>, S: MviState, I: MviIntent, A: MviAction, R: MviResult> :
    IPresenter<V> {
    val initialState: S
    val reducer: MviReducer<S, R>
    val actionsProcessor: ActionsProcessor<A, R>
    val intentActionResolver: IntentActionResolver<I, A>
}

abstract class BasePresenter<V: MviView<I, S>, S: MviState, I: MviIntent, A: MviAction, R: MviResult> :
    MviPresenter<V, S, I, A, R> {
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
            .compose(intentActionResolver)
            .compose(actionsProcessor)
            .scan(initialState, reducer)
            .distinctUntilChanged()
            .subscribeUntilDetached {
                view.render(it)
            }
    }

    protected inline fun <reified AR: A> Observable<A>.addProcessor(processor: Processor): Observable<R> {
        return ofType(AR::class.java).compose(processor)
    }

    protected inner class Processor(val func: (obs: Observable<A>) -> Observable<R>) : ObservableTransformer<A,R> {
        override fun apply(upstream: Observable<A>): ObservableSource<R> {
            return func(upstream)
        }
    }
}

interface MviView<I : MviIntent, in S: MviState> :
    IView {
    val intentObservable: Observable<I>
    fun render(state: S)
}



