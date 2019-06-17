package uk.co.scraigie.onscreen.core.framework

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

interface MviIntent

interface MviAction

interface MviResult

interface MviState

class MviReducer<S : MviState, R : MviResult>(val reducer: (init: S, result: R) -> S) : BiFunction<S,R,S> {
    override fun apply(initialState: S, result: R): S {
        return reducer(initialState,result)
    }
}

interface IPresenter<in V : IView, S: MviState, in IB: MviIntent, out A: MviAction, R: MviResult> {
    val reducer: MviReducer<S,R>
    fun <I : IB> intentActionResolver(intent: I): A
    fun onAttach(view: V)
    fun onDetach()
}

abstract class BasePresenter<in V: IView, S: MviState, in I: MviIntent, out A: MviAction, R: MviResult> : IPresenter<V, S, I, A, R> {
    private var disposables = CompositeDisposable()

    fun <T> Observable<T>.subscribeUntilDetached(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = {}) {
        disposables.add(subscribe(onSuccess,onError))
    }

    override fun onDetach() {
        disposables.dispose()
    }
}

interface IView

