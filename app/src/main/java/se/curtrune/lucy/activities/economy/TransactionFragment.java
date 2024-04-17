package se.curtrune.lucy.activities.economy;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.TransactionAdapter;
import se.curtrune.lucy.classes.economy.EcMoney;
import se.curtrune.lucy.classes.economy.Transaction;
import se.curtrune.lucy.workers.TransactionWorker;

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
    private EcMoney.Type type;
    public static boolean VERBOSE = true;
    private List<Transaction> items = new ArrayList<>();

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
        initComponents(view);
        items = TransactionWorker.selectTransactions(LocalDate.now(), LocalDate.now());
        initRecycler(items);
        return view;
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
    private void initRecycler(List<Transaction> items){
        if( VERBOSE) log("...initRecycler(List<Listable>)", items.size());
        adapter = new TransactionAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }
}