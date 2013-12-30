package com.wessolowski.app.android.util.media;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class AudioTrack extends MediaTrack
{

	public AudioTrack(Context context, MediaPlayer mediaPlayer, Uri uri) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		super(context, mediaPlayer, uri);
	}

}
