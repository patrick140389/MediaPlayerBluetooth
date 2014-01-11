package com.wessolowski.app.mediaplayerbluetooth;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.wessolowski.app.android.util.adapter.CoverFlowImageAdapter;
import com.wessolowski.app.android.util.coverflow.CoverFlow;
import com.wessolowski.app.mediaplayerbluetooth.config.Config;
import com.wessolowski.wessolowski.app.mediaplayerbluetooth.R;

public class MainActivity extends Activity
{

	MediaPlayer					mediaPlayer	= null;

	private Button				playPause;
	private Button				titleBackward;
	private Button				titleForward;

	private static final String	TAG			= MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final CoverFlow coverFlow = (CoverFlow) findViewById(R.id.coverflow);
		if (coverFlow == null)
		{
			System.out.println("coverFlow is null");
		}
		else
		{
			System.out.println("coverFlow is not null");
			setUptCoverFlow(coverFlow);
		}

		Uri uriToPlay = Uri.fromFile(new File("/storage/sdcard0/Music/would.mp3"));
		
		playPause = (Button) findViewById(R.id.playPause);
		titleForward = (Button) findViewById(R.id.titleForward);
		titleBackward = (Button) findViewById(R.id.titleBackward);
		
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try
		{
			mediaPlayer.setDataSource(this, uriToPlay);
		} catch (IllegalArgumentException e)
		{
			Log.i(TAG, "IllegalArgumentException");
			e.printStackTrace();
		} catch (SecurityException e)
		{
			Log.i(TAG, "SecurityException");
			e.printStackTrace();
		} catch (IllegalStateException e)
		{
			Log.i(TAG, "IllegalStateException");
			e.printStackTrace();
		} catch (IOException e)
		{
			Log.i(TAG, "IOException");
			e.printStackTrace();
		}
		try
		{
			mediaPlayer.prepare();
		} catch (IllegalStateException e)
		{
			Log.i(TAG, "IllegalStateException");
			e.printStackTrace();
		} catch (IOException e)
		{
			Log.i(TAG, "IOException");
			e.printStackTrace();
		}

//		playPause.setOnLongClickListener(new OnLongClickListener()
//		{
//
//			@Override
//			public boolean onLongClick(View v)
//			{
//				if (mediaPlayer.isPlaying())
//				{
//					mediaPlayer.pause();
//					mediaPlayer.stop();
//					return true;
//				}
//				return false;
//			}
//		});

		playPause.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mediaPlayer.isPlaying())
				{
					mediaPlayer.pause();
				}
				else
				{
					mediaPlayer.start();
				}
			}
		});

		titleForward.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

			}
		});

		titleBackward.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setUptCoverFlow(final CoverFlow coverFlow)
	{
		//@formatter:off
		
		coverFlow.configureCoverFlow(Config.COVERFLOW_STYLEABLE.COVERFLOW.getCoverFlowStyleable(),
				Config.COVERFLOW_STYLEABLE.COVERFLOW_IMAGE_WIDTH.getCoverFlowStyleableDimensions(),
				Config.COVERFLOW_STYLEABLE.COVERFLOW_IMAGE_HEIGHT.getCoverFlowStyleableDimensions());
		
		//@formatter:on

		BaseAdapter coverImageAdapter = new CoverFlowImageAdapter(this, Config.DRAWABLE_RESSOURCE_CONVENTION_NAMES.toStringArray());

		coverFlow.setAdapter(coverImageAdapter);
		coverFlow.setSelection(2, true);
	}
}
