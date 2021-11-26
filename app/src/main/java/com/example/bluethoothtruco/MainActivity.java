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

    //Declaro los componentes que vamos a usar
    CheckBox botonhabilitado, botonvisible;
    ImageView botonbuscar;
    TextView nombre_bt;
    ListView listaView;

    //Declaro un adaptador de bluetooth para comprobar si el servicio Bluetooth esta disponible en el dispositivo.
    //Dentro del archivo Manifest hay que declarar dos permisos adecuados para poder usar el Bluetooth.
    private BluetoothAdapter adaptadorBluetooth;

    //Este objeto de tipo BluetoothDevice permite crear una conexion con el dispositivo respectivo o consultar informacion sobre él,nombre, dirección...
    private Set<BluetoothDevice> emparejardispositivos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asigno las variables a sus respectivos componentes
        botonhabilitado = findViewById(R.id.botonhabilitado);
        botonvisible = findViewById(R.id.botonvisible);
        botonbuscar = findViewById(R.id.botonbuscar);

        nombre_bt = findViewById(R.id.nombrebluetooth);
        //mediante este metodo mostramos el nombre del dispositivo Bluetooth
        nombre_bt.setText(getLocalBluetoothName());

        //Lista donde saldrán los dispositivos
        listaView = findViewById(R.id.lista_view);

        //Aqui comenzamos a usar el adaptador para comprobar si se puede usar el Bluetooth
        adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();

        //Si no es soportado,no se realiza nada
        if (adaptadorBluetooth == null) {
            Toast.makeText(this, "Bluetooth no soportado", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Si esta habilitado el checkbox se marca
        if (adaptadorBluetooth.isEnabled()) {
            botonhabilitado.setChecked(true);
        }

        /**
         * Implementar el metodo OnCheckedListener para mostrar que hacer en cada caso
         * Cuando no está seleccionado el checkbox quiere decir que el bluetooth está apagado(llama al metodo) y muestra un mensaje
         * Si no realiza un intent en el que busca otra vez la accion para habilitar el bluetooth de nuevo y muestra otro mensaje
         * Para esto hay que añadir el permiso "android.permission.BLUETOOTH_CONNECT" al archivo Manifest
         */
        botonhabilitado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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

        /**
         * En caso del checkbox visible, indica si el dispositivo bluetooth esta visible para otrs dispositivos
         * Para que esta accion funcione hay que añadir el permiso "android.permission.BLUETOOTH_ADVERTISE" al archivo Manifest
         */
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