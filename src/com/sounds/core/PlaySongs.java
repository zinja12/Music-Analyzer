package com.sounds.core;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class PlaySongs implements Runnable{
	String filename;
	
	public PlaySongs(String filename){
		this.filename = filename;
	}
	
	public void play_sound(){
		try {
		    File yourFile;
		    AudioInputStream stream;
		    AudioFormat format;
		    DataLine.Info info;
		    Clip clip;
		    
		    yourFile = new File(filename);
		    stream = AudioSystem.getAudioInputStream(yourFile);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		}
		catch (Exception e) {
		    //whatevers
		}
	}
	
	public void run(){
		play_sound();
	}
}
