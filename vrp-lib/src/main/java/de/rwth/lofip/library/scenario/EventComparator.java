package de.rwth.lofip.library.scenario;

import java.util.Comparator;

/**
 * Just a simple comparator to sort the events depending on the
 * {@code pointInTime} when the changes accrues.
 * 
 * @author Olga Bock
 * 
 */
public class EventComparator implements Comparator<Event> {

	// @Override
	public int compare(Event event0, Event event1) {

		if (event0.getPointInTime() < event1.getPointInTime()) {
			return -1;
		}
		if (event0.getPointInTime() > event1.getPointInTime()) {
			return 1;
		}
		return 0;
	}

}
