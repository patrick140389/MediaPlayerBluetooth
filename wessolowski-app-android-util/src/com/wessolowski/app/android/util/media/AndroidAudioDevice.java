package com.wessolowski.app.android.util.media;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AndroidAudioDevice
{
	AudioTrack track;
	private final static int BUFFER_SIZE = 500000;
	private byte[] buffer = new byte[BUFFER_SIZE*2];

	
//	short[] buffer = new short[1024];

	public AndroidAudioDevice()
	{
		int minSize = AudioTrack.getMinBufferSize(44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, minSize, AudioTrack.MODE_STREAM);
		track.play();
	}

	public void writeSamples(float[] samples)
	{
		int write = 10;
		fillBuffer(samples);
		for(int i = 0; i< samples.length;)
		{
			track.write(buffer, i, write);
			i += write;
		}
	}

	private void fillBuffer(float[] samples)
	{
		for( int i = 0, j = 0; i < samples.length; i++, j+=2 )
		{
			short value = (short)(samples[i] * Short.MAX_VALUE);
			buffer[j] = (byte)(value | 0xff);
			buffer[j+1] = (byte)(value >> 8 );
		}
		
		
//		if (buffer.length < samples.length)
//			buffer = new short[samples.length];
//
//		for (int i = 0; i < samples.length; i++)
//			buffer[i] = (short) (samples[i] * Short.MAX_VALUE);
//		;
	}
}
