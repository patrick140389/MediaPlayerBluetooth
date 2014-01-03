package com.wessolowski.app.android.util.media;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class BaseAudioTrack extends BaseMediaTrack
{

	public BaseAudioTrack(Context context, MediaPlayer mediaPlayer) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		super(context, mediaPlayer);
		
	}

}
