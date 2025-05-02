package se.curtrune.lucy.screens.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.app.Settings.PanicAction
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.dialogs.PanicActionDialog
import se.curtrune.lucy.dialogs.UpdateDialog
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.affirmations.Quote
import se.curtrune.lucy.screens.appointments.AppointmentsFragment
import se.curtrune.lucy.screens.contacts.ContactsFragment
import se.curtrune.lucy.screens.daycalendar.CalendarDayFragment
import se.curtrune.lucy.screens.dev.DevActivity
import se.curtrune.lucy.screens.duration.DurationFragment
import se.curtrune.lucy.screens.enchilada.EnchiladaFragment
import se.curtrune.lucy.screens.log_in.OldLogInActivity
import se.curtrune.lucy.screens.main.composables.ChoosePanicActionDialog
import se.curtrune.lucy.screens.main.composables.QuoteDialog
import se.curtrune.lucy.screens.medicine.MedicineFragment
import se.curtrune.lucy.screens.mental_stats.MentalStatsFragment
import se.curtrune.lucy.screens.message_board.MessageBoardFragment
import se.curtrune.lucy.screens.monthcalendar.MonthFragment
import se.curtrune.lucy.screens.my_day.MyDayFragment
import se.curtrune.lucy.screens.projects.ProjectsFragment
import se.curtrune.lucy.screens.todo.TodoFragment
import se.curtrune.lucy.screens.user_settings.UserSettingsFragment
import se.curtrune.lucy.screens.week_calendar.WeekFragment
import se.curtrune.lucy.util.Constants
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.VersionInfo
import java.util.Objects

class MainActivity : AppCompatActivity() {
    private var drawerLayout: DrawerLayout? = null
    private val mainViewModel: MainViewModel by viewModels()
    //private var mainViewModel: MainViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        println("MainActivity.onCreate(Bundle of joy)")
        initComponents()
        initListeners()
        initViewModel()
        initContent()
        val intent = intent
        val fragmentName = intent.getStringExtra(Constants.INITIAL_SCREEN)
        if (fragmentName != null) {
            Logger.log("...fragmentName", fragmentName)
            navigate(InitialScreen.valueOf(fragmentName))
        } else {
            navigate(CalendarDayFragment())
        }
    }

    private fun initComponents() {
        drawerLayout = findViewById(R.id.navigationDrawer_drawerLayout)
        val navigationView =
            findViewById<NavigationView>(R.id.navigationDrawerActivity_navigationView)
        //val view = navigationView.inflateHeaderView(R.layout.navigation_header)
        val onNavigationItemSelectedListener =
            NavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
                Logger.log(
                    "...onNavigationItemSelected(MenuItem) ",
                    Objects.requireNonNull(item.title).toString()
                )
                when (item.itemId) {
                    R.id.navigationDrawer_monthCalender -> {
                        navigate(MonthFragment())
                    }
                    R.id.bottomNavigation_today -> {
                        navigate(CalendarDayFragment())
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

                    R.id.navigationDrawer_weekly -> {
                        navigate(WeekFragment())
                    }
                    R.id.navigationDrawer_messageBoardFragment -> {
                        navigate(MessageBoardFragment())
                    }
                    R.id.navigationDrawer_medicines  ->{
                        navigate(MedicineFragment())
                    }
                    R.id.navigationDrawer_customizeFragment -> {
                        navigate(UserSettingsFragment())
                    }
                    R.id.navigationDrawer_mentalFragment -> {
                        navigate(MyDayFragment())
                    }
                    R.id.navigationDrawer_mentalStatsFragment -> {
                        navigate(MentalStatsFragment())
                    }
                    R.id.navigationDrawer_contactsFragment -> {
                        navigate(ContactsFragment())
                    }
                    R.id.navigationDrawer_logOut -> {
                        Logger.log("...log out")
                        val intent = Intent(
                            this,
                            OldLogInActivity::class.java
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
    @OptIn(ExperimentalMaterial3Api::class)
    private fun initContent(){
        //println("initContent()")
        val composeViewMental = findViewById<ComposeView>(R.id.mainActivity_composeViewMental)
        composeViewMental.setContent {
            val state = mainViewModel.state.collectAsState()
            var showPanicDialog by remember{
                mutableStateOf(false)
            }
            var showAffirmation by remember {
                mutableStateOf(false)
            }
            var showQuoteDialog by remember {
                mutableStateOf(false)
            }
            var quote by remember {
                mutableStateOf(Quote())
            }
            //var topAppBarState = mainViewModel?.topAppBarState?.collectAsState()
            val topAppBarState = TopAppbarModule.topAppBarState.collectAsState()
            LaunchedEffect(mainViewModel) {
                mainViewModel.eventChannel?.collect { event ->
                    when (event) {
                        MainChannelEvent.ShowPanicDialog -> {
                            showPanicDialog = true
                        }
                        is MainChannelEvent.ShowAffirmation -> {
                            Toast.makeText(applicationContext,
                                event.affirmation?.affirmation ?: "you're the greatest", Toast.LENGTH_LONG).show()
                        }
                        is MainChannelEvent.ShowBoostDialog -> {
                            Toast.makeText(applicationContext, "boost me", Toast.LENGTH_LONG).show()
                        }

                        is MainChannelEvent.ShowQuoteDialog -> {
                            quote = event.quote!!
                            showQuoteDialog = true
                        }

                        is MainChannelEvent.UpdateAvailable -> {
                            state.value.versionInfo?.let { showUpdateDialog(it) }
                        }

                        is MainChannelEvent.StartSequence -> {
                            //navigate(SequenceFragment(event.root))
                            Toast.makeText(applicationContext, "start sequence not implemented", Toast.LENGTH_LONG).show()
                        }

                        is MainChannelEvent.OpenNavigationDrawer -> {
                            println("open navigation drawer")
                            drawerLayout!!.open()
                        }

                        is MainChannelEvent.ShowDayCalendar -> { navigate(CalendarDayFragment()) }
                        is MainChannelEvent.ShowMessage -> {
                            Toast.makeText(applicationContext, event.message, Toast.LENGTH_LONG).show()
                        }

                        MainChannelEvent.NavigateDevActivity -> {
                            startActivity(Intent(applicationContext, DevActivity::class.java))
                        }
                    }
                }
            }
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            LucyTheme {
                FlexibleTopBar(
                    scrollBehavior = scrollBehavior,
                    content = {
                        LucindaTopAppBar(
                            state = topAppBarState.value,
                            onEvent = { appBarEvent ->
                                println("appBarEvent $appBarEvent")
                                mainViewModel.onEvent(appBarEvent)
                            })
                    }, onEvent = { event ->
                        //devViewModel.onEvent(event)
                    }
                )
            }
            if (showPanicDialog) {
                ChoosePanicActionDialog(
                    state = state.value,
                    onDismiss = {
                        showPanicDialog = false
                    },
                    onEvent = {})
            }
            if (showQuoteDialog) {
                QuoteDialog(onDismiss = {
                    showQuoteDialog = false
                }, quote = quote)
            }
            if (showAffirmation) {
                println("showAffirmation")
            }
        }
    }


    private fun initListeners() {
        println("...initListeners()")
        //textViewLucindaHome!!.setOnClickListener { view: View? -> openWebPage("https://curtfurumark.se/lucinda") }
    }

    private fun initViewModel() {
        println("...initViewModel()")
        //mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.fragment.observe(this) { fragment: Fragment? ->
            Logger.log(" new fragment observed")
            navigate(fragment)
        }
        mainViewModel.message.observe(
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

    private fun navigate(initialScreen: InitialScreen) {
        Logger.log("...navigate(InitialScreen)", initialScreen.toString())
        when (initialScreen) {
            InitialScreen.CALENDER_DATE -> navigate(CalendarDayFragment())
            InitialScreen.CALENDER_WEEK -> navigate(WeekFragment())
            InitialScreen.CALENDER_MONTH -> navigate(MonthFragment())
            InitialScreen.CALENDER_APPOINTMENTS -> navigate(AppointmentsFragment())
            InitialScreen.TODO_FRAGMENT -> navigate(TodoFragment())

        }
    }
    private fun openWebPage(url: String) {
        Logger.log("...openWebPage(String url)", url)
        if (LucindaApplication.appModule.internetWorker.isConnected()) {
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
        /*        int phoneNumber = UserPrefs.getICE(this);
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
            PanicAction.GAME ->{
                Toast.makeText(applicationContext, "game not implemented", Toast.LENGTH_LONG).show()
            }
                //startActivity(
/*                Intent(
                    this,
                    //GameActivity::class.java
                )*/
            PanicAction.URL -> {
                val url = UserPrefs.getRandomPanicUrl(this)
                openWebPage(url)
            }

            PanicAction.SEQUENCE -> {
                println("...SEQUENCE")
                mainViewModel.onEvent(MainEvent.StartSequence(Item()))
                //navigate(SequenceFragment(panicRoot))
                //}
            }

            PanicAction.ICE -> panicActionICE()
            PanicAction.PENDING -> panicActionPending()
        }
    }

/*    private fun showPanicAction() {
        val panicAction = mainViewModel!!.getPanicAction(this)
        Logger.log("...showPanicAction()", panicAction.toString())
        when (panicAction) {
            PanicAction.GAME -> startActivity(
                Intent(
                    this,
                    GameActivity::class.java
                )
            )

            PanicAction.URL -> {
                val url = UserPrefs.getRandomPanicUrl(this)
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
    }*/
    private fun showUpdateDialog(versionInfo: VersionInfo){
        val dialog = UpdateDialog(versionInfo) {
            Logger.log("here we go")
            Toast.makeText(applicationContext, "here we go,", Toast.LENGTH_LONG).show()
            Logger.log("updated url", versionInfo.url)
            versionInfo.url?.let { openWebPage(it) }
        }
        dialog.show(supportFragmentManager, "update lucinda")
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}