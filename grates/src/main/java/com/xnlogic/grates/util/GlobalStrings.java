package com.xnlogic.grates.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class GlobalStrings {
    private static Configuration stringConfig = null;
    
    public static String getString(String key) {
        String toReturn = "";
        
        try {
            checkOrLoadConfig();
            
            toReturn = stringConfig.getString(key);
        } catch (ConfigurationException ce) {
            
        } // try
        
        return toReturn;
    } // getString
    
    private static void checkOrLoadConfig() throws ConfigurationException {
        if (stringConfig == null) {
            stringConfig = new PropertiesConfiguration("strings.properties");
        } // if
    } // checkOrLoadConfig
}
