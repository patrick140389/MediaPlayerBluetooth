package com.wessolowski.app.android.util.media;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class BaseMediaPlayer
{

	private int								MODE				= 0;
	private Context							context;

	private ArrayList<Uri>					audioUris			= new ArrayList<Uri>();
	private ArrayList<Uri>					videoUris			= new ArrayList<Uri>();
	private static ArrayList<AudioTrack>	audioTracks			= new ArrayList<AudioTrack>();
	private static ArrayList<VideoTrack>	videoTracks			= new ArrayList<VideoTrack>();

	private AudioTrack						actualAudioTrack	= null;
	private VideoTrack						actualVideoTrack	= null;

	private static BaseMediaPlayer			baseMediaPlayer		= null;

	private BaseMediaPlayer(Context context)
	{
		this.context = context;
	}

	public static BaseMediaPlayer getInstance(Context context)
	{
		if (baseMediaPlayer == null)
		{
			baseMediaPlayer = new BaseMediaPlayer(context);
		}
		return baseMediaPlayer;
	}

	public synchronized void play(int index)
	{
		if (MODE == 0)
		{
			if (actualAudioTrack == null)
			{
				actualAudioTrack = audioTracks.get(index);
				actualAudioTrack.getAudioPlayer().start();
			}
			else if (!actualAudioTrack.getAudioPlayer().isPlaying())
			{
				actualAudioTrack = audioTracks.get(index);
				actualAudioTrack.getAudioPlayer().start();
			}
			else
			{
				pause(MODE);
				play(index);
			}
		}
		else if (MODE == 1)
		{
			// Here the videoTrackPlayer
		}
	}

	public synchronized void pause(int mode)
	{
		if (mode == 0)
		{
			actualAudioTrack.getAudioPlayer().pause();
		}
		else if (mode == 1)
		{
			// Here the videoTrackPlayer
		}

	}

	public synchronized void stop(int mode)
	{
		if (mode == 0)
		{
			actualAudioTrack.getAudioPlayer().stop();
		}
		else if (mode == 1)
		{
			// Here the videoTrackPlayer
		}

	}

	public synchronized void changeTrack(int index)
	{
		if (MODE == 0)
		{
			pause(MODE);
			play(index);
		}
		else if (MODE == 1)
		{
			// Here the videoTrackPlayer
		}
	}

	public synchronized void setMode(int mode)
	{
		this.MODE = mode;
	}

	public void loadMediaPlayer()
	{
		loadAudioPlayerList();
		loadVideoPlayerList();
	}

	public void loadAudioPlayerList()
	{
		// TODO nur 30 uris auf einmal laden!!!

		for (int i = 0; i < audioUris.size(); i++)
		{
			try
			{
				AudioTrack audioTrack = new AudioTrack(context, new MediaPlayer(), audioUris.get(i));
			} catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void loadVideoPlayerList()
	{

	}

	public void addAudioUri(Uri uri)
	{
		audioUris.add(uri);
	}

	public void addVideoUri(Uri uri)
	{
		videoUris.add(uri);
	}

	public int loadAllMusicAndVideoUris()
	{
		// TODO Write in wessolowski-app-util an class that will load all uri's
		// from filesystem
		int uriSize = 0;

		int musicUriSize = loadAllMusicUris();
		int videoUriSize = loadAllVideoUris();

		return musicUriSize + videoUriSize;
	}

	public int loadAllMusicUris()
	{
		return 0;
	}

	public int loadAllVideoUris()
	{
		return 0;
	}

	public static ArrayList<AudioTrack> getAudioTrackList()
	{
		return audioTracks;
	}

	public static ArrayList<VideoTrack> getVideoTrackList()
	{
		return videoTracks;
	}

}
