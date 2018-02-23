package hr.math.pemarti.popravni;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    String url1 = "https://web.math.pmf.unizg.hr/~karaga/android/sportasidata.txt";
    String url2 = "https://web.math.pmf.unizg.hr/~karaga/android/sportovidata.txt";
    TextView sportasi;
    TextView sportovi;
    TextView bazaSportovi;
    TextView bazaSportasi;

    String resultSportasi;
    String resultSportovi;

    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sportasi=(TextView)findViewById(R.id.sportasi);
        //sportovi=(TextView)findViewById(R.id.sportovi);
        bazaSportovi=(TextView)findViewById(R.id.bazaSportovi);
        bazaSportasi=(TextView)findViewById(R.id.bazaSportasi);

        db = new DBAdapter(this);



    }
    
    private InputStream OpenHttpConnection(String urlString)
            throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }
    private String DownloadText(String URL)
    {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e) {
            Log.d("NetworkingActivity", e.getLocalizedMessage());
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer))>0) {
                //---convert the chars to a String---
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("NetworkingActivity", e.getLocalizedMessage());
            return "";
        }
        return str;
    }

    private class DownloadTextTask1 extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            //sportasi.setText(result);
            resultSportasi=result;
            db.open();
            long id;
            String[] listaSportasa = resultSportasi.split("\n");
            for(int i=0;i<listaSportasa.length;i++){
                String[] sportas=listaSportasa[i].split(" ");
                id=db.insertSportas(sportas[1],sportas[2]);
            }
            db.close();
            Toast.makeText(getBaseContext(), "uspjesno", Toast.LENGTH_LONG).show();
        }
    }
    private class DownloadTextTask2 extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            //sportovi.setText(result);
            resultSportovi=result;
            db.open();
            long id;
            String[] listaSportova = resultSportovi.split("\n");
            for(int i=0;i<listaSportova.length;i++){
                String[] sport=listaSportova[i].split(" ");
                id=db.insertSport(sport[1],sport[2],sport[3]);
            }
            db.close();
            Toast.makeText(getBaseContext(), "uspjesno", Toast.LENGTH_LONG).show();
        }
    }

    public void downloadSportasi(View view) {

        // Execute DownloadImage AsyncTask
        new DownloadTextTask1().execute(url1);
    }
    public void downloadSportovi(View view) {

        // Execute DownloadImage AsyncTask
        new DownloadTextTask2().execute(url2);
    }

    public void showSportas(View view){
            db.open();
            Cursor c = db.getAllSportas();
            if (c.moveToFirst())
            {
                do {
                    DisplaySportas(c);
                } while (c.moveToNext());
            }
            db.close();
            Toast.makeText(this,"kraj if-a",Toast.LENGTH_LONG).show();

    }
    public void showSport(View view){
        db.open();
        Cursor c = db.getAllSport();
        if (c.moveToFirst())
        {
            do {
                DisplaySport(c);
            } while (c.moveToNext());
        }
        db.close();
        Toast.makeText(this,"kraj if-a",Toast.LENGTH_LONG).show();

    }
    public void showDB(View view){
        showSport(view);
        showSportas(view);

    }


    //---add a contact---
        /*db.open();
        long id = db.insertContact("Wei-Meng Lee", "weimenglee@learn2develop.net");
        id = db.insertContact("Mary Jackson", "mary@jackson.com");
        db.close();



        //--get all contacts---
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst())
        {
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }
        db.close();



        //---get a contact---
        db.open();
        Cursor cu = db.getContact(2);
        if (cu.moveToFirst())
            DisplayContact(cu);
        else
            Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
        db.close();



        //---update contact---
        db.open();
        if (db.updateContact(1, "Wei-Meng Lee", "weimenglee@gmail.com"))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();



        //---delete a contact---
        db.open();
        if (db.deleteContact(1))
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();*/




    //funkcija za ispis
    public void DisplaySport(Cursor c)
    {
        /*Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Ime: " + c.getString(1) + "\n" +
                        "Prezime:  " + c.getString(2),
                Toast.LENGTH_LONG).show();*/
        String sport="id: " + c.getString(0) + ", " +
                "Naziv: " + c.getString(1) + ", " +
                "Vrsta:  " + c.getString(2) + ", " +
                "Olimpijski: "+ c.getString(3)+"\n";
        bazaSportovi.setText(bazaSportovi.getText()+sport);
    }

    public void DisplaySportas(Cursor c)
    {
        /*Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Ime: " + c.getString(1) + "\n" +
                        "Prezime:  " + c.getString(2),
                Toast.LENGTH_LONG).show();*/
        String sportas="id: " + c.getString(0) + ", " +
                "Ime: " + c.getString(1) + ", " +
                "Prezime:  " + c.getString(2) + "\n";
        bazaSportasi.setText(bazaSportasi.getText()+sportas);
    }



}
