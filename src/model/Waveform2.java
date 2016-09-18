package model;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.JFrame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

public class Waveform2 {

	public static void main(String[] args) {
		try {

			JFrame frame = new JFrame("Waveform Display Simulator"); 
			frame.setBounds(200,200, 500, 350);

			String filename = "music/CSIE_NIGHT.wav";
			File file = new File(filename); 
			AudioInputStream audioInputStream 
				= AudioSystem.getAudioInputStream(file);
        
			WaveformPanelContainer container = new WaveformPanelContainer(); 
			container.setAudioToDisplay(audioInputStream);
       
			frame.getContentPane().setLayout(new BorderLayout());		
			frame.getContentPane().add(container, BorderLayout.CENTER);
		
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
			frame.validate();
			frame.repaint();

		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
