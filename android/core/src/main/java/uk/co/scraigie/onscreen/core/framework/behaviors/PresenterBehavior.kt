package uk.co.scraigie.onscreen.core.framework.behaviors

import java.lang.ref.WeakReference

interface IPresenter<T : IView> {
    fun onAttach(view: T)
    fun onDetach()
}

class PresenterBehavior<V: IView> constructor(view: V, private inline val provider: () -> IPresenter<V>) : IFragmentLifecycleBehavior.Impl() {

    private val weakView = WeakReference(view)

    private val presenter by lazy { provider() }

    override fun onStart() {
        weakView.get()?.let { presenter.onAttach(it) }
    }

    override fun onStop() {
        presenter.onDetach()
    }
}