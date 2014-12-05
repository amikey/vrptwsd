package de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * An instance of this class collects customer-demand-changing-events to
 * describe Scenarios for a specific VRP-instance.<br>
 * An {@code DemandScenario} contains a description and {@code SortedSet<Event>}
 * of events.
 * 
 * @author Olga Bock
 */

public class DemandScenario {
	/**
	 * Just a freetext, short description of the scenario. This may e.g. be the
	 * name of the changed Solomon instance (like R103n).
	 */
	private String description;

	/**
	 * The Events of this problem.
	 */
	private SortedSet<Event> events;

	// Constructor
	public DemandScenario() {
		events = Collections.synchronizedSortedSet(new TreeSet<Event>(
				new EventComparator()));
	}

	/**
	 * Getter and Setter
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SortedSet<Event> getEvents() {
		return events;
	}

	public void setEvents(SortedSet<Event> events) {
		this.events = events;
	}

	public void addEvent(Event event) {
		if (events == null) {
			events = Collections.synchronizedSortedSet(new TreeSet<Event>(
					new EventComparator()));
		}
		events.add(event);
	}

	public String getDemandScenarioAsString() {
		String s = "";
		for (Event e : events) {
			s += e.getEventAsTupel() + "\n";
		}
		return s;
	}

}
