package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.adapters.CalenderAdapter;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.workers.ItemsWorker;

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
    }
/*    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        return new AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        log("BoostDialog.onClick()");
                        dismiss();
                    }
                }).create();
    }*/

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
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
            }

            @Override
            public void onItemClick(Item item) {
                if (VERBOSE) log("...onItemClick(Item)", item.getHeading());
                Intent intent = new Intent(getContext(), ItemSession.class);
                intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.CALENDER_FRAGMENT);
                intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
                startActivity(intent);
            }

            @Override
            public void onLongClick(Item item) {
                log("...onLongClick(Item)", item.getHeading());
                EditItemDialog dialog = new EditItemDialog(item);
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
                dialog.show(getChildFragmentManager(), "edit item");
            }

            @Override
            public void onCheckboxClicked(Item item, boolean checked) {
                log("...onCheckboxClicked(Item, boolean)");
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }

    private void setUserInterface() {
        textViewDate.setText(date.toString());
    }

}
