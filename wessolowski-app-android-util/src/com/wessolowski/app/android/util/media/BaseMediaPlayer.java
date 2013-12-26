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
	private boolean							PREPARED			= false;
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
		if (PREPARED)
		{
			if (MODE == 0)
			{
				if (actualAudioTrack == null)
				{
					Log.i("play", "audioTracks size: " + audioTracks.size());
					actualAudioTrack = audioTracks.get(index);
					actualAudioTrack.getAudioPlayer().seekTo(0);
					actualAudioTrack.getAudioPlayer().start();
				}
				else if (!actualAudioTrack.getAudioPlayer().isPlaying())
				{
					actualAudioTrack = audioTracks.get(index);
					actualAudioTrack.getAudioPlayer().seekTo(0);
					actualAudioTrack.getAudioPlayer().start();
				}
				else
				{
					pause();
					play(index);
				}
			}
			else if (MODE == 1)
			{
				// Here the videoTrackPlayer
			}
		}
	}

	public synchronized void pause()
	{
		if (PREPARED)
		{
			if (MODE == 0 && actualAudioTrack != null)
			{
				actualAudioTrack.getAudioPlayer().pause();
			}
			else if (MODE == 1 && actualVideoTrack != null)
			{
				// Here the videoTrackPlayer
			}
		}
	}

	public synchronized void stop()
	{
		if (PREPARED)
		{
			if (MODE == 0)
			{
				actualAudioTrack.getAudioPlayer().stop();
			}
			else if (MODE == 1)
			{
				// Here the videoTrackPlayer
			}
		}
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

	private void identifyNewTrack(boolean forOrBackTrack)
	{
		if (MODE == 0)
		{
			for (int i = 0; i < audioTracks.size(); i++)
			{
				if (audioTracks.get(i).equals(actualAudioTrack))
				{
					if (forOrBackTrack)
					{
						if (i < (audioTracks.size() - 1))
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
							changeTrack((audioTracks.size() - 1));
						}
						else
						{
							changeTrack(i--);
						}
					}
				}
			}
		}
		else if (MODE == 1)
		{

		}
	}

	public boolean isPlaying()
	{
		// TODO besser machen mit enum
		if (PREPARED)
		{
			if (MODE == 0 && actualAudioTrack != null)
			{
				return actualAudioTrack.getAudioPlayer().isPlaying();
			}
			else if (MODE == 0 && actualAudioTrack == null)
			{
				return false;
			}
			else if (MODE == 1 && actualVideoTrack != null)
			{
				return true;
			}
			else if (MODE == 1 && actualVideoTrack == null)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
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
		PREPARED = true;
	}

	public void loadAudioPlayerList()
	{
		// TODO nur 30 uris auf einmal laden!!!

		for (int i = 0; i < audioUris.size(); i++)
		{
			Log.i("loadAudioPlayerList", "i: " + i);
			try
			{
				AudioTrack audioTrack = new AudioTrack(context, new MediaPlayer(), audioUris.get(i));
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
	}

	public void loadVideoPlayerList()
	{

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

}
