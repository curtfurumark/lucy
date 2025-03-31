package se.curtrune.lucy.screens.economy;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.economy.workers.TransactionWorker;
import se.curtrune.lucy.activities.economy.classes.Transaction;

public class TransactionActivity extends AppCompatActivity{

    //private TransactionAdapter adapter;
    private Transaction currentTransaction;
    private EditText editTextDescription;
    private EditText editTextAmount;
    private TextView textViewDate;
    private RecyclerView recycler;
    private LocalDate date = LocalDate.now();
    private String account;
    private Transaction.Type type;
    public static boolean VERBOSE = true;
    private List<Transaction> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions_fragment);
        setTitle("expenditure");
        log("TransactionActivity.onCreate(Bundle)");
        //setContentView(R.layout.transactions_fragment);
        log("EconomyActivity.onCreate(Bundle)");
        initComponents();
        initListeners();
        initDefaults();
        items = TransactionWorker.selectTransactions(LocalDate.now(), LocalDate.now(), this);
        initRecycler(items);
        initSpinnerTypes();
        initSpinnerAccounts();

    }
/*    private void filterTransactions(){
        log("...filterTransactions()");
        adapter.setList(TransactionWorker.filterTransactions(items, account, type));
        adapter.notifyDataSetChanged();
    }
    private void filterTransactions(LocalDate date){
        log("...filterTransactions(LocalDate)", date.toString());
        adapter.setList(TransactionWorker.filterTransactions(date, items));

    }*/
    private Transaction getTransaction(){
        log("...getTransaction()");
        currentTransaction = new Transaction();
        currentTransaction.setDescription(editTextDescription.getText().toString());
        String strAmount = editTextAmount.getText().toString();
        currentTransaction.setAmount(Float.parseFloat(strAmount));
        //currentTransaction.setAccount(account);
        //currentTransaction.setDate(textViewDate.getText().toString());
        //currentTransaction.setType(type);
        return currentTransaction;

    }
    private void initComponents(){
        log("...initComponents()");
        editTextDescription = findViewById(R.id.transactionActivity_goods);
        editTextAmount = findViewById(R.id.economyActivity_amount);
        textViewDate= findViewById(R.id.transactionFragment_date);
        textViewDate.setText(LocalDate.now().toString());
/*        spinnerAccounts = findViewById(R.id.economyActivity_accounts);
        spinnerTypes = findViewById(R.id.economyActivity_types);*/
        //recycler = findViewById(R.id.transactionActivity_recycler);
        //progressBar = findViewById(R.id.economyActivity_progressBar);

    }
    private void initDefaults(){
        log("...initDefaults()");
/*        account ="bank norwegian";
        type = Transaction.Type.RUNNING;
        date = LocalDate.now();*/

    }
    private void initListeners(){
        log("...initListeners()");
        textViewDate.setOnClickListener(view->showDatePickerDialog());

    }
    private void initRecycler(List<Transaction> items){
        if( VERBOSE) log("...initRecycler(List<Listable>)", items.size());
/*        adapter = new TransactionAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);*/
    }
    private void initSpinnerTypes(){
        log("...initSpinnerTypes()");
/*        //String[] types  = Accountant.getTypes();
        adapterTypes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Transaction.Type.values());
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerTypes.setAdapter(adapterTypes);
        spinnerTypes.setSelection(type.ordinal());
        spinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = adapterTypes.getItem(position);
                filterTransactions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    private void initSpinnerAccounts(){
        log("...initSpinnerAccounts()");
        String[] accounts  = Transaction.getAccounts();
/*        adapterAccounts = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accounts);
        adapterAccounts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerAccounts.setAdapter(adapterAccounts);
        //spinner_types.setSelection(initial_type.ordinal());
        spinnerAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                account = adapterAccounts.getItem(position);
                filterTransactions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        })*/;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.economy_activity, menu);
        return true;
    }

    public void onDateClick(View view){
        showDatePickerDialog();
    }

    public void onItemClick(Transaction item) {
        log("...onItemClick(Transaction) ");
        setUserInterface(item);
    }

    public void onItemLongClick(Transaction item) {

    }


    private void save(){
        log("...save()");
        currentTransaction = getTransaction();
        //RemoteDB.insert(currentTransaction, this);
    }
    private void setUserInterface(){
        log("...setUserInterface()");
        editTextAmount.setText("");
        editTextDescription.setText("");
        textViewDate.setText(date.toString());
        //spinnerTypes.setSelection(Transaction.Type.RUNNING.ordinal());
        //spinnerAccounts.setSelection(Transaction.getAccountOrdinal("bank norwegian"));
    }
    private void setUserInterface(Transaction transaction){
        log("...setUserInterface(Transaction)");
        //log(transaction);
        editTextDescription.setText(transaction.getDescription());
        editTextAmount.setText(String.valueOf(transaction.getAmount()));
        //date = transaction.getLocalDate();
        //textViewDate.setText(transaction.getDate());
        //spinnerAccounts.setSelection(transaction.getAccountOrdinal());
        //spinnerTypes.setSelection(transaction.getType().ordinal());
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                log("...onDateSet(DatePicker, year,month, dayOfMonth)");
                date = LocalDate.of(year, month +1 , dayOfMonth);
                textViewDate.setText(date.toString());
                //filterTransactions(date);
            }
        });
        datePickerDialog.show();
    }
}