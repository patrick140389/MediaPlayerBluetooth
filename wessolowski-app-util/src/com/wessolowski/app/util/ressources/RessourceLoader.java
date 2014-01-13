package com.wessolowski.app.util.ressources;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class RessourceLoader
{

        /*
         * Load Ressources from internal; Load Ressources from SD Card; Load
         * Ressources from internal memory;
         */

        /*
         * Load from project internal Ressources
         */

        /**
         * Create a Drawable object from the given Ressource from drawable
         *
         * @param context
         *
         * The Context.
         *
         * @param ressource
         *
         * The Ressource in drawable (R.drawable.Image01).
         *
         * @return Drawable
         *
         * Returns a Drawable Object from the given Ressource
         */
        public static Drawable loadDrawableFromInternalRessources(Context context, int ressource)
        {
                final Drawable drawable = context.getResources().getDrawable(ressource);
                return drawable;
        }

        /**
         * Create a Bitmap object from the given Ressource from drawable
         *
         * @param context
         *
         * The Context.
         *
         * @param ressource
         *
         * The Ressource in drawable (R.drawable.Image01).
         *
         * @return Bitmap
         *
         * Returns a Bitmap Object from the given Ressource
         */
        public static Bitmap loadBitmapFromInternalRessources(Context context, int ressource)
        {
                final Bitmap bitmap = ((BitmapDrawable) loadDrawableFromInternalRessources(context, ressource)).getBitmap();
                return bitmap;
        }

        /**
         * Create a new ImageView object and set a Bitmap to the ImageView.
         *
         * @param context
         *
         * The Context.
         *
         * @param ressource
         *
         * The Ressource in drawable (R.drawable.Image01).
         *
         * @return ImageView
         *
         * Returns the ImageView
         */
        public static ImageView loadImageViewWithInternalRessource(Context context, int ressource)
        {
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(loadBitmapFromInternalRessources(context, ressource));
                return imageView;
        }

        /**
         * Create a HashMap (String, Integer) and load all Ressources of the given
         * ID's in the ArrayList.
         *
         * @param context
         * The Context.
         *
         * @param ressource
         * An String[] from the Conventionname of the drawable ressources
         * that will be loaded
         *
         * @return HashMap (String, Integer)
         *
         * Returns the HashMap (String, Integer). {{String: The name of the
         * given Ressource}} | {{Integer: The ID's of the given Ressource}}
         */
        public static final HashMap<String, Integer> loadAllDrawableRessourcesAsHashMapConventionName(Context context, String... ressourceConventionName)
        {
                ArrayList<Integer> idList = loadAllDrawableRessourcesAsIDS(context, ressourceConventionName);
                HashMap<String, Integer> ressourceMap = loadAllDrawableRessourcesAsHashMap(context, idList);
                return ressourceMap;
        }

        /**
         * Create a HashMap (String, Integer) and load all Ressources of the given
         * ID's in the ArrayList.
         *
         * @param context
         * The Context.
         *
         * @param ressource
         * An ArrayList from ID's of the drawable ressources that will be
         * loaded
         *
         * @return HashMap (String, Integer)
         *
         * Returns the HashMap (String, Integer). {{String: The name of the
         * given Ressource}} | {{Integer: The ID's of the given Ressource}}
         */

        public static final HashMap<String, Integer> loadAllDrawableRessourcesAsHashMap(Context context, ArrayList<Integer> ressourceIDS)
        {
                HashMap<String, Integer> ressourceMap = new HashMap<String, Integer>();

                if (ressourceIDS.size() != 0 && !ressourceIDS.isEmpty())
                {
                        for (int i = 0; i < ressourceIDS.size(); i++)
                        {
                                String ressourceName = context.getResources().getResourceName(ressourceIDS.get(i));
                                ressourceMap.put(ressourceName, ressourceIDS.get(i));
                        }
                }
                return ressourceMap;
        }

        /**
         * Create a ArrayList(Integer) and load all Ressources in drawable on the
         * given name
         *
         * @param context
         * The Context.
         *
         * @param ressource
         * The Ressource names in drawable (R.drawable.Image01,
         * R.drawable.Image02, ...).
         *
         * @return ArrayList(Drawable)
         *
         * Returns an ArrayList with Drawable object's.
         */
        public static final ArrayList<Drawable> loadAllDrawableRessourcesAsDrawable(Context context, String... ressourceName)
        {
                ArrayList<Drawable> ressourceDrawableList = new ArrayList<Drawable>();
                ArrayList<Integer> ressourceIDList = loadAllDrawableRessourcesAsIDS(context, ressourceName);

                int listSize = ressourceIDList.size();
                for (int i = 0; i < listSize; i++)
                {
                        ressourceDrawableList.add(loadDrawableFromInternalRessources(context, ressourceIDList.get(i)));
                }
                return ressourceDrawableList;

        }

        /**
         * Create an ArrayList(Integer) and load all Ressource ID's of the give
         * Ressource names
         *
         * @param context
         * @param ressourceConventionName
         * The Ressource Conventionname (image, ...)
         * @return ArrayList(Integer)
         *
         * An ArrayList(Integer) with the Ressource ID's
         */
        public static final ArrayList<Integer> loadAllDrawableRessourcesAsIDS(Context context, String... ressourceConventionName)
        {
                ArrayList<Integer> ressourceIDS = new ArrayList<Integer>();

                int ressourceNameSize = ressourceConventionName.length;

                for (int i = 0; i < ressourceNameSize; i++)
                {
                        int ressourceNumber = 0;
                        int noSuchResults = 0;
                        boolean loading = true;

                        while (loading)
                        {
                                String fullRessourceName = ressourceConventionName[i] + ressourceNumber;
                                int ressourceId = context.getResources().getIdentifier(fullRessourceName, "drawable", context.getPackageName());

                                if (ressourceId != 0 && noSuchResults < 3)
                                {
                                        ressourceIDS.add(ressourceId);
                                }
                                else
                                {
                                        if (noSuchResults >= 3)
                                        {
                                                loading = false;
                                        }
                                        noSuchResults++;
                                }
                                ressourceNumber++;
                        }
                }
                return ressourceIDS;
        }

        /*
         *
         * Load from SD Card
         */

        /*
         * Load from internal memory
         */

}