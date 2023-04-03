package com.mycompany.Project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class Rename extends Processing_elements {

    private String suffix = "";

    // constructor
    public Rename(ArrayList<String> inputValue, ArrayList<String> pastEntries) {

        //loops through the inputValues since it will contain the suffix value
        for (String text : inputValue) {
            
            //if the next line contains the word value and the suffix boolean is true(one of the lines before contained the word sufffix)
            if (text.contains("value")) {

                //gets rid of non relatvent information like value and : 
                suffix = text.replace("value", "").replace(".", "").replace(":", "").trim();
                break;
            }
        }

        //if there was no suffix
        if(suffix == ""){

            //return the past entry list
            addPastEntries(pastEntries);

        }
        else{
            
            //add the past entries to the new entries 
            inputValue.addAll(pastEntries);

            //go through each entry 
            loopEntries(inputValue);
        }

        // for(String text: outputList){
        // System.out.println(text);
        // }
    }

    protected void operations() {

        //if statement for if it's local or remote
        if (local) {

            //checks if it's a file or a folder
            File file = new File(path);
            if (file.isFile()) {

                //create the new path name
                String temp = path.substring(0, path.length() - 4).concat(suffix).concat(".txt");

                //add the new path name to the outputlist
                generateLocalJson(temp);

                //read the last path to copy the file
                readfile(file);
                File newfile = new File(temp);

                //copy the file details
                try {
                    copyFileUsingStream(newfile);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {

                //if it's a directory

                //add the new path to the list
                generateLocalJson(path.concat(suffix));
                File src = new File(path);
                File desFile = new File(path.concat(suffix));

                //makes the directory
                desFile.mkdir();

                //copies the contents of the directory
                try {
                    FileUtils.copyDirectory(src, desFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //add the last json to 
            generateLocalJson(path);
        } else {
            // if it's remote we create a file from the remote with the suffix.
            String temp = getEntriesRemoteFileName(this.entryID);

            //if it's a file
            if (!isRemoteDIR(this.entryID)) {
                
                //make modifications to the name
                temp = temp.concat(suffix).concat(".txt");
                generateLocalJson(temp);
                getEntriesRemote(Integer.parseInt(entryID));

                //create local file and add the txt information
                copyFileUsingStream(new File(temp));

            } else {
                //if it's a directory

                //change the directory name and add the directory to the output list
                generateLocalJson(temp.concat(suffix));

                //create local directory
                File dir = new File(temp.concat(suffix));
                dir.mkdirs();

                //get the files from the remote directry
                getEntriesRemoteFileNamesDIR();
                
                ArrayList<String> childId = new ArrayList<String>(data);

                //get directory path
                String absoluteDirPath = dir.getAbsolutePath();
                data.clear();

                //go through each file in that directory and copy it into the local verision of the directory
                for(String childFile: childId){

                    //create the txt name for the child file
                    String childFileName = getEntriesRemoteFileName(childFile) + ".txt";

                    //get the data of the child file
                    getEntriesRemote(Integer.parseInt(childFile));

                    //create the child file
                    copyFileUsingStream(new File(absoluteDirPath + "\\" + childFileName));
                    data.clear();
                }

                //create folder
                //create files in folder
            }

            //add the remote entry to the output list
            generateRemoteJson(this.repoID, this.entryID);
        }
    };

    // public Processing_elements(ArrayList<String> inputValue,
    // ArrayList<Processing_elements> pastEntries){

    // }

    // constructors will have 2 parameters; an arraylist of the past entries and an
    // arraylist of the information

    //copy file content to a new file
    private void copyFileUsingStream(File dest) {

        try {

            FileWriter myWriter = new FileWriter(dest);

            //loops through data(arraylist with each index being a new line) and add it to the new file
            for (String text : data) {
                myWriter.write(text + "\n");
            }
            myWriter.close();
        } catch (Exception e) {

        }
    }

}
