package com.wessolowski.app.android.util.media;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.wessolowski.app.util.checks.Checks;
import com.wessolowski.app.util.converters.Converter;
import com.wessolowski.app.util.ressources.FileLoader;

public class BaseMediaPlayer
{

	private int						MODE				= 0;
	private boolean					PRESSPAUSE			= false;
	private boolean					PREPARED			= false;
	private float					VOLUME				= 0f;
	private Context					context;

	private static ArrayList<Uri>	audioUris			= new ArrayList<Uri>();
	private static ArrayList<Uri>	videoUris			= new ArrayList<Uri>();

	private AudioTrack				actualAudioTrack	= null;
	private VideoTrack				actualVideoTrack	= null;
	private MediaTrack				actualMediaTrack	= null;

	private static BaseMediaPlayer	baseMediaPlayer		= null;

	private static final String		TAG					= BaseMediaPlayer.class.getSimpleName();

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
		try
		{
			if (actualMediaTrack != null)
			{
				actualMediaTrack.getMediaPlayer().stop();
				actualMediaTrack.getMediaPlayer().release();
			}
			if (MODE == 0 && audioUris.size() != 0)
			{
				// audio
				AudioTrack audioTrack = new AudioTrack(context, new MediaPlayer(), audioUris.get(index));
				audioTrack.preparePlayer();
				actualAudioTrack = audioTrack;
				actualMediaTrack = actualAudioTrack;
				return true;
			}
			else if (MODE == 1 && videoUris.size() != 0)
			{
				// video
				VideoTrack videoTrack = new VideoTrack(context, new MediaPlayer(), videoUris.get(index));
				videoTrack.preparePlayer();
				actualVideoTrack = videoTrack;
				actualMediaTrack = actualVideoTrack;
				return true;
			}
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
		return false;
	}

	public synchronized boolean play(int index)
	{

		if (PREPARED && setActualMediaPlayers(index) && actualMediaTrack != null && !actualMediaTrack.getMediaPlayer().isPlaying())
		{
			Log.i(TAG, "Track: " + actualMediaTrack.getTrackName());
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
			if (list.equals(audioUris) && list.get(i).equals(actualAudioTrack.getTrackUri()))
			{
				break;
			}
			else if (list.equals(videoUris) && list.get(i).equals(actualVideoTrack.getTrackUri()))
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
			iterateMediaLists(audioUris, forOrBackTrack);
		}
		else if (MODE == 1)
		{
			iterateMediaLists(videoUris, forOrBackTrack);
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
		loadAllMusicAndVideoUris();
		setActualMediaPlayers(0);
		PREPARED = true;
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

		int audioUriSize = loadAllMusicUris();
		int videoUriSize = loadAllVideoUris();

		return audioUriSize + videoUriSize;
	}

	public int loadAllMusicUris()
	{
		File dir = new File("/storage/sdcard0/Music/");
		ArrayList<Uri> loadedUris = FileLoader.loadUris(dir, ".mp3", ".m4a");
		audioUris.addAll(loadedUris);

		return loadedUris.size();
	}

	public int loadAllVideoUris()
	{

		return 0;
	}

	public static ArrayList<Uri> getAudioTrackList()
	{
		return audioUris;
	}

	public static ArrayList<Uri> getVideoTrackList()
	{
		return videoUris;
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
