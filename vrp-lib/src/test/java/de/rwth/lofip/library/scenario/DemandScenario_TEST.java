package de.rwth.lofip.library.scenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.junit.Test;

public class DemandScenario_TEST {

	@Test
	public void testScenarioFromFile() throws IOException {

		File datei = new File("solomon-problems/r201n.txt");
		BufferedReader br = new BufferedReader(new FileReader(datei));
		String zeile = "";
		List<String> liste = new ArrayList<String>(0);
		while ((zeile = br.readLine()) != null) {
			liste.add(zeile);
		}
		System.out.println("Lines: " + liste.size());
		System.out.println("Lines: " + liste);
		assertNotSame(0, liste.size());
		// tests whether the same number of lines is read as can be found in the
		// file
		assertSame(34, liste.size());

		DemandScenario dScenario = DemandScenarioUtils
				.createScenarioFromStringList(liste);

		// tests whether the correct scenario is read
		assertTrue(dScenario.getDescription().equals("R201n"));
		// tests whether the correct number of events is read
		assertSame(29, dScenario.getEvents().size());

		// tests whether the events are sorted by time ascending
		SortedSet<Event> events = dScenario.getEvents();
		Event event1 = events.first();
		Event event2 = events.first();

		for (Event event : events) {
			event1 = event2;
			event2 = event;

			assertTrue(event1.getPointInTime() <= event2.getPointInTime());
		}

		assertSame(liste.size() - 5, dScenario.getEvents().size());

		System.out.println("Scenario Name:" + dScenario.getDescription()
				+ " , Size: " + dScenario.getEvents().size());
		System.out.println("Events:");
		System.out.println("CustNO	Time	Demand");
		System.out.println("----------------------");
		for (Event event : events) {
			System.out.println(event.getCustomerNo() + "	"
					+ event.getPointInTime() + "	" + event.getDemand());
		}

		br.close();

	}

	@Test
	public void test5EventsKick2Out() throws IOException {

		List<String> liste = new ArrayList<String>();

		liste.add("test");
		liste.add("");
		liste.add("CUSTOMER-EVENT");
		liste.add("CUST NO.   DEMAND    Action TIME");
		liste.add("");
		liste.add("1          10       3");
		liste.add("2          10       2");
		liste.add("3          10       1");
		liste.add("4          10       2");
		liste.add("5          10       1");

		DemandScenario dScenario = DemandScenarioUtils
				.createScenarioFromStringList(liste);

		// if events with the same action time occur, all double action times
		// will be eliminated
		// out of 5, only 3 events stay
		assertSame(3, dScenario.getEvents().size());

		// tests whether the events are sorted by time ascending
		SortedSet<Event> events = dScenario.getEvents();
		Event event1 = events.first();
		Event event2 = events.first();
		assertTrue(event1.getPointInTime() <= event2.getPointInTime());

		for (Event event : events) {
			event1 = event2;
			event2 = event;

			assertTrue(event1.getPointInTime() <= event2.getPointInTime());
		}

	}

	@Test
	public void test() throws IOException {

		List<String> liste = new ArrayList<String>();

		liste.add("test");
		liste.add("");
		liste.add("CUSTOMER-EVENT");
		liste.add("CUST NO.   DEMAND    Action TIME");
		liste.add("");
		liste.add("1          10       3");
		liste.add("2          10       2");
		liste.add("3          10       1");
		liste.add("4          10       2");
		liste.add("5          10       1");
		liste.add("6           5       4");

		DemandScenario dScenario = DemandScenarioUtils
				.createScenarioFromStringList(liste);

		// if events with the same action time occur, all double action times
		// will be eliminated
		// out of 6, only 4 events stay
		assertSame(4, dScenario.getEvents().size());

		// tests whether the events are sorted by time ascending
		SortedSet<Event> events = dScenario.getEvents();
		Event event1 = events.first();
		Event event2 = events.first();
		assertTrue(event1.getPointInTime() <= event2.getPointInTime());

		for (Event event : events) {
			event1 = event2;
			event2 = event;

			assertTrue(event1.getPointInTime() <= event2.getPointInTime());
		}

		assertEquals(6, event2.getCustomerNo());
		assertEquals(5, event2.getDemand());
		assertEquals(4, event2.getPointInTime(), 0);
	}

	@Test
	public void testScenario() throws IOException {

		File datei = new File("solomon-problems/r201n.txt");
		BufferedReader br = new BufferedReader(new FileReader(datei));
		String zeile = "";
		List<String> liste = new ArrayList<String>(0);
		while ((zeile = br.readLine()) != null) {
			liste.add(zeile);
		}
		System.out.println("Lines: " + liste.size());
		System.out.println("Lines: " + liste);
		assertNotSame(0, liste.size());
		// tests whether the same number of lines is read as can be found in the
		// file
		assertSame(34, liste.size());

		DemandScenario dScenario = DemandScenarioUtils
				.createScenarioFromStringList(liste);

		// tests whether the correct scenario is read
		assertTrue(dScenario.getDescription().equals("R201n"));
		// tests whether the correct number of events is read
		assertSame(29, dScenario.getEvents().size());

		// tests whether the events are sorted by time ascending
		SortedSet<Event> events = dScenario.getEvents();
		Event event1 = events.first();
		Event event2 = events.first();

		for (Event event : events) {
			event1 = event2;
			event2 = event;

			assertTrue(event1.getPointInTime() <= event2.getPointInTime());
		}

		assertSame(liste.size() - 5, dScenario.getEvents().size());

		br.close();
	}
}
