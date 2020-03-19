package com.example.myhome.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhome.Adapter.picture_adapter;
import com.example.myhome.LoginActivity;
import com.example.myhome.MapsActivity;
import com.example.myhome.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    TextView bl_name, bl_phone, bl_address, bl_price, bl_water, bl_electric, bl_P_lot, bl_net, bl_service, bl_detail,bl_city, bl_district, bl_type;
    FloatingActionButton bl_map,bl_note,bl_call;
    RecyclerView bl_recycleview;
    ArrayList<Uri> picture = new ArrayList<Uri>();
    String like, ID;


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_blank, container, false);
        bl_name = view.findViewById(R.id.bl_name);
        bl_phone = view.findViewById(R.id.bl_phone);
        bl_address = view.findViewById(R.id.bl_address);
        bl_price = view.findViewById(R.id.bl_price);
        bl_water = view.findViewById(R.id.bl_water);
        bl_electric = view.findViewById(R.id.bl_electric);
        bl_P_lot = view.findViewById(R.id.bl_P_lot);
        bl_net = view.findViewById(R.id.bl_net);
        bl_service = view.findViewById(R.id.bl_service);
        bl_detail = view.findViewById(R.id.bl_detail);
        bl_city = view.findViewById(R.id.bl_city);
        bl_district = view.findViewById(R.id.bl_district);
        bl_type = view.findViewById(R.id.bl_type);
        bl_map = view.findViewById(R.id.bl_note);
        bl_note = view.findViewById(R.id.bl_note);
        bl_call = view.findViewById(R.id.bl_call);
        bl_recycleview = view.findViewById(R.id.bl_recycleview);

        Bundle b= getArguments();
        if (b != null){
            like=b.getString("like");
            ID=b.getString("id");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.document(like);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        bl_P_lot.setText(documentSnapshot.get("house_P_lot").toString());
                        bl_phone.setText(documentSnapshot.get("house_phone").toString());
                        bl_address.setText(documentSnapshot.get("house_address").toString());
                        bl_price.setText(documentSnapshot.get("house_price").toString());
                        bl_water.setText(documentSnapshot.get("house_water").toString());
                        bl_electric.setText(documentSnapshot.get("house_electric").toString());
                        bl_net.setText(documentSnapshot.get("house_net").toString());
                        bl_service.setText(documentSnapshot.get("house_service").toString());
                        bl_detail.setText(documentSnapshot.get("house_detail").toString());
                        bl_city.setText(documentSnapshot.get("house_city").toString());
                        bl_district.setText(documentSnapshot.get("house_district").toString());
                        bl_type.setText(documentSnapshot.get("house_type").toString());
                        bl_name.setText(documentSnapshot.get("house_owner").toString());
                        ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("house_picture_id");
                        // System.out.println(list.toString());
                        for (String doc:list) {
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            storageRef.child("images/" + doc).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    picture.add(uri);
                                    picture_adapter adapter = new picture_adapter(BlankFragment.this.getContext(), picture);
                                    LinearLayoutManager manager = new LinearLayoutManager(BlankFragment.this.getContext(), LinearLayoutManager.HORIZONTAL, false);
                                    bl_recycleview.setLayoutManager(manager);
                                    bl_recycleview.setAdapter(adapter);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                        bl_map.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(BlankFragment.this.getActivity(),MapsActivity.class);
                                intent.putExtra("address", documentSnapshot.get("house_address") + ","+documentSnapshot.get("house_district") + "," + documentSnapshot.get("house_city") );
                                startActivity(intent);
                            }
                        });
                        bl_call.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View arg0) {
                                try {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:"+documentSnapshot.get("house_phone")));
                                    startActivity(callIntent);
                                } catch (Exception e){
                                    Log.e("Demo application", "Failed to invoke call", e);
                                }
                            }
                        });
                        bl_note.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Bạn đã thêm vào danh sách quan tâm rồi", Toast.LENGTH_SHORT).show();


                            }
                        });
                    }
                }
            });
        }
        return view;
    }

}
