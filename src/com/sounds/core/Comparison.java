package com.sounds.core;

import com.musicg.*;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

public class Comparison implements Runnable{
	Wave song, reference;
	float score;
	
	public Comparison(Wave song, Wave reference){
		this.song = song;
		this.reference = reference;
	}
	
	public void run(){
		FingerprintSimilarity similarity_score = song.getFingerprintSimilarity(reference);
		
		this.score = similarity_score.getScore();
	}
	
	public float get_score(){
		return score;
	}
}
