package com.mycompany.Project;

import java.util.ArrayList;
import java.io.*;
import java.util.List;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardDownRightHandler;

import org.apache.commons.lang3.StringUtils;

public class CountFilter extends Processing_elements {

    private String key;
    private int min;

    public CountFilter(ArrayList<String> inputValues, ArrayList<String> pastEntries) { //Constructor
        
        Boolean keyBool = false; //KeyBool and MinBool used to differentiate the value that is assigened to key and min.
        Boolean minBool = false;
        for (String text : inputValues) {
            if(text.toLowerCase().contains("key")){ //Checking if key is found, if so set to true
                keyBool = true;
            }
            if(text.toLowerCase().contains("min")){ //Checking if min is found if so set to true
                minBool = true;
            }
            if (text.contains("value") && keyBool) { //When value is found and keybool is true, the if loop replaces all the excess (words,semicolons,spaces) and sets key to the raw value entered.
                key = text.replaceAll("value", "").replaceAll(" ", "").replaceAll(":", "");
                keyBool = false;
            }
            if (text.contains("value") && minBool) { //When value is found and Minbool is true, the if loop replaces all the excess (words,semicolons,spaces) and sets Min to the raw value entered.
                min = Integer.parseInt(text.replaceAll("value", "").replaceAll(" ", "").replaceAll(":", ""));
                minBool = false;
            }
        }
        System.out.println("data " + min); //Just a check to see that the values entered are correct.
        System.out.println(key);
        
        if(key == null && min == 0){
            System.out.println("Parameters couldn't be found in JSON, check formatting");
        }

        //add this file into inputs arrayList
        else
        {
            inputValues.addAll(pastEntries);
        
            loopEntries(inputValues);
        }

        // for (String text : data) { // use data arrayList to print each line of text
        //    // System.out.println(text);
        // }
    }

    public static boolean ifFile(String filePath) { //Method to check if the path entered is a file
        File file = new File(filePath);
        return file.isFile();
    }

    public static boolean ifDirectory(String filePath) { //Method to check if the path entered is a directory
        File file = new File(filePath);
        return file.isDirectory();
    }

    @Override //Override Operations method from the superclass
    public void operations() {
        

        if (local == false) { //If it is remote enter loop
            if (isRemoteDIR(this.entryID)) { //checks if it is a directory, if so it will not accept it
                System.out.println("Please use valid file path");
            } else {
            Boolean totalKey = false; //Sets totalkey to false

            getEntriesRemote(Integer.parseInt(entryID)); //Uses the getEtriesRemote function in processing elements and parses the entry ID

            // try {
            //     File filename = new File(path); //gets the filename
            //     readfile(filename);
            int count = 0; //Set the count value to zero

            
                for(String line: data){ //Reads through each line of the data
                        if (line.contains(key)){ //if the key is found in the line, count increases
                            count++;
                        } 
                    }
                        if (count <= min) { //if count is less than the minimum amount of instances of the keyword, totalkey is false.
                            totalKey = false; 
                            System.out.println("The key " + key + " is not found at least " + min + " times");
                        } else {
                            totalKey = true; //if count is greater than the minimum amount of instances of the keyword, totalKey is true.
                            addFileToList();
                            System.out.println("The Key " + key + " is found at least " + min + " times");
                            addFileToList();
                        }
            }

        } else {
            if (ifDirectory(path)) { //checks if it is a directory, if so it will not accept it
                System.out.println("Please use valid file path");
            } else if (ifFile(path)) { //enter loop if the entry is a file
                Boolean totalKey = false; //Sets totalkey to false

                File newFile = new File(path); //Create a new file with the filepath and read it in the next line
                readfile(newFile);

                int count = 0; //set count to 0
                for (String line : data) { //Read through each line in the file
                    System.out.println(count);
                    if (line.contains(key)) { //If the key is found increase the count
                        
                        count += StringUtils.countMatches(line, this.key);
                    }
                }
                System.out.println(count);
                    if (count <= min) { //Case for when the count is less than the min instances of the keyword
                        System.out.println("The key " + key + " is not found at least " + min + " times in the file");
                    } else { //Case for when the keyword is found equal to or greater the min instances of the keyword
                        totalKey = true;
                        System.out.println("The Key " + key + " is found at least " + min + " times in the file");
                        addFileToList(); //Adds the path to the outputList
                    }
            } else {
                System.out.println("File path not found");
            }
        
        }
    }
}
