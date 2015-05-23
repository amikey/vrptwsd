package de.rwth.lofip.library.solver.metaheuristics.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;

public class AdaptiveMemory {
	
	//this set is sorted such that tours with lowest value are at the beginning (index 0)
	//and tours with highest value are at the end (index lengthOfSet-1)
	private List<Tour> allToursInMemory = new ArrayList<Tour>();
	private int lengthOfList = 1000;
	private VrpProblem vrpProblem;
	
	private List<Customer> unservedCustomers;
	private List<Tour> toursThatContainOnlyUnservedCustomers;
	private Tour currentNewTour;
	private SolutionGot currentNewSolution;
	
	private static int seed = 0;
	private Random randomGenerator = new Random(seed); 
	
	public AdaptiveMemory() {
		seed++;
	}
	
	public void addTours(SolutionGot solution) {	
		setVrpProblem(solution);
		labelToursWithSolutionValue(solution);		
		addToursOfSolutionToTours(solution);
		removeToursWithOneCustomer();
		sortTours();
		cutSetAtLenthOfSet();
	}

		private void setVrpProblem(SolutionGot solution) {
			vrpProblem = solution.getVrpProblem();
		}
	
		private void labelToursWithSolutionValue(SolutionGot solution) {
			for (Tour tour : solution.getTours())
				tour.setSolutionValue(solution.getTotalDistanceWithCostFactor());		
		}
		
		private void addToursOfSolutionToTours(SolutionGot solution) {
			allToursInMemory.addAll(solution.getTours());
		}
		
		private void removeToursWithOneCustomer() {
			Iterator<Tour> tourIterator = allToursInMemory.iterator();
			while (tourIterator.hasNext()) {
				Tour tour = tourIterator.next();
				if (tour.getCustomerSize() <= 1)
					tourIterator.remove();
			}		
		}
		
		private void sortTours() {
			Collections.sort(allToursInMemory, new Comparator<Tour>() {
	            @Override
	            public int compare(Tour o1,
	                    Tour o2) {
	                double v1 = (o1.getSolutionValue());
	                double v2 = (o2.getSolutionValue());
	                if(v1 == v2) {
	                   return 0;
	                }
	                if(v1 < v2) {
	                   return -1; //return negative integer if first argument is less than second
	                }
	                return 1;
	            }
	        });
		}
		
		private void cutSetAtLenthOfSet() {	
			if (allToursInMemory.size() >= lengthOfList)
				allToursInMemory.subList(0, lengthOfList); 	
		}

	
	
	
	public SolutionGot constructSolutionFromTours() {
		initialiseCurrentNewSolution();
		initialiseUnservedCustomers();		
		initialiseToursThatContainOnlyUnservedCustomers();
		while (!(setOfSelectedToursCoversAllCustomers() ||
				thereIsNoAdmissibleTourLeftInAdaptiveMemory())) {			
			pickTourWithProbability();
			updateUnservedCustomers();			
			removeAllToursThatContainAlreadyServedCustomers();		
		}
		constructSolutionWithCustomersThatRemainUnserved();
		return currentNewSolution;
	}	

		private void initialiseCurrentNewSolution() {
			currentNewSolution = new SolutionGot(vrpProblem);
		}
	
		private void initialiseUnservedCustomers() {
			unservedCustomers = new ArrayList<Customer>(vrpProblem.getCustomers());
		}
		
		private void initialiseToursThatContainOnlyUnservedCustomers() {
			 toursThatContainOnlyUnservedCustomers = new ArrayList<Tour>(allToursInMemory);
		}
		
		private boolean setOfSelectedToursCoversAllCustomers() {
			return unservedCustomers.isEmpty();
		}
		
		private boolean thereIsNoAdmissibleTourLeftInAdaptiveMemory() {
			return toursThatContainOnlyUnservedCustomers.isEmpty();
		}
		
	
		private void pickTourWithProbability() {
			double randomNumber = randomGenerator.nextDouble();
			double cumulativeProbability = 0.0;
			for (int j = 0; j <= allToursInMemory.size()-1; j++) {
				int indexForProbability = allToursInMemory.size() - j; //tours with low index in Set shall have high probability. See Rochat And Taillard 1995 for explanation 		
				double probabilityOfTour = 2 * indexForProbability;
				probabilityOfTour = probabilityOfTour / (toursThatContainOnlyUnservedCustomers.size() * (toursThatContainOnlyUnservedCustomers.size() + 1));
				cumulativeProbability += probabilityOfTour;
				if (randomNumber <= cumulativeProbability) {
					currentNewTour = toursThatContainOnlyUnservedCustomers.get(j);
					break;
				}
			}			
			currentNewSolution.addTourToLastOrNewGot(currentNewTour);
		}
			
		private void updateUnservedCustomers() {
			unservedCustomers.removeAll(currentNewTour.getCustomers());		
		}
		
		private void removeAllToursThatContainAlreadyServedCustomers() {
			Iterator<Tour> tourIterator = toursThatContainOnlyUnservedCustomers.iterator();
			while (tourIterator.hasNext()) {
				Tour tour = tourIterator.next();
				if (tour.containsCustomers(currentNewTour))
					tourIterator.remove();
			}
		}
		
		private void constructSolutionWithCustomersThatRemainUnserved() {
			GreedyInsertion gi = new GreedyInsertion();
			//DESIGN_TODO: hier werden alle Kunden eingefügt, anders als im Paper, in dem nur Kunden in die bestehenden Touren eingefügt und keine neuen Touren eröffnet werden.
			gi.insertCustomers(currentNewSolution, unservedCustomers);
		}

		
		//****************
		public static void setSeedTo(int i) {
			seed = i;
		}
	
}
