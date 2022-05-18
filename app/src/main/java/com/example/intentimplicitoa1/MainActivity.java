package com.example.intentimplicitoa1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //atributos de sus vistas
    private EditText etTelefono;
    private ImageButton btnLlamar, btnCamara;

    //atributos de tipo primitivo
    private String numeroTelefono;

    //Atributos -> codigos de servicios del celular.
    private final int PHONE_CODE = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarVistas();
        btnLlamar.setOnClickListener(view ->
        {
            obtenerInformacion();
            configurarIntentImplicito();
        });
    }

    private void configurarIntentImplicito() {
        //Primero validadmos si el campo de texto no esta vaci0
        if(!numeroTelefono.isEmpty())
        {
            //primer problema
            //las llamadas han cambiado desde la version 6 o sdk 23
            // apartir de esa version se hace el codigo con algunos
            // cambios antes de esa version tenia otra manera de abordar
            //validar si la version de tu proyecto es mayor o igual a la version de android donde cambio la forma de procesar la llamada
            //EJ: SDK_INT = 24
            //para asalir a varias versiones hya que investigar un poco sobre los codigos VERSION_CODES.M
            //
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Para versiones nuevas
                //Versiones superiores a la 23 se usan funciones de las librerias de Android
                //e incorporan un asunto llamado asincronía
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CODE);
            }
            else {
                //Para versiones antiguas
                configurarVersionesAntiguas();
            }
        }
    }


    private void configurarVersionesAntiguas() {
        //configurar el Intent para  versiones anteriores a la verison donde cambio el codigo
        //Las diversas acciones ya estan en las librerias
        //la accion es de llamada
        //EL CONCEPTO SE LLAMA URI ES COMO ESCRIBIR LOS PARAMETROS EN LA PAGINA WEB ES COMO CREAR CLAVES VALOR QUE VIAJARIAN COMO UNA URL
        //intent implicito ->
        // 1)que accion a realizar
        //2)QUE DATOS   QUIERES ENVIAR EN EL INTENT
        //LA URI ES COMO EL URL DE WEB DONDE PUEDES CONFIGURAR LAS CABECERAS DE TU RUTA DONDE QUIERES PASAR DATOS
        //no sirven solo para enviar son como texto pano
        //asi se construye un uri ->
        //lo que viaja es una cadena que se parsea
        //esto funcionaria si el usuario siempre da permiso -> aplicacion de permisos
        //tener en cuenta cuando un apliCacion se valla a conectar al los servicios del celular hay que configurar esos permisos
        if (revisarPermisos(Manifest.permission.CALL_PHONE)) {
            Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+numeroTelefono));
            startActivity(intentLlamada);
        } else {
            Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerInformacion() {
        numeroTelefono = etTelefono.getText().toString();
    }

    private void inicializarVistas() {
        etTelefono = findViewById(R.id.etTelefono);
        btnLlamar = findViewById(R.id.btnLlamar);
        btnCamara = findViewById(R.id.btnCamara);
    }

    private boolean revisarPermisos(String permiso)
    {
        //android manega los permiso de esra manera
        //GRANTED : permiso otorgado 0
        //DENIED: permiso no otorgado -1
        //validar si el permiso a evaluar en su aplicacion tiene el valor qie android maneja para un permido otorgado
        int resultadoPermiso = checkCallingOrSelfPermission(permiso);
        return resultadoPermiso == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PHONE_CODE:
                String permiso = permissions[0];
                int valorPermiso = grantResults[0];
                //Para evitar que hayan errores humanos mas que nada
                if (permiso.equals(Manifest.permission.CALL_PHONE)) {
                    //Validar si se tiene el permiso o no
                    if (valorPermiso == PackageManager.PERMISSION_GRANTED) {
                        Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+numeroTelefono));
                        startActivity(intentLlamada);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    //radio button - radio group - agrupa vertical o horizontal
    //explicitos nosotros indicamos que actividad resuelva la accion para que se lana una nueva pantalla
    //implicitos el sistema escoje la accion, el propio sistema escoje
    //cuando accedemos por ejemplo a la camara la cosano es tan simple por que android protege a sus usuarios
    //deja que el usuario decida
    //hay que configurar los permisos
    //el problema tambien depende de que version este siendo utilizada
    //imagebutton usara magenes para representar la ccion de tu boton
}