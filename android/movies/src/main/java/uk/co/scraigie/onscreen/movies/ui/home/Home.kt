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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_item_cropped.view.*
import kotlinx.android.synthetic.main.view_carousel.view.*
import org.koin.android.ext.android.inject
import uk.co.scraigie.onscreen.core.behaviors.PresenterBehavior
import uk.co.scraigie.onscreen.core_android.behavior.BehaviorFragment
import uk.co.scraigie.onscreen.movies.R
import uk.co.scraigie.onscreen.movies.data.dtos.MovieDto
import uk.co.scraigie.onscreen.movies.domain.MoviesInteractor
import uk.co.scraigie.onscreen.movies.ui.load

interface MoviesHomeView : MviView<MoviesHomeIntents, MoviesHomeState>

class MoviesHomeFragment: BehaviorFragment(), MoviesHomeView {

    private val presenter: MoviesHomePresenter by inject()
    private val moviesHomeAdapter = MoviesHomeAdapter()
    private val moviesHomeLayoutManager by lazy { LinearLayoutManager(activity, RecyclerView.VERTICAL, false) }

    init {
        addBehavior(PresenterBehavior(this) { presenter })
    }

    override val intentObservable: Observable<MoviesHomeIntents>
        get() = Observable.just(MoviesHomeIntents.InitialIntent)

    override fun render(state: MoviesHomeState) {
        loader?.apply {
            if(state.loading) show() else hide()
        }
        moviesHomeAdapter.apply {
            setData(state.moviesList)
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

    companion object {
        const val LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE"
    }
}

class CarouselAdapter: RecyclerView.Adapter<CarouselAdapter.CarouselItemViewHolder>() {

    var dataItems = listOf<CarouselItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder = CarouselItemViewHolder(parent)

    override fun getItemCount(): Int = dataItems.size

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) = holder.onBind(this.dataItems[position])

    fun setData(items: List<CarouselItem>) {
        this.dataItems = items
        notifyDataSetChanged()
    }

    inner class CarouselItemViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_item_full, parent, false)) {
        fun onBind(item: CarouselItem) {
            itemView.apply {
                content_image.load(item.imageUrl)
            }
        }
    }

    data class CarouselItem(
        val imageUrl: String)
}

sealed class MoviesAdapterItem(val type: Int) {
    data class Single(val movie: MovieDto) : MoviesAdapterItem(MoviesHomeAdapter.SINGLE)
    data class Carousel(val items: List<MovieDto>,
                        var layoutManagerState: Parcelable? = null) : MoviesAdapterItem(MoviesHomeAdapter.CAROUSEL)
}

class MoviesHomeAdapter: RecyclerView.Adapter<MoviesViewHolder<*>>() {

    private var moviesList: List<MoviesAdapterItem> = emptyList()

    override fun getItemViewType(position: Int): Int =
        moviesList[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder<*> {
        return when(viewType) {
            CAROUSEL -> MoviesViewHolder.CarouselViewHolder(parent)
            SINGLE -> MoviesViewHolder.MovieItemViewHolder(parent)
            else -> throw NoSuchElementException("MoviesHomeAdapter cannot resolve viewType: $viewType")
        }
    }

    override fun onViewRecycled(holder: MoviesViewHolder<*>) {
        when(holder){
            is MoviesViewHolder.CarouselViewHolder -> holder.setLayoutStateInItem()
        }
        super.onViewRecycled(holder)
    }

    private fun MoviesViewHolder.CarouselViewHolder.setLayoutStateInItem() {
        (moviesList[adapterPosition] as MoviesAdapterItem.Carousel).layoutManagerState = itemView.carousel.layoutManager?.onSaveInstanceState()
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun setData(moviesList: List<MoviesAdapterItem>) {
        this.moviesList = moviesList
    }

    override fun onBindViewHolder(holder: MoviesViewHolder<*>, position: Int) {
        when(holder) {
            is MoviesViewHolder.MovieItemViewHolder -> holder.onBind((moviesList[position] as MoviesAdapterItem.Single))
            is MoviesViewHolder.CarouselViewHolder -> holder.onBind((moviesList[position] as MoviesAdapterItem.Carousel))
        }
    }

    companion object {
        const val CAROUSEL = 0
        const val SINGLE = 1
    }
}

sealed class MoviesViewHolder<T : MoviesAdapterItem>(parent: ViewGroup, @LayoutRes layoutId: Int) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    abstract fun onBind(item: T)

    class MovieItemViewHolder(parent: ViewGroup) : MoviesViewHolder<MoviesAdapterItem.Single>(parent, R.layout.home_item_cropped) {

        override fun  onBind(item: MoviesAdapterItem.Single) {
            val movie = item.movie
            itemView.apply {
                title.text = movie.title
                director.text = movie.crew.firstOrNull { it.job == "Director" }?.name ?: "Unknown"
                cast.text = movie.cast.map { it.name }.joinToString(", ")
                rating.text = movie.rating.toString()
                content_image.load(movie.posterImageUrl) { bitmap ->
                    val palette = Palette.from(bitmap).generate()
                    genres_chips.apply {
                        removeAllViews()
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

    class CarouselViewHolder(parent: ViewGroup) : MoviesViewHolder<MoviesAdapterItem.Carousel>(parent, R.layout.view_carousel) {

        private val adapter = CarouselAdapter()

        init {
            itemView.carousel.adapter = adapter
        }

        override fun onBind(item: MoviesAdapterItem.Carousel) {
            adapter.setData(item.items.map { CarouselAdapter.CarouselItem(imageUrl = it.posterImageUrl) })
            itemView.carousel.layoutManager?.onRestoreInstanceState(item.layoutManagerState)
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
            .subscribeOn(Schedulers.io()) }
            .map {
                mutableListOf<MoviesAdapterItem>(
                    MoviesAdapterItem.Carousel(items = it.movies)
                ).apply {
                    addAll(it.movies.map { MoviesAdapterItem.Single(it) }.take(3))
                    add(MoviesAdapterItem.Carousel(items = it.movies))
                    addAll(it.movies.map { MoviesAdapterItem.Single(it) }.drop(3).take(3))
                    add(MoviesAdapterItem.Carousel(items = it.movies))
                    addAll(it.movies.map { MoviesAdapterItem.Single(it) }.drop(6).take(3))
                    add(MoviesAdapterItem.Carousel(items = it.movies))
                    addAll(it.movies.map { MoviesAdapterItem.Single(it) }.drop(9).take(3))
                }
            }
            .map<MoviesHomeResult> { MoviesHomeResult.LoadHomeResult(it) }
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