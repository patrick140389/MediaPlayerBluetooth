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
	short currentBoost =  500;
	
	private BassBoost bassBoost = null;
	private Equalizer androidEqualizer = null;
	//private LoudnessEnhancer loudness = null;
	private MediaPlayer androidMediaPlayer = null;
	
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

	public void setMediaPlayer(MediaPlayer mediaPlayer)
	{
		this.androidMediaPlayer = mediaPlayer;
		bassBoost = new BassBoost(0, androidMediaPlayer.getAudioSessionId());
		androidEqualizer = new Equalizer(0, androidMediaPlayer.getAudioSessionId());
		//loudness = new LoudnessEnhancer(androidMediaPlayer.getAudioSessionId());
		androidEqualizer.setEnabled(true);
		bassBoost.setEnabled(true);
		//loudness.setEnabled(true);
		currentBoost = 0;
		getEqu();
		
	}
	
/*	public void setLoudnessUp()
	{
		float targetGain = loudness.getTargetGain();
		Log.i(TAG, "loudness gain " + targetGain);
		if((int) targetGain < 1500)
		{
			loudness.setTargetGain((int) targetGain + 100);
		}
	}
	
	public void setLoudnessDown()
	{
		float targetGain = loudness.getTargetGain();
		Log.i(TAG, "loudness gain " + targetGain);
		if((int) targetGain > 0)
		{
			loudness.setTargetGain((int) targetGain - 100);
		}
	}
	*/
	public void getEqu()
	{
		Log.i(TAG, "number of bands: " + androidEqualizer.getNumberOfBands());
	}
	
	public void setBandLevelUp(short band)
	{
		short[] range = androidEqualizer.getBandLevelRange();
		Log.i("", "min range " + range[0] + "max range " + range[1]);
		if(androidEqualizer.getBandLevel(band) < range[1])
		{
			short level = androidEqualizer.getBandLevel(band);
			androidEqualizer.setBandLevel(band, (short)(level + 100));
			Log.i("", "band level up " + androidEqualizer.getBandLevel(band));
		}
	}
	
	public void setBandLevelDown(short band)
	{
		short[] range = androidEqualizer.getBandLevelRange();
		Log.i("", "min range " + range[0] + "max range " + range[1]);
		if(androidEqualizer.getBandLevel(band) > range[0])
		{
			short level = androidEqualizer.getBandLevel(band);
			androidEqualizer.setBandLevel(band, (short)(level - 100));
			Log.i("", "band level up " + androidEqualizer.getBandLevel(band));
		}
	}

	public short setBassBoostUp(MediaPlayer androidMediaPlayer)
	{
		//BassBoost bassBoost = new BassBoost(0, androidMediaPlayer.getAudioSessionId());
	//	bassBoost.setEnabled(true);
		if (currentBoost < BASS_BOOST_MAX_STRENGTH)
		{
			short strength = (short) ((short) bassBoost.getRoundedStrength() + BASS_BOOST_RANGE);
			bassBoost.setStrength(strength);
			currentBoost = bassBoost.getRoundedStrength();
		}
		return bassBoost.getRoundedStrength(); 
	}

	public short setbassBoostDown(MediaPlayer androidMediaPlayer)
	{
	//	BassBoost bassBoost = new BassBoost(0, androidMediaPlayer.getAudioSessionId());
	//	bassBoost.setEnabled(true);
		if (currentBoost > BASS_BOOST_MIN_STRENGTH)
		{
			short strength = (short) ((short) bassBoost.getRoundedStrength() - BASS_BOOST_RANGE);
			bassBoost.setStrength(strength);
			currentBoost = bassBoost.getRoundedStrength();
		}
		return bassBoost.getRoundedStrength();
	}

}
