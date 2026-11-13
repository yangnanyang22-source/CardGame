package cn.edu.sicnu.cs.yn.cardgame

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_STUDENT_ID = "extra_student_id"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_HOBBY = "extra_hobby"
        const val EXTRA_HIGH_SCORE = "extra_high_score"

        const val REQUEST_EDIT = 100
        const val REQUEST_GAME = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        if (savedInstanceState == null) {
            switchFragment(ProfileFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_profile -> ProfileFragment()
                R.id.nav_two_cards -> TwoCardGameFragment()
                R.id.nav_three_cards -> ThreeCardGameFragment()
                else -> return@setOnItemSelectedListener false
            }
            switchFragment(fragment)
            true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_about_title)
                    .setMessage(R.string.dialog_about_message)
                    .setPositiveButton(R.string.btn_ok, null)
                    .show()
                return true
            }
            R.id.menu_exit -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
