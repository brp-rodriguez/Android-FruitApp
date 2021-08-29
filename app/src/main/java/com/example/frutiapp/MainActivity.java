package com.example.frutiapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_personaje;
    private EditText et_nombre ;
    private TextView et_score ;
    private MediaPlayer mp ;

    int num_aleatorio = (int)(Math.random()*10);
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_personaje = (ImageView)findViewById(R.id.imageView_personaje);
        et_nombre = (EditText)findViewById(R.id.txt_nombre);
        et_score = (TextView) findViewById(R.id.textView_bestScore);

        // Icono en el action bar
       getSupportActionBar().setDisplayShowHomeEnabled(true);
       //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
       getSupportActionBar().setIcon(R.mipmap.ic_launcher_foreground);

        int id;
        if (num_aleatorio==0 || num_aleatorio==10){
            id = getResources().getIdentifier("mango","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }else if (num_aleatorio==1 || num_aleatorio==9){
            id = getResources().getIdentifier("fresa","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }else if (num_aleatorio==2 || num_aleatorio==8){
            id = getResources().getIdentifier("manzana","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }else if (num_aleatorio==3 || num_aleatorio==7){
            id = getResources().getIdentifier("sandia","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }else if (num_aleatorio==4 || num_aleatorio==5 || num_aleatorio == 6 ){
            id = getResources().getIdentifier("uva","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"BD",null,1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select Max(score) from puntaje)",null);

        if(consulta.moveToFirst()){ // Si hay datos ?
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            et_score.setText("Record de " + temp_score + " de " + temp_nombre);
        }else{
            BD.close();
        }

        // Creamos nuestra player en el objeto mp
        mp = MediaPlayer.create(this,R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);
    }

    public void jugar(View view){
        String nombre = et_nombre.getText().toString();
        if (!nombre.equals("")){
            // detener musica
            mp.stop();
            // destruir nuestro objeto para liberar recursos
            mp.release();
            // Ir al otro activity
            Intent intent = new Intent(this,Main2Activity_Nivel1.class);
            // envio de datos
            intent.putExtra("jugador",nombre);
            // iniciamos actividad
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Escribe tu nombre", Toast.LENGTH_SHORT).show();

            // Mantener el focus de escritura
            et_nombre.requestFocus();
            // llamamos al metodo para sacar el teclado
            InputMethodManager imm = (InputMethodManager)getSystemService( this.INPUT_METHOD_SERVICE);
            // Mostramos el teclado
            imm.showSoftInput(et_nombre,InputMethodManager.SHOW_IMPLICIT);
        }
    }


    @Override
    public void onBackPressed(){

    }
}