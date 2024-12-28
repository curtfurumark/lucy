package se.curtrune.lucy.activities.economy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.DevActivity
import se.curtrune.lucy.activities.economy.persist.ECDBAdmin
import se.curtrune.lucy.util.Logger

class EconomyActivity : AppCompatActivity() {
    private var tabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.log("EconomyActivity.onCreate() kotlin")
        setContentView(R.layout.economy_activity)
        initComponents()
        initListeners()
    }

    private fun createTables() {
        Logger.log("...createTables()")
        ECDBAdmin.createEconomyTables(this)
    }

    private fun dropTables() {
        Logger.log("...dropTables()")
        ECDBAdmin.dropTables(this)
    }

    private fun initComponents() {
        Logger.log("...initComponents()")
        tabLayout = findViewById(R.id.economyActivity_tabLayout)
    }

    private fun initListeners() {
        Logger.log("...initListeners()")
        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                Logger.log("...onTabSelected(Tab) position", position)
                when (position) {
                    0 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.economyActivity_fragmentContainer, TransactionFragment())
                        .commit()

                    1 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.economyActivity_fragmentContainer, AssetsFragment()).commit()

                    2 -> supportFragmentManager.beginTransaction().replace(
                        R.id.economyActivity_fragmentContainer,
                        EconomyStatisticsFragment()
                    ).commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                Logger.log("...onTabUnselected(TabLayout)")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                Logger.log("...onTabReselected(TabLayout)")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.economy_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Logger.log("...onOptionsItemSelected(MenuItem)")
        if (item.itemId == R.id.economyActivity_home) {
            startActivity(Intent(this, DevActivity::class.java))
        } else if (item.itemId == R.id.economyActivity_dropTables) {
            dropTables()
        } else if (item.itemId == R.id.economyActivity_createTables) {
            createTables()
        }
        return super.onOptionsItemSelected(item)
    }
}