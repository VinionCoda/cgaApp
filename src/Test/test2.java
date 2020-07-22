/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author takeb
 */
public class test2 {

    private static final Logger logr = LogManager.getLogger(test2.class);


    public static void main(String[] args) {
        logr.error("Didn't do it.");
        logr.debug("hello world");
        logr.info("Dine at Als");
    }

}
