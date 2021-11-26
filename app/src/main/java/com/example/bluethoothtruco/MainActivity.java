package com.example.bluethoothtruco;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    CheckBox botonenable, botonvisible;
    ImageView botonbuscar;
    TextView nombre_bt;
    ListView listaView;

    private BluetoothAdapter adaptadorBluetooth;
    private Set<BluetoothDevice> emparejardispositivos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonenable = findViewById(R.id.botonenable);
        botonvisible = findViewById(R.id.botonvisible);
        botonbuscar = findViewById(R.id.botonbuscar);

        nombre_bt = findViewById(R.id.nombrebluetooth);
        nombre_bt.setText(getLocalBluetoothName());

        listaView = findViewById(R.id.lista_view);

        adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (adaptadorBluetooth == null) {
            Toast.makeText(this, "Bluetooth no soportado", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (adaptadorBluetooth.isEnabled()) {
            botonenable.setChecked(true);
        }

        botonenable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton vistaBoton, boolean seleccionado) {
                if (!seleccionado) {
                    adaptadorBluetooth.disable();
                    Toast.makeText(MainActivity.this, "Apagado", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentencendido = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intentencendido, 0);
                    Toast.makeText(MainActivity.this, "Encendido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton vistaBoton, boolean seleccionado) {
                if (seleccionado) {
                    Intent volversevisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(volversevisible, 0);
                    Toast.makeText(MainActivity.this, "Visible por 2 minutos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list();
            }
        });
    }

    public void list() {
        emparejardispositivos = adaptadorBluetooth.getBondedDevices();

        ArrayList lista = new ArrayList();

        for (BluetoothDevice dispositivoBluetooth : emparejardispositivos) {
            lista.add(dispositivoBluetooth.getName());
        }

        Toast.makeText(this, "Mostrando dispositivos", Toast.LENGTH_SHORT).show();
        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        listaView.setAdapter(adaptador);
    }

    public String getLocalBluetoothName(){
        if (adaptadorBluetooth == null){
            adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();
        }
        String nombre = adaptadorBluetooth.getName();
        if(nombre == null){
            nombre = adaptadorBluetooth.getAddress();
        }
        return nombre;
    }
}