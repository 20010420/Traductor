package com.example.traductor;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.mbms.DownloadStatusListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link english_P#newInstance} factory method to
 * create an instance of this fragment.
 */
public class english_P extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // La variable vista es el puente que nos permite reconoser el id del Button
    View vista;
    ImageButton btnHablar;
    TextView textoTraduccion;
    TextToSpeech textToSpeech;

    public english_P() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment english_P.
     */
    // TODO: Rename and change types and number of parameters
    public static english_P newInstance(String param1, String param2) {
        english_P fragment = new english_P();
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
        //textoTraduccion = vista.findViewById(R.id.textoTraduccion);
    }
    //lansador de intent
    ActivityResultLauncher<Intent> lanzadorIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), //iniciar el intent
            //recibir la informacion tomada por la linea de arriba
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //aqui procesamos la informacion
                    if (result.getResultCode()== Activity.RESULT_OK){
                        //si esta condicional se da resulta que todo salio bien
                        Intent info = result.getData();
                        //necesitamos la informacion necesaria entonces creamos un array
                        ArrayList<String> data = info.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        //con esto ya tenemos la informacion en un array de tipo string
                        //textoTraduccion.setText(data.get(0));//pasamos el texto reconocido en la entrada de voz

                        TranslatorOptions options = new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.SPANISH) // Idioma de entrada
                                .setTargetLanguage(TranslateLanguage.ENGLISH) // Idioma de salida
                                .build();
                        final Translator spanishEnglishTranslator = Translation.getClient(options);
                        //condicionamos la descarga del modelo de traduccion
                        DownloadConditions conditions = new DownloadConditions.Builder()
                                .requireWifi()
                                .build(); // aqui ya tenemos las condiciones para poder descargar nuestro modelo de traduccion

                        //descargamos el modelo
                        spanishEnglishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {// se descargo el modelo de traduccion
                                        spanishEnglishTranslator.translate(data.get(0)).addOnSuccessListener(//queremos que tradusca el elemento dero de el array data
                                                new OnSuccessListener<String>() {
                                                    @Override
                                                    public void onSuccess(String s) {
                                                        //aqui nos dara un parametro tipo String con el texto traducido
                                                        // TRADUCCION
                                                        textoTraduccion.setText(s);//se coloca el texto traducido
                                                        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null,null);//usando el metodo no depresiado que tiene  4 parametros
                                                    }
                                                }
                                        ).addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Error al traducir el mensaje",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                        );

                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() { // manera de manejar los errores
                                    @Override
                                    public void onFailure(@NonNull Exception e) {//Hubo un error al descargar el modelo de traduccion
                                        Toast.makeText(getContext(), "Error al descargar el paquete de idiomas",Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }
                }
            }
    );

    public void btnHablar(View v){
        Intent intentReconocimiento = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//aqui le decimos que inicie una actividad de reconocimiento de voz
        //le decimos el idioma a reconocer
        intentReconocimiento.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "SPANISH"); // español
        try {
            //lanzamos el intent
            lanzadorIntent.launch(intentReconocimiento);
        }catch (ActivityNotFoundException e ){
            Toast.makeText(getContext(),"Error al inicial el reconocimiento de voz", Toast.LENGTH_SHORT).show();
            //revisar       ********
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_english__p, container, false);;

        btnHablar = (ImageButton) vista.findViewById(R.id.btnHablar);
        textoTraduccion = vista.findViewById(R.id.textoTraduccion);
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {//Inicializamos el texto a voz
            @Override
            public void onInit(int i) { // validamos si hay algun error al inicialozar el text a voz
                if(i != TextToSpeech.ERROR){// si al crear no es igual a error, lo conficguramos
                    textToSpeech.setLanguage(Locale.ENGLISH);//lo configuramos eon el idioma español

                }
            }
        });


        //txtEnglish = vista.findViewById(R.id.textView15);

        btnHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnHablar(btnHablar);
                //textoTraduccion.setText("Hello, this application has been designed by 'Yissel, Juan Fernando and Juanny Gil");
            }
        });


        return vista;
        //return inflater.inflate(R.layout.fragment_english__p, container, false);
    }
}
