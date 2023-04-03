package com.mycompany.Project;

import java.io.File;
import java.util.ArrayList;



public class Print extends Processing_elements {

    // constructor
    protected Print(ArrayList<String> inputValue, ArrayList<String> pastEntries) {
            
        //add the past entries to the new entries 
        inputValue.addAll(pastEntries);
        //goes through all the entries and calls operations
        loopEntries(inputValue);


    }

    // define these functions
    protected void operations() {

        //if local 
        if (local) {
            try {

                //check if file is local
                File file = new File(path);

                if (file.isFile()) {


                    //get the absolute path
                    String absolute = file.getAbsolutePath();

                    //get the file length
                    long length = file.length();

                    //get the file name 
                    String[] split = absolute.split("\\\\");

                    //print out the information
                    System.out.println("Path: " + absolute + "\nLength: " + Long.toString(length) + "\nName: " + split[split.length - 1] + "\n");
                }

                // if a directory 
                else {

                    //get the absolute path
                    String absolute = file.getAbsolutePath();

                    // get the length of all the files in side the folder and add them up
                    long length = getFolderSize(file);// length function in processing elements;

                    //get the folder name
                    String[] split = path.split("\\\\");

                    //print the information
                    System.out.println("Path: " + absolute + "\nLength: " + Long.toString(length) + "\nName: " + split[split.length - 1] + "\n");
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            //if it's remote
        } else {

            //get file name
            String name = getEntriesRemoteFileName(this.entryID);

            //get file path
            String absolute = getEntriesAbsolutePath();

            long length = 0;

            //if it's a folder 
            //add all the lengths of files inside
            if(isRemoteDIR(this.entryID)){

                //get the childern entries
                getEntriesRemoteFileNamesDIR();
                for(String childFile: data){

                    //add length
                    length += getRemoteFileSize(childFile);// length function in processing elements;
                }
            }else{
            
                //set length 
                length = getRemoteFileSize(this.entryID);// length function in processing elements;
            }

            //print out details
            System.out.println("EntryID: " + entryID + "\nPath: " + absolute + "\nLength: " + Long.toString(length) + "\nName: " + name + "\n");

        }

    };

    //gets the numbers of bytes in the folder
    protected long getFolderSize(File folder) {
        long length = 0;

        // listFiles() is used to list the
        // contents of the given folder
        File[] files = folder.listFiles();

        int count = files.length;

        // loop for traversing the directory
        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            } else {
                length += getFolderSize(files[i]);
            }
        }

        //once it sums up all the files in the folder it returns the total size of the folder
        return length;
    }

}
