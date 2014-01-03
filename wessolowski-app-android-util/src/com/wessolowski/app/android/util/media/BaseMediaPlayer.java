package com.wessolowski.app.android.util.media;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mathematik.Mathematik;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import classholder.ClassHolderOne;
import classholder.SortHolder;

import com.wessolowski.app.util.checks.Checks;
import com.wessolowski.app.util.ressources.FileLoader;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

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
	
	private BaseAudioTrack				actualAudioTrack		= null;
	private BaseVideoTrack				actualVideoTrack		= null;
	private BaseMediaTrack				actualMediaTrack		= null;
	
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
	
	
	
	public void loadMediaPlayer()
	{
		loadAllMusicAndVideoUris();
		try
		{
			actualAudioTrack = new BaseAudioTrack(context, new MediaPlayer());
			actualAudioTrack.configureMediaPlayer();
			Log.i(TAG, "sessionID: " + actualAudioTrack.getMediaPlayer().getAudioSessionId());
			mediaPlayerEqualizer = MediaPlayerEqualizer.getInstance();
			mediaPlayerEqualizer.configEqualizer(actualAudioTrack.getMediaPlayer().getAudioSessionId());
			playSong(0);
			
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PREPARED = true;
		
	}
	
	
	
	private void playSong(int index)
	{
		try
		{
			actualAudioTrack.getMediaPlayer().reset();
			actualAudioTrack.setUri(audioTracks.get(index).URI);
			actualAudioTrack.preparePlayer();
			Log.i(TAG, "sessionID: " + actualAudioTrack.getMediaPlayer().getAudioSessionId());
			actualAudioTrack.getMediaPlayer().start();
			actualAudioTrack.setTrackName(audioTracks.get(index).NAME);
			
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static byte[] decode(String path, int startMs, int maxMs) throws IOException, DecoderException
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(1024);
		
		float totalMs = 0;
		boolean seeking = true;
		
		File file = new File(path);
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file), (8 * 1024));
		try
		{
			Bitstream bitstream = new Bitstream(inputStream);
			Decoder decoder = new Decoder();
			
			boolean done = false;
			while (!done)
			{
				Header frameHeader = bitstream.readFrame();
				if (frameHeader == null)
				{
					done = true;
				}
				else
				{
					totalMs += frameHeader.ms_per_frame();
					
					if (totalMs >= startMs)
					{
						seeking = false;
					}
					
					if (!seeking)
					{
						SampleBuffer output = (SampleBuffer) decoder.decodeFrame(frameHeader, bitstream);
						
						if (output.getSampleFrequency() != 44100 || output.getChannelCount() != 2)
						{
							throw new DecoderException("mono or non-44100 MP3 not supported", null);
						}
						
						short[] pcm = output.getBuffer();
						for (short s : pcm)
						{
							outStream.write(s & 0xff);
							outStream.write((s >> 8) & 0xff);
						}
					}
					
					if (totalMs >= (startMs + maxMs))
					{
						done = true;
					}
				}
				bitstream.closeFrame();
			}
			
			return outStream.toByteArray();
		}
		catch (BitstreamException e)
		{
			throw new IOException("Bitstream error: " + e);
		}
		catch (DecoderException e)
		{
			Log.w(TAG, "Decoder error", e);
			throw new DecoderException(maxMs, e);
		}
		finally
		{
		}
	}
	
	
	
	public void playWithAudioTrack() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException, DecoderException
	{
		String path = "/storage/sdcard0/Music/lordi.mp3";
		File file = new File(path);
		
		MediaPlayer mPlayer = new MediaPlayer();
		mPlayer.setDataSource(path);
		mPlayer.prepare();
		
		int durationMs = mPlayer.getDuration();
		byte[] buffer = decode(path, 0, durationMs);
		
		int minSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
		AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO,	AudioFormat.ENCODING_PCM_16BIT, minSize, AudioTrack.MODE_STATIC);
		track.write(buffer, 0, (int)file.length());
		track.play();
	}
	
	
	
	public synchronized boolean play(int index)
	{
		if (actualAudioTrack.getMediaPlayer().isPlaying())
		{
			if (Checks.checkNull(actualAudioTrack))
			{
				pause();
			}
		}
		else
		{
			if (Checks.checkNull(actualAudioTrack))
			{
				start();
			}
		}
		
		return false;
	}
	
	
	
	public synchronized boolean pause()
	{
		if (Checks.checkNull(actualAudioTrack))
		{
			actualAudioTrack.getMediaPlayer().pause();
		}
		return false;
	}
	
	
	
	public synchronized boolean start()
	{
		if (Checks.checkNull(actualAudioTrack))
		{
			actualAudioTrack.getMediaPlayer().start();
		}
		return false;
	}
	
	
	
	public synchronized boolean stop()
	{
		return false;
	}
	
	
	
	public synchronized void changeTrack(int index)
	{
		playSong(index);
	}
	
	
	
	public String getTrackName()
	{
		return actualAudioTrack.getTrackName();
	}
	
	
	
	public synchronized void nextTrack()
	{
		identifyNewTrack(NEXT_TRACK);
	}
	
	
	
	public synchronized void previousTrack()
	{
		identifyNewTrack(PREVIOUS_TRACK);
	}
	
	
	
	@SuppressWarnings ("rawtypes")
	private void iterateMediaLists(ArrayList list, boolean forOrBackTrack)
	{
		// dont do it at work!!!
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
			// hm... strange...
			if (i == 0)
			{
				changeTrack((list.size() - 1));
			}
			else
			{
				// boese!
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
		return false;
	}
	
	
	
	public synchronized void setMode(int mode)
	{
		this.MODE = mode;
	}
	
	
	
	public void addAudioUri(String trackName, Uri uri)
	{
		ClassHolderOne holder = new ClassHolderOne(trackName, uri);
		audioTracks.add(holder);
	}
	
	
	
	public void addVideoUri(String trackName, Uri uri)
	{
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
		short strength = mediaPlayerEqualizer.setBassBoostUp();
		Log.i(TAG, "strength UP: " + strength);
	}
	
	
	
	public void setBoostDown()
	{
		short strength = mediaPlayerEqualizer.setbassBoostDown();
		Log.i(TAG, "strength DOWN: " + strength);
	}
	
	
	
	public float getCurrentVolume()
	{
		return CURRENT_VOLUME;
	}
	
	
	
	private void setVolume(float volume)
	{
		actualAudioTrack.getMediaPlayer().setVolume(volume, volume);
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
	
	
	
	public void setBandUp(short band)
	{
		mediaPlayerEqualizer.setBandLevelUp(band);
	}
	
	
	
	public void setBandDown(short band)
	{
		mediaPlayerEqualizer.setBandLevelDown(band);
	}
	
	
	
	public void setVirtualizerLevelUp()
	{
		mediaPlayerEqualizer.setVirtualizerLevelUp();
	}
	
	
	
	public void setVirtualizerLevelDown()
	{
		mediaPlayerEqualizer.setVirtualizerLevelDown();
	}
	
	
	
//	public void setPrev()
//	{
//		mediaPlayerEqualizer.setPresetReverb();
//	}
	
	public void destroyBaseMediaPlayer()
	{
	}
}
