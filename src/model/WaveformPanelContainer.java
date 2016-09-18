package model;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WaveformPanelContainer extends JPanel{
	private ArrayList singleChannelWaveformPanels = new ArrayList();
	private AudioInfo audioInfo = null;
   
	public WaveformPanelContainer() {
		setLayout(new GridLayout(0,1));
	}

	public void setAudioToDisplay(AudioInputStream audioInputStream){
		singleChannelWaveformPanels = new ArrayList();
		audioInfo = new AudioInfo(audioInputStream);
		for (int t=0; t<audioInfo.getNumberOfChannels(); t++){
			SingleWaveformPanel waveformPanel = new SingleWaveformPanel(audioInfo, t);
			singleChannelWaveformPanels.add(waveformPanel);
			add(createChannelDisplay(waveformPanel, t));
		}
	}
	private JComponent createChannelDisplay(SingleWaveformPanel waveformPanel,int index) {

       JPanel panel = new JPanel(new BorderLayout());
	   panel.add(waveformPanel, BorderLayout.CENTER);

	   JLabel label = new JLabel("Channel " + ++index);
	   panel.add(label, BorderLayout.NORTH);

	   return panel; 
	} 
}

class SingleWaveformPanel extends JPanel{

	public SingleWaveformPanel(AudioInfo audioInfo, int t) {
		
	}
	
}
