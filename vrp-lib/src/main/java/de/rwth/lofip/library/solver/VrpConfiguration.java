package de.rwth.lofip.library.solver;

import java.util.Iterator;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * Read in configuration parameters for the solver. The name VrpConfiguration is
 * a bit misleading since the parameters are used for the solver not for the
 * instance of the vrp problem. The following parameters are read:
 * 
 * approximateEquality baseDirectory outputDirectory maximumIterations
 * maximumImprovementIterations deviationFactor
 * 
 * The parameters are read in in three hierarchical steps. The highest priority
 * do have system properties. When executing a java application, they can be set
 * in the invoking command line with the help of the -D switch. Second, a file
 * vrp.configuration is searched in the classpath. Third, the default parameters
 * are used.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 */
public class VrpConfiguration {

	private CompositeConfiguration configuration;

	public VrpConfiguration() {
		configuration = new CompositeConfiguration();
		configuration.addConfiguration(new SystemConfiguration());
		try {
			configuration.addConfiguration(new PropertiesConfiguration(
					"vrp.configuration"));
		} catch (ConfigurationException e) {
			System.out
					.println("No configuration file found. Using defaults for unconfigured parameters.");
		}
		try {
			configuration.addConfiguration(new PropertiesConfiguration(
					"vrp.configuration.defaults"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		configuration.setThrowExceptionOnMissing(true);
	}

	/**
	 * This method helps to find a configuration parameter in one of three
	 * 
	 * @param <T>
	 * 
	 * @param configurationParameter
	 * @param defaultValue
	 * @return
	 */
	public final String getConfigurationValueString(
			String configurationParameter) {
		return configuration.getString(configurationParameter);
	}

	public final double getConfigurationValueLong(String configurationParameter) {
		return configuration.getLong(configurationParameter);
	}

	public final double getConfigurationValueDouble(
			String configurationParameter) {
		return configuration.getDouble(configurationParameter);
	}

	public String getConfigurationAsString() {
		String s = "";
		Iterator<String> keys = configuration.getKeys();
		while (keys.hasNext()) {
			String key = keys.next();
			s += key;
			s += "=";
			s += configuration.getProperty(key);
			s += "\n";
		}
		return s;
	}
}
