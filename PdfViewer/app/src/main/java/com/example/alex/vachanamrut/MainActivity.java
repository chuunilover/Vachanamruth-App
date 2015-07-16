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
                File gujVachFile = new File(getFilesDir(), "vachanamrut_guj.pdf");
                try {
                    //Make file if it doesn't exist
                    count = 0;
                    if(!gujVachFile.exists()){
                        gujVachFile.createNewFile();
                        AssetManager assetManager = getAssets();
                        InputStream is = assetManager.open("Vachanamrut.pdf");
                        FileOutputStream fos = new FileOutputStream(gujVachFile);
                        int i;
                        byte[] buffer = new byte[512];
                        while((i = is.read(buffer, 0, 512)) >= 0){
                            fos.write(buffer, 0, i);
                            count+=i;
                            handler.sendEmptyMessage(0);
                        }
                    }
                    if(!vachFile.exists()) {
                        vachFile.createNewFile();
                        AssetManager assetManager = getAssets();
                        InputStream is = assetManager.open("vachanamrut-4.pdf");
                        FileOutputStream fos = new FileOutputStream(vachFile);
                        int i;
                        byte[] buffer = new byte[512];
                        while((i = is.read(buffer, 0, 512)) >= 0){
                            fos.write(buffer, 0, i);
                            count+=i;
                            handler.sendEmptyMessage(0);
                        }
                    }
                    handler.sendEmptyMessage(0);
                    String[] pdfNames = {vachFile.getAbsolutePath(), gujVachFile.getAbsolutePath()};
                    openMultiPdfIntent(pdfNames);
                    //openPdfIntent(vachFile.getAbsolutePath());
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
        //check if the vachanamrut.pdf has already been created
        /*
        File vachFile = new File(getFilesDir(), "vachanamrut.pdf");
        if(vachFile.exists()){
            openPdfIntent(vachFile.getAbsolutePath());
            finish();
        }*/
        //Make the Vachanamrut PDF file after the loading screen has been created
        getSupportActionBar().hide();
        handler.postDelayed(runMakeFile, 1000);
    }

    /*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        String path = imagelist[(int)id].getAbsolutePath();
        openPdfIntent(path);
    }*/

    private void openMultiPdfIntent(String[] paths){
        try{
            if(paths.length == 0){
                return;
            }
            final Intent intent = new Intent(MainActivity.this, PDFReaderActivity.class);
            int i = 0;
            intent.putExtra(PdfViewerActivity.EXTRA_MULTIPDF, "TRUE");
            for (i = 0; i < paths.length; i++) {
                intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, paths);
            }
            startActivity(intent);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void openPdfIntent(String path)
    {
        try
        {
            final Intent intent = new Intent(MainActivity.this, PDFReaderActivity.class);
            intent.putExtra(PdfViewerActivity.EXTRA_MULTIPDF, "FALSE");
            intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, path);
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
