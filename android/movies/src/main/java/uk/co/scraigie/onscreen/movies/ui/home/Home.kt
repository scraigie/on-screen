package uk.co.scraigie.onscreen.movies.ui.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_item_cropped.view.cast
import kotlinx.android.synthetic.main.home_item_cropped.view.content_image
import kotlinx.android.synthetic.main.home_item_cropped.view.director
import kotlinx.android.synthetic.main.home_item_cropped.view.genres_chips
import kotlinx.android.synthetic.main.home_item_cropped.view.rating
import kotlinx.android.synthetic.main.home_item_cropped.view.title
import kotlinx.android.synthetic.main.home_item_hero.view.*
import kotlinx.android.synthetic.main.view_carousel.view.*
import org.koin.android.ext.android.inject
import uk.co.scraigie.onscreen.core.behaviors.PresenterBehavior
import uk.co.scraigie.onscreen.core.framework.*
import uk.co.scraigie.onscreen.core_android.behavior.BehaviorFragment
import uk.co.scraigie.onscreen.core_android.lists.BaseAdapter
import uk.co.scraigie.onscreen.core_android.lists.BaseViewHolder
import uk.co.scraigie.onscreen.core_android.lists.ListItemContent
import uk.co.scraigie.onscreen.movies.R
import uk.co.scraigie.onscreen.movies.data.dtos.MovieDto
import uk.co.scraigie.onscreen.movies.domain.MoviesInteractor
import uk.co.scraigie.onscreen.movies.ui.home.MoviesAdapterItem.Companion.CAROUSEL
import uk.co.scraigie.onscreen.movies.ui.home.MoviesAdapterItem.Companion.HERO
import uk.co.scraigie.onscreen.movies.ui.home.MoviesAdapterItem.Companion.SINGLE
import uk.co.scraigie.onscreen.movies.ui.home.MoviesViewHolder.*
import uk.co.scraigie.onscreen.movies.ui.load

interface MoviesHomeView : MviView<MoviesHomeIntents, MoviesHomeState>

class MoviesHomeFragment: BehaviorFragment(), MoviesHomeView {

    private val presenter: MoviesHomePresenter by inject()
    private val moviesHomeAdapter = MoviesHomeAdapter()
    private val moviesHomeLayoutManager by lazy { LinearLayoutManager(activity, RecyclerView.VERTICAL, false) }
    private val intentSubject = PublishSubject.create<MoviesHomeIntents>()

    init {
        addBehavior(PresenterBehavior(this) { presenter })
    }

    override val intentObservable: Observable<MoviesHomeIntents> by lazy {
        intentSubject.hide()
    }

    override fun render(state: MoviesHomeState) {
        loader?.apply {
            if (state.loading) show() else hide()
        }
        moviesHomeAdapter.apply {
            items = state.moviesList
            notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movies_home_rv.apply {
            this.adapter = moviesHomeAdapter
            this.layoutManager = moviesHomeLayoutManager
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(LAYOUT_MANAGER_STATE, moviesHomeLayoutManager.onSaveInstanceState())
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.getParcelable<Parcelable>(LAYOUT_MANAGER_STATE)?.let {
            moviesHomeLayoutManager.onRestoreInstanceState(it)
            savedInstanceState.remove(LAYOUT_MANAGER_STATE)
        }

        super.onViewStateRestored(savedInstanceState)
    }

    override fun onResume() {
        if(moviesHomeAdapter.itemCount == 0) {
            intentSubject.onNext(MoviesHomeIntents.InitialIntent)
        }
        super.onResume()
    }

    companion object {
        const val LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE"
    }
}

class CarouselAdapter: BaseAdapter<CarouselAdapter.CarouselItem, CarouselAdapter.CarouselItemViewHolder>(){

    override val viewHoldersMap: Map<Int, (ViewGroup) -> BaseViewHolder<out CarouselItem>>
        get() = mapOf(
            CAROUSEL_ITEM to ::CarouselItemViewHolder
        )

    inner class CarouselItemViewHolder(parent: ViewGroup)
        : BaseViewHolder<CarouselItem>(parent, R.layout.home_item_full) {
        override fun bind(item: CarouselItem) {
            itemView.apply {
                content_image.load(item.imageUrl)
            }
        }
    }

    companion object {
        const val CAROUSEL_ITEM = 0
    }

    data class CarouselItem(
        val imageUrl: String) : ListItemContent(CAROUSEL_ITEM)
}

sealed class MoviesAdapterItem(type: Int): ListItemContent(type) {
    data class Single(val movie: MovieDto) : MoviesAdapterItem(SINGLE)
    data class Carousel(val items: List<MovieDto>,
                        var layoutManagerState: Parcelable? = null) : MoviesAdapterItem(CAROUSEL)
    data class Hero(val movie: MovieDto) : MoviesAdapterItem(HERO)
    companion object {
        const val CAROUSEL = 0
        const val SINGLE = 1
        const val HERO = 2
    }
}

class MoviesHomeAdapter: BaseAdapter<MoviesAdapterItem, MoviesViewHolder<MoviesAdapterItem>>() {
    override val viewHoldersMap = mapOf(
            SINGLE to ::MovieItemViewHolder,
            CAROUSEL to ::CarouselViewHolder,
            HERO to ::MovieHeroViewHolder
        )
}

sealed class MoviesViewHolder<T : MoviesAdapterItem>(parent: ViewGroup, @LayoutRes layoutId: Int) : BaseViewHolder<T>(parent, layoutId) {

    class MovieItemViewHolder(parent: ViewGroup) : MoviesViewHolder<MoviesAdapterItem.Single>(parent, R.layout.home_item_cropped) {

        override fun bind(item: MoviesAdapterItem.Single) {
            val movie = item.movie
            itemView.apply {
                title.text = movie.title
                director.text = movie.crew.firstOrNull { it.job == "Director" }?.name ?: "Unknown"
                cast.text = movie.cast.map { it.name }.joinToString(", ")
                rating.text = movie.rating.toString()
                genres_chips.removeAllViews()
                content_image.load(movie.posterImageUrl) { bitmap ->
                    val palette = Palette.from(bitmap).generate() // cache palette necessary?
                    genres_chips.apply {
                        movie.genres.map { it.name }.forEach { chipText ->
                            addView((LayoutInflater.from(context).inflate(
                                R.layout.view_text_chip, genres_chips, false) as Chip).apply {
                                this.text = chipText
                                this.chipBackgroundColor = ColorStateList.valueOf(palette.getDarkVibrantColor(Color.GRAY))
                                this.setTextColor(palette.getLightVibrantColor(Color.WHITE))
                            })
                        }
                    }
                }
            }
        }
    }

    class MovieHeroViewHolder(parent: ViewGroup) : MoviesViewHolder<MoviesAdapterItem.Hero>(parent, R.layout.home_item_hero) {
        override fun bind(item: MoviesAdapterItem.Hero) {
            val movie = item.movie
            itemView.apply {
                content_image.load(movie.posterImageUrl){ bitmap ->
                    val palette = Palette.from(bitmap).generate() // cache palette necessary?
                    listOf(palette_transition_bg, palette_bg).forEach {
                        it.backgroundTintList = ColorStateList.valueOf(palette.getLightMutedColor(Color.BLACK))
                    }
                }
            }
        }

    }

    class CarouselViewHolder(parent: ViewGroup) : MoviesViewHolder<MoviesAdapterItem.Carousel>(parent, R.layout.view_carousel) {

        private val adapter = CarouselAdapter()

        init {
            itemView.carousel.adapter = adapter
        }

        override fun bind(item: MoviesAdapterItem.Carousel) {
            adapter.items = item.items.map { CarouselAdapter.CarouselItem(imageUrl = it.posterImageUrl) }
            itemView.carousel.layoutManager?.onRestoreInstanceState(item.layoutManagerState)
        }

        override fun onViewRecycled(items: List<*>) {
            if(adapterPosition in 0 until items.size) {
                (items[adapterPosition] as MoviesAdapterItem.Carousel).layoutManagerState =
                    itemView.carousel.layoutManager?.onSaveInstanceState()
            }
        }
    }
}

class MoviesHomePresenter constructor(private val moviesInteractor: MoviesInteractor): BasePresenter<MoviesHomeView, MoviesHomeState, MoviesHomeIntents, MoviesHomeActions, MoviesHomeResult>() {

    override val intentActionResolver = IntentActionResolver<MoviesHomeIntents, MoviesHomeActions> {
        when(it)  {
            is MoviesHomeIntents.InitialIntent -> MoviesHomeActions.LoadHomeAction
            is MoviesHomeIntents.RefreshIntent -> MoviesHomeActions.LoadHomeAction
        }
    }

    override val actionsProcessor = ActionsProcessor<MoviesHomeActions, MoviesHomeResult> {
            listOf(
                it.addProcessor<MoviesHomeActions>(loadHomeProcessor)
            )
        }

    override val initialState: MoviesHomeState
        get() = MoviesHomeState.INITIAL_STATE

    override val reducer = MviReducer<MoviesHomeState, MoviesHomeResult> {
            previousState, result -> when(result) {
                is MoviesHomeResult.LoadHomeResult ->
                    previousState.copy(
                        loading = false,
                        moviesList = result.moviesList
                    )
            }
    }

    private val loadHomeProcessor = Processor {
        it.switchMap { moviesInteractor.getHomeContent()
            .subscribeOn(Schedulers.io())
            .map {
                mutableListOf<MoviesAdapterItem>(
                    MoviesAdapterItem.Carousel(items = it.movies)
                ).apply {
                    addAll(it.movies.map { MoviesAdapterItem.Hero(it) }.take(3))
                    add(MoviesAdapterItem.Carousel(items = it.movies))
                    addAll(it.movies.map { MoviesAdapterItem.Single(it) }.drop(3).take(3))
                    add(MoviesAdapterItem.Carousel(items = it.movies))
                    addAll(it.movies.map { MoviesAdapterItem.Single(it) }.drop(6).take(3))
                    add(MoviesAdapterItem.Carousel(items = it.movies))
                    addAll(it.movies.map { MoviesAdapterItem.Single(it) }.drop(9).take(3))
                }
            }
            .map<MoviesHomeResult> { MoviesHomeResult.LoadHomeResult(it) }
        }
            .observeOn(AndroidSchedulers.mainThread())

    }
}

sealed class MoviesHomeIntents : MviIntent {
    object InitialIntent : MoviesHomeIntents()
    object RefreshIntent : MoviesHomeIntents()
}

sealed class MoviesHomeActions : MviAction {
    object LoadHomeAction: MoviesHomeActions()
}

sealed class MoviesHomeResult: MviResult {
    data class LoadHomeResult(
        val moviesList: List<MoviesAdapterItem>
    ) : MoviesHomeResult()
}

data class MoviesHomeState(
    val loading: Boolean,
    val moviesList: List<MoviesAdapterItem>) : MviState {
    companion object {
        val INITIAL_STATE = MoviesHomeState(
            loading = true,
            moviesList = emptyList())
    }
}