package utils;

import java.util.Comparator;

public class DancerCompareType {

	public int index;
	public double x;
	public double y;

	public DancerCompareType(int index, double x, double y) {
		this.index = index;
		this.x = x;
		this.y = y;
	}

	class CompareByX implements Comparator<DancerCompareType> {
		public int compare(DancerCompareType o1, DancerCompareType o2) {
			if (o1.x == o2.x) {
				if (o1.y == o2.y) {
					return o1.index - o2.index;
				}
				return (o1.y - o2.y > 0) ? 1 : -1;
			}
			return (o1.x - o2.x > 0) ? 1 : -1;
		}
	}

}
