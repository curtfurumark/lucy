package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import se.curtrune.lucy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText editTextSubject;
    private EditText editTextContent;
    private EditText editTextMailAddress;
    private Button buttonSendMail;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
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
        log("ContactFragment.onCreateView(...)");
        View view =  inflater.inflate(R.layout.contact_fragment, container, false);
        initComponents(view);
        initListeners();
        return view;
    }
    private void initComponents(View view){
        log("...initComponents(View)");
        editTextContent = view.findViewById(R.id.contactFragment_content);
        editTextSubject = view.findViewById(R.id.contactFragment_subject);
        buttonSendMail = view.findViewById(R.id.contactFragment_sendMail);
        editTextMailAddress = view.findViewById(R.id.contactFragment_mailAddress);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSendMail.setOnClickListener(view->sendMail());

    }
    private void sendMail(){
        log("...sendMail()");
        if( !validateInput()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto:","", null));
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"curt.r.truc@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, editTextSubject.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, editTextContent.getText().toString());
        intent.setType("message/rfc822");
        //if( intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "choose client"));
            log("...message sent, well not really but you know what i mean");
            //Toast.makeText(getContext(), "message sent", Toast.LENGTH_LONG).show();
        //}else{
        //    log("ERROR, intent.resolveActivity");
        //}
    }
    private boolean validateInput(){
        if( editTextSubject.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "missing subject", Toast.LENGTH_LONG).show();
            return false;
        }
        if( editTextContent.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "missing subject", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}