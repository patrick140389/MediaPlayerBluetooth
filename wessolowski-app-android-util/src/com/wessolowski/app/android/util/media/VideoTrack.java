package com.wessolowski.app.android.util.media;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class VideoTrack extends MediaTrack
{

	public VideoTrack(Context context, MediaPlayer mediaPlayer, Uri uri) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		super(context, mediaPlayer, uri);
		// TODO Auto-generated constructor stub
	}

}
