package uk.co.scraigie.onscreen

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.scraigie.onscreen.movies.MoviesHomeFragment

class MainActivity : BaseActivity() {

    private val onNavigationItemSelectedListener by lazy {
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    title = getString(R.string.title_home)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_movies -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, MoviesHomeFragment())
                        .addToBackStack(null)
                        .commit()

                    title = getString(R.string.title_dashboard)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_tv -> {
                    title = getString(R.string.title_notifications)
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
        }
    }
}
