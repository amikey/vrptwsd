package de.rwth.lofip.library.util;

import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;


public class TestCostCalculation {

    @Test
    public void testExpectedRecourseCalculation() {
        Customer c19 = new Customer();
        c19.setxCoordinate(15);
        c19.setyCoordinate(80);
        c19.setCustomerNo(19);
        c19.setDemand(10);
        c19.setTimeWindowOpen(278);
        c19.setTimeWindowClose(345);
        c19.setServiceTime(90);

        Customer c16 = new Customer();
        c16.setCustomerNo(16);
        c16.setxCoordinate(20);
        c16.setyCoordinate(85);
        c16.setDemand(40);
        c16.setTimeWindowOpen(475);
        c16.setTimeWindowClose(528);
        c16.setServiceTime(90);

        Customer c12 = new Customer();
        c12.setCustomerNo(12);
        c12.setxCoordinate(25);
        c12.setyCoordinate(85);
        c12.setDemand(20);
        c12.setTimeWindowOpen(625);
        c12.setTimeWindowClose(721);
        c12.setServiceTime(90);

        Depot depot = new Depot();
        depot.setxCoordinate(40);
        depot.setyCoordinate(50);

        Tour tour = new Tour(depot, new Vehicle(1, 75));
        tour.addCustomer(c19);
        tour.addCustomer(c16);
        tour.addCustomer(c12);

        double expectedRecourseCost = tour.getExpectedRecourseCost();
        assert (expectedRecourseCost >= 0);

        Customer c1 = new Customer(0, 1);
        c1.setDemand(1);
        Customer c2 = new Customer(1, 1);
        c2.setDemand(2);
        Customer c3 = new Customer(1, 0);
        c3.setDemand(3);
        Depot d = new Depot();
        d.setxCoordinate(0);
        d.setyCoordinate(0);
        Tour t = new Tour(d, new Vehicle(1, 4));
        t.addCustomer(c1);
        t.addCustomer(c2);
        t.addCustomer(c3);
        System.out.println("recourse: " + t.getExpectedRecourseCost());

    }

    @Test
    public void testDistanceCalculation() {
        Customer c1 = new Customer();
        c1.setxCoordinate(10);
        c1.setyCoordinate(10);

        Customer c2 = new Customer();
        c2.setxCoordinate(10);
        c2.setyCoordinate(20);

        Customer c3 = new Customer();
        c3.setxCoordinate(20);
        c3.setyCoordinate(20);

        Depot d = new Depot();
        d.setxCoordinate(20);
        d.setyCoordinate(10);

        Tour t = new Tour(d, new Vehicle(1, 10));
        t.addCustomer(c3);
        t.addCustomer(c2);
        t.addCustomer(c1);
        assert (t.getTotalDistance() == 40);
    }

}
