package uk.co.scraigie.onscreen

import android.os.Bundle
import co.uk.scraigie.onscreen.tv.TvHomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.scraigie.onscreen.movies.ui.home.MoviesHomeFragment

class MainActivity : BaseActivity() {

    private val onNavigationItemSelectedListener by lazy {
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MoviesHomeFragment())
                        .addToBackStack(null)
                        .commit()
                    title = getString(R.string.title_home)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_movies -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MoviesHomeFragment())
                        .addToBackStack(null)
                        .commit()

                    title = getString(R.string.title_movies)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_tv -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TvHomeFragment())
                        .addToBackStack(null)
                        .commit()
                    title = getString(R.string.title_tv)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view?.apply {
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_home
        }
    }
}
