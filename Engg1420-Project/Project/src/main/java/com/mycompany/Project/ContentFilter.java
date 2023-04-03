package com.mycompany.Project;

import java.io.*;
import java.util.ArrayList;

public class ContentFilter extends Processing_elements {

    private boolean localScenario = false;
    private String key;

    public ContentFilter(ArrayList<String> inputValues, ArrayList<String> pastEntries) {

        for (String text : inputValues) { // Extracts key from inputValues
            if (text.contains("value") || text.contains("Value")) {
                key = text.replaceAll("value", "").replaceAll(" ", "").replaceAll(":", "");
            }
        }

        if(key == null){
            System.out.println("Parameters couldn't be found in JSON, check formatting");
        }

        //add this file into inputs arrayList
        else
        {
            inputValues.addAll(pastEntries);

            loopEntries(inputValues); // Loop through and print each line from input values
                                      // If local, extract path
                                      // If remote, extract entryID and repoID
        }
    }


    
    @Override
    public void operations() { // *Assume operations only requires one instance of key in each file for it to be passed on to next filter*
                               // *Assume operations is not required to check for keys in remote directories*
                            

        if (local == false) { // *SCENARIO FOR REMOTE ENTRIES*

            boolean hasKey = false; // hasKey false by default

            if (isRemoteDIR(this.entryID)) { 
                System.out.println("Please use valid file path"); // *Operations does not deal with remote directories*

            }else{  // *If remote entry is a single document*

                getEntriesRemote(Integer.parseInt(entryID)); // Parse entryID as an int and create instance of remote client with getEntriesRemote method.
                                                             // If remote entry is single document, create local instance of document with custom filepath and read that file.
                                                             // After successful, close client.

                for (String element : data) { // Iterate through each line of the local file that was just created from the remote client.

                    if (element.contains(key)) {
                        hasKey = true;
                        break;
                    }
                }
                                          // *REMOTE OUTPUT*
                if (hasKey) {             // If document contains the key print message and generateRemoteJson method.
                    generateRemoteJson(this.repoID, this.entryID);// Method will add type, entryID, repoID to output ArrayList.
                    System.out.println("Key has been found in the contents of the remote file entry and passed to the next filter.");

                } else { // Otherwise, message will be prompted and the document will not be passed to the next filter.
                    System.out.println("Key is not found in the contents of the entry.");
                }
            }
        }


        else { // *SCENARIO FOR LOCAL DIRECTORY ENTRY*
               // Read from single file contents whose path was extracted from inputValues ArrayList.

            if (ifDirectory(path)) { // *SCENARIO IF LOCAL ENTRY IS A DIRECTORY*

                getEntriesLocalFileNames(path);

                ArrayList<String> test = new ArrayList<String>(data); // New ArrayList to hold each file in folder path

                for (String text : test) {
                    data.clear();

                    boolean hasKey = false;          // hasKey false by default
                    File newFile = new File(text);   // Create new local file with the extracted path to hold contents of file.
                    readfile(newFile);
                    for (String element : data) {    // Read contents of file line by line.

                        if (element.contains(key)) { // Check if key is in contents of the file.
                            hasKey = true;           // *ASSUME IF ATLEAST 1 FILE CONTAINS KEYWORD, ENTIRE FOLDER WILL BE PASSED TO NEXT FILTER* 
                            break;
                        }
                    }

                    if (hasKey) {    
                        localScenario = true;          
                        System.out.println("Key has been found in the contents of this file within the directory."); 
                        break;
                        // Print message for every file that contains the key. 
                        // Set localScenario to true
                 
                    } else {
                        System.out.println("Key has not been found in the contents of this file within the directory.");
                        localScenario = false;
                        // Print message for every file that doesn't contain key.
                        // Set localScenario to false
                    }
                }
            }
            if (ifFile(path)) { // *SCENARIO IF LOCAL ENTRY IS A SINGLE FILE*
                boolean hasKey = false; // hasKey false by default
                File newFile = new File(path); // Create new local file with the extracted path to hold contents of
                                               // file.
                readfile(newFile);
                for (String element : data) { // Read contents of file line by line.

                    if (element.contains(key)) { // Check if key is in contents of the file.
                        hasKey = true;
                        break;
                    }
                }

                if (hasKey) {
                    localScenario = true;
                    System.out.println("Key has been found in the contents of the file."); // If key is found, print prompt that key has been found and set localScenario boolean to true.

                } else {
                    System.out.println("Key is not found in the contents of the file.");  // If key is not found, prompt that key is not found and it will not be passed on.
                }
            } 

                                         // *LOCAL OUTPUT*
            if (localScenario) {         // If localScenario it true, generateLocalJson method.
                generateLocalJson(path); // Method will add type and successful file path to the output ArrayList.
                System.out.println("The Key has been found in the contents of the individual file or atleast 1 file in the directory and passed to next filter.");
            }
        }
    }



    public static boolean ifFile(String filePath) { // Method that checks if entry is a file
        File file = new File(filePath);
        return file.isFile();
    }

    public static boolean ifDirectory(String filePath) { // Method that checks if entry is a directory
        File file = new File(filePath);
        return file.isDirectory();
    }
}

