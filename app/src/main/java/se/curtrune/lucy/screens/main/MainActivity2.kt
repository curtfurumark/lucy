package se.curtrune.lucy.screens.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation3.runtime.rememberNavBackStack
import kotlinx.coroutines.launch
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.app.Settings.PanicAction
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.dialogs.PanicActionDialog
import se.curtrune.lucy.dialogs.UpdateDialog
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.modules.UserSettings
import se.curtrune.lucy.screens.affirmations.Quote
import se.curtrune.lucy.screens.appointments.AppointmentsFragment
import se.curtrune.lucy.screens.daycalendar.CalendarDayFragment
import se.curtrune.lucy.screens.dev.DevActivity
import se.curtrune.lucy.screens.main.composables.ChoosePanicActionDialog
import se.curtrune.lucy.screens.navigation.LucindaNavigationDrawer
import se.curtrune.lucy.screens.main.composables.QuoteDialog
import se.curtrune.lucy.screens.navigation.DayCalendarNavKey
import se.curtrune.lucy.screens.navigation.NavigationRoot
import se.curtrune.lucy.screens.monthcalendar.MonthFragment
import se.curtrune.lucy.screens.todo.TodoFragment
import se.curtrune.lucy.screens.week_calendar.WeekFragment
import se.curtrune.lucy.util.Constants
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.VersionInfo
import java.time.LocalDate

class MainActivity2 : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        println("MainActivity2.onCreate(Bundle of joy)")
        //initListeners()
        initViewModel()
        initContent()
        val intent = intent
        val fragmentName = intent.getStringExtra(Constants.INITIAL_SCREEN)
        if (fragmentName != null) {
            Logger.log("...fragmentName", fragmentName)
            loadFragment(InitialScreen.valueOf(fragmentName))
        } else {
            loadFragment(CalendarDayFragment())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    private fun initContent(){
        //println("initContent()")
        val composeViewMental = findViewById<ComposeView>(R.id.mainActivity_composeViewMental)
        composeViewMental.setContent {
            val state by  mainViewModel.state.collectAsState()
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
            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val topAppBarState = TopAppbarModule.topAppBarState.collectAsState()
            val navigationDrawerState = UserSettings(application).navigationDrawerState.collectAsState()

            LaunchedEffect(mainViewModel) {
                mainViewModel.eventChannel.collect { event ->
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
                            state.versionInfo?.let { showUpdateDialog(it) }
                        }

                        is MainChannelEvent.StartSequence -> {
                            //navigate(SequenceFragment(event.root))
                            Toast.makeText(applicationContext, "start sequence not implemented", Toast.LENGTH_LONG).show()
                        }


                        is MainChannelEvent.ShowDayCalendar -> { loadFragment(CalendarDayFragment()) }
                        is MainChannelEvent.ShowMessage -> {
                            Toast.makeText(applicationContext, event.message, Toast.LENGTH_LONG).show()
                        }

                        MainChannelEvent.NavigateDevActivity -> {
                            startActivity(Intent(applicationContext, DevActivity::class.java))
                        }

                        is MainChannelEvent.ShowNavigationDrawer -> {
                            if( event.show){
                                drawerState.open()
                            }else{
                                drawerState.close()
                            }
                        }
                    }
                }
            }

            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            LucyTheme {
                val backStack = rememberNavBackStack(DayCalendarNavKey(LocalDate.now().toString()))
                //val backStack = remember { mutableStateListOf<NavKey>(DayCalendarNavKey(LocalDate.now().toString())) }
                //val backStack = NavBackStack<NavKey>(DayCalendarNavKey(LocalDate.now().toString()))
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        LucindaNavigationDrawer(onClick = {navKey->
                            scope.launch {
                                println("navKey $navKey")
                                backStack.add(navKey)
                                drawerState.close()
                            }
                        }, state = navigationDrawerState.value)
                    }
                ) {
                    Scaffold(topBar = {
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
                                println("onEvent $event")
                                //devViewModel.onEvent(event)
                            }
                        )

                    }) { innerPadding ->
                        NavigationRoot(modifier = Modifier.padding(innerPadding), backStack = backStack)
                    }
                }
                if (showPanicDialog) {
                    ChoosePanicActionDialog(
                        state = state,
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
    }


    private fun initViewModel() {
        println("...initViewModel()")
        //mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.fragment.observe(this) { fragment: Fragment? ->
            Logger.log(" new fragment observed")
            loadFragment(fragment)
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
    private fun loadFragment(fragment: Fragment?) {
        println("...loadFragment(Fragment: ${fragment.toString()})")
        if (fragment == null) {
            println("...loadFragment(Fragment) called with null fragment, i surrender")
            return
        }
        //Logger.log("MainActivity.navigate(Fragment) ", fragment.javaClass.name)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navigationDrawer_frameContainer, fragment)
            .addToBackStack("previous fragment")
            .commit()
    }

    private fun loadFragment(initialScreen: InitialScreen) {
        println("...loadFragment(InitialScreen:  $initialScreen.toString()}")
        when (initialScreen) {
            InitialScreen.CALENDER_DATE -> loadFragment(CalendarDayFragment())
            InitialScreen.CALENDER_WEEK -> loadFragment(WeekFragment())
            InitialScreen.CALENDER_MONTH -> loadFragment(MonthFragment())
            InitialScreen.CALENDER_APPOINTMENTS -> loadFragment(AppointmentsFragment())
            InitialScreen.TODO_FRAGMENT -> loadFragment(TodoFragment())
            InitialScreen.NEW_DAY_CALENDER -> {
                println("NEW DAY CALENDAR")
            }
        }
    }
    private fun openWebPage(url: String) {
        Logger.log("...openWebPage(String url)", url)
        if (LucindaApplication.appModule.internetWorker.isConnected()) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
            return
        }
        val webPage = url.toUri()
        try {
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        } catch (e: Exception) {
            Logger.log("EXCEPTION, probably not valid ur")
            println("EXCEPTION, probably not valid ur $e")
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