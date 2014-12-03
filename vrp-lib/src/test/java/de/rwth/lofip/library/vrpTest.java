package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.xml.sax.SAXException;

public class vrpTest {

	@Test
	public void testXmlVrp() throws JAXBException, IOException,
			SAXException, NoSuchFieldException, SecurityException {

		// set up
		Customer c11 = new Customer();
		c11.setxCoordinate(15);
		c11.setyCoordinate(80);
		c11.setCustomerNo(11);
		c11.setDemand(50);
		c11.setTimeWindowOpen(278);
		c11.setTimeWindowClose(345);
		c11.setServiceTime(90);

		Customer c12 = new Customer();
		c12.setCustomerNo(12);
		c12.setxCoordinate(20);
		c12.setyCoordinate(85);
		c12.setDemand(40);
		c12.setTimeWindowOpen(475);
		c12.setTimeWindowClose(528);
		c12.setServiceTime(90);

		Customer c13 = new Customer();
		c13.setCustomerNo(13);
		c13.setxCoordinate(25);
		c13.setyCoordinate(85);
		c13.setDemand(20);
		c13.setTimeWindowOpen(625);
		c13.setTimeWindowClose(721);
		c13.setServiceTime(90);

		Depot depot = new Depot();
		depot.setxCoordinate(40);
		depot.setyCoordinate(50);

		Vehicle vehicle = new Vehicle(1, 75);
		Set<Vehicle> vehicles = new HashSet<Vehicle>();
		vehicles.add(vehicle);

		Tour tour = new Tour(depot, vehicle);
		tour.addCustomer(c11);
		tour.addCustomer(c12);
		tour.addCustomer(c13);

		JAXBContext ctx = JAXBContext
				.newInstance(new Class[] { Tour.class });

//		SchemaCreatingSchemaOutput out = new SchemaCreatingSchemaOutput();
//		ctx.generateSchema(out);
//		System.out.println(out.out.toString());
//		Schema imlicitSchema = out.createSchema();
//		System.out.println(out.out.toString());

		Marshaller marsh = ctx.createMarshaller();
	//	marsh.setSchema(imlicitSchema);
		File file = new File("testfile.xml");
		marsh.marshal(tour, file);
		//marsh.marshal(tour, System.out);
		

		Unmarshaller unmarsh = ctx.createUnmarshaller();
	//	unmarsh.setSchema(imlicitSchema);
		Object o = unmarsh.unmarshal(file);
		Customer tour2 = (Customer) unmarsh.unmarshal(file);
		assertEquals(tour.getClass(), o.getClass());
		System.out.println();
		System.out.println(tour2.getAsString());


		file.delete();

	}

}
