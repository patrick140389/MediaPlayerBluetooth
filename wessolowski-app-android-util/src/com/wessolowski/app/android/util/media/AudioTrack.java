package com.wessolowski.app.android.util.media;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

public class AudioTrack
{
	private Context context;
	
	private String trackName;
	private int trackLength;
	
	private MediaPlayer audioPlayer = null;
	private Uri trackUri;
	
	
	public AudioTrack(Context context, MediaPlayer mediaPlayer, Uri uri) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		this.audioPlayer = mediaPlayer;
		this.trackUri = uri;
		this.context = context;
		
		configureAudioPlayer();
	}
	
	public MediaPlayer getAudioPlayer()
	{
		return audioPlayer;
	}
	
	
	public void configureAudioPlayer() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		audioPlayer.setDataSource(context, trackUri);
		audioPlayer.prepare();
		
	}
	
	

	public String getTrackName()
	{
		return trackName;
	}

	public void setTrackName(String trackName)
	{
		this.trackName = trackName;
	}

	public int getTrackLength()
	{
		return trackLength;
	}

	public void setTrackLength(int trackLength)
	{
		this.trackLength = trackLength;
	}
	
	
	
	

}
