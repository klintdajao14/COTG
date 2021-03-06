package com.example.gittest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class ProductDesc extends AppCompatActivity {

    ImageView imgProd;
    TextView txtProdName, txtProdDesc, txtProdPrice, txtSeller, txtProdId;
    DatabaseHelper db;
    String prodImgURI, prodName, prodDesc;
    Double prodPrice;
    ProductInfo p;
    VendorInfo v;
    File imgFile;
    Button btnAddProd, btnVisitStore;
    int count=0;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_desc);
        Intent intent = getIntent();
        p = new ProductInfo();
        v = new VendorInfo();
        int userid = intent.getIntExtra("prodID_key", 0);
        Log.d(TAG, "onCreate: hello"+userid);
        db = new DatabaseHelper(this);
        p =  db.readProduct(userid);
        prodDesc = p.getProdDesc();
        prodName = p.getProdName();
        prodPrice = p.getProdPrice();
        prodImgURI = p.getProdImg();

        v = db.readVendor(p.getVendorID());

        imgProd = findViewById(R.id.prodIMG);
        txtSeller = findViewById(R.id.txtSeller);
        txtProdName = findViewById(R.id.txt_prodName);
        txtProdDesc = findViewById(R.id.txt_prodDesc);
        txtProdPrice = findViewById(R.id.txt_prodPrice);

        txtSeller.setText(v.getName());
        txtProdName.setText(prodName);
        txtProdPrice.setText(prodPrice.toString());
        txtProdDesc.setText(prodDesc);


        imgFile = new File(String.valueOf(prodImgURI));
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imgProd.setImageBitmap(bitmap);

        btnAddProd = findViewById(R.id.btnAddProd3);
        btnVisitStore = findViewById(R.id.btnVisitStore);

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = db.checkOrderQuantity(userid, loginID.id);
                count++;
                if (count <= 1) {
                    db.addToCart(userid, count, loginID.id);
                    Toast.makeText(ProductDesc.this, "Ordered this " + count + " time!", Toast.LENGTH_SHORT).show();
                } else {
                    db.updateOrder(loginID.id,userid, count);
                    Toast.makeText(ProductDesc.this, "Ordered this " + count + " times!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVisitStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDesc.this, StoreInfo.class);
                i.putExtra("prodID_Key", userid);
                Log.d("ProductDesc.java: ", "Product ID: " + userid);
                startActivity(i);
            }
        });




    }
}