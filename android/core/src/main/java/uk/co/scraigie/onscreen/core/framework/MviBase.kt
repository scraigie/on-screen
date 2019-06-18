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

class MviActionsProcessor <A: MviAction, R: MviResult> constructor(val actionProcessors: (combinedActionObservable: Observable<A>) -> List<Observable<R>>) : ObservableTransformer<A,R> {
    override fun apply(actions: Observable<A>): ObservableSource<R> {
        return actions.publish {
            Observable.merge(actionProcessors(it))
        }
    }
}

inline fun <reified A: MviAction, R: MviResult> Observable<in A>.addProcessor(processor: ObservableTransformer<A,R>): Observable<R> {
    return ofType(A::class.java).compose(processor)
}


interface IPresenter<in V : IView<I,S>, S: MviState, I: MviIntent, A: MviAction, R: MviResult> {
    val initialState: S
    val actionProcessor: MviActionsProcessor<A,R>
    val reducer: MviReducer<S,R>
    fun intentActionResolver(intent: I): A
    fun onAttach(view: V)
    fun onDetach()
}

abstract class BasePresenter<in V: IView<I,S>, S: MviState, I: MviIntent, A: MviAction, R: MviResult> : IPresenter<V, S, I, A, R> {
    private var disposables = CompositeDisposable()

    protected fun <T> Observable<T>.subscribeUntilDetached(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = {}) {
        disposables.add(subscribe(onSuccess,onError))
    }

    protected fun <T> Observable<T>.subscribeUntilDetached(onSuccess: (T) -> Unit) {
        subscribeUntilDetached(onSuccess, {})
    }

    override fun onDetach() {
        disposables.dispose()
    }

    override fun onAttach(view: V) {
        view.intentObservable
            .map(this::intentActionResolver)
            .compose(actionProcessor)
            .scan(initialState, reducer)
            .subscribeUntilDetached {
                view.render(it)
            }
    }
}

interface IView<I : MviIntent, in S: MviState> {
    val intentObservable: Observable<I>
    fun render(state: S)
}



