package com.wessolowski.app.android.util.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.wessolowski.app.util.ressources.RessourceLoader;

@SuppressLint("UseSparseArrays")
@SuppressWarnings("deprecation")
public class CoverFlowImageAdapter extends BaseAdapter
{

	private static final String							TAG					= CoverFlowImageAdapter.class
																					.getSimpleName();
	private final Context								context;

	private float										width				= 0;
	private float										height				= 0;

	private static final int							DEFAULT_LIST_SIZE	= 20;

	private static final List<Integer>					IMAGE_RESOURCE_IDS	= new ArrayList<Integer>(
																					DEFAULT_LIST_SIZE);

	private final Map<Integer, WeakReference<Bitmap>>	bitmapMap			= new HashMap<Integer, WeakReference<Bitmap>>();

	public CoverFlowImageAdapter(final Context context, String... ressourceConventionName)
	{
		super();
		this.context = context;
		setResources(RessourceLoader.loadAllDrawableRessourcesAsIDS(context, ressourceConventionName));
	}

	public synchronized void setWidth(final float width)
	{
		this.width = width;
	}

	public synchronized void setHeight(final float height)
	{
		this.height = height;
	}

	public final synchronized void setResources(final ArrayList<Integer> resourceIds)
	{
		IMAGE_RESOURCE_IDS.addAll(resourceIds);
		notifyDataSetChanged();
	}

	private Bitmap createBitmap(final int position)
	{
		Log.v(TAG, "creating item " + position);
		final Bitmap bitmap = RessourceLoader.loadBitmapFromInternalRessources(
				context, IMAGE_RESOURCE_IDS.get(position));
		bitmapMap.put(position, new WeakReference<Bitmap>(bitmap));
		return bitmap;
	}

	@Override
	public final Bitmap getItem(final int position)
	{
		final WeakReference<Bitmap> weakBitmapReference = bitmapMap
				.get(position);

		if (weakBitmapReference != null)
		{
			final Bitmap bitmap = weakBitmapReference.get();

			if (bitmap != null)
			{
				Log.v(TAG, "Empty bitmap reference at position: " + position
						+ ":" + this);
			} else
			{
				Log.v(TAG, "Reusing bitmap item at position: " + position + ":"
						+ this);
				return bitmap;
			}

		}

		Log.v(TAG, "Creating item at position: " + position + ":" + this);
		final Bitmap bitmap = createBitmap(position);
		bitmapMap.put(position, new WeakReference<Bitmap>(bitmap));
		Log.v(TAG, "Created item at position: " + position + ":" + this);
		return bitmap;

	}

	@Override
	public final synchronized long getItemId(final int position)
	{
		return position;
	}

	@Override
	public final synchronized ImageView getView(final int position,
			final View convertView, final ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			final Context context = parent.getContext();
			Log.v(TAG, "Creating Image view at position: " + position + ":"
					+ this);
			imageView = new ImageView(context);
			imageView.setLayoutParams(new Gallery.LayoutParams((int) width,
					(int) height));
		} else
		{
			Log.v(TAG, "Reusing view at position: " + position + ":" + this);
			imageView = (ImageView) convertView;
		}
		imageView.setImageBitmap(getItem(position));
		return imageView;
	}

	@Override
	public synchronized int getCount()
	{
		return IMAGE_RESOURCE_IDS.size();
	}

}
