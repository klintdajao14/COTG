package com.example.gittest.ui.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gittest.AccountInfo;
import com.example.gittest.DatabaseHelper;
import com.example.gittest.EditAccount;
import com.example.gittest.EditAccountChangePassword;
import com.example.gittest.Home;
import com.example.gittest.Login;
import com.example.gittest.R;
import com.example.gittest.loginID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment2 extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment2 newInstance(String param1, String param2) {
        AccountFragment2 fragment = new AccountFragment2();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        TextView txtEmail = (TextView) view.findViewById(R.id.txtAccountEmail);
        TextView txtName = (TextView) view.findViewById(R.id.txtAccountName);
        TextView edit = (TextView) view.findViewById(R.id.txtEditProfile);
        TextView delete = (TextView) view.findViewById(R.id.txtDelete);
        AccountInfo a = new AccountInfo();
        DatabaseHelper db = new DatabaseHelper(getActivity());
        a = db.readUser(loginID.id);
        String name = a.getFn() +" " + a.getLn();

        txtEmail.setText(a.getEmail());
        txtName.setText(name);
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        switch (view.getId()){
            case R.id.txtEditProfile:
                Intent intent = new Intent(getActivity(), EditAccount.class);
                startActivity(intent);
                break;
            case R.id.txtDelete:
                boolean delete = db.deleteUser(loginID.id);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure u want to delete your account??")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(delete){
                                    Toast.makeText(getActivity(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(getActivity(), Login.class);
                                    startActivity(in);
                                }
                            }
                        }).setNegativeButton("no",null);

                AlertDialog ad = builder.create();
                ad.show();
                break;
        }
    }
}