package se.curtrune.lucy.screens.economy;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.economy.classes.Asset;
import se.curtrune.lucy.activities.economy.workers.TransactionWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssetsFragment extends Fragment {
    private RecyclerView recycler;
    //private AssetAdapter adapter;
    private List<Asset> assets;
    private EditText editTextAccount;
    private EditText editTextAmount;
    private TextView textViewDate;
    private LocalDate currentDate;
    private Asset currentAsset;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AssetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssetsFragment newInstance(String param1, String param2) {
        AssetsFragment fragment = new AssetsFragment();
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
        log("AssetsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view =  inflater.inflate(R.layout.assets_fragment, container, false);
        initComponents(view);
        initData();
        initRecycler();
        initListeners();
        setUserInterface();
        setHasOptionsMenu(true);
        return view;
    }
    private Asset getAsset(){
        log("...getAsset()");
        Asset asset = new Asset();
        asset.setAmount(Float.parseFloat(editTextAmount.getText().toString()));
        asset.setDate(currentDate);
        asset.setAccount(editTextAccount.getText().toString());
        return asset;
    }
    private void initComponents(View view){
        log("...initComponents()");
        recycler = view.findViewById(R.id.assetsFragment_recycler);
        editTextAccount = view.findViewById(R.id.assetsFragment_account);
        editTextAmount = view.findViewById(R.id.assetsFragment_amount);
        textViewDate = view.findViewById(R.id.assetsFragment_date);
    }
    private void initData(){
        log("...initData()");
        assets = TransactionWorker.selectAssets(getContext());
        log("...number of assets", assets.size());
        currentDate = LocalDate.now();
    }

    private void initListeners(){
        log("...initListeners()");
        textViewDate.setOnClickListener(view->showDateDialog());

    }
    private void initRecycler(){
        log("...initRecycler()");
/*        adapter = new AssetAdapter(assets, new AssetAdapter.Callback() {
            @Override
            public void onItemClick(Asset asset) {
                log("...onItemClick(Transaction)");
                currentAsset = asset;
                setUserInterface(asset);
            }

            @Override
            public void onItemLongClick(Asset asset) {
                log("...onItemLongClick(Asset)");
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);*/
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.economyActivity_save){
            saveAsset();
        }
        return super.onOptionsItemSelected(item);
    }
    private void saveAsset(){
        log("...saveAsset");
/*        if( !validateInput()){
            return;
        }
        Asset asset = getAsset();
        asset = TransactionWorker.insert(asset, getContext());
        if( asset == null){
            log("ERROR inserting asset");
        }else{
            assets.add(asset);
            adapter.notifyDataSetChanged();
        }*/

    }

    private void setUserInterface(){
        log("...setUserInterface()");
        textViewDate.setText(currentDate.toString());
    }
    private void setUserInterface(Asset asset){
        editTextAmount.setText(String.valueOf(asset.getAmount()));
        editTextAccount.setText(asset.getAccount());
        textViewDate.setText(asset.getDate().toString());
    }
    private void showDateDialog(){
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                log("...onDateSet(DatePicker, year,month, dayOfMonth)");
                currentDate = LocalDate.of(year, month +1 , dayOfMonth);
                textViewDate.setText(currentDate.toString());
            }
        });
        datePickerDialog.show();
    }
    private boolean validateInput(){
        if( editTextAccount.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "missing account", Toast.LENGTH_LONG).show();
            return false;
        }
        if( editTextAmount.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"missing amount", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}