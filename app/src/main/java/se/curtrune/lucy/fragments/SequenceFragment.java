package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.SequenceAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.DurationDialog;
import se.curtrune.lucy.screens.util.Converter;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.DurationWorker;
import se.curtrune.lucy.workers.ItemsWorker;

public class SequenceFragment extends Fragment implements SequenceAdapter.Callback{

    private TextView textViewParentHeading;
    private TextView textViewEstimatedTotalDuration;
    private TextView textViewEstimatedEnergy;
    //private TextView textViewInfo;
    //private CheckBox checkBoxState;
    private static final String PARENT_ITEM = "PARENT_ITEM";
    private TextView textViewNumberItems;
    private Item parentItem;
    private Item currentItem;
    private int currentItemIndex = 0;
    private List<Item> items;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager layoutManager;
    private SequenceAdapter adapter;
    private LucindaViewModel viewModel;
    public static boolean VERBOSE = false;
    public SequenceFragment(){
        log("...SequenceFragment() constructor");
    }
    public SequenceFragment(Item parentItem){
        assert  parentItem != null;
        log("SequenceFragment(Item)");
        this.parentItem = parentItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        log("SequenceFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.sequence_fragment, container, false);
        initComponents(view);
        initSequence();
        initRecycler();
        initViewModel();
        setUserInterface();
        return view;
    }

/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_activity);
        log("SequenceActivity.onCreate(Bundle)");
        Intent intent = getIntent();

        if ( savedInstanceState != null){
            log("...savedInstanceState != null");
            parentItem = (Item) savedInstanceState.getSerializable(PARENT_ITEM);
        }else{
            parentItem = (Item) intent.getSerializableExtra(Constants.INTENT_SEQUENCE_PARENT);
            if( parentItem == null){
                log("ERROR iam deeply sorry but parentItem is null, i surrender");
                Toast.makeText(this, "no parentItem", Toast.LENGTH_LONG).setMentalType();
                return;
            }
        }
        initComponents();
        initSequence();
        initRecycler();
        initListeners();
        setUserInterface();
    }*/
    private int calculateEnergy(){
        return  items.stream().mapToInt(Item::getEnergy).sum();
    }
    private void initSequence(){
        log("...initSequence()");
        items = ItemsWorker.selectChildren(parentItem, getContext());
        items.sort(Comparator.comparingLong(Item::getTargetTimeSecondOfDay));
        log("...number of items in sequence", items.size());
        if( items.size() < 1){
            Toast.makeText(getContext(), "need at least one item for this thing to make sense", Toast.LENGTH_LONG).show();
            return;
        }
        currentItem = items.get(currentItemIndex);
    }
    private void initComponents(View view){
        log("...initComponents()");
        textViewNumberItems = view.findViewById(R.id.sequenceFragment_nItems);
        textViewParentHeading = view.findViewById(R.id.sequenceFragment_parentItem);
        recycler = view.findViewById(R.id.sequenceFragment_recycler);
        textViewEstimatedTotalDuration = view.findViewById(R.id.sequeFragment_estimatedTotalTime);
        textViewEstimatedEnergy = view.findViewById(R.id.seFragment_estimatedTotalEnergy);
    }
    private void initListeners(){
        log("...initListeners()");
/*        checkBoxState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkBoxState.isChecked()){
                    log("...checkBoxState isChecked");
                    setNextItem();
                }
            }
        });*/
    }
    private void initRecycler(){
        log("...initRecycler()");
        adapter = new SequenceAdapter(items, this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recycler);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //log("...dx", dx);
                log("...position",((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition());
            }
        });
    }
    private void initViewModel(){
        if( VERBOSE)  log("...initViewModel()");
        viewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
    }

/*    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        log("...onSaveInstanceState(Bundle)");
        outState.putSerializable(PARENT_ITEM, parentItem);
        super.onSaveInstanceState(outState);
    }*/

    /**
     * init static views, ie the data remains the same
     */
    private void setUserInterface(){
        log("...initUserInterface()");
        String textNumberItems = String.format(Locale.getDefault(), "number of activities in sequence %d", items.size());
        textViewNumberItems.setText(textNumberItems);
        textViewParentHeading.setText(parentItem.getHeading());

        updateUserInterface();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sequence_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

/*    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.sequenceActivity_home){
            startActivity(new Intent(this, DevActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }*/
    private void setNextItem(){
        log("...setNextItem() ", currentItemIndex);
        if( currentItemIndex >= items.size() -1){
            log("...end of the sequence");
            Toast.makeText(getContext(), "congrats you're done", Toast.LENGTH_LONG).show();
            return;
        }
        updateUserInterface();
    }

    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Item)", item.getHeading());
        viewModel.updateFragment(new ItemSessionFragment(item));
    }

    @Override
    public void onEditTime(Item item) {
        log("SequenceActivity.onEditTime(Item)");
        LocalTime rightNow = LocalTime.now();
        int minutes = rightNow.getMinute();
        int hour = rightNow.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            LocalTime targetTime = LocalTime.of(hourOfDay, minute);
            item.setTargetTime(targetTime);
            ItemsWorker.update(item, getContext());
            items.sort(Comparator.comparingLong(Item::getTargetTimeSecondOfDay));
            adapter.notifyDataSetChanged();
        }, hour, minutes, true);
        timePicker.show();
    }

    @Override
    public void onEditDuration(Item item) {
        log("...onEditDuration(Item)");
        DurationDialog dialog = new DurationDialog(Duration.ofSeconds(0), duration->{
            log("...onDurationDialog(Duration)");
            item.setDuration(duration.getSeconds());
            ItemsWorker.update(item, getContext());
            adapter.notifyDataSetChanged();
            updateUserInterface();
        });
        dialog.show(getChildFragmentManager(), "edit duration");
    }

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick(Item)");
    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckboxClicked(Item, boolean)", checked);
        item.setState(checked ? State.DONE: State.TODO);
        int rowsAffected = ItemsWorker.update(item, getContext());
        if( rowsAffected != 1){
            log("ERROR, error updating state of item");
        }
        updateUserInterface();
    }
    private void update(Item item){
        log("...update(Item)", item.getHeading());
        ItemsWorker.update(item, getContext());
        items.sort(Comparator.comparingLong(Item::getTargetTimeSecondOfDay));
        adapter.notifyDataSetChanged();
    }

    /**
     * update the dynamic parts of the ui, ie number done items
     * */
    private void updateUserInterface(){
        log("...updateInterface()");
        long numberDone  = items.stream().filter(item->item.isDone()).count();
        String textInfo = String.format(Locale.getDefault(), "%d/%d done", numberDone, items.size());
        textViewNumberItems.setText(textInfo);
        long seconds = DurationWorker.calculateEstimate(items);
        String textEstimatedDuration = String.format(Locale.getDefault() ,"estimated total duration %s", Converter.formatSecondsWithHours(seconds));
        textViewEstimatedTotalDuration.setText(textEstimatedDuration);
        String textEstimatedEnergy = String.format(Locale.getDefault(), "estimated total energy %d",calculateEnergy());
        textViewEstimatedEnergy.setText(textEstimatedEnergy);
    }
}