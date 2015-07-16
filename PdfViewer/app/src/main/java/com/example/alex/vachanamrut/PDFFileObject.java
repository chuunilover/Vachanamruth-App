package com.example.alex.vachanamrut;
import com.sun.pdfview.PDFFile;

/**
 * Created by Alex on 15/07/2015.
 */
public class PDFFileObject {
    private int[][] CHAPTERS_TO_PAGES;
    private PDFFile pdfFile;

    public PDFFileObject(PDFFile file, int[][] pages){
        pdfFile = file;
        CHAPTERS_TO_PAGES = pages;
    }
    public int indexPages(int chapter, int section){
        return CHAPTERS_TO_PAGES[chapter][section];
    }

    public PDFFile getPdfFile(){
        return pdfFile;
    }
}
