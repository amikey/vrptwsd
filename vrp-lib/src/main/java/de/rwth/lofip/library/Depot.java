package de.rwth.lofip.library;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * A separate class for a depot, which is just a special
 * {@code AbstractPointInSpace}.
 * 
 * <p>
 * Java class for depot complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="depot">
 *   &lt;complexContent>
 *     &lt;extension base="{http://library.lofip.rwth.de}abstractPointInSpace">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "depot")
public class Depot extends AbstractPointInSpace {

	@Override
	public void setId() {
    	this.id ="D"+Math.round(Math.random()*1000)+" PS"+this.toString();
    }

}
