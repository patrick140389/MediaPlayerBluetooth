package com.wessolowski.app.util.ressources;

import java.io.File;
import java.util.ArrayList;

import android.net.Uri;

import com.wessolowski.app.util.checks.Checks;
import com.wessolowski.app.util.classholders.ClassHolderOne;

public class FileLoader
{
        private static final String        TAG        = FileLoader.class.getSimpleName();

        public static ArrayList<ClassHolderOne> loadUrisWithFileName(File dir, String... searchName)
        {
//                Log.i(TAG, "File dir: " + dir.getAbsolutePath());
                File[] files = dir.listFiles();
//                Log.i(TAG, "Files lenght: " + files.length);
                ArrayList<ClassHolderOne> loadedUris = new ArrayList<ClassHolderOne>();

                if (Checks.checkNull(files))
                {
                        for (int i = 0; i < files.length; i++)
                        {
                                if (!files[i].isDirectory())
                                {
//                                        Log.i(TAG, "File: " + files[i].getName());
//                                        Log.i(TAG, "File: " + files[i].getAbsolutePath());
                                        for (int searchIndex = 0; searchIndex < searchName.length; searchIndex++)
                                        {
                                                if (files[i].getName().contains(searchName[searchIndex]))
                                                {
                                                        Uri loadedUri = Uri.fromFile(files[i]);
                                                        ClassHolderOne holder = new ClassHolderOne(files[i].getName(), loadedUri);
                                                        loadedUris.add(holder);
                                                }
                                        }
                                }
                        }
                }
                return loadedUris;
        }

}