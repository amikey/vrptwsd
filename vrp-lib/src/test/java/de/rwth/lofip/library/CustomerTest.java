package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.xml.sax.SAXException;

public class CustomerTest {

	@Test
	public void testXmlCustomer() throws JAXBException, IOException,
			SAXException, NoSuchFieldException, SecurityException {

		// set up
		Customer customer = new Customer();
		customer.setxCoordinate(15);
		customer.setyCoordinate(80);
		customer.setCustomerNo(11);
		customer.setDemand(50);
		customer.setTimeWindowOpen(278);
		customer.setTimeWindowClose(345);
		customer.setServiceTime(90);

		JAXBContext ctx = JAXBContext
				.newInstance(new Class[] { Customer.class });

//		SchemaCreatingSchemaOutput out = new SchemaCreatingSchemaOutput();
//		ctx.generateSchema(out);
//		Schema imlicitSchema = out.createSchema();

		Marshaller marsh = ctx.createMarshaller();
	//	marsh.setSchema(imlicitSchema);
		File file = new File("testfile.xml");
		marsh.marshal(customer, file);
		marsh.marshal(customer, System.out);
		// System.out.println(out.out.toString());

		Unmarshaller unmarsh = ctx.createUnmarshaller();
	//	unmarsh.setSchema(imlicitSchema);
		Object o = unmarsh.unmarshal(file);
		Customer customer2 = (Customer) unmarsh.unmarshal(file);
		assertEquals(customer.getClass(), o.getClass());
		System.out.println();
		System.out.println(customer2.getAsString());
		System.out.println("x"+customer2.getxCoordinate()+", y"+customer2.getyCoordinate());

		//file.delete();

	}

}
