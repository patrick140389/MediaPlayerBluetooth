package com.wessolowski.app.android.util.media;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.wessolowski.app.util.checks.Checks;

abstract class BaseMediaTrack
{

	private Context		context;

	private String		trackName;
	private int			trackLength;

	private MediaPlayer	mediaPlayer	= null;
	private Uri			trackUri;

	protected BaseMediaTrack(Context context, MediaPlayer mediaPlayer) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		this.mediaPlayer = mediaPlayer;
		this.context = context;

		configureMediaPlayer();
	}

	protected MediaPlayer getMediaPlayer()
	{
		return mediaPlayer;
	}

	protected void configureMediaPlayer()
	{
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	protected void setDataSource(Uri uri) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		this.trackUri = uri;
		mediaPlayer.setDataSource(context, trackUri);
	}

	protected void preparePlayer() throws IllegalStateException, IOException
	{
		mediaPlayer.prepare();
	}

	protected String getTrackName()
	{
		return trackName;
	}

	protected Uri getTrackUri()
	{
		return trackUri;
	}

	protected void setTrackName(String trackName)
	{
		this.trackName = trackName;
	}

	protected int getTrackLength()
	{
		return trackLength;
	}

	protected void setTrackLength(int trackLength)
	{
		this.trackLength = trackLength;
	}

		
	protected void destroy()
	{
		if (Checks.checkNull(mediaPlayer))
		{
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
