package utils;

public class Utils {
	public static boolean sameTime(double kft1, double kft2) {
		return Math.abs(kft1 - kft2) < 15;
	}
}
