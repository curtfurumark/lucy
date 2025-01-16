package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.screens.daycalendar.CalenderAdapter;
import se.curtrune.lucy.classes.Item;

/**
 * dialog that shows a list of items supplied to its constructor
 * used by MonthCalender, should be used in WeekCalendar, but is not, at least that's what i thing
 */

public class ItemsDialog extends DialogFragment {
    private TextView textViewDate;
    private Button buttonOK;
    private RecyclerView recycler;
    private CalenderAdapter adapter;
    private List<Item> items;
    private LocalDate date;
    public static boolean VERBOSE = false;

    public ItemsDialog(List<Item> items, LocalDate date) {
        log("ItemsDialog(List<Item>, LocalDate) number of items", items.size());
        this.date = date;
        this.items = items;
        items.forEach(System.out::println);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("ItemsDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.items_dialog, container);
        initComponents(view);
        initListeners();
        initRecycler();
        setUserInterface();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("ItemsDialog.onCreate(Bundle)");
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    public void initComponents(View view) {
        log("...initComponents(View)");
        buttonOK = view.findViewById(R.id.itemsDialog_buttonOK);
        textViewDate = view.findViewById(R.id.itemsDialog_date);
        recycler = view.findViewById(R.id.itemsDialog_recycler);
    }

    private void initListeners() {
        log("...initListeners()");
        buttonOK.setOnClickListener(view -> dismiss());
    }

    private void initRecycler() {
        log("...initRecycler()");
        adapter = new CalenderAdapter(items, new CalenderAdapter.Callback() {
            @Override
            public void onEditTime(Item item) {
                if (VERBOSE) log("...onEditTime(Item item");
                showTimeDialog();
            }

            @Override
            public void onItemClick(Item item) {
                if (VERBOSE) log("...onItemClick(Item)", item.getHeading());
            }

            @Override
            public void onLongClick(Item item) {
                log("...onLongClick(Item)", item.getHeading());
/*                EditItemDialog dialog = new EditItemDialog(item);
                dialog.setCallback(new EditItemDialog.Callback() {
                    @Override
                    public void onUpdate(Item item) {
                        log("...onUpdate(Item)");
                        log(item);
                        int rowsAffected = ItemsWorker.update(item, getContext());
                        if (rowsAffected != 1) {
                            log("ERROR updating item", item.getHeading());
                        } else {
                            log("...item updated");
                            items.sort(Comparator.comparingLong(Item::compareTargetTime));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                dialog.setMentalType(getChildFragmentManager(), "edit item");*/
            }

            @Override
            public void onCheckboxClicked(Item item, boolean checked) {
                log("...onCheckboxClicked(Item, boolean)");
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new DividerItemDecoration(recycler.getContext(), DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);
    }

    private void setUserInterface() {
        log("...setUserInterface()");
        textViewDate.setText(date.toString());
    }
    private void showTimeDialog(){
        log("...showTimeDialog()");

    }

}
