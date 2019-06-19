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
    override val actionsProcessor = ActionsProcessor {
            listOf(
                it.addProcessor<MoviesHomeActions.LoadHomeAction>(loadHomeProcessor),
                it.addProcessor<MoviesHomeActions.RefreshAction>(refreshProcessor)
            )
        }

    override val initialState: MoviesHomeState
        get() = MoviesHomeState()

    override fun intentActionResolver(intent: MoviesHomeIntents): MoviesHomeActions =
        when (intent) {
            is MoviesHomeIntents.InitialIntent -> MoviesHomeActions.LoadHomeAction()
            is MoviesHomeIntents.RefreshIntent -> MoviesHomeActions.LoadHomeAction()
        }

    override val reducer = MviReducer<MoviesHomeState, MoviesHomeResult> {
            previousState, result -> initialState.copy(property = true)
    }

    private val loadHomeProcessor = Processor {
        it.map { MoviesHomeResult.LoadHomeResult() }
    }

    private val refreshProcessor = Processor {
        it.map { MoviesHomeResult.RefreshResult() }
    }
}

sealed class MoviesHomeIntents : MviIntent {
    class InitialIntent : MoviesHomeIntents()
    class RefreshIntent : MoviesHomeIntents()
}

sealed class MoviesHomeActions : MviAction {
    class LoadHomeAction: MoviesHomeActions()
    class RefreshAction: MoviesHomeActions()
}

sealed class MoviesHomeResult: MviResult {
    class LoadHomeResult : MoviesHomeResult()
    class RefreshResult: MoviesHomeResult()
}



data class MoviesHomeState(val property: Boolean = true) : MviState