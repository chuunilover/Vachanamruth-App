package com.example.alex.vachanamrut;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ListActivity {


    File[] imagelist;
    String[] pdflist;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File images = Environment.getExternalStorageDirectory();
        imagelist = images.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return ((name.endsWith(".pdf")));
            }
        });
        pdflist = new String[imagelist.length];
        for(int i = 0;i<imagelist.length;i++)
        {
            pdflist[i] = imagelist[i].getName();
        }
        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pdflist));

        //Creates the Vachanamrut pdf from the assets folder if the pdf does not already exist
        try {
            File vachFile = new File(this.getFilesDir(), "vachanamrut.pdf");
            //Make file if it doesn't exist
            if(!vachFile.exists()) {
                vachFile.createNewFile();
                AssetManager assetManager = getAssets();
                InputStream is = assetManager.open("vachanamrut-4.pdf");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                FileOutputStream fos = new FileOutputStream(vachFile);
                int i;
                //unbuffered copy
                while ((i = is.read()) != -1) {
                    fos.write(i);
                }
            }
            //start pdfvieweractivity; close this one
            openPdfIntent(vachFile.getAbsolutePath());
            finish();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        // openPdfIntent(this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        String path = imagelist[(int)id].getAbsolutePath();
        openPdfIntent(path);
    }

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
