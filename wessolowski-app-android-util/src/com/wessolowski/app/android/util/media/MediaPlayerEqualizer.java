package com.wessolowski.app.android.util.media;

import com.wessolowski.app.util.checks.Checks;

import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.util.Log;
import android.media.audiofx.*;

public class MediaPlayerEqualizer
{
	private static final short			BASS_BOOST_RANGE					= 50;
	private static final short			BASS_BOOST_MIN_STRENGTH				= 0;
	private static final short			BASS_BOOST_MAX_STRENGTH				= 1000;
	private static final short			DEFAULT_BASS_BOOST_STRENGTH			= 500;
	private short						currentBoost						= 0;

	private static final short			EQUALIZER_BAND_ONE					= 0;
	private static final short			EQUALIZER_BAND_TWO					= 1;
	private static final short			EQUALIZER_BAND_THREE				= 2;
	private static final short			EQUALIZER_BAND_FOUR					= 3;
	private static final short			EQUALIZER_BAND_FIVE					= 4;
	private static final short			DEFAULT_EQUALIZER_BAND_ONE_LEVEL	= 0;
	private static final short			DEFAULT_EQUALIZER_BAND_TWO_LEVEL	= 0;
	private static final short			DEFAULT_EQUALIZER_BAND_THREE_LEVEL	= 0;
	private static final short			DEFAULT_EQUALIZER_BAND_FOUR_LEVEL	= 0;
	private static final short			DEFAULT_EQUALIZER_BAND_FIVE_LEVEL	= 0;
	private static final short			EQUALIZER_BAND_LEVEL_RANGE			= 100;

	private static final short			DEFAULT_VIRTUALIZER_LEVEL			= 0;

	private static MediaPlayerEqualizer	mediaPlayerEqualizer				= null;

	private static final String			TAG									= MediaPlayerEqualizer.class.getSimpleName();

	private BassBoost					bassBoost							= null;
	private Equalizer					androidEqualizer					= null;
	private Virtualizer					virtualizer							= null;

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
		Log.i(TAG, "config equalizer");
		bassBoost = new BassBoost(0, sessoinId);
		bassBoost.setEnabled(true);

		androidEqualizer = new Equalizer(0, sessoinId);
		androidEqualizer.setEnabled(true);

		virtualizer = new Virtualizer(0, sessoinId);
		virtualizer.setEnabled(true);
		loadDefaultEqualizerSettings();
	}

	public void loadDefaultEqualizerSettings()
	{
		Log.i(TAG, "load default settings");
		loadDefaultBandLevel();
		loadDefaultVirtualizerlevel();
		loadDefaultBassBoostLevel();
	}

	public void loadDefaultBandLevel()
	{
		if (Checks.checkNull(androidEqualizer))
		{
			androidEqualizer.setBandLevel(EQUALIZER_BAND_ONE	, DEFAULT_EQUALIZER_BAND_ONE_LEVEL);
			androidEqualizer.setBandLevel(EQUALIZER_BAND_TWO	, DEFAULT_EQUALIZER_BAND_TWO_LEVEL);
			androidEqualizer.setBandLevel(EQUALIZER_BAND_THREE	, DEFAULT_EQUALIZER_BAND_THREE_LEVEL);
			androidEqualizer.setBandLevel(EQUALIZER_BAND_FOUR	, DEFAULT_EQUALIZER_BAND_FOUR_LEVEL);
			androidEqualizer.setBandLevel(EQUALIZER_BAND_FIVE	, DEFAULT_EQUALIZER_BAND_FIVE_LEVEL);
		}
	}

	private void setBandLevel(short band, short level)
	{
		androidEqualizer.setBandLevel(band, (short) (level + 100));
	}

	public void setBandLevelUp(short band)
	{
		short[] range = androidEqualizer.getBandLevelRange();
		if (androidEqualizer.getBandLevel(band) < range[1])
		{
			short level = androidEqualizer.getBandLevel(band);
			setBandLevel(band, level);
			Log.i(TAG, "Set Band: " + band + " Level UP to: " + androidEqualizer.getBandLevel(band));
		}
		else
		{
			Log.i(TAG, "Band: " + band + " is MAX Level: " + androidEqualizer.getBandLevel(band));
		}
	}

	public void setBandLevelDown(short band)
	{
		short[] range = androidEqualizer.getBandLevelRange();
		if (androidEqualizer.getBandLevel(band) > range[0])
		{
			short level = androidEqualizer.getBandLevel(band);
			androidEqualizer.setBandLevel(band, (short) (level - 100));
			Log.i(TAG, "Set Band: " + band + " Level DOWN to: " + androidEqualizer.getBandLevel(band));
		}
		else
		{
			Log.i(TAG, "Band: " + band + " is MIN Level: " + androidEqualizer.getBandLevel(band));
		}
	}
	
	public void loadDefaultBassBoostLevel()
	{
		setBassBoostLevel(DEFAULT_BASS_BOOST_STRENGTH);
	}

	private void setBassBoostLevel(short level)
	{
		bassBoost.setStrength(level);
	}

	public short setBassBoostLevelUp()
	{
		if (currentBoost < BASS_BOOST_MAX_STRENGTH)
		{
			short strength = (short) ((short) bassBoost.getRoundedStrength() + BASS_BOOST_RANGE);
			setBassBoostLevel(strength);
			currentBoost = bassBoost.getRoundedStrength();
		}
		return bassBoost.getRoundedStrength();
	}

	public short setbassBoostLevelDown()
	{
		if (currentBoost > BASS_BOOST_MIN_STRENGTH)
		{
			short strength = (short) ((short) bassBoost.getRoundedStrength() - BASS_BOOST_RANGE);
			setBassBoostLevel(strength);
			currentBoost = bassBoost.getRoundedStrength();
		}
		return bassBoost.getRoundedStrength();
	}
	
	public void loadDefaultVirtualizerlevel()
	{
		setVirtualizerLevel(DEFAULT_VIRTUALIZER_LEVEL);
	}

	private void setVirtualizerLevel(short level)
	{
		virtualizer.setStrength(level);
	}

	public void setVirtualizerLevelMAX()
	{
		short maxLevel = 1000;
		setVirtualizerLevel(maxLevel);
	}

	public void setVirtualizerLevelMIN()
	{
		short minLevel = 0;
		setVirtualizerLevel(minLevel);
	}

	public void setVirtualizerLevelUp()
	{
		if (virtualizer.getRoundedStrength() < 1000)
		{
			short level = (short) (virtualizer.getRoundedStrength() + 200);
			setVirtualizerLevel(level);
			Log.i(TAG, "Set Virtualizer Level UP to: " + virtualizer.getRoundedStrength());
		}
		else
		{
			Log.i(TAG, "Virtualizer Level is MAX: " + virtualizer.getRoundedStrength());
		}
	}

	public void setVirtualizerLevelDown()
	{
		if (virtualizer.getRoundedStrength() > 0)
		{
			short level = (short) (virtualizer.getRoundedStrength() - 200);
			setVirtualizerLevel(level);
			Log.i(TAG, "Set Virtualizer Level Down to: " + virtualizer.getRoundedStrength());
		}
		else
		{
			Log.i(TAG, "Virtualizer Level is MIN: " + virtualizer.getRoundedStrength());
		}
	}

	public void destroyMediaEqualizer()
	{
		if (Checks.checkNull(bassBoost))
		{
			bassBoost.release();
		}
		if (Checks.checkNull(virtualizer))
		{
			virtualizer.release();
		}
		if (Checks.checkNull(androidEqualizer))
		{
			androidEqualizer.release();
		}
	}

}
