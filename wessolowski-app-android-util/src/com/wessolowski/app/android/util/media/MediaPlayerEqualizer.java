package com.wessolowski.app.android.util.media;

import com.wessolowski.app.util.checks.Checks;

import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.util.Log;
import android.media.audiofx.*;

public class MediaPlayerEqualizer
{
	private static final short			BASS_BOOST_RANGE		= 50;
	private static final short			BASS_BOOST_MIN_STRENGTH	= 0;
	private static final short			BASS_BOOST_MAX_STRENGTH	= 1000;

	private static MediaPlayerEqualizer	mediaPlayerEqualizer	= null;

	private static final String			TAG						= MediaPlayerEqualizer.class.getSimpleName();
	short								currentBoost			= 500;

	private BassBoost					bassBoost				= null;
	private Equalizer					androidEqualizer		= null;
	private Virtualizer					virtualizer				= null;

	// private LoudnessEnhancer loudness = null;

	private MediaPlayerEqualizer()
	{

	}

	public static MediaPlayerEqualizer getInstance()
	{
		if (!Checks.checkNull(mediaPlayerEqualizer))
		{
			mediaPlayerEqualizer = new MediaPlayerEqualizer();
			return mediaPlayerEqualizer;
		}
		return mediaPlayerEqualizer;
	}

	public void configEqualizer(int sessoinId)
	{
		bassBoost = new BassBoost(0, sessoinId);
		bassBoost.setEnabled(true);
		
		androidEqualizer = new Equalizer(0, sessoinId);
		androidEqualizer.setEnabled(true);
		
		virtualizer = new Virtualizer(0, sessoinId);
	}

	public void setBandLevelUp(short band)
	{
		short[] range = androidEqualizer.getBandLevelRange();
		Log.i("", "min range " + range[0] + "max range " + range[1]);
		if (androidEqualizer.getBandLevel(band) < range[1])
		{
			short level = androidEqualizer.getBandLevel(band);
			androidEqualizer.setBandLevel(band, (short) (level + 100));
			Log.i("", "band level up " + androidEqualizer.getBandLevel(band));
		}
	}

	public void setBandLevelDown(short band)
	{
		short[] range = androidEqualizer.getBandLevelRange();
		Log.i("", "min range " + range[0] + "max range " + range[1]);
		if (androidEqualizer.getBandLevel(band) > range[0])
		{
			short level = androidEqualizer.getBandLevel(band);
			androidEqualizer.setBandLevel(band, (short) (level - 100));
			Log.i("", "band level up " + androidEqualizer.getBandLevel(band));
		}
	}

	public short setBassBoostUp()
	{
		if (currentBoost < BASS_BOOST_MAX_STRENGTH)
		{
			short strength = (short) ((short) bassBoost.getRoundedStrength() + BASS_BOOST_RANGE);
			bassBoost.setStrength(strength);
			currentBoost = bassBoost.getRoundedStrength();
		}
		return bassBoost.getRoundedStrength();
	}

	public short setbassBoostDown()
	{
		if (currentBoost > BASS_BOOST_MIN_STRENGTH)
		{
			short strength = (short) ((short) bassBoost.getRoundedStrength() - BASS_BOOST_RANGE);
			bassBoost.setStrength(strength);
			currentBoost = bassBoost.getRoundedStrength();
		}
		return bassBoost.getRoundedStrength();
	}
	
	public void setVirtualizerLevelUp()
	{
		if(virtualizer.getRoundedStrength() < 1000)
		{
			virtualizer.setStrength((short)(virtualizer.getRoundedStrength() + 200));
			Log.i(TAG, "if virtual up" + virtualizer.getRoundedStrength());
		}
		else
		{
			Log.i(TAG, "else virtual up" + virtualizer.getRoundedStrength());
		}
	}
	
	public void setVirtualizerLevelDown()
	{
		if(virtualizer.getRoundedStrength() > 0)
		{
			virtualizer.setStrength((short)(virtualizer.getRoundedStrength() - 200));
			Log.i(TAG, "if virtual down" + virtualizer.getRoundedStrength());
		}
		else
		{
			Log.i(TAG, "else virtual down" + virtualizer.getRoundedStrength());
		}
	}
	
	

	public void destroyMediaEqualizer()
	{
		bassBoost.setEnabled(false);
		bassBoost.release();
		// androidEqualizer.release();
		// androidMediaPlayer.release();
	}

}
