package com.wessolowski.app.android.util.media;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

abstract class MediaTrack
{

	private Context		context;

	private String		trackName;
	private int			trackLength;

	private MediaPlayer	mediaPlayer	= null;
	private Uri			trackUri;

	protected MediaTrack(Context context, MediaPlayer mediaPlayer, Uri uri) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		this.mediaPlayer = mediaPlayer;
		this.trackUri = uri;
		this.context = context;

		configureMediaPlayer();
	}

	protected MediaPlayer getMediaPlayer()
	{
		return mediaPlayer;
	}

	protected void configureMediaPlayer() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
}
