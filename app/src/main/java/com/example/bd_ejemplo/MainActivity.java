package com.example.bd_ejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edtCodigo, edtNombre;
    private Button btnInsertar, btnBorrar, btnMostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Helper bd = new Helper(this, "BDUsuarios", null, 1);
        final SQLiteDatabase escribirBD = bd.getWritableDatabase();

        edtCodigo = findViewById(R.id.edtCodigo);
        edtNombre = findViewById(R.id.edtNombre);
        btnInsertar = findViewById(R.id.btnInsertar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnMostrar = findViewById(R.id.btnMostrar);

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int codigo = Integer.parseInt(edtCodigo.getText().toString());
                String nombre = edtNombre.getText().toString();
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("codigo", codigo);
                nuevoRegistro.put("nombre", nombre);

                //Inserción
                long logInsert = escribirBD.insert("usuarios", null, nuevoRegistro);
                if (logInsert != -1) {
                    Log.i("BD", "Inserción realizada correctamente. Codigo: " + logInsert);
                } else{
                    Log.i("BD", "Ya existe un usuario con ese código");
                }

                edtCodigo.setText("");
                edtNombre.setText("");
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int codigo = Integer.parseInt(edtCodigo.getText().toString());

                //Borrado
                int logDelete = escribirBD.delete("usuarios", "codigo = " + codigo, null);
                if (logDelete != 0) {
                    Log.i("BD", "Borrado realizado correctamente. Codigo: " + logDelete);
                } else {
                    Log.i("BD", "No existe ningún registro con ese código");
                }

                edtCodigo.setText("");
            }
        });
        
        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] fields={"codigo", "nombre"};
                Cursor c = escribirBD.query("usuarios", fields, null, null, null, null, null);
                if (c.moveToFirst()) {
                    do {
                        int codigo = c.getInt(0);
                        String nombre = c.getString(1);
                        Toast.makeText(MainActivity.this,
                                "Codigo: " + codigo + "\n" +
                                        "Nombre: " + nombre,
                                Toast.LENGTH_SHORT).show();
                    }while (c.moveToNext());
                } else {
                    Toast.makeText(MainActivity.this, "No hay usuarios", Toast.LENGTH_SHORT).show();
                }
                c.close();
            }
        });
    }
}
