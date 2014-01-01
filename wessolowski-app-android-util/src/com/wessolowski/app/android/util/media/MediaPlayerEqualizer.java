package com.wessolowski.app.android.util.media;

import com.wessolowski.app.util.checks.Checks;

import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.util.Log;

public class MediaPlayerEqualizer
{
	private static final short			BASS_BOOST_RANGE		= 50;
	private static final short			BASS_BOOST_MIN_STRENGTH	= 0;
	private static final short			BASS_BOOST_MAX_STRENGTH	= 1000;
	
	private static MediaPlayerEqualizer	mediaPlayerEqualizer	= null;
	
	private static final String			TAG						= MediaPlayerEqualizer.class.getSimpleName();

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

	short currentBoost =  0;

	public short setBassBoostUp(MediaPlayer androidMediaPlayer)
	{
		BassBoost bassBoost = new BassBoost(0, androidMediaPlayer.getAudioSessionId());
		bassBoost.setEnabled(true);
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
		BassBoost bassBoost = new BassBoost(0, androidMediaPlayer.getAudioSessionId());
		bassBoost.setEnabled(true);
		if (currentBoost > BASS_BOOST_MIN_STRENGTH)
		{
			short strength = (short) ((short) bassBoost.getRoundedStrength() - BASS_BOOST_RANGE);
			bassBoost.setStrength(strength);
			currentBoost = bassBoost.getRoundedStrength();
		}
		return bassBoost.getRoundedStrength();
	}

}
