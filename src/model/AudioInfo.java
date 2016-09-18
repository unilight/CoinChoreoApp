package model;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

public class AudioInfo {
	AudioInputStream audioInputStream;
	public int[][] toReturn;
	int numChannels;
	
	public AudioInfo(AudioInputStream audioInputStream) {
		this.audioInputStream = audioInputStream;
		int frameLength = (int) audioInputStream.getFrameLength();
		int frameSize = (int) audioInputStream.getFormat().getFrameSize();
		byte[] bytes = new byte[frameLength * frameSize];
		int result = 0;
		try {
			result = audioInputStream.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		numChannels = audioInputStream.getFormat().getChannels();
		toReturn = new int[numChannels][frameLength];
		int sampleIndex = 0;

		for (int t = 0; t < bytes.length;) {
			for (int channel = 0; channel < numChannels; channel++) {
				int low = (int) bytes[t];
				t++;
				int high = (int) bytes[t];
				t++;
				int sample = getSixteenBitSample(high, low);
				toReturn[channel][sampleIndex] = sample;
			}
			sampleIndex++;
		}
	}
	private int getSixteenBitSample(int high, int low) {
		return (high << 8) + (low & 0x00ff);
	}
	public int getNumberOfChannels() {
		return numChannels;
	}

}
