package uk.co.scraigie.onscreen.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import uk.co.scraigie.onscreen.core.framework.*

interface MoviesHomeView : IView<MoviesHomeIntents, MoviesHomeState>

class MoviesHomeFragment: Fragment(), MoviesHomeView {

    override val intentObservable: Observable<MoviesHomeIntents>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun render(state: MoviesHomeState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home, container, false)
}

class MoviesHomePresenter: BasePresenter<MoviesHomeView, MoviesHomeState, MoviesHomeIntents, MoviesHomeActions, MoviesHomeResult>() {

    override val intentActionResolver = IntentActionResolver<MoviesHomeIntents, MoviesHomeActions> {
        when(it)  {
            is MoviesHomeIntents.InitialIntent -> MoviesHomeActions.LoadHomeAction
            is MoviesHomeIntents.RefreshIntent -> MoviesHomeActions.LoadHomeAction
        }
    }

    override val actionsProcessor = ActionsProcessor<MoviesHomeActions, MoviesHomeResult> {
            listOf(
                it.addProcessor<MoviesHomeActions.LoadHomeAction>(loadHomeProcessor),
                it.addProcessor<MoviesHomeActions.RefreshAction>(refreshProcessor)
            )
        }

    override val initialState: MoviesHomeState
        get() = MoviesHomeState()

    override val reducer = MviReducer<MoviesHomeState, MoviesHomeResult> {
            previousState, result -> initialState.copy(property = true)
    }

    private val loadHomeProcessor = Processor {
        it.map { MoviesHomeResult.LoadHomeResult }
    }

    private val refreshProcessor = Processor {
        it.map { MoviesHomeResult.RefreshResult }
    }
}

sealed class MoviesHomeIntents : MviIntent {
    object InitialIntent : MoviesHomeIntents()
    object RefreshIntent : MoviesHomeIntents()
}

sealed class MoviesHomeActions : MviAction {
    object LoadHomeAction: MoviesHomeActions()
    object RefreshAction: MoviesHomeActions()
}

sealed class MoviesHomeResult: MviResult {
    object LoadHomeResult : MoviesHomeResult()
    object RefreshResult: MoviesHomeResult()
}



data class MoviesHomeState(val property: Boolean = true) : MviState