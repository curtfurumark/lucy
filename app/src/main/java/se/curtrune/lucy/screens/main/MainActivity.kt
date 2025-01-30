package se.curtrune.lucy.screens.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.flying_fish.GameActivity
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.app.FirstPage
import se.curtrune.lucy.app.Settings.PanicAction
import se.curtrune.lucy.app.User
import se.curtrune.lucy.classes.Affirmation
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.MentalMeter
import se.curtrune.lucy.dialogs.BoostDialog
import se.curtrune.lucy.dialogs.PanicActionDialog
import se.curtrune.lucy.dialogs.UpdateDialog
import se.curtrune.lucy.fragments.ContactFragment
import se.curtrune.lucy.fragments.CustomizeFragment
import se.curtrune.lucy.fragments.DailyGraphFragment
import se.curtrune.lucy.fragments.EnchiladaFragment
import se.curtrune.lucy.fragments.EstimateFragment
import se.curtrune.lucy.fragments.MentaHistoryFragment
import se.curtrune.lucy.fragments.SequenceFragment
import se.curtrune.lucy.screens.todo.TodoFragment
import se.curtrune.lucy.screens.top_ten.TopTenFragment
import se.curtrune.lucy.modules.MentalModule
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.screens.appointments.AppointmentsFragment
import se.curtrune.lucy.screens.contacts.ContactsFragment
import se.curtrune.lucy.screens.daycalendar.CalendarDateFragment
import se.curtrune.lucy.screens.daycalendar.CalenderDateFragmentOld
import se.curtrune.lucy.screens.dev.DevActivity
import se.curtrune.lucy.screens.duration.DurationFragment
import se.curtrune.lucy.screens.index20.IndexActivityKt
import se.curtrune.lucy.screens.log_in.LogInActivity
import se.curtrune.lucy.screens.mental.MentalDateFragment
import se.curtrune.lucy.screens.message_board.MessageBoardFragment
import se.curtrune.lucy.screens.monthcalendar.MonthFragment
import se.curtrune.lucy.screens.projects.ProjectsFragment
import se.curtrune.lucy.screens.timers.TimerFragment
import se.curtrune.lucy.screens.week_calendar.CalendarWeekHostFragment
import se.curtrune.lucy.util.Constants
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.VersionInfo
import se.curtrune.lucy.workers.InternetWorker
import java.util.Objects

class MainActivity : AppCompatActivity() {
    private var drawerLayout: DrawerLayout? = null
    private var viewModel: LucindaViewModel? = null

    //private Fragment currentFragment;
    private var textViewPanic: TextView? = null
    private var textViewBoost: TextView? = null
    //private var textViewEnergy: TextView? = null
    private var textViewLucindaHome: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Logger.log("MainActivity.onCreate(Bundle of joy)")
        initComponents()
        initListeners()
        initViewModel()
        initContent()
        val intent = intent
        val fragmentName = intent.getStringExtra(Constants.MAIN_ACTIVITY_CHILD_FRAGMENT)
        if (fragmentName != null) {
            Logger.log("...fragmentName", fragmentName)
            navigate(FirstPage.valueOf(fragmentName))
        } else {
            navigate(CalenderDateFragmentOld())
        }
    }

    private fun boostMe() {
        //viewModel!!.requestAffirmation()
        viewModel!!.onEvent(MainEvent.ShowBoost(true))
    }

    private fun initComponents() {
        if (VERBOSE) Logger.log("...initComponents()")
        val toolbar = findViewById<MaterialToolbar>(R.id.navigationDrawer_toolbar)
        setSupportActionBar(toolbar)
        textViewPanic = findViewById(R.id.mainActivity_panic)
        textViewBoost = findViewById(R.id.mainActivity_buttonBoost)
        //textViewEnergy = findViewById(R.id.mainActivity_energy)
        drawerLayout = findViewById(R.id.navigationDrawer_drawerLayout)
        val navigationView =
            findViewById<NavigationView>(R.id.navigationDrawerActivity_navigationView)
        val view = navigationView.inflateHeaderView(R.layout.navigation_header)
        textViewLucindaHome = view.findViewById(R.id.navigationHeader_lucindaHome)
        val actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { v: View? ->
            if (VERBOSE) Logger.log("... toolbar on click")
            drawerLayout!!.open()
        }
        val onNavigationItemSelectedListener =
            NavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
                Logger.log(
                    "...onNavigationItemSelected(MenuItem) ",
                    Objects.requireNonNull(item.title).toString()
                )
                when (item.itemId) {
                    R.id.navigationDrawer_graphFragment -> {
                        navigate(DailyGraphFragment())
                    }
                    R.id.navigationDrawer_monthCalender -> {
                        navigate(MonthFragment())
                    }
                    R.id.bottomNavigation_today -> {
                        navigate(CalenderDateFragmentOld())
                    }
                    R.id.navigationDrawer_topTen -> {
                        navigate(TopTenFragment())
                    }
                    R.id.bottomNavigation_todo -> {
                        navigate(TodoFragment())
                    }
                    R.id.bottomNavigation_appointments -> {
                        navigate(AppointmentsFragment())
                    }
                    R.id.bottomNavigation_enchilada -> {
                        navigate(EnchiladaFragment())
                    }
                    R.id.bottomNavigation_projects -> {
                        navigate(ProjectsFragment())
                    }
                    R.id.navigationDrawer_durationFragment -> {
                        navigate(DurationFragment())
                    }
                    R.id.navigationDrawer_estimateFragment -> {
                        navigate(EstimateFragment())
                    }
                    R.id.navigationDrawer_weekly -> {
                        navigate(CalendarWeekHostFragment())
                    }
                    R.id.navigationDrawer_contactFragment -> {
                        navigate(ContactFragment())
                    }
                    R.id.navigationDrawer_messageBoardFragment -> {
                        navigate(MessageBoardFragment())
                    }
                    R.id.navigationDrawer_countDownTimer -> {
                        navigate(TimerFragment())
                    }
                    R.id.navigationDrawer_customizeFragment -> {
                        navigate(CustomizeFragment())
                    }
                    R.id.navigationDrawer_mentalFragment -> {
                        navigate(MentalDateFragment())
                    }
                    R.id.navigationDrawer_mentalHistoryFragment -> {
                        navigate(MentaHistoryFragment())
                    }
                    R.id.navigationDrawer_contactsFragment -> {
                        navigate(ContactsFragment())
                    }
                    R.id.navigationDrawer_logOut -> {
                        Logger.log("...log out")
                        val intent = Intent(
                            this,
                            LogInActivity::class.java
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        //finish();
                    }
                }
                drawerLayout!!.close()
                true
            }
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
    private fun initContent(){
        println("initContent()")
        val composeView = findViewById<ComposeView>(R.id.mainActivity_composeView)
        val composeViewMental = findViewById<ComposeView>(R.id.mainActivity_composeViewMental)
        composeViewMental.setContent {
            LucyTheme {
                val currentMental = MentalModule.currentMental
                var mental by remember{
                    mutableStateOf(Mental())
                }
                currentMental.observe(this) {
                    println("MENTAL CHANGE ${it.energy}")
                    mental = it
                }
                MentalMeter(mental = mental)
            }
        }
        /*composeView.setContent {
            val state = viewModel!!.state.collectAsState()
            val mentalModule = LucindaApplication.mentalModule
            val currentMental = MentalModule.currentMental
            var mental by remember{
                mutableStateOf(Mental())
            }
            currentMental.observe(this) {
                println("MENTAL CHANGE ${it.energy}")
                mental = it
            }
            MentalMeter(mental = mental)
        }*/
    }

    private fun initListeners() {
        println("...initListeners()")
        textViewBoost!!.setOnClickListener { view: View? -> boostMe() }
        textViewPanic!!.setOnClickListener { view: View? -> showPanicAction() }
        textViewLucindaHome!!.setOnClickListener { view: View? -> openWebPage("https://curtfurumark.se/lucinda") }
    }

    private fun initViewModel() {
        println("...initViewModel()")
        viewModel = ViewModelProvider(this)[LucindaViewModel::class.java]
        viewModel!!.fragment.observe(this) { fragment: Fragment? ->
            Logger.log(" new fragment observed")
            navigate(fragment)
        }

        viewModel!!.affirmation.observe(
            this
        ) { affirmation: Affirmation -> this.showBoostDialog(affirmation) }
        viewModel!!.updateAvailable().observe(this) { versionInfo: VersionInfo ->
            Logger.log("...updateAvailable(VersionInfo)")
            val dialog = UpdateDialog(versionInfo) {
                Logger.log("here we go")
                Toast.makeText(applicationContext, "here we go,", Toast.LENGTH_LONG).show()
                Logger.log("updated url", versionInfo.url)
                openWebPage(versionInfo.url)
            }
            dialog.show(supportFragmentManager, "update lucinda")
        }
        viewModel!!.message.observe(
            this
        ) { message: String? ->
            Toast.makeText(
                applicationContext, message, Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * @param fragment, the fragment to go to
     */
    private fun navigate(fragment: Fragment?) {
        if (fragment == null) {
            Logger.log("...navigate(Fragment) called with null fragment, i surrender")
            return
        }
        Logger.log("MainActivity.navigate(Fragment) ", fragment.javaClass.name)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navigationDrawer_frameContainer, fragment)
            .addToBackStack("previous fragment")
            .commit()
    }

    private fun navigate(firstPage: FirstPage) {
        Logger.log("...navigate(FirstPage)", firstPage.toString())
        when (firstPage) {
            FirstPage.CALENDER_DATE -> navigate(CalendarDateFragment())
            FirstPage.CALENDER_WEEK -> navigate(CalendarWeekHostFragment())
            FirstPage.CALENDER_MONTH -> navigate(MonthFragment())
            FirstPage.CALENDER_APPOINTMENTS -> navigate(AppointmentsFragment())
            FirstPage.TODO_FRAGMENT -> navigate(TodoFragment())

        }
    }

    override fun onBackPressed() {
        Logger.log("MainActivity.onBackPressed()")
        //super.onBackPressed();
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            Logger.log("...drawerLayout is open, closing")
            drawerLayout!!.close()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        val searchView = menu.findItem(R.id.mainActivity_search).actionView as SearchView?
        if (searchView == null) {
            Logger.log("searchView == null, onCreteOptionsMenu(Menu), MainActivity")
            return false
        }
        searchView.queryHint = "search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Logger.log("...onQueryTextSubmit(String)", query)

                return false
            }

            override fun onQueryTextChange(filter: String): Boolean {
                Logger.log("...onQueryTextChange(String)", filter)
                viewModel!!.filter(filter)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Logger.log(
            "...onOptionsItemSelected(MenuItem item)",
            Objects.requireNonNull(item.title).toString()
        )
        when (item.itemId) {
            R.id.navigationDrawer_graphFragment -> {
                navigate(DailyGraphFragment())
            }
            R.id.mainActivity_dev -> {
                startActivity(Intent(this, DevActivity::class.java))
            }
            R.id.mainActivityCheckForUpdate -> {
                viewModel!!.checkIfUpdateAvailable(this)
            }
            R.id.mainActivity_calendar -> {
                //navigate(CalenderDateFragmentOld())
                navigate(CalendarDateFragment())
            }
            R.id.mainActivity_lucinda20 -> {
                startActivity(Intent(this, IndexActivityKt::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun openWebPage(url: String) {
        Logger.log("...openWebPage(String url)", url)
        if (!InternetWorker.isConnected(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
            return
        }
        val webPage = Uri.parse(url)
        try {
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        } catch (e: Exception) {
            Logger.log("EXCEPTION, probably not valid ur")
            Toast.makeText(this, "page not found", Toast.LENGTH_LONG).show()
        }
    }

    private fun panicActionICE() {
        Logger.log("...panicActionICE")
        //FIX PERMISSION, IN CUSTOMIZEFRAGEMENT PERHAPS
        /*        int phoneNumber = User.getICE(this);
        if( phoneNumber == -1){
            Toast.makeText(this, "no phone number to call", Toast.LENGTH_LONG).setMentalType();
            return;
        }
        String stringURI = String.format(Locale.getDefault(), "tel:%d", phoneNumber);
        log("...stringURI", stringURI);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(stringURI));
        startActivity(intent)*/
    }

    private fun panicActionPending() {
        Logger.log("...panicActionPending()")
        val dialog = PanicActionDialog()
        dialog.setListener { panicAction: PanicAction ->
            Logger.log("onPanicAction(PanicAction)")
            panic(panicAction)
        }
        dialog.show(supportFragmentManager, "panic action")
    }

    private fun panic(panicAction: PanicAction) {
        Logger.log("...panic(PanicAction))")
        Logger.log("....panicAction", panicAction.toString())
        when (panicAction) {
            PanicAction.GAME -> startActivity(
                Intent(
                    this,
                    GameActivity::class.java
                )
            )

            PanicAction.URL -> {
                val url = User.getRandomPanicUrl(this)
                openWebPage(url)
            }

            PanicAction.SEQUENCE -> {
                val panicRoot = ItemsWorker.getPanicRoot(this)
                if (panicRoot == null) {
                    Logger.log("ERROR...panicRoot == null")
                } else {
                    navigate(SequenceFragment(panicRoot))
                }
            }

            PanicAction.ICE -> panicActionICE()
            PanicAction.PENDING -> panicActionPending()
        }
    }

    private fun showBoostDialog(affirmation: Affirmation) {
        Logger.log("...showBoostDialog(Affirmation)")
        val boostDialog = BoostDialog(affirmation.affirmation)
        boostDialog.show(supportFragmentManager, "boost me")
    }

    private fun showPanicAction() {
        val panicAction = viewModel!!.getPanicAction(this)
        Logger.log("...showPanicAction()", panicAction.toString())
        when (panicAction) {
            PanicAction.GAME -> startActivity(
                Intent(
                    this,
                    GameActivity::class.java
                )
            )

            PanicAction.URL -> {
                val url = User.getRandomPanicUrl(this)
                openWebPage(url)
            }

            PanicAction.SEQUENCE -> {
                val panicRoot = ItemsWorker.getPanicRoot(this)
                if (panicRoot == null) {
                    Logger.log("ERROR...panicRoot == null")
                } else {
                    navigate(SequenceFragment(panicRoot))
                }
            }

            PanicAction.ICE -> panicActionICE()
            PanicAction.PENDING -> panicActionPending()
            else ->                 //TODO, translate
                Toast.makeText(
                    this,
                    "go to customize and set preferred panic action",
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}