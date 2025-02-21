package se.curtrune.lucy.screens.item_editor

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.classes.Repeat
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.dialogs.AddItemDialog
import se.curtrune.lucy.dialogs.ChooseCategoryDialog
import se.curtrune.lucy.dialogs.ChooseChildTypeDialog
import se.curtrune.lucy.dialogs.ChooseChildTypeDialog.ChildType
import se.curtrune.lucy.dialogs.DurationDialog
import se.curtrune.lucy.dialogs.NotificationDialog
import se.curtrune.lucy.dialogs.RepeatDialog
import se.curtrune.lucy.dialogs.TagsDialog
import se.curtrune.lucy.screens.EditableListFragment
import se.curtrune.lucy.item_settings.ItemSetting
import se.curtrune.lucy.item_settings.ItemSettingAdapter
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.screens.item_editor.composables.ItemEditorDev
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.util.Converter
import se.curtrune.lucy.util.Constants
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.NotificationsWorker
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import java.util.Objects

class ItemEditorFragment : Fragment {
    private var duration = 0L
    private var editTextHeading: EditText? = null
    private var editTextComment: EditText? = null
    private var textViewDuration: TextView? = null
    private var textViewAnxiety: TextView? = null
    private var textViewStress: TextView? = null
    private var textViewMood: TextView? = null
    private var textViewEnergy: TextView? = null
    private var layoutDev: LinearLayout? = null
    private var buttonSave: Button? = null
    private var buttonTimer: Button? = null
    private var checkBoxIsDone: CheckBox? = null
    private var currentItemSetting: ItemSetting? = null
    private var seekBarEnergy: SeekBar? = null
    private var seekBarAnxiety: SeekBar? = null
    private var seekBarStress: SeekBar? = null
    private var seekBarMood: SeekBar? = null

    private var actionRecycler: RecyclerView? = null
    private var buttonAddItem: FloatingActionButton? = null
    private var currentItem: Item? = null
    private var targetTime: LocalTime? = null
    private var lucindaViewModel: MainViewModel? = null
    private var itemSessionViewModel: ItemSessionViewModel? = null
    private var itemSettingAdapter: ItemSettingAdapter? = null

    constructor()
    constructor(item: Item) {
        println("ItemEditorFragment(${item.heading})")
        this.currentItem = item
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.log("ItemEditorFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)")
        val view = inflater.inflate(R.layout.item_session_fragment, container, false)
        initViewModel()
        initComponents(view)
        initListeners()
        initItemSettingRecycler()
        setUserInterface(currentItem!!)
        initContent(view)
        return view
    }

    private fun formatMental(label: String, value: Int): String {
        return String.format(Locale.getDefault(), "%s: %d", label, value)
    }
    private fun initContent(view: View){
        println("....initContent()")
        val composeView = view.findViewById<ComposeView>(R.id.itemEditor_composeView)
        composeView.setContent {
            LucyTheme {
                var visible by remember{
                    mutableStateOf(false)
                }
                Text(
                    modifier = Modifier.clickable {
                        visible = !visible
                    },
                    text = "advanced",
                    color = Color.White,
                    fontSize = 24.sp)
                AnimatedVisibility(visible = visible) {
                    currentItem?.let { ItemEditorDev(item = it, onEvent = { event->
                        println("on event: ${event.toString()}")
                        itemSessionViewModel!!.onEvent(event)
                    }) }
                }
            }
        }
    }

    private fun initItemSettingRecycler() {
        Logger.log("....initItemSettingRecycler")
        itemSettingAdapter = ItemSettingAdapter(
            itemSessionViewModel!!.getItemSettings(currentItem!!, requireContext())
        ) { setting ->
            Logger.log("...onClick(ItemSetting)", setting.toString())
            currentItemSetting = setting
            when (setting.key) {
                ItemSetting.Key.IS_CALENDAR_ITEM -> {
                    currentItem!!.setIsCalenderItem(setting.isChecked)
                    itemSessionViewModel!!.onEvent(
                        ItemEvent.Update(
                            currentItem!!
                        )
                    )
                }

                ItemSetting.Key.CATEGORY -> showCategoryDialog()
                ItemSetting.Key.COLOR -> showColorDialog()
                ItemSetting.Key.DURATION -> showDurationDialog()
                ItemSetting.Key.NOTIFICATION -> showNotificationDialog()
                ItemSetting.Key.REPEAT -> showRepeatDialog()
                ItemSetting.Key.TIME -> showTimeDialog()
                ItemSetting.Key.DATE -> showDateDialog()
                ItemSetting.Key.TEMPLATE -> {
                    //itemSessionViewModel.setIsTemplate(setting.isChecked(), getContext());
                    currentItem!!.setIsTemplate(setting.isChecked)
                    itemSessionViewModel!!.onEvent(
                        ItemEvent.Update(
                            currentItem!!
                        )
                    )
                }

                ItemSetting.Key.PRIORITIZED -> {
                    currentItem!!.priority = if (currentItemSetting!!.isChecked) 1 else 0
                    itemSessionViewModel!!.onEvent(
                        ItemEvent.Update(
                            currentItem!!
                        )
                    )
                }

                ItemSetting.Key.TAGS -> showTagsDialog()
                else -> Logger.log("unknown SETTING", setting.toString())
            }
        }
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        actionRecycler!!.layoutManager = layoutManager
        actionRecycler!!.itemAnimator = DefaultItemAnimator()
        actionRecycler!!.adapter = itemSettingAdapter
        itemSettingAdapter!!.notifyDataSetChanged()
    }

    private fun initMental() {
        Logger.log("...initMental()")
        Logger.log(itemSessionViewModel!!.mental)
        seekBarStress!!.progress =
            itemSessionViewModel!!.stress + Constants.STRESS_OFFSET
        setMentalLabel(itemSessionViewModel!!.stress, Mental.Type.STRESS)
        seekBarMood!!.progress =
            itemSessionViewModel!!.mood + Constants.MOOD_OFFSET
        setMentalLabel(itemSessionViewModel!!.mood, Mental.Type.MOOD)
        seekBarEnergy!!.progress =
            itemSessionViewModel!!.energy + Constants.ENERGY_OFFSET
        setMentalLabel(itemSessionViewModel!!.energy, Mental.Type.ENERGY)
        seekBarAnxiety!!.progress =
            itemSessionViewModel!!.anxiety + Constants.ANXIETY_OFFSET
        setMentalLabel(itemSessionViewModel!!.anxiety, Mental.Type.ANXIETY)
    }

    private fun initViewModel() {
        if (VERBOSE) Logger.log("...initViewModel()")
        lucindaViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        itemSessionViewModel = ViewModelProvider(requireActivity())[ItemSessionViewModel::class.java]
        itemSessionViewModel!!.init(currentItem!!, context)
        //mentalViewModel = new ViewModelProvider(requireActivity()).get(MentalViewModel.class);
    }



    private fun initComponents(view: View) {
        if (VERBOSE) Logger.log("...initComponents()")
        editTextHeading = view.findViewById(R.id.itemSessionFragment_heading)
        editTextComment = view.findViewById(R.id.itemSessionFragment_comment)
        checkBoxIsDone = view.findViewById(R.id.itemSessionFragment_checkboxIsDone)
        actionRecycler = view.findViewById(R.id.itemSessionFragment_actionRecycler)
        buttonTimer = view.findViewById(R.id.itemSessionFragment_buttonTimer)
        textViewDuration = view.findViewById(R.id.itemSessionFragment_textViewDuration)
        buttonSave = view.findViewById(R.id.itemSessionFragment_buttonSave)
        layoutDev = view.findViewById(R.id.itemSessionFragment_layoutDev)
        buttonAddItem = view.findViewById(R.id.itemSessionFragment_buttonAdd)
        seekBarEnergy = view.findViewById(R.id.itemSessionFragment_seekBarEnergy)
        seekBarStress = view.findViewById(R.id.itemSessionFragment_seekBarStress)
        seekBarAnxiety = view.findViewById(R.id.itemSessionFragment_seekBarAnxiety)
        seekBarMood = view.findViewById(R.id.itemSessionFragment_seekBarMood)
        textViewAnxiety = view.findViewById(R.id.itemSessionFragment_labelAnxiety)
        textViewEnergy = view.findViewById(R.id.itemSessionFragment_labelEnergy)
        textViewMood = view.findViewById(R.id.itemSessionFragment_labelMood)
        textViewStress = view.findViewById(R.id.itemSessionFragment_labelStress)
    }

    private fun initListeners() {
        if (VERBOSE) Logger.log("...initListeners()")
        buttonTimer!!.setOnClickListener { view: View? -> toggleTimer() }
        buttonTimer!!.setOnLongClickListener { v: View? ->
            Logger.log("...onLongClick(View) cancel timer please")
            itemSessionViewModel!!onEvent(ItemEvent.CancelTimer)
            buttonTimer!!.text = getString(R.string.ui_start)
            true
        }
        buttonSave!!.setOnClickListener { view: View? -> updateItem() }
        textViewDuration!!.setOnClickListener { view: View? ->
            showDurationDialogActual(
                false
            )
        }
        buttonAddItem!!.setOnClickListener { view: View? ->
            showChooseChildTypeDialog(
                currentItem
            )
        }
        seekBarEnergy!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Logger.log("....seekbar onProgressChanged()", progress)
                if (fromUser) {
                    //int energyChange = progress - startEnergy;
                    if (currentItem!!.isDone) {
                        itemSessionViewModel!!.onEvent(
                            ItemEvent.Update(
                                currentItem!!
                            )
                        )
                    } else {
                        //lucindaViewModel.estimateEnergy(progress - Constants.ENERGY_OFFSET);
                        setMentalLabel(progress - Constants.ENERGY_OFFSET, Mental.Type.ENERGY)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Logger.log("...onStopTrackingTouch(SeekBar)", seekBar.progress)
                currentItem!!.energy = seekBarEnergy!!.progress - 5
                itemSessionViewModel!!.onEvent(
                    ItemEvent.Update(
                        currentItem!!
                    )
                )
            }
        })
        seekBarAnxiety!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setMentalLabel(progress - Constants.ANXIETY_OFFSET, Mental.Type.ANXIETY)
                    //lucindaViewModel.estimateAnxiety(progress - Constants.ANXIETY_OFFSET, getContext());
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Logger.log("seekBarAnxiety on startTracking(SeekBar)", seekBar.progress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Logger.log("seekBarAnxiety.onStopTrackingTouch(SeekBar)", seekBar.progress)
                currentItem!!.anxiety = seekBar.progress - 5
                itemSessionViewModel!!.onEvent(
                    ItemEvent.Update(
                        currentItem!!
                    )
                )
            }
        })
        seekBarMood!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    //lucindaViewModel.estimateMood(progress - Constants.MOOD_OFFSET, getContext());
                    setMentalLabel(progress - Constants.MOOD_OFFSET, Mental.Type.MOOD)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                currentItem!!.mood = seekBar.progress - 5
                itemSessionViewModel!!.onEvent(
                    ItemEvent.Update(
                        currentItem!!
                    )
                )
            }
        })
        seekBarStress!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    //lucindaViewModel.estimateStress( progress - Constants.STRESS_OFFSET , getContext());
                    setMentalLabel(progress - Constants.STRESS_OFFSET, Mental.Type.STRESS)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                currentItem!!.stress = seekBar.progress - 5
                itemSessionViewModel!!.onEvent(
                    ItemEvent.Update(
                        currentItem!!
                    )
                )
            }
        })
        itemSessionViewModel!!.error.observe(requireActivity()) { error: String? ->
            Logger.log("ItemSessionViewModel.getError(String)", error)
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
        itemSessionViewModel!!.duration.observe(
            requireActivity()
        ) { secs: Long? ->
            if (secs != null) {
                duration = secs
            }
            textViewDuration!!.text =
                Converter.formatSecondsWithHours(
                    secs!!
                )
        }
        itemSessionViewModel!!.timerState.observe(
            requireActivity()
        ) { value -> Logger.log("...getTimerState.onChanged(State)", value.toString()) }
    }
    private val item: Item?
        /**
         * TODO, use the itemSessionViewModel
         * @return
         */
        get() {
            println("...getItem()")
            currentItem!!.heading = editTextHeading!!.text.toString()
            currentItem!!.comment = editTextComment!!.text.toString()
            currentItem!!.duration = duration
            println("...setting duration to ${Converter.formatSecondsWithHours(duration)}")
            currentItem!!.state = if (checkBoxIsDone!!.isChecked) State.DONE else State.TODO
            //TODO, this feels like a hack
            //currentItem!!.duration = itemSessionViewModel!!.duration.value!!
            return currentItem
        }

    private fun returnToPreviousFragment() {
        Logger.log("...returnToPreviousFragment()")
        parentFragmentManager.popBackStackImmediate()
    }

    private fun setUserInterface(item: Item) {
        println("...setUserInterface(${item.heading})")
        itemSessionViewModel!!.item
        editTextHeading!!.setText(item.heading)
        editTextComment!!.setText(item.comment)
        checkBoxIsDone!!.isChecked = item.isDone
        textViewDuration!!.text =
            Converter.formatSecondsWithHours(item.duration)
        println("Lucinda.Dev ${UserPrefs.isDevMode(requireContext())}")
        initMental()
    }

    private fun showChooseChildTypeDialog(item: Item?) {
        Logger.log("...showChooseChildTypeDialog(Item)")
        val dialog = ChooseChildTypeDialog(item) { childType: ChildType ->
            Logger.log(
                "...ChooseChildTypeDialog.onClick(ChildType)",
                childType.toString()
            )
            when (childType) {
                ChildType.CHILD -> showAddChildItemDialog()
                ChildType.LIST -> lucindaViewModel!!.updateFragment(
                    EditableListFragment(
                        currentItem
                    )
                )
                ChildType.PHOTOGRAPH ->                     //Toast.makeText(getContext(), "NOT IMPLEMENTED", Toast.LENGTH_LONG).show();
                    //takePhoto();
                    openCameraAndSaveImage()
            }
        }
        dialog.show(childFragmentManager, "choose type")
    }

    private fun showAddChildItemDialog() {
        Logger.log("...showAddChildItemDialog()")
        val dialog = AddItemDialog(currentItem, false)
        dialog.setCallback { item: Item? ->
            val itemWithId = ItemsWorker.insertChild(currentItem, item, context)
            if (VERBOSE) Logger.log(itemWithId)
            returnToPreviousFragment()
        }
        dialog.show(requireActivity().supportFragmentManager, "add child")
    }

    private fun showCategoryDialog() {
        Logger.log("...showCategoryDialog()")
        val dialog = ChooseCategoryDialog(currentItem!!.category)
        dialog.setCallback { category: String? ->
            Logger.log("...onSelected(String)", category)
            currentItemSetting!!.value = category
            currentItem!!.category = category
            itemSessionViewModel!!.onEvent(
                ItemEvent.Update(
                    currentItem!!
                )
            )
            itemSettingAdapter!!.notifyDataSetChanged()
        }
        dialog.show(childFragmentManager, "choose category")
    }

    private fun showColorDialog() {
        Logger.log("...showColorDialog()")
        //newItem.setColor(Color.RED);
        ColorPickerDialog.Builder(context)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton(getString(R.string.ok),
                ColorEnvelopeListener { envelope, fromUser ->
                    Logger.log("...onColorSelected(ColorEnvelope, boolean)")
                    //currentItemSetting.setColor(envelope.getColor());
                    currentItemSetting!!.value = envelope.color.toString()
                    currentItem!!.color = envelope.color
                    itemSettingAdapter!!.notifyDataSetChanged()
                })
            .setNegativeButton(
                getString(R.string.dismiss)
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(true) // the default value is true.
            .attachBrightnessSlideBar(true) // the default value is true.
            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
            .show()
    }

    private fun showDateDialog() {
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            Logger.log("DatePickerDialog.onDateSet(...)")
            val targetDate = LocalDate.of(year, month + 1, dayOfMonth)
            Logger.log("...date", targetDate.toString())
            currentItemSetting!!.value = targetDate.toString()
            currentItem!!.targetDate = targetDate
            itemSessionViewModel!!.onEvent(
                ItemEvent.Update(
                    currentItem!!
                )
            )
            //itemSessionViewModel.update(currentItem, getContext());
            itemSettingAdapter!!.notifyDataSetChanged()
        }
        datePickerDialog.show()
    }

    private fun showDurationDialog() {
        Logger.log("...showDurationDialog()")
        val dialog = DurationDialog(
            Duration.ofSeconds(0)
        ) { duration ->
            Logger.log("...onDurationDialog(Duration)")
            //itemSessionViewModel.setDuration(duration, getContext());
            currentItem!!.duration = duration.seconds
            currentItemSetting!!.value =
                Converter.formatSecondsWithHours(duration.seconds)
            itemSessionViewModel!!.onEvent(
                ItemEvent.Update(
                    currentItem!!
                )
            )
            itemSettingAdapter!!.notifyDataSetChanged()
        }
        dialog.show(childFragmentManager, "duration")
    }

    private fun showDurationDialogActual(durationEstimate: Boolean) {
        Logger.log("...showDurationDialog()")
        val item = itemSessionViewModel!!.item
        val dialog = DurationDialog(
            Duration.ofSeconds(item!!.duration)
        ) { duration: Duration ->
            println("DurationDialog.onDurationDialog(${duration.seconds})")
            this.duration = duration.seconds
            textViewDuration!!.text =
                Converter.formatSecondsWithHours(duration.seconds)
            buttonTimer!!.text = getString(R.string.ui_resume)
            currentItem!!.duration = duration.seconds
            itemSessionViewModel!!.setElapsedDuration(duration.seconds)
            itemSessionViewModel!!.onEvent(
                ItemEvent.Update(
                    currentItem!!
                )
            )
        }
        dialog.show(childFragmentManager, "actual duration")
    }


    private fun showNotificationDialog() {
        Logger.log("...showNotificationDialog()")
        val dialog = NotificationDialog(currentItem)
        dialog.setListener { notification, action ->
            Logger.log(
                "...onNotification(Notification, Action)",
                action.toString()
            )
            when (action) {
                NotificationDialog.Action.INSERT -> {
                    currentItemSetting!!.value = notification.toString()
                    currentItem!!.notification = notification
                    itemSettingAdapter!!.notifyDataSetChanged()
                    NotificationsWorker.setNotification(currentItem, context)
                }

                NotificationDialog.Action.EDIT -> {
                    Toast.makeText(context, "edit not implemented", Toast.LENGTH_LONG).show()
                    NotificationsWorker.cancelNotification(currentItem, context)
                    NotificationsWorker.setNotification(currentItem, context)
                    itemSessionViewModel!!.updateNotification(context)
                }

                NotificationDialog.Action.DELETE ->                         //NotificationsWorker.cancelNotification(currentItem, getContext());
                    //Toast.makeText(getContext(), "delete not implemented", Toast.LENGTH_LONG).show();
                    itemSessionViewModel!!.cancelNotification(context)
                null ->{
                    println("notification dialog null")
                }
            }
        }
        dialog.show(childFragmentManager, "set notification ")
    }

    private fun showRepeatDialog() {
        Logger.log("...showRepeatDialog()")
        if (itemSessionViewModel!!.itemHasRepeat()) {
            val repeat = Objects.requireNonNull(
                itemSessionViewModel!!.getCurrentItem().value
            )?.period
            Logger.log("...item has repeat")
            Logger.log(repeat)
        }
        val dialog = RepeatDialog()

        dialog.setCallback { repeat: Repeat ->
            Logger.log("...onRepeat(Unit)", repeat.toString())
            currentItemSetting!!.value = repeat.toString()
            currentItem!!.setRepeat(repeat)
            currentItem!!.setIsTemplate(true)
            //itemSessionViewModel.update(currentItem, getContext());
            itemSessionViewModel!!.onEvent(
                ItemEvent.Update(
                    currentItem!!
                )
            )
            itemSettingAdapter!!.notifyDataSetChanged()
        }
        dialog.show(childFragmentManager, "repeat")
    }

    private fun showTagsDialog() {
        Logger.log("...showTagsDialog()")
        val item = itemSessionViewModel!!.item
        val dialog = TagsDialog(item!!.tags) { tags ->
            Logger.log("...onTags(String)", tags)
            itemSessionViewModel!!.addTags(tags, context)
            currentItemSetting!!.value = tags
            itemSettingAdapter!!.notifyDataSetChanged()
        }
        dialog.show(childFragmentManager, "add/edit tags")
    }

    private fun showTimeDialog() {
        Logger.log("...showTimeDialog()")
        val now = LocalTime.now()
        val minutes = now.minute
        val hour = now.hour
        val timePicker =
            TimePickerDialog(context, { view: TimePicker?, hourOfDay: Int, minute: Int ->
                targetTime = LocalTime.of(hourOfDay, minute)
                currentItemSetting!!.value = targetTime.toString()
                currentItem!!.targetTime = targetTime
                itemSessionViewModel!!.onEvent(
                    ItemEvent.Update(
                        currentItem!!
                    )
                )
                itemSettingAdapter!!.notifyDataSetChanged()
            }, hour, minutes, true)
        timePicker.show()
    }

    private fun toggleTimer() {
        println("...toggleTimer()")
        val state = itemSessionViewModel!!.timerState.value
        println("...current timer state ${state.toString()}")
        when (state) {
            ItemSessionViewModel.TimerState.PENDING -> {
                itemSessionViewModel!!.onEvent(ItemEvent.StartTimer)
                buttonTimer!!.setText(R.string.ui_pause)
            }
            ItemSessionViewModel.TimerState.RUNNING -> {
                itemSessionViewModel!!.onEvent(ItemEvent.PauseTimer)
                buttonTimer!!.setText(R.string.ui_resume)
            }
            ItemSessionViewModel.TimerState.PAUSED -> {
                itemSessionViewModel!!.onEvent(ItemEvent.ResumeTimer)
                buttonTimer!!.setText(R.string.ui_pause)
            }
            null -> {
                println("timer state null, WTF")
            }
        }
    }





    private fun setMentalLabel(energy: Int, type: Mental.Type) {
        if (VERBOSE) Logger.log("...setMentalLabel(int)", energy)
        when (type) {
            Mental.Type.ENERGY -> textViewEnergy!!.text =
                formatMental(getString(R.string.energy), energy)

            Mental.Type.ANXIETY -> textViewAnxiety!!.text =
                formatMental(getString(R.string.anxiety), energy)

            Mental.Type.STRESS -> textViewStress!!.text =
                formatMental(getString(R.string.stress), energy)

            Mental.Type.MOOD -> textViewMood!!.text = formatMental(getString(R.string.mood), energy)
        }
    }

    private fun takePhoto() {
        Logger.log("...takePhoto()")
        askCameraPermission()
    }

    private fun openCameraAndSaveImage() {
        Logger.log("...openCameraAndSaveImage()")
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
            Logger.log("...photoFile path", photoFile.absolutePath)
        } catch (e: IOException) {
            Logger.log("EXCEPTION occurred creating image file")
            e.printStackTrace()
            return
        }
        if (photoFile == null) {
            Logger.log("ERROR no photoFile created")
            return
        }
        val photoUri = FileProvider.getUriForFile(requireContext(), "curtfurumark.se", photoFile)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        cameraIntent.putExtra(Constants.IMAGE_FILE_PATH, photoFile.absolutePath)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun askCameraPermission() {
        Logger.log("...askCameraPermissionU()")
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Logger.log("...camera permission not granted")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            //Toast.makeText(getContext(), "YOU GOT CAMERA PERMISSION", Toast.LENGTH_SHORT).show();
            //dispatchTakePictureIntent();
            openCamera()
            //openCameraAndSaveImage();
        }
    }

    private fun openCamera() {
        Logger.log("...openCamera()")
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        /*        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            log("EXCEPTION occurred creating image file");
            e.printStackTrace();
            return;
        }
        if( photoFile == null){
            log("ERROR no photoFile created");
            return;
        }
        Uri photoUri = FileProvider.getUriForFile(getContext(), "curtfurumark.se", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);*/
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        Logger.log("...createImageFile()")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        val currentPhotoPath = image.absolutePath
        return image
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.log("...onActivityResult(...)")
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    Logger.log("PICTURE TAKEN, DO SOMETHING")
                    // Handle the image taken from the camera
                    val media = MediaContent()
                    val imagePath = data.getStringExtra(Constants.IMAGE_FILE_PATH)
                    media.setFilePath(imagePath)
                    media.setFileType(MediaContent.FileType.IMAGE_JPEG)
                    var item = Item("my first image")
                    item.content = media
                    item = ItemsWorker.insert(item, context)
                    Logger.log("item inserted with id", item.id)
                }
            }
        } else {
            Logger.log("Result code was not OK: ", resultCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //openCamera();
                Toast.makeText(context, "YEAH CAMERA PERMISSION GRANTED", Toast.LENGTH_LONG).show()
            } else {

                Toast.makeText(
                    context,
                    "Camera permission is required to use camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }*/

    /**
     * update item
     */
    private fun updateItem() {
        println("...updateItem() ${currentItem!!.heading}")
        if (!validate()) {
            Logger.log("....item did not validate, i surrender, missing heading?")
            return
        }
        currentItem = item
        itemSessionViewModel!!.onEvent(
            ItemEvent.Update(
                currentItem!!
            )
        )
        itemSessionViewModel!!.onEvent(ItemEvent.CancelTimer)
        requireActivity().supportFragmentManager.popBackStackImmediate()
    }

    private fun validate(): Boolean {
        if (VERBOSE) Logger.log("...validate")
        if (editTextHeading!!.text.toString().isEmpty()) {
            Toast.makeText(context, getString(R.string.missing_heading), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 101
        var VERBOSE: Boolean = false
    }
}