package utils;

import java.util.Comparator;

public class DancerComparatorByY implements Comparator<DancerCompareType> {
	public int compare(DancerCompareType o1, DancerCompareType o2) {
		if (o1.y == o2.y) {
			if (o1.x == o2.x) {
				return o1.index - o2.index;
			}
			return (o1.x - o2.x > 0) ? 1 : -1;
		}
		return (o1.y - o2.y > 0) ? 1 : -1;
	}
}
