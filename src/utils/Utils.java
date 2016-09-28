package utils;

import javafx.util.Duration;

public class Utils {
	public static boolean sameTime(double kft1, double kft2) {
		return Math.abs(kft1 - kft2) < 15;
	}

	public static String formatTime(Duration elapsed) {
		int intElapsed = (int) Math.floor(elapsed.toSeconds());
		int elapsedMinutes = intElapsed / 60;
		int elapsedSeconds = intElapsed - elapsedMinutes * 60;
		if (elapsedSeconds < 0) {
			elapsedSeconds = 0;
		}
		return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
	}
}
