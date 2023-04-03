package com.mycompany.Project;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class List extends Processing_elements {

    // initialize variables
    private int Max;
    private String localPath = "empty";
    public ArrayList<String> outputList = new ArrayList<String>();

    public void setMax() {
        this.Max = Max;
    }

    public int getMax() {
        return this.Max;
    }

    // constructor to process input values and entries
    public List(ArrayList<String> inputValue, ArrayList<String> entries) {
        for (String text : inputValue) {
            System.out.println(text);

            if (text.contains("value") || text.contains("Value")) {
                this.Max = Integer.parseInt(text.replaceAll("value", "").replaceAll(" ", "").replaceAll(":", ""));
            }

        }
        for (String files : entries) {
            inputValue.add(files);
        }

        loopEntries(inputValue);
    }

    // define the operations function to process the input directory and create
    // output list
    public void operations() {
        // check if local path is specified

        if (local) {
            File file = new File(path);
            if (file.isDirectory()) {
                ArrayList<String> dirList = listProcess(file);
                int num = Math.min(dirList.size(), getMax());
                for (int i = 0; i < num; i++) {
                    outputList.add(dirList.get(i));
                }
            }
            System.out.println("List of Files\n"+ outputList);
        }
        if (!local) {
            System.out.println(isRemoteDIR(this.entryID));
            if (isRemoteDIR(this.entryID)) {
                getEntriesRemoteFileNamesDIR();
                // String temp = getEntriesRemoteFileName(this.entryID);
                //get directory path
                // File dir = new File(temp);
                // String absoluteDirPath = dir.getAbsolutePath();
                // ArrayList<String> dirList = listProcess(dir);
                // int num = Math.min(dirList.size(), getMax());
                if(data.size() < Max){
                    Max = data.size();
                }
                for (int i = 0; i < Max; i++) {
                    generateRemoteJson(repoID, data.get(i));
                }
            }
            System.out.println("List of Files\n"+ outputList);
        }
    }

    private static ArrayList<String> listProcess(File dir){
        ArrayList<String> dirList = new ArrayList<String>();

        //
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                // Gets everyfile from directory
                dirList.addAll(listProcess(file));
            } else {
                // Individual Entries will be added to the array
                dirList.add(file.getAbsolutePath());
            }
        }
        return dirList;
    }
}
