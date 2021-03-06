package com.example.gittest.vendorUI.products;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gittest.DatabaseHelper;
import com.example.gittest.ProductDesc_Vendor_AddProduct;
import com.example.gittest.R;
import com.example.gittest.databinding.FragmentProductsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;


public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;
    DatabaseHelper db;
    private ArrayList<Integer> mProdId = new ArrayList<>();
    private ArrayList<String> mProdNames  = new ArrayList<>();
    private ArrayList<Double> mProdPrice = new ArrayList<>();
    private ArrayList<Bitmap> mProdImageURI = new ArrayList<>();
    FloatingActionButton fab;
    TextView txtNoProds;
    ImageView imgNoProds;

//    private ArrayList<String> mVendorId = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onStart();
        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = new DatabaseHelper(getActivity());
        String userid = getActivity().getIntent().getStringExtra("userid_key");
        String vendorId = getActivity().getIntent().getStringExtra("vendorId_key");
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);

        txtNoProds = root.findViewById(R.id.txtNoProds);
        imgNoProds = root.findViewById(R.id.imgNoProd);
        fab = root.findViewById(R.id.fab);

        mProdId = db.checkVendorProdIDList(vendorId);
        mProdNames = db.checkVendorProdNameList(vendorId);
        mProdPrice = db.checkVendorProdPriceList(vendorId);

//        mVendorId = db.checkProdVendorId();
        Log.d(TAG, "mProdNames size: " + mProdNames.size());
        Log.d(TAG, "mProdPrice size: " + mProdNames.size());
        Log.d(TAG, "mProdImageURI get(0): " + db.checkProdImgURIList().size());
        Log.d(TAG, "mProdImageURI toString: " + mProdImageURI.toString());
        Log.d(TAG, "initImageBitmaps: creating bitmaps...");
//        int vendorprodcount=0;
//        for (int i=0;i< mVendorId.size();i++){
//            Log.d(TAG, "onCreateView: "+mVendorId.get(i));
//            if(mVendorId.get(i).equals(vendorId)){
//                vendorprodcount++;
//            }
//        }

        Log.d(TAG, "onCreateView: "+vendorId);
//        Log.d(TAG, "onCreateView: "+mVendorId.get(0));
//        Log.d(TAG, "vendorcount: "+vendorprodcount);
        File imgFile[] = new File[mProdNames.size()];
        for(int i = 0; i<mProdNames.size();i++){
            imgFile[i] = new File(String.valueOf(db.checkVendorProdImgURIList(vendorId).get(i)));
            if(imgFile[i].exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile[i].getAbsolutePath());
                mProdImageURI.add(bitmap);
            }
        }
//        mProdImageURI = db.checkProdImgURIList();
        if(mProdNames.size()==0){
            txtNoProds.setVisibility(View.VISIBLE);
            imgNoProds.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "initRecyclerView: init recyclerview called.");
        RecyclerView recyclerView = root.findViewById(R.id.productRecyclerView);
        ProductsFragmentViewAdapter adapter = new ProductsFragmentViewAdapter(root.getContext(), mProdId, mProdNames, mProdPrice, mProdImageURI, userid);
        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(root.getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProdId = db.checkVendorProdIDList(vendorId);
                mProdNames = db.checkVendorProdNameList(vendorId);
                mProdPrice = db.checkVendorProdPriceList(vendorId);

                File imgFile[] = new File[mProdNames.size()];
                for(int i = 0; i<mProdNames.size();i++){
                    imgFile[i] = new File(String.valueOf(db.checkProdImgURIList().get(i)));
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile[i].getAbsolutePath());
                    mProdImageURI.add(bitmap);

                }
                RecyclerView recyclerView = root.findViewById(R.id.productRecyclerView);
                ProductsFragmentViewAdapter adapter = new ProductsFragmentViewAdapter(root.getContext(), mProdId, mProdNames, mProdPrice, mProdImageURI, userid);
                recyclerView.setAdapter(adapter);
                pullToRefresh.setRefreshing(false);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductDesc_Vendor_AddProduct.class);
                intent.putExtra("vendorId_key", vendorId);
                startActivity(intent);
            }
        });
        return root;
    }

    public void onStart(){

        super.onStart();
        //update your fragment
    }
}