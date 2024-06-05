package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Set;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomizeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomizeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextUrl;
    private Button buttonAddUrl;
    private ListView listViewPanicUrls;
    public CustomizeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomizeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomizeFragment newInstance(String param1, String param2) {
        CustomizeFragment fragment = new CustomizeFragment();
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
        log("CustomizeFragment.onCreateView(...)");
         View view = inflater.inflate(R.layout.customize_fragment, container, false);
         initComponents(view);
         initListeners();
         initListView();
         return view;
    }
    private void addPanicUrl(){
        log("...addPanicUrl()");
        User.addPanicUrl(editTextUrl.getText().toString(), getContext());
        editTextUrl.setText("");
        initListView();
    }
    private void initComponents(View view){
        log("...initComponents(View view)");
        editTextUrl = view.findViewById(R.id.customizeFragment_url);
        buttonAddUrl = view.findViewById(R.id.customizeFragment_buttonAddUrl);
        listViewPanicUrls = view.findViewById(R.id.customizeFragment_panicUrls);
    }
    private void initListeners(){
        buttonAddUrl.setOnClickListener(view->addPanicUrl());
    }
    private void initListView(){
        log("...initListView()");
        Set<String> urls = User.getPanicUrls(getContext());
        ArrayAdapter<String> arr = new ArrayAdapter<String>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                urls.toArray(new String[1]));
        listViewPanicUrls.setAdapter(arr);


    }
}