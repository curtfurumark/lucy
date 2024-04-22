package se.curtrune.lucy.activities.economy;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.economy.classes.Transaction;
import se.curtrune.lucy.activities.economy.workers.TransactionWorker;
import se.curtrune.lucy.adapters.TransactionAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {
    private TransactionAdapter adapter;
    private Transaction currentTransaction;
    private EditText editTextDescription;
    private EditText editTextAmount;
    private TextView textViewDate;
    private RecyclerView recycler;
    private LocalDate date = LocalDate.now();
    private String account;
    private Transaction.Type type;
    private List<Transaction> transactions;
    public static boolean VERBOSE = true;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("TransactionFragment.onCreate(...)");
        View view = inflater.inflate(R.layout.transactions_fragment, container, false);
        setHasOptionsMenu(true);
        initDefaults();
        initComponents(view);
        initListeners();
        transactions = TransactionWorker.selectTransactions(getContext());
        initRecycler(transactions);
        return view;
    }
    private Transaction getTransaction(){
        log("...getTransaction()");
        currentTransaction = new Transaction();
        currentTransaction.setDescription(editTextDescription.getText().toString());
        currentTransaction.setAmount(Float.parseFloat(editTextAmount.getText().toString()));
        currentTransaction.setDate(date);
        return currentTransaction;

    }
    private void initComponents(View view){
        log("...initComponents()");
        editTextDescription = view.findViewById(R.id.transactionActivity_goods);
        editTextAmount = view.findViewById(R.id.economyActivity_amount);
        textViewDate= view.findViewById(R.id.transactionActivity_date);
        textViewDate.setText(LocalDate.now().toString());
/*        spinnerAccounts = findViewById(R.id.economyActivity_accounts);
        spinnerTypes = findViewById(R.id.economyActivity_types);*/
        recycler = view.findViewById(R.id.transactionActivity_recycler);
    }
    private void initDefaults(){
        log("...initDefaults()");
        date = LocalDate.now();

    }
    private void initListeners(){
        log("...initListeners()");
        textViewDate.setOnClickListener(view->showDatePickerDialog());

    }
    private void initRecycler(List<Transaction> items){
        if( VERBOSE) log("...initRecycler(List<Listable>)", items.size());
        adapter = new TransactionAdapter(items, new TransactionAdapter.Callback() {
            @Override
            public void onItemClick(Transaction transaction) {
                log("...onItemClick(Transaction)");
                currentTransaction = transaction;
                setUserInterface(transaction);
            }

            @Override
            public void onItemLongClick(Transaction item) {
                log("...onItemLongClick(Transaction=");
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)", item.getTitle().toString());
        if( item.getItemId() == R.id.economyActivity_save){
            saveTransaction();
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetUserInterface(){
        log("...resetUserInterface()");
        date = LocalDate.now();
        textViewDate.setText(date.toString());
        editTextAmount.setText("");
        editTextDescription.setText("");

    }
    private void saveTransaction(){
        log("...saveTransaction()");
        if( !validateInput()){
            return;
        }
        currentTransaction = getTransaction();
        currentTransaction = TransactionWorker.insert(currentTransaction, getContext());
        transactions.add(currentTransaction);
        adapter.notifyDataSetChanged();
        resetUserInterface();
    }
    private void setUserInterface(Transaction transaction){
        log("...setUserInterface(Transaction)");
        editTextAmount.setText(String.valueOf(transaction.getAmount()));
        editTextDescription.setText(transaction.getDescription());
    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                log("...onDateSet(DatePicker, year,month, dayOfMonth)");
                date = LocalDate.of(year, month +1 , dayOfMonth);
                textViewDate.setText(date.toString());
            }
        });
        datePickerDialog.show();
    }
    private boolean validateInput(){
        if( editTextDescription.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "missing description", Toast.LENGTH_LONG).show();
            return false;
        }
        if( editTextAmount.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "missing amount", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}