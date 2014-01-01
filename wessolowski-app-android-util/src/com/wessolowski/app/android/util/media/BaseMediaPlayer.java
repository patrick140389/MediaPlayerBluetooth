package com.wessolowski.app.android.util.media;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mathematik.Mathematik;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.util.Log;
import classholder.ClassHolderOne;
import classholder.SortHolder;

import com.wessolowski.app.util.checks.Checks;
import com.wessolowski.app.util.ressources.FileLoader;

public class BaseMediaPlayer
{

	public static final int				AUDIO_MODE				= 0;
	public static final int				VIDEO_MODE				= 1;
	private static float				CURRENT_VOLUME			= 0.02f;
	private static final float			MAX_VOLUME				= 1.0f;
	private static final float			MIN_VOLUME				= 0.0f;
	private static final float			VOLUME_RANGE			= 0.02f;
	private static final int			DECIMAL_PLACE			= 2;
	public static final boolean			NEXT_TRACK				= true;
	public static final boolean			PREVIOUS_TRACK			= false;
	private static final String			DEFAULT_AUDIO_DIRECTORY	= "/storage/sdcard0/Music/";

	private int							MODE					= AUDIO_MODE;
	private boolean						PRESS_PAUSE				= false;
	private boolean						PREPARED				= false;

	private Context						context;

	private ArrayList<ClassHolderOne>	audioTracks				= new ArrayList<ClassHolderOne>();
	private ArrayList<ClassHolderOne>	videoTracks				= new ArrayList<ClassHolderOne>();
	private ArrayList<String>			directorySources		= new ArrayList<String>();

	private AudioTrack					actualAudioTrack		= null;
	private VideoTrack					actualVideoTrack		= null;
	private MediaTrack					actualMediaTrack		= null;

	private static BaseMediaPlayer		baseMediaPlayer			= null;
	private MediaPlayerEqualizer		mediaPlayerEqualizer	= null;

	private static final String			TAG						= BaseMediaPlayer.class.getSimpleName();

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

	private boolean setActualMediaPlayer(int index)
	{
		try
		{
			if (actualMediaTrack != null)
			{
				actualMediaTrack.getMediaPlayer().stop();
				actualMediaTrack.getMediaPlayer().release();
			}
			if (MODE == AUDIO_MODE && audioTracks.size() != 0)
			{
				// audio
				AudioTrack audioTrack = new AudioTrack(context, new MediaPlayer(), audioTracks.get(index).URI);
				audioTrack.preparePlayer();
				actualAudioTrack = audioTrack;
				actualMediaTrack = actualAudioTrack;
				actualMediaTrack.setTrackName(audioTracks.get(index).NAME);
				return true;
			}
			else if (MODE == VIDEO_MODE && videoTracks.size() != 0)
			{
				// video
				VideoTrack videoTrack = new VideoTrack(context, new MediaPlayer(), videoTracks.get(index).URI);
				videoTrack.preparePlayer();
				actualVideoTrack = videoTrack;
				actualMediaTrack = actualVideoTrack;
				actualMediaTrack.setTrackName(videoTracks.get(index).NAME);
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
		Log.i(TAG, "PRESS_PAUSE" + PRESS_PAUSE);
		if (PREPARED && actualMediaTrack != null && !actualMediaTrack.getMediaPlayer().isPlaying())
		{
			if (!PRESS_PAUSE && setActualMediaPlayer(index))
			{
				actualMediaTrack.getMediaPlayer().seekTo(0);
				actualMediaTrack.getMediaPlayer().start();
				setVolume(CURRENT_VOLUME);
				PRESS_PAUSE = false;
				return true;
			}
			else if (PRESS_PAUSE)
			{
				int currentPosition = actualMediaTrack.getMediaPlayer().getCurrentPosition();
				actualMediaTrack.getMediaPlayer().seekTo(currentPosition);
				actualMediaTrack.getMediaPlayer().start();
				setVolume(CURRENT_VOLUME);
				PRESS_PAUSE = false;
				return true;
			}
		}
		return false;
	}

	public synchronized boolean pause(boolean pausePressed)
	{
		PRESS_PAUSE = pausePressed;
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
			pause(PRESS_PAUSE);
			play(index);
		}
	}

	public String getTrackName()
	{
		return actualMediaTrack.getTrackName();
	}

	public synchronized void nextTrack()
	{
		identifyNewTrack(NEXT_TRACK);
	}

	public synchronized void previousTrack()
	{
		identifyNewTrack(PREVIOUS_TRACK);
	}

	@SuppressWarnings("rawtypes")
	private void iterateMediaLists(ArrayList list, boolean forOrBackTrack)
	{
		int i;
		for (i = 0; i < list.size(); i++)
		{
			if (list.equals(audioTracks) && list.get(i).equals(actualAudioTrack.getTrackUri()))
			{
				break;
			}
			else if (list.equals(videoTracks) && list.get(i).equals(actualVideoTrack.getTrackUri()))
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
		if (MODE == AUDIO_MODE)
		{
			iterateMediaLists(audioTracks, forOrBackTrack);
		}
		else if (MODE == VIDEO_MODE)
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
		int size = loadAllMusicAndVideoUris();
		Log.i(TAG, "total size tracks: " + size);
		setActualMediaPlayer(0);
		mediaPlayerEqualizer = MediaPlayerEqualizer.getInstance();
		PREPARED = true;
	}

	public void addAudioUri(String trackName, Uri uri)
	{
		// TODO ueberpruefen ob es sich um ein audioformat handelt (mp3 etc)
		ClassHolderOne holder = new ClassHolderOne(trackName, uri);
		audioTracks.add(holder);
	}

	public void addVideoUri(String trackName, Uri uri)
	{
		// TODO ueberpruefen ob es sich um ein videoformat handelt (mp4 etc)
		ClassHolderOne holder = new ClassHolderOne(trackName, uri);
		videoTracks.add(holder);
	}

	public int loadAllMusicAndVideoUris()
	{
		// TODO Write in wessolowski-app-util an class that will load all uri's
		// from filesystem

		int audioUriSize = loadAllMusicUris();
		int videoUriSize = loadAllVideoUris();

		return audioUriSize + videoUriSize;
	}

	public int loadAllMusicUris()
	{
		File dir = null;
		ArrayList<ClassHolderOne> loaded = null;

		if (directorySources.isEmpty())
		{
			dir = new File(DEFAULT_AUDIO_DIRECTORY);
			loaded = FileLoader.loadUrisWithFileName(dir, ".mp3", ".m4a");
		}
		else
		{
			for (int i = 0; i < directorySources.size(); i++)
			{
				dir = new File(directorySources.get(i));
				loaded = FileLoader.loadUrisWithFileName(dir, ".mp3", ".m4a");
			}
		}

		Collections.sort((List<ClassHolderOne>) loaded, new SortHolder());
		audioTracks.addAll(loaded);
		return audioTracks.size();
	}

	public int loadAllVideoUris()
	{
		return 0;
	}

	public void setDirectoryDataSource(ArrayList<String> dataSources)
	{
		if (!dataSources.isEmpty())
		{
			directorySources.addAll(dataSources);
		}
	}

	public void addDirectoryDataSource(String dataSource)
	{
		if (Checks.checkNull(dataSource))
		{
			directorySources.add(dataSource);
		}
	}

	public ArrayList<ClassHolderOne> getAudioTrackList()
	{
		return audioTracks;
	}

	public ArrayList<ClassHolderOne> getVideoTrackList()
	{
		return videoTracks;
	}

	public boolean isPrepared()
	{
		return PREPARED;
	}

	int	boost	= 0;

	public void setBoostUp()
	{
		short strength = mediaPlayerEqualizer.setBassBoostUp(actualMediaTrack.getMediaPlayer());
		Log.i(TAG, "strength UP: " + strength);

		// BassBoost bassBoost = new BassBoost(0,
		// actualMediaTrack.getMediaPlayer().getAudioSessionId());
		// bassBoost.setEnabled(true);
		// Log.i(TAG, "support boost: " + bassBoost.getStrengthSupported());
		// Log.i(TAG, "vorher get strength: " + bassBoost.getRoundedStrength());
		// boost += 100;
		// bassBoost.setStrength((short) boost);
		// Log.i(TAG, "nachher get strength: " +
		// bassBoost.getRoundedStrength());
	}

	public void setBoostDown()
	{
		short strength = mediaPlayerEqualizer.setbassBoostDown(actualMediaTrack.getMediaPlayer());
		Log.i(TAG, "strength DOWN: " + strength);
		// BassBoost bassBoost = new BassBoost(0,
		// actualMediaTrack.getMediaPlayer().getAudioSessionId());
		// Log.i(TAG, "support boost: " + bassBoost.getStrengthSupported());
		// Log.i(TAG, "vorher get strength: " + bassBoost.getRoundedStrength());
		// boost -= 100;
		// bassBoost.setStrength((short) boost);
		// Log.i(TAG, "nachher get strength: " +
		// bassBoost.getRoundedStrength());
	}

	public float getCurrentVolume()
	{
		return CURRENT_VOLUME;
	}

	private void setVolume(float volume)
	{
		actualMediaTrack.getMediaPlayer().setVolume(volume, volume);
	}

	public synchronized float setVolumeUp()
	{
		if (CURRENT_VOLUME < MAX_VOLUME)
		{
			CURRENT_VOLUME = Mathematik.round(CURRENT_VOLUME + VOLUME_RANGE, DECIMAL_PLACE);
			setVolume(CURRENT_VOLUME);
		}
		Log.i(TAG, "current volume UP: " + (CURRENT_VOLUME));
		return CURRENT_VOLUME;
	}

	public float setVolumeDown()
	{
		if (CURRENT_VOLUME > MIN_VOLUME)
		{
			CURRENT_VOLUME = Mathematik.round(CURRENT_VOLUME - VOLUME_RANGE, DECIMAL_PLACE);
			setVolume(CURRENT_VOLUME);
		}
		Log.i(TAG, "current volume DOWN: " + (CURRENT_VOLUME));
		return CURRENT_VOLUME;
	}
}
