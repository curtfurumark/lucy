package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ActionAdapter;
import se.curtrune.lucy.classes.Action;
import se.curtrune.lucy.classes.Categories;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.workers.CategoryWorker;


public class AddItemDialog extends BottomSheetDialogFragment {
    private EditText editText_heading;
    private TextView textViewParentList;

    private String heading;
    private Button buttonSave;
    private Button buttonDismiss;
    private String category;
    private Categories categories;
    private LocalTime targetTime;
    private ActionAdapter actionAdapter;
    private RecyclerView actionRecycler;

    private Action currentAction;
    public static boolean VERBOSE = true;
    private Item parent;
    private Item newItem;
    public interface Callback{
        void onAddItem(Item item);
    }

    private Callback listener;

    public AddItemDialog(Item parent) {
        log("AddItemDialog(Item parent)");
        assert  parent != null;
        this.parent = parent;
        this.newItem = createNewItem(parent);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_item_dialog, container, false);
        initComponents(view);
        initActionRecycler();
        initListeners();
        initDefaults();
        editText_heading.setText(heading);
        initUserInterface(parent);

        return view;
    }
    private Item createNewItem(Item parent){
        log("...createNewItem(Item)");
        newItem = new Item();
        newItem.setParentId(parent.getID());
        newItem.setCategory(parent.getCategory());
        return newItem;
    }
    private List<Action> getActionList(){
        log("...getActionList()");
        Action notification = new Action();
        notification.setTitle("notification");
        notification.setType(Action.Type.NOTIFICATION);
        Action repeat = new Action();
        repeat.setTitle("repeat");
        repeat.setType(Action.Type.REPEAT);
        Action category = new Action();
        category.setTitle("category");
        category.setType(Action.Type.CATEGORY);
        Action time = new Action();
        time.setTitle("time");
        time.setType(Action.Type.TIME);
        ArrayList<Action> actionList = new ArrayList<>();
        actionList.add(repeat);
        actionList.add(notification);
        actionList.add(category);
        actionList.add(time);
        return actionList;
    }

    private Item getItem(){
        log("...getItem()");
        //Item item = new Item();
        newItem.setHeading(editText_heading.getText().toString());
        if( parent != null) {
            log("...parent is not null");
            newItem.setTags(parent.getTags());
            //item.setParentId(parent.getID());
        }else{
            log("...parent is null, project");
            newItem.setParentId(0);
        }
        //item.setState(parent.getState());
        return newItem;

    }
    private void initActionRecycler(){
        log("...initActionRecycler()");
        ActionAdapter.VERBOSE = true;
        actionAdapter = new ActionAdapter(getActionList(), new ActionAdapter.Callback() {
            @Override
            public void onAction(Action action) {
                log("...onAction(Action)", action.getTitle());
                log("...type", action.getType().toString());
                currentAction = action;
                switch (action.getType()){
                    case NOTIFICATION:
                        showNotificationDialog();
                        break;
                    case CATEGORY:
                        showCategoryDialog();
                        break;
                    case TIME:
                        showTimeDialog();
                        break;
                    case REPEAT:
                        showRepeatDialog();
                        break;
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        actionRecycler.setLayoutManager(layoutManager);
        actionRecycler.setItemAnimator(new DefaultItemAnimator());
        actionRecycler.setAdapter(actionAdapter);
        actionAdapter.notifyDataSetChanged();
    }
    private void initComponents(View view){
        log("...initComponents(View)");
        editText_heading = view.findViewById(R.id.addItemDialog_heading);
        buttonSave = view.findViewById(R.id.addItemDialog_buttonOK);
        buttonDismiss = view.findViewById(R.id.addItemDialog_buttonDismiss);
        actionRecycler = view.findViewById(R.id.addItemDialog_actionRecycler);
        textViewParentList = view.findViewById(R.id.addItemDialog_parentList);
    }
    private void initDefaults(){
        log("...initDefaults()");
        //state = State.PENDING;
        categories = new Categories(CategoryWorker.getCategories(getContext()));
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            log("...saveItem()");
            Item item = getItem();
            log(item);
            listener.onAddItem(item);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());

    }

    public void initUserInterface(Item parentItem){
        log("...initUserInterface(Item)");
        String strParentList = String.format(Locale.getDefault(), "%s: %s",getString(R.string.add_to_list), parentItem.getHeading());
        textViewParentList.setText(strParentList);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //this.listener = (Callback) context;
    }
    public void setCallback( Callback callback){
        this.listener = callback;
    }

    public void setHeading(String heading){
        this.heading = heading;
    }
    public void setCategory(String category){
        this.category = category;
    }

    private void showCategoryDialog(){
        log("...showCategoryDialog()");
        ChooseCategoryDialog dialog = new ChooseCategoryDialog(parent.getCategory());
        dialog.setCallback(new ChooseCategoryDialog.Callback() {
            @Override
            public void onSelected(String category) {
                log("...onSelected(String)", category);
                currentAction.setTitle(category);
                newItem.setCategory(category);
                actionAdapter.notifyDataSetChanged();
            }
        });
        dialog.show(getChildFragmentManager(), "choose category");
    }

    public void showNotificationDialog(){
        log("...showNotificationDialog()");
        NotificationDialog dialog = new NotificationDialog(parent);
        dialog.setListener(new NotificationDialog.Callback() {
            @Override
            public void onNotification(Notification notification) {
                log("...onNotification(Notification)");
                currentAction.setTitle(notification.toString());
                newItem.setNotification(notification);
                actionAdapter.notifyDataSetChanged();
            }
        });
        dialog.show(getChildFragmentManager(), "notification ");

    }
    private void showRepeatDialog(){
        log("...showRepeatDialog()");
        RepeatDialog dialog = new RepeatDialog();
        dialog.setCallback(new RepeatDialog.Callback() {
            @Override
            public void onRepeat(Repeat.Period period) {
                log("...onRepeat(Period)", period.toString());
                currentAction.setTitle(period.toString());
                actionAdapter.notifyDataSetChanged();
            }
        });
        dialog.show(getChildFragmentManager(), "repeat");

    }
    private void showTimeDialog(){
        log("...showTimeDialog()");
        LocalTime now = LocalTime.now();
        int minutes = now.getMinute();
        int hour = now.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            targetTime = LocalTime.of(hourOfDay, minute);
            newItem.setTargetTime(targetTime);
            currentAction.setTitle(targetTime.toString());
            actionAdapter.notifyDataSetChanged();
        }, hour, minutes, true);
        timePicker.show();

    }
}
