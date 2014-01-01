package com.wessolowski.app.mediaplayerbluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wessolowski.app.android.util.adapter.CoverFlowImageAdapter;
import com.wessolowski.app.android.util.coverflow.CoverFlow;
import com.wessolowski.app.android.util.media.BaseMediaPlayer;
import com.wessolowski.app.mediaplayerbluetooth.config.Config;
import com.wessolowski.wessolowski.app.mediaplayerbluetooth.R;

public class MainActivity extends Activity
{
	private static final boolean	PAUSE_PRESSED	= true;

	int								index			= 0;
	private Button					playPause;
	private Button					titleBackward;
	private Button					titleForward;
	private Button					volumeUp;
	private Button					volumeDown;
	private TextView				title;

	BaseMediaPlayer					baseMediaPlayer	= null;

	private static final String		TAG				= MainActivity.class.getSimpleName();

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

		playPause = (Button) findViewById(R.id.playPause);
		titleForward = (Button) findViewById(R.id.titleForward);
		titleBackward = (Button) findViewById(R.id.titleBackward);
		volumeUp = (Button) findViewById(R.id.volumeUp);
		volumeDown = (Button) findViewById(R.id.volumeDown);
		title = (TextView) findViewById(R.id.title);

		baseMediaPlayer = BaseMediaPlayer.getInstance(this);

		baseMediaPlayer.setMode(BaseMediaPlayer.AUDIO_MODE);
		baseMediaPlayer.loadMediaPlayer();

		playPause.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (baseMediaPlayer.isPlaying())
				{
					Log.i(TAG, "is Playing true: " + baseMediaPlayer.isPlaying());
					baseMediaPlayer.pause(PAUSE_PRESSED);
				}
				else
				{
					Log.i(TAG, "is Playing false: " + baseMediaPlayer.isPlaying());
					baseMediaPlayer.play(index);
					title.setText(baseMediaPlayer.getTrackName());
				}
			}
		});

		titleForward.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Log.i(TAG, "index vor erhoehung: " + index);
				index++;
				Log.i(TAG, "nach vor erhoehung: " + index);
				if (index > (baseMediaPlayer.getAudioTrackList().size() - 1))
				{
					index = 0;
					Log.i(TAG, "index 0 " + index);
				}
				baseMediaPlayer.changeTrack(index);
				title.setText(baseMediaPlayer.getTrackName());
			}
		});

		titleBackward.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				index--;
				if (index < 0)
				{
					index = (baseMediaPlayer.getAudioTrackList().size() - 1);
				}
				baseMediaPlayer.changeTrack(index);
				title.setText(baseMediaPlayer.getTrackName());
			}
		});

		volumeUp.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setVolumeUp();
			}
		});

		volumeDown.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setVolumeDown();
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
