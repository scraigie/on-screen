package uk.co.scraigie.onscreen.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uk.co.scraigie.core.framework.*
import uk.co.scraigie.onscreen.core.framework.*
import java.lang.Exception

interface MoviesHomeView : IView

class MoviesHomeFragment: Fragment(), MoviesHomeView {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home, container, false)
}

class MoviesHomePresenter: BasePresenter<MoviesHomeView, MoviesHomeState, MoviesHomeIntents, MoviesHomeActions, MoviesHomeResult>(){

    override fun <I : MoviesHomeIntents> intentActionResolver(intent: I): MoviesHomeActions =
        when (intent) {
            is MoviesHomeIntents.InitialIntent -> MoviesHomeActions.LoadHomeAction()
            is MoviesHomeIntents.RefreshIntent -> MoviesHomeActions.LoadHomeAction()
            else -> throw Exception()
        }

    override val reducer = MviReducer<MoviesHomeState, MoviesHomeResult> {
            initialState, result -> initialState
    }

    override fun onAttach(view: MoviesHomeView) {

    }

    override fun onDetach() {
    }

}

sealed class MoviesHomeIntents : MviIntent {
    class InitialIntent : MoviesHomeIntents()
    class RefreshIntent : MoviesHomeIntents()
}

sealed class MoviesHomeActions : MviAction {
    class LoadHomeAction: MoviesHomeActions()
}

sealed class MoviesHomeResult: MviResult {
    sealed class LoadHomeResult : MoviesHomeResult() {
        class Success : LoadHomeResult()
        class Failure : LoadHomeResult()
    }
}

data class MoviesHomeState(val property: Boolean = true) : MviState