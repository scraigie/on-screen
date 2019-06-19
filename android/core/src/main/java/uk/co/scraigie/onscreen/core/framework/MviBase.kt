package uk.co.scraigie.onscreen.core.framework

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

interface MviIntent

interface MviAction

interface MviResult

interface MviState

class MviReducer<S : MviState, R : MviResult>(val reducer: (previousState: S, result: R) -> S) : BiFunction<S, R, S> {
    override fun apply(previousState: S, result: R): S {
        return reducer(previousState, result)
    }
}

interface IPresenter<in V : IView<I,S>, S: MviState, I: MviIntent, A: MviAction, R: MviResult> {
    val initialState: S
    val reducer: MviReducer<S,R>
    val actionsProcessor: BasePresenter<V,S,I,A,R>.ActionsProcessor

    fun intentActionResolver(intent: I): A
    fun onAttach(view: V)
    fun onDetach()
}

abstract class BasePresenter<in V: IView<I,S>, S: MviState, I: MviIntent, A: MviAction, R: MviResult> : IPresenter<V, S, I, A, R> {
    private var disposables = CompositeDisposable()

    private fun <T> Observable<T>.subscribeUntilDetached(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = {}) {
        disposables.add(subscribe(onSuccess,onError))
    }

    private fun <T> Observable<T>.subscribeUntilDetached(onSuccess: (T) -> Unit) {
        subscribeUntilDetached(onSuccess, {})
    }

    override fun onDetach() {
        disposables.dispose()
    }

    override fun onAttach(view: V) {
        view.intentObservable
            .map(this::intentActionResolver)
            .compose(actionsProcessor)
            .scan(initialState, reducer)
            .subscribeUntilDetached {
                view.render(it)
            }
    }

    inner class ActionsProcessor(val processorsProvider: (Observable<A>) -> List<Observable<R>>) : ObservableTransformer<A,R> {
        override fun apply(upstream: Observable<A>): ObservableSource<R> {
            return upstream.publish { combined ->
                Observable.merge(processorsProvider(combined))
            }
        }
    }

    protected inline fun <reified AR: A> Observable<A>.addProcessor(processor: Processor): Observable<R> {
        return ofType(AR::class.java).compose(processor)
    }

    inner class Processor(val func: (obs: Observable<A>) -> Observable<R>) : ObservableTransformer<A,R> {
        override fun apply(upstream: Observable<A>): ObservableSource<R> {
            return func(upstream)
        }
    }
}

interface IView<I : MviIntent, in S: MviState> {
    val intentObservable: Observable<I>
    fun render(state: S)
}



