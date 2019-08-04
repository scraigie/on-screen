package uk.co.scraigie.onscreen.core.framework.behaviors

interface IFragmentLifecycleBehavior {
    fun onStart()
    fun onStop()

    abstract class Impl : IFragmentLifecycleBehavior {
        override fun onStart() {}
        override fun onStop() {}
    }
}

interface IView