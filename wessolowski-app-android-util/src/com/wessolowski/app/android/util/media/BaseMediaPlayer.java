package com.wessolowski.app.android.util.media;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import com.wessolowski.app.util.checks.*;
import java.io.*;
import com.wessolowski.app.util.ressources.*;
import com.wessolowski.app.util.*;
import com.wessolowski.app.util.Coverters.*;

public class BaseMediaPlayer
{

	private int								MODE				= 0;
	private boolean							PREPARED			= false;
	private float							VOLUME				= 0f;
	private Context							context;

	private ArrayList<Uri>					audioUris			= new ArrayList<Uri>();
	private ArrayList<Uri>					videoUris			= new ArrayList<Uri>();
	private static ArrayList<AudioTrack>	audioTracks			= new ArrayList<AudioTrack>();
	private static ArrayList<VideoTrack>	videoTracks			= new ArrayList<VideoTrack>();

	private AudioTrack						actualAudioTrack	= null;
	private VideoTrack						actualVideoTrack	= null;
	private MediaTrack						actualMediaTrack	= null;

	private static BaseMediaPlayer			baseMediaPlayer		= null;

	private static final String				TAG					= BaseMediaPlayer.class.getSimpleName();

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

	private boolean setActualMediaPlayers(int index)
	{
		if (MODE == 0 && audioTracks.size() != 0)
		{
			actualAudioTrack = audioTracks.get(index);
			actualMediaTrack = actualAudioTrack;
			return true;
		}
		else if (MODE == 1 && videoTracks.size() != 0)
		{
			actualVideoTrack = videoTracks.get(index);
			actualMediaTrack = actualVideoTrack;
			return true;
		}
		return false;
	}

	public synchronized boolean play(int index)
	{
		if (PREPARED && setActualMediaPlayers(index) && actualMediaTrack != null && !actualMediaTrack.getMediaPlayer().isPlaying())
		{
				actualMediaTrack.getMediaPlayer().seekTo(0);
				actualMediaTrack.getMediaPlayer().start();
				return true;
		}
		return false;
	}

	public synchronized boolean pause()
	{
		if (Checks.ckeckNull(PREPARED, actualMediaTrack))
		{
				actualMediaTrack.getMediaPlayer().pause();
				return true;
		}
		return false;
	}

	public synchronized boolean stop()
	{
		if (Checks.ckeckNull(PREPARED, actualMediaTrack))
		{
			actualMediaTrack.getMediaPlayer().stop();
			return true;
		}
		return false;
	}

	public synchronized void changeTrack(int index)
	{
		if (PREPARED)
		{
			pause();
			play(index);
		}
	}

	public synchronized void nextTrack()
	{
		identifyNewTrack(true);
	}

	public synchronized void previewsTrack()
	{
		identifyNewTrack(false);
	}

	@SuppressWarnings("rawtypes")
	private void iterateMediaLists(ArrayList list, boolean forOrBackTrack)
	{
		int i;
		for (i = 0; i < list.size(); i++)
		{
			if (list.equals(audioTracks) && list.get(i).equals(actualAudioTrack))
			{
				break;
			}
			else if (list.equals(videoTracks) && list.get(i).equals(actualVideoTrack))
			{
				break;
			}
		}

		if (forOrBackTrack)
		{
			if (i < (list.size() - 1))
			{
				changeTrack(i++);
			}
			else
			{
				changeTrack(0);
			}
		}
		else
		{
			if (i == 0)
			{
				changeTrack((list.size() - 1));
			}
			else
			{
				changeTrack(i--);
			}
		}
	}

	private void identifyNewTrack(boolean forOrBackTrack)
	{
		if (MODE == 0)
		{
			iterateMediaLists(audioTracks, forOrBackTrack);
		}
		else if (MODE == 1)
		{
			iterateMediaLists(videoTracks, forOrBackTrack);
		}
	}

	public boolean isPlaying()
	{
		// TODO besser machen mit enum
		if (Checks.ckeckNull(PREPARED, actualMediaTrack))
		{
			return actualMediaTrack.getMediaPlayer().isPlaying();
		}
		return false;
	}
	
	

	public synchronized void setMode(int mode)
	{
		this.MODE = mode;
	}

	public void loadMediaPlayer()
	{
		loadAudioPlayerList();
		loadVideoPlayerList();
		PREPARED = true;
		setActualMediaPlayers(0);
	}

	public int loadAudioPlayerList()
	{
		// TODO nur 30 uris auf einmal laden!!!
		int size;
		for (size = 0; size < audioUris.size(); size++)
		{
			try
			{
				AudioTrack audioTrack = new AudioTrack(context, new MediaPlayer(), audioUris.get(size));
				audioTrack.preparePlayer();
				audioTracks.add(audioTrack);
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
		return size;
	}

	public int loadVideoPlayerList()
	{
		int size;
		for (size = 0; size < videoUris.size(); size++)
		{
			try
			{
				VideoTrack videoTrack = new VideoTrack(context, new MediaPlayer(), videoUris.get(size));
				videoTrack.preparePlayer();
				videoTracks.add(videoTrack);
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
		return size;
	}

	public void addAudioUri(Uri uri)
	{
		// TODO ueberpruefen ob es sich um ein audioformat handelt (mp3 etc)
		audioUris.add(uri);
	}

	public void addVideoUri(Uri uri)
	{
		// TODO ueberpruefen ob es sich um ein videoformat handelt (mp4 etc)
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
		File dir = new File("/storage/sdcard0/Music/");
		
		
		ArrayList<Uri> loadedUris = FileLoader.loadUris(dir, Converter.toStringArray(".mp3"));
		
	
		
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

	public boolean isPrepared()
	{
		return PREPARED;
	}

	public synchronized float setVolumeUp()
	{
		VOLUME += 0.1f;
		actualMediaTrack.getMediaPlayer().setVolume(VOLUME, VOLUME);
		return VOLUME;
	}

	public float setVolumeDown()
	{
		if (VOLUME > 0)
		{
			VOLUME -= 0.1f;
		}
		actualMediaTrack.getMediaPlayer().setVolume(VOLUME, VOLUME);
		return VOLUME;
	}
}
