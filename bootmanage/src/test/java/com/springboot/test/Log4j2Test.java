package com.springboot.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log4j2Test {
	
	 public static final Logger logger = LogManager.getLogger(Log4j2Test.class);  
	 
	 public static void main(String[] args) {
	        logger.debug("Did it debug!");  
	        logger.info("Did it info!");  
	        logger.warn("Did it warn!");  
	        logger.error("Did it error!");  
	}
}
