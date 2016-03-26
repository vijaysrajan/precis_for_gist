package com.fratics.precis.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import com.fratics.precis.fis.base.PrecisBase;

public class ConfigObject extends PrecisBase {

    public static String configFile = "./conf/precisconfig.properties";
    private Properties properties = new Properties();

    public ConfigObject() {
    }

    public boolean initialize() throws FileNotFoundException, IOException {
	properties.load(new FileInputStream(configFile));
	return true;
    }

    public Properties getProperties() {
	return properties;
    }

    public static String getConfigFile() {
	return configFile;
    }

    public static void setConfigFile(String configFile) {
	ConfigObject.configFile = configFile;
    }

    public void setProperties(Properties properties) {
	this.properties = properties;
    }

    public void store(String fileName) throws IOException {
	this.properties.store(new FileOutputStream(fileName),
		"dumping precis properties");
    }

    public void dump() throws IOException {
	PrintWriter writer = new PrintWriter(System.err);
	properties.list(writer);
	writer.flush();
    }

    public static void main(String[] args) throws Exception {
	ConfigObject c = new ConfigObject();
	c.initialize();
	c.getProperties().setProperty("test", Character.toString('\001'));
	c.store("./data/tmpStore.txt");
	c.dump();
    }
}
