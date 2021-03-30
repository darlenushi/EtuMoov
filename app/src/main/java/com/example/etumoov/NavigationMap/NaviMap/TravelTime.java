package com.example.etumoov.NavigationMap.NaviMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etumoov.NavigationMap.NaviBD.Universite;
import com.example.etumoov.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TravelTime extends AppCompatActivity {
    private DatabaseReference reference;
    private ListView UniversiteList;
    private ArrayList<String> UnivArray = new ArrayList<>();
    private EditText numero, rue, codePostal, numero2, rue2, codePostal2;
    private Button btn_valide;
    private APIGoogleDistance api;
    private TextView duree;
    private long sec, minutes, hours, days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_time);
        numero = findViewById(R.id.txt_edit_numero);
        rue = findViewById(R.id.txt_edit_rue);
        codePostal = findViewById(R.id.txt_edit_codePostal);
        numero2 = findViewById(R.id.txt_edit_numero2);
        rue2 = findViewById(R.id.txt_edit_rue2);
        codePostal2 = findViewById(R.id.txt_edit_codePostal2);
        btn_valide = findViewById(R.id.button_valider);
        duree = findViewById(R.id.txt_view_trajet_tps);
        api = new APIGoogleDistance();

        ArrayAdapter<String> UnivArrayAdapter = new ArrayAdapter<String>(TravelTime.this, android.R.layout.simple_list_item_1,
                UnivArray);
        reference = FirebaseDatabase.getInstance().getReference("Universite");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Universite universite = dataSnapshot.getValue(Universite.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TravelTime.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
            }
        });

        btn_valide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res;
                res = api.getTravelTime(numero.getText() + " " + rue.getText() + "," + codePostal.getText(), numero2.getText() + " " + rue2.getText() + "," + codePostal2.getText());
                calculateTime(Long.parseLong(res));
                duree.setText(hours + " heures " + minutes + " minutes");
            }
        });
    }

    public void calculateTime(long seconds) {
        sec = seconds % 60;
        minutes = seconds % 3600 / 60;
        hours = seconds % 86400 / 3600;
    }
}


