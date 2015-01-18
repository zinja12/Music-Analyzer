package com.sounds.core;

import java.util.ArrayList;
import java.util.Scanner;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;
import com.musicg.*;
import com.wrapper.spotify.*;


public class Main {
	public static void main(String args[]){
		String song_1 = "res/Mark_Ronson_Uptown_Funk_ft_Bruno_Mars.wav";
		String song_2 = "res/Clair_de_Lune_ Claude_Debussy_Piano.wav";
		String song_3 = "res/sugar_Maroon5_V.wav";
		String song_4 = "res/christina_perri_sad_song.wav";
		String happy_song_ref = "res/sugar_Maroon5_V.wav";
		String slow_sad_song_ref = "res/christina_perri_sad_song.wav";
		
		Wave wave;
		Wave happy_ref = new Wave(happy_song_ref);
		Wave slow_sad_ref = new Wave(slow_sad_song_ref);
		wave = new Wave(song_1);
		
		System.out.println("Song to analyze: " + song_1);
		System.out.println(wave);
		
		double[] amplitudes = wave.getNormalizedAmplitudes();
		double[] amp_deviation_differences = deviation_differences(amplitudes);
		//ArrayList<Double> least_points = find_least_points(amp_deviation_differences, amplitudes);
		int frequency = wave.getWaveHeader().getSampleRate();
		int speed = 2;
		
		float score_1;
		float score_2;
		
		/*if(significant_amp_drops(amplitudes)){
			System.out.println("Dancy");
		} else {
			System.out.println("Not dancy");
		}*/
		
		Comparison happy_comparison = new Comparison(wave, happy_ref);
		Thread t_1 = new Thread(happy_comparison);
		t_1.start();
		
		Comparison sad_comparison = new Comparison(wave, slow_sad_ref);
		Thread t_2 = new Thread(sad_comparison);
		t_2.start();
		
		System.out.println("Thinking...");
		
		try {
			t_1.join();
			t_2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Done thinking.");
		
		/*score_1 = factor_reference_audio(wave, happy_ref);
		score_2 = factor_reference_audio(wave, slow_sad_ref);*/
		score_1 = happy_comparison.get_score();
		score_2 = sad_comparison.get_score();
		
		System.out.println("Fingerprint Similarity Happy Score: " + score_1);
		System.out.println("Fingerprint Similarity Sad Score: " + score_2);
		/*System.out.println("Fingerprint Similarity Happy Score: " + happy_comparison.get_score());
		System.out.println("Fingerprint Similarity Sad Score: " + sad_comparison.get_score());*/
		
		/*System.out.println(wave.getWaveHeader().WAVE_HEADER);*/
		interpret_score(score_1, score_2);
		
		/*print_array(amplitudes);
		print_array(differences);*/
		
		/*System.out.println("Max Amp: " + find_max(amplitudes));
		System.out.println("Max Difference: " + find_max(amp_deviation_differences));*/
	}
	
	public static void interpret_score(float happy_score, float sad_score){
		System.out.print("Mood: ");
		if(happy_score > sad_score){
			System.out.println("Happy, dancy");
		} else if(happy_score == sad_score){
			System.out.println("Neutral");
		} else if(happy_score < sad_score){
			System.out.println("Sad, not dancy");
		}
	}
	
	public static double find_max(double[] array){
		double max = 0;
		
		for(int i = 0; i < array.length; i++){
			if(max < array[i]){
				max = array[i];
			}
		}
		
		return max;
	}
	
	public static double mean(double[] array){
		double mean = 0;
		
		for(int i = 0; i < array.length; i++){
			mean += array[i];
		}
		
		mean /= array.length;
		
		return mean;
	}
	
	public static double find_middle_of_mean_and_max_amp(double[] amps){
		double mean = mean(amps);
		double mean_2 = 0;
		int count = 0;
		
		for(int i = 0; i < amps.length; i++){
			if(amps[i] > mean){
				mean_2 += amps[i];
				count++;
			}
		}
		
		mean_2 /= count;
		
		return mean_2;
	}
	
	public static boolean significant_amp_drops(double[] amps){
		int drop_count = 0;
		
		for(int i = 0; i < amps.length; i++){
			if(i != amps.length - 1 && amps[i] > amps[i + 1]){
				if(amps[i] - amps[i + 1] < (amps[i] / 2)){
					drop_count++;
				}
			}
		}
		
		System.out.println("Drop count: " + drop_count);
		if(drop_count < /*amps.length / 9*/ 2400000){
			return true;
		}
		
		return false;
	}
	
	public static boolean sig_amp_drops(double[] amps){
		int highs = 0;
		
		double mean_2 = find_middle_of_mean_and_max_amp(amps);
		
		return false;
	}
	
	public static float factor_reference_audio(Wave song, Wave reference){
		FingerprintSimilarity score = song.getFingerprintSimilarity(reference);
		
		return score.getScore();
	}
	
	public static ArrayList<Double> find_least_points(double[] deviation_diff, double[] amps){
		ArrayList<Double> least_points = new ArrayList<Double>();
		double range = mean(amps) + find_max(deviation_diff) / 2;
		
		
		for(int i = 0; i < deviation_diff.length; i++){
			if(deviation_diff[i] <= range){
				least_points.add(deviation_diff[i]);
			}
		}
		
		return least_points;
	}
	
	public static double[] deviation_differences(double array[]){
		double mean = 0;
		double differences[] = new double[array.length];
		
		mean = mean(array);
		
		for(int j = 0; j < array.length; j++){
			differences[j] = Math.abs(array[j] - mean);
		}
		
		return differences;
	}
	
	//Slightly broken
	/*public static double[] find_differences(double[] amps){
		int length;
		if((amps.length / 2) % 2 == 0){
			length = amps.length / 2;
		} else {
			length = (amps.length / 2) + 1;
		}
		System.out.println("Length: " + length);
		
		double[] differences = new double[length];
		
		for(int i = 0; i < differences.length; i++){
			for(int j = 0; j < amps.length; j++){
				if(j == amps.length - 1){
					return differences;
				}
				differences[i] = amps[i] - amps[i + 1];
				//System.out.println("Difference: " + differences[i]);
			}
		}
		
		return differences;
	}*/
	
	public static void print_array(double[] array){
		System.out.println("Printing Array");
		for(int i = 0; i < array.length; i++){
			System.out.println("Element: " + array[i]);
		}
		System.out.println("Finished printing array");
	}
}
