package com.example.alex.vachanamrut;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity /*ListActivity*/ {

    private Thread makeFile;
    private Runnable runMakeFile;
    private Handler handler;
    private final long FILE_SIZE = 2316594;
    long count;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressBar loadProgress = (ProgressBar)findViewById(R.id.progressBar);
        loadProgress.setMax((int)FILE_SIZE);

        //Creates the Vachanamrut pdf from the assets folder if the pdf does not already exist

        runMakeFile = new Runnable(){
            @Override
        public void run(){
                File vachFile = new File(getFilesDir(), "vachanamrut.pdf");
                try {
                    //Make file if it doesn't exist
                    count = 0;
                    if(!vachFile.exists()) {
                        vachFile.createNewFile();
                        AssetManager assetManager = getAssets();
                        InputStream is = assetManager.open("vachanamrut-4.pdf");
                        FileOutputStream fos = new FileOutputStream(vachFile);
                        int i;
                        byte[] buffer = new byte[2048];
                        while((i = is.read(buffer, 0, 2048)) >= 0){
                            fos.write(buffer, 0, i);
                            count+=i;
                            handler.sendEmptyMessage(0);
                        }
                    }
                    openPdfIntent(vachFile.getAbsolutePath());
                    finish();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }};
        handler = new Handler(){
            @Override
        public void handleMessage(Message msg){
                loadProgress.setProgress((int)count);
            }
        };
        File vachFile = new File(getFilesDir(), "vachanamrut.pdf");
        if(vachFile.exists()){
            openPdfIntent(vachFile.getAbsolutePath());
            finish();
        }
        //Make the Vachanamrut PDF file after the loading screen has been created
        handler.postDelayed(runMakeFile, 150);
    }

    /*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        String path = imagelist[(int)id].getAbsolutePath();
        openPdfIntent(path);
    }*/

    private void openPdfIntent(String path)
    {
        try
        {
            final Intent intent = new Intent(MainActivity.this, PDFReaderActivity.class);

            intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, path);

            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
