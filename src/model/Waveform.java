package model;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

public class Waveform extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Waveform");

		String filename = "music/CSIE_NIGHT.wav";
		File file = new File(filename);

		int available = 0;
		int[] amp = new int[available];
		double[][] audioBytes = new double[0][0];
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			AudioInputStream din = null;
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			din = AudioSystem.getAudioInputStream(decodedFormat, ais);

			audioBytes = getAudioBytes(din);
			ais.close();
			din.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (audioBytes != null) {
			int max=0;
			
			final NumberAxis xAxis = new NumberAxis(0, 10000, 10);
			final NumberAxis yAxis = new NumberAxis(-5, 5, 1);
			final AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis, yAxis);
			ac.setCreateSymbols(false);
			ac.setHorizontalGridLinesVisible(false);
			ac.setVerticalGridLinesVisible(false);

			Series<Number, Number> series = new Series<>();
			double gain=0.1*audioBytes[0].length;
			for (int i = 0; i < audioBytes[0].length; i += audioBytes[0].length/10000) {
				series.getData().add(new Data<Number, Number>(i, (double)(audioBytes[0][i]*gain/audioBytes[0].length)));
				System.out.println((double)(audioBytes[0][i]*gain/audioBytes[0].length));
				
				if(audioBytes[0][i]*2/audioBytes[0].length>10){
					max++;
				}
				if (i % 10000 == 0) {
					System.out.println("processing to " + i);
				}
			}

			ac.getData().add(series);
			Scene scene = new Scene(ac, 1200, 600);

			primaryStage.setScene(scene);
			primaryStage.show();
			System.out.println("end "+max);
		}
	}

	private int getSixteenBitSample(int high, int low) {
		return (high << 8) + (low & 0x00ff);
	}

	public double[][] getAudioBytes(AudioInputStream ais) {
		int frameLength = (int) ais.getFrameLength();
		int frameSize = (int) ais.getFormat().getFrameSize();
		double frameRate = (double) ais.getFormat().getFrameRate();
		System.out.println(frameLength/frameRate);
		byte[] bytes = new byte[frameLength * frameSize];
		int result = 0;
		try {
			result = ais.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int numChannels = ais.getFormat().getChannels();
		double[][] toReturn = new double[numChannels][frameLength];
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
		return toReturn;
	}
}
