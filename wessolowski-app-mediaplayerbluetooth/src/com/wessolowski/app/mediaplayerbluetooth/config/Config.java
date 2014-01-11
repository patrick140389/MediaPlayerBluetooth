package com.wessolowski.app.mediaplayerbluetooth.config;

import java.util.HashMap;

import android.content.Context;

import com.wessolowski.app.util.ressources.RessourceLoader;
import com.wessolowski.wessolowski.app.mediaplayerbluetooth.R;

public class Config
{
	private static Context	context;

	public static enum COVERFLOW_STYLEABLE
	{
		COVERFLOW(R.styleable.CoverFlow),
		COVERFLOW_IMAGE_WIDTH(R.styleable.CoverFlow_imageWidth),
		COVERFLOW_IMAGE_HEIGHT(R.styleable.CoverFlow_imageHeight);

		private int[]	coverFlow;
		private int		coverFlowDimensions;

		private COVERFLOW_STYLEABLE(final int[] coverFlow)
		{
			this.coverFlow = coverFlow;
		}

		private COVERFLOW_STYLEABLE(final int CoverFlowDimensions)
		{
			this.coverFlowDimensions = CoverFlowDimensions;
		}

		public int[] getCoverFlowStyleable()
		{
			return coverFlow;
		}

		public int getCoverFlowStyleableDimensions()
		{
			return coverFlowDimensions;
		}

	};

	public static enum DRAWABLE_RESSOURCE_CONVENTION_NAMES
	{
		/*
		 * RessourceConventionNames;
		 */
		IMAGE("image");

		private final String	ressourceConventionName;

		private DRAWABLE_RESSOURCE_CONVENTION_NAMES(final String ressourceConventionName)
		{
			this.ressourceConventionName = ressourceConventionName;
		}

		@Override
		public String toString()
		{
			return ressourceConventionName;
		}

		public static String[] toStringArray()
		{
			int enumLength = DRAWABLE_RESSOURCE_CONVENTION_NAMES.values().length;
			String[] sArr = new String[enumLength];
			for (int i = 0; i < enumLength; i++)
			{
				sArr[i] = DRAWABLE_RESSOURCE_CONVENTION_NAMES.values()[i].toString();
			}
			return sArr;
		}
	};

	public static enum DEFAULT_COVERFLOW_RESSOURCES
	{
		/*
		 * Ressource names;
		 */
		IMAGE01("image01"),
		IMAGE02("image02"),
		IMAGE03("image03"),
		IMAGE04("image04"),
		IMAGE05("image05");

		private final String	ressource;

		private DEFAULT_COVERFLOW_RESSOURCES(final String ressource)
		{
			this.ressource = ressource;
		}

		@Override
		public String toString()
		{
			return ressource;
		}
	};

	public static HashMap<String, Integer>	DEFAULT_COVERFLOW_RESSOURCE_NAMES_IDS	= RessourceLoader.loadAllDrawableRessourcesAsHashMapConventionName(context,
																							DRAWABLE_RESSOURCE_CONVENTION_NAMES.toStringArray());

}
