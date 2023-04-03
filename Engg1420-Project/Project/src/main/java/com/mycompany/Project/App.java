package com.mycompany.Project;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class App {

    
    //arraylist for the past filters
    public static ArrayList<String> pastEntries = new ArrayList<String>();
    public static void main(String[] args) throws Exception {


        // get the fle location
        String fileLocation = getFile();
        System.out.println(fileLocation);

        // open file
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
       
            String s;
            String name = null;

            // read each line indiviually
            while ((s = reader.readLine()) != null ) {

                //name gives senario name 
                if (s.contains("name")) {
                    s = s.replace("\"", "").replace("name", "").replace(":", "").replace(",", "");
                    name = s.stripIndent().strip();
                    s = reader.readLine();
                    System.out.println(name);
                    break;
                }
                //the next line should be processing
                //this line does not contain relavent information
            }

            //generate filters
            generateFliters(reader);
            reader.close();

            // exception handling
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        System.exit(0);
    }

    //gets the json file from user input
    public static String getFile() {

        // create scanner
        Scanner textScan = new Scanner(System.in);

        // read in file
        System.out.println("Enter File location: ");
        String fileLocation = textScan.nextLine();
        textScan.close();

        return fileLocation.strip();
    }

    public static void generateFliters(BufferedReader reader) {

        //continue to read the file
        try {


            String s;
            boolean newfilter = false;

            //insideParameters is to help match curly brackets and make sure each scenario is seperate
            int insideParameters = 0;
            String type = null;

            //stores each line in an arraylist
            ArrayList <String> filterDetails = new ArrayList<String>();

            while ((s = reader.readLine()) != null) {
                //keeps track of if the reader is still in the same scenario

                if(s.contains("{")){
                    insideParameters++;
                }
                if(s.contains("}")){
                    insideParameters--;
                }

                //if it is exiting the scenario 
                if(insideParameters > 0){
                    newfilter = true;
                }
                //if it's exiting the scenario
                if(insideParameters == 0 && type != null){
                    
                    //create a new filter class
                    newfilter = false;
                    generatefilterClass(filterDetails, type);
                    filterDetails.removeAll(filterDetails);
                    type = null;
                }

                //and details from each line as long as it is not a curly bracket line
                if(newfilter == true && !(s.strip().contains("{")|| s.strip().contains("}")||s.strip().contains("[")|| s.strip().contains("]") )){


                    //help clean up text to make it easier to use
                    filterDetails.add(s.replace("\"", " ").replace(",", "").trim());
                    
                    //identify which type of scenario it is
                    if(s.contains("type") && type == null){
                        type = s;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    //create new scenario class
    //this will preform the operations 
    //each processing element will then return a list of entries
    //these entries will be stored in an arraylist and later passed to the next processing element
    public static void generatefilterClass(ArrayList<String> inputValues, String type){


        //remove the type, filter and other key words for the switch statement
        type = type.toLowerCase();
        type = type.strip().replace("type", "").replace("\"", "").replace(":", "").replace(",", "").replace("filter", "");
        // System.out.println("\n");
        // System.out.println(type.stripIndent());
        
        //switch statement to match the type to the scenario
        //then create the new object and add it to the arraylist of past filters
        switch(type.stripIndent()){
            case "list":    
                List list = new List(inputValues, pastEntries);
                pastEntries = list.outputs();
                System.out.println("\n");
                for(String text: pastEntries){
                    System.out.println(text);
                }
                System.out.println("\n");
                break;
            case "length":    
                LengthFilter lengthfilter = new LengthFilter(inputValues, pastEntries);
                pastEntries = lengthfilter.outputs();
                break;
            case "name":    
                NameFilter namefilter = new NameFilter(inputValues, pastEntries);
                pastEntries = namefilter.outputs();
                // System.out.println("\nFiles containing key:\n");
                // for (String text: namefilter.outputs())
                //     {
                //         System.out.println(text);
                //     }
                //     if (namefilter.outputs().size() == 0)
                //     {
                //         System.out.println("None");
                //     }
                break;
            case "content":    
                ContentFilter contentfilter = new ContentFilter(inputValues, pastEntries);
                pastEntries = contentfilter.outputs();
                break;
            case "count":    
                CountFilter countfilter = new CountFilter(inputValues, pastEntries);
                pastEntries = countfilter.outputs();
                // for(String text: pastEntries){
                //     System.out.println(text);
                // }
                break;
            case "split":    
                Split split = new Split(inputValues, pastEntries);
                pastEntries = split.outputs();
                break;
            case "rename":    
                Rename rename = new Rename(inputValues, pastEntries);
                pastEntries = rename.outputs();
                // for(String text: pastEntries){
                //     System.out.println(text);
                // }
                break;
            case "print":    
                Print print = new Print(inputValues, pastEntries);
                pastEntries = print.outputs();
                break;

            default:
                System.out.println("The Processing Type In The JSON File Does Not Exist");
                break;

        }
        System.out.println("\n");
        for(String text: pastEntries){
            System.out.println(text);
        }
        System.out.println("\n");
    }

}
