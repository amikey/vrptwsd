package de.rwth.lofip.library.solver.metaheuristics.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.IOUtils;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.RecourseCost;

public class TourMatchingWithNumberOfRecourseActions {
	
	protected SolutionGot newSolution;
	protected SolutionGot oldSolution;
	
	protected List<RecourseCost> listOfRecourseCosts;
	List<Integer> listOfUsedIndizes;
	
	GroupOfTours currentGot;
	
	//this rematches tours to gots such that recourse cost are minimized (but only with a greedy procedure)
	public SolutionGot matchToursToGots (SolutionGot solution) throws IOException {
		if (Parameters.getMaximalNumberOfToursInGots() != 2)
			return solution;
			//throw new RuntimeException("Tourmatching soll durchgeführt werden, obwohl maximale Anzahl an Touren in Got " + Parameters.getMaximalNumberOfToursInGots() + " ist");
		
		initialiseFields(solution);    
		calculateRecourseCostsBetweenTours();
		findCostMinimalMatchingForToursWithGreedy();
		return returnEitherNewOrOldSolutionDependingOnWhichHasLessCost();		
	}

	private void initialiseFields(SolutionGot solution) {
	    oldSolution = solution;
	    newSolution = new SolutionGot(oldSolution.getVrpProblem());
	    listOfRecourseCosts = new ArrayList<RecourseCost>();
	    listOfUsedIndizes = new ArrayList<Integer>();
	}

	private void calculateRecourseCostsBetweenTours() {
		//IMPORTANT_TODO: für anzTouren in Got != 2 implementieren
		//TODO: this assumes that I want to match exactly two tours		
		for (int i = 0; i < oldSolution.getNumberOfTours(); i++)
			for (int j = i+1; j < oldSolution.getNumberOfTours(); j++) {
				//RUNTIME_TODO: hier könnte man gucken, ob es Got schon gibt, so dass Recourse Kosten nicht neu berechnet werden müssen.
				System.out.println("Berechne Recourse Kosten für Touren " + i + " (Demand: " + oldSolution.getTour(i).getUseOfCapacity() + ") und " + j + " (Demand: " + oldSolution.getTour(j).getUseOfCapacity());
				GroupOfTours got = new GroupOfTours(oldSolution);
				got.addTour(oldSolution.getTour(i));
				got.addTour(oldSolution.getTour(j));
				RecourseCost rc = got.getExpectedRecourse();
				rc.addTourIndex(i);	
				rc.addTourIndex(j);
				listOfRecourseCosts.add(rc);
			}		
	}

	private void findCostMinimalMatchingForToursWithGreedy() {				
		sortListOfRecourseCostsAccordingToConvexCombinationOfRecourseCostAndNumberOfRecourseActions(); 
		for (int i = 0; i < listOfRecourseCosts.size(); i++) {
			if (allToursAreAlreadyAssignedToGotsOrThereIsOnlyOneTourLeftThatIsNotAssignedYet())
				break;
			else {
				RecourseCost rc = listOfRecourseCosts.get(i);
				if (!toursThatAreUsedInRecourseCostAreAlreadyAssignedToGots(rc)) {
					createGotWithTours(rc);
					addGotToSolution();
					addIndizesOfToursToListOfAlreadyUsedIndizes(rc);
				}
			}
		}	
		if (thereIsATourLeftThatDoesNotBelongToAGot()) {
			createGotForTour();
			addGotToSolution();
		}
		maintainSolutionsForGots();
	}

	protected void sortListOfRecourseCostsAccordingToConvexCombinationOfRecourseCostAndNumberOfRecourseActions() {		
			Comparator<RecourseCost> byCombinationOfCostAndNumberRecourseActions = (e1,e2) -> Double.compare(e1.getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart(oldSolution),e2.getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart(oldSolution));		
			Collections.sort(listOfRecourseCosts, byCombinationOfCostAndNumberRecourseActions);						
	}
	
	private boolean allToursAreAlreadyAssignedToGotsOrThereIsOnlyOneTourLeftThatIsNotAssignedYet() {
		int size = listOfUsedIndizes.size();
		return size >= oldSolution.getNumberOfTours()-1;
	}

	//public so that TourMatching can be tested
	public boolean toursThatAreUsedInRecourseCostAreAlreadyAssignedToGots(RecourseCost rc) {
		for (Integer i : rc.getIndizesOfTours()) 
			if (listOfUsedIndizes.contains(i))
				return true;
		return false;
	}
	
	private void createGotWithTours(RecourseCost rc) {
		//RUNTIME_TODO: das kann man auch mit toursThatAreUsedInRecourseCostAreAlreadyAssignedToGots zusammenlegen, auf Kosten der Code-Lesbarkeit
		currentGot = new GroupOfTours(oldSolution);
		for (Integer i : rc.getIndizesOfTours())
			currentGot.addTour(oldSolution.getTour(i));
	}

	private void addGotToSolution() {
		newSolution.addGot(currentGot);
	}
	
	private void maintainSolutionsForGots() {
		for (GroupOfTours got : newSolution.getGots())
			got.setSolution(newSolution);
	}

	private void addIndizesOfToursToListOfAlreadyUsedIndizes(RecourseCost rc) {
		//RUNTIME_TODO: das kann man auch mit toursThatAreUsedInRecourseCostAreAlreadyAssignedToGots zusammenlegen, auf Kosten der Code-Lesbarkeit
		for (Integer i : rc.getIndizesOfTours())
			listOfUsedIndizes.add(i);
	}
	
	private boolean thereIsATourLeftThatDoesNotBelongToAGot() {
		return ((oldSolution.getNumberOfTours() - listOfUsedIndizes.size()) == 1);
	}
	
	private void createGotForTour() {
		//find tour that has no Got yet
		int indexOfTour = Integer.MAX_VALUE;
		for (int i = 0; i < oldSolution.getNumberOfTours(); i++)
			if (!listOfUsedIndizes.contains(i))
				indexOfTour = i;
		if (indexOfTour == Integer.MAX_VALUE)
			throw new RuntimeException("No Index for tour could be found that is not already assigned to a got");
		currentGot = new GroupOfTours(oldSolution);
		currentGot.addTour(oldSolution.getTour(indexOfTour));
	}
	
	protected SolutionGot returnEitherNewOrOldSolutionDependingOnWhichHasLessCost() throws IOException {
		if (newSolution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse() <= oldSolution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse()) {
			System.out.println("JUHUU, TourMatching hat bessere Lösung gefunden!");
			if (Parameters.isPublishSolutionAtEndOfAMTSSearch()) 
				IOUtils.write("JUHUU, TourMatching hat bessere Lösung gefunden! \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(newSolution));
			return newSolution;
		} else {
			System.out.println("Oh nein, TourMatching hat KEINE bessere Lösung gefunden!");
			
			System.out.println("DetCost vor Matching: " + oldSolution.getTotalDistanceWithCostFactor());
			System.out.println("RecourseCost vor Matching: " + oldSolution.getRecourseCost().getRecourseCost());
			System.out.println("#diff Rec Act vor Matching: " + oldSolution.getRecourseCost().getNumberOfDifferentRecourseActions());
			System.out.println("Convexkombination: " + oldSolution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse());
						
			System.out.println("DetCost vor Matching: " + newSolution.getTotalDistanceWithCostFactor());
			System.out.println("RecourseCost nach Matching: " + newSolution.getRecourseCost().getRecourseCost());
			System.out.println("#diff Rec Act nach Matching: " + newSolution.getRecourseCost().getNumberOfDifferentRecourseActions());
			System.out.println("Convexkombination: " + newSolution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse());
			return oldSolution;
		}			
	}
	
	// Test Utilities

	public void setListOfUsedIndizesForTesting(List<Integer> usedIndizes) {
		listOfUsedIndizes = usedIndizes;
	}

}
