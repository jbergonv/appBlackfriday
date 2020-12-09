package com.example.appblackfriday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    Button csv,xml,json;
    ListView lista;
    ProgressDialog progressDialog;
    static String SERVIDOR = "http://192.168.0.22";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        csv = findViewById(R.id.csv);
        xml = findViewById(R.id.xml);
        json = findViewById(R.id.json);
        lista = findViewById(R.id.lista);

        csv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DescargarCSV descargarCSV = (DescargarCSV) new DescargarCSV().execute("/blackfriday/listadoCSV.php");
                //descargarCSV.doInBackground();


            }
        });

        xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DescargaXML descargaXML = (DescargaXML) new DescargaXML().execute("/blackfriday/listadoXML.php");

            }
        });

        json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DescargaJSON descargaJSON = (DescargaJSON) new DescargaJSON().execute("/blackfriday/listadoJSON.php");

            }
        });

    }

    private class DescargarCSV extends AsyncTask<String,Void,Void>{

        String total = "";

        @Override
        protected void onPreExecute() {

            /*Context context;
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Descargando datos");
            progressDialog.setIndeterminate(true);
            progressDialog.show();*/

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAdapter<String> adapter;
            List<String> list = new ArrayList<String>();

            String[] lineas = total.split("<br>");


                    for(String linea : lineas){

                        String[] campos = linea.split(";");
                        String dato = " ID "+campos[0];
                        dato += " MODELO "+campos[1];
                        dato += " MARCA "+campos[2];
                        dato += " PRECIO "+campos[3];

                        list.add(dato);


                    }

                    adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);

                    lista.setAdapter(adapter);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... strings) {

            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;


            try {
                url = new URL(SERVIDOR+script);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                    String linea = "";

                        while((linea = br.readLine())!=null){

                                total += linea;

                        }

                        br.close();
                        inputStream.close();

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class DescargaXML extends AsyncTask<String,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Context context;
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Descargando datos");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... strings) {


            return null;
        }
    }

    private class DescargaJSON extends AsyncTask<String,Void,Void>{

        String total = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            ArrayAdapter<String> adapter;
            List<String> list = new ArrayList<String>();

            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(total).getAsJsonArray();
            String[] fila = new String[4];




            for(JsonElement elemento : jsonArray){

                JsonObject objeto = elemento.getAsJsonObject();

                fila[0] = objeto.get("id").getAsString();
                fila[1] = objeto.get("marca").getAsString();
                fila[2] = objeto.get("modelo").getAsString();
                fila[3] = objeto.get("precio").getAsString();

                String aux = " ID "+fila[0]+" MARCA "+fila[1]+" MODELO "+fila[2]+" PRECIO "+fila[3];


                list.add(aux);


            }

            adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);

            lista.setAdapter(adapter);

            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... strings) {

            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;


            try {
                url = new URL(SERVIDOR+script);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                    String linea = "";

                    while((linea = br.readLine())!=null){

                        total += linea+"\n";

                    }

                    br.close();
                    inputStream.close();

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}