package com.wessolowski.app.mediaplayerbluetooth;

import java.io.IOException;

import javazoom.jl.decoder.DecoderException;

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
	private Button					bassBoostUp;
	private Button					bassBoostDown;
	private Button					bandOneUp;
	private Button					bandOneDown;
	private Button					bandTwoUp;
	private Button					bandTwoDown;
	private Button					bandThreeUp;
	private Button					bandThreeDown;
	private Button					bandFourUp;
	private Button					bandFourDown;
	private Button					bandFiveUp;
	private Button					bandFiveDown;
	private Button					loudnessUp;
	private Button					loudnessDown;
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
		bassBoostUp = (Button) findViewById(R.id.bassBoostUp);
		bassBoostDown = (Button) findViewById(R.id.bassBoostDown);
		loudnessUp = (Button) findViewById(R.id.loudnessUp);
		loudnessDown = (Button) findViewById(R.id.loudnessDown);
		bandOneUp = (Button) findViewById(R.id.bandOneUp);
		bandOneDown = (Button) findViewById(R.id.bandOneDown);
		bandTwoUp = (Button) findViewById(R.id.bandTwoUp);
		bandTwoDown = (Button) findViewById(R.id.bandTwoDown);
		bandThreeUp = (Button) findViewById(R.id.bandThreeUp);
		bandThreeDown = (Button) findViewById(R.id.bandThreeDown);
		bandFourUp = (Button) findViewById(R.id.bandFourUp);
		bandFourDown = (Button) findViewById(R.id.bandFourDown);
		bandFiveUp = (Button) findViewById(R.id.bandFiveUp);
		bandFiveDown = (Button) findViewById(R.id.bandFiveDown);
		title = (TextView) findViewById(R.id.title);

		baseMediaPlayer = BaseMediaPlayer.getInstance(this);

		baseMediaPlayer.setMode(BaseMediaPlayer.AUDIO_MODE);
		baseMediaPlayer.loadMediaPlayer();

		playPause.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				baseMediaPlayer.play(index);
				title.setText(baseMediaPlayer.getTrackName());
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

		bassBoostUp.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBoostUp();
			}
		});

		bassBoostDown.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBoostDown();
			}
		});

		loudnessUp.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// baseMediaPlayer.setLoudnessLevelUp();
				baseMediaPlayer.setVirtualizerLevelUp();
			}
		});

		loudnessDown.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// baseMediaPlayer.setLoudnessLevelDown();
				baseMediaPlayer.setVirtualizerLevelDown();
			}
		});

		bandOneUp.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandUp((short) 0);
			}
		});

		bandOneDown.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandDown((short) 0);
			}
		});

		bandTwoUp.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandUp((short) 1);
			}
		});

		bandTwoDown.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandDown((short) 1);
			}
		});

		bandThreeUp.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandUp((short) 2);
			}
		});

		bandThreeDown.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandDown((short) 2);
			}
		});

		bandFourUp.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandUp((short) 3);
			}
		});

		bandFourDown.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandDown((short) 3);
			}
		});

		bandFiveUp.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandUp((short) 4);
			}
		});

		bandFiveDown.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				baseMediaPlayer.setBandDown((short) 4);
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
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		baseMediaPlayer.destroyBaseMediaPlayer();
		super.onDestroy();
		Log.i(TAG, "destroyed");
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
