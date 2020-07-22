/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.io.FileReader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author takeb
 */
public class test {

    private static final Logger logr = LogManager.getLogger(test.class);

    public static void main(String[] args) {
//        logr.error("Didn't do it.");
//        logr.debug("hello world");
//        logr.info("Dine at Als");

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("src/resources/appinfo.json"));

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            JSONObject jsonObject = (JSONObject) obj;

            // A JSON array. JSONObject supports java.util.List interface.
           // JSONArray companyList = (JSONArray) jsonObject.get("Company List");

            System.out.println(jsonObject.get("app_name"));
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
