package uk.co.scraigie.onscreen.core_android.behavior

import androidx.fragment.app.Fragment
import uk.co.scraigie.onscreen.core.behaviors.IFragmentLifecycleBehavior

abstract class BehaviorFragment: Fragment() {

    private val behaviorsList = mutableListOf<IFragmentLifecycleBehavior>()

    fun addBehavior(behavior: IFragmentLifecycleBehavior) {
        behaviorsList.add(behavior)
    }

    override fun onStart() {
        super.onStart()
        behaviorsList.forEach { it.onStart() }
    }

    override fun onStop() {
        behaviorsList.forEach { it.onStop() }
        super.onStop()
    }
}