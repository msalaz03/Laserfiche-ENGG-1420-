package com.mycompany.Project;

import java.io.*;
import java.util.ArrayList;

public class Split extends Processing_elements {

    public ArrayList<File> splitFile = null;
    private int lines;
    public ArrayList<String> outputLists = new ArrayList<String>();

    // constructor that takes inputValue and entries as parameters
    public Split(ArrayList<String> inputValues, ArrayList<String> pastEntries) {
        for (String text : inputValues) {
            if (text.contains("value") || text.contains("Value")) {
                lines = Integer.parseInt(text.replaceAll("value", "").replaceAll(" ", "").replaceAll(":", ""));
            }
        }

        for (String files : pastEntries) {
            inputValues.add(files);

        }

        loopEntries(inputValues);
    }

    public void operations() {
        if (local) {
            File file = new File(path);
            // create a new ArrayList for splitFile
            if (file.isFile()) {
                splitFile = new ArrayList<File>();

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    int lineNumber = 0;
                    int partNumber = 1;
                    ArrayList<String> lines = new ArrayList<String>(this.lines);
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        lines.add(line);
                        lineNumber++;
                        if (lineNumber == this.lines) {
                            // write lines to new file
                            String fileName = editName(file) + ".part" + partNumber + ".txt";
                            File newFile = new File(file.getParentFile(), fileName);
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                                // write each line to the new file
                                for (String l : lines) {
                                    writer.write(l);
                                    writer.newLine();
                                }
                            }
                            // add the new file to splitFile
                            splitFile.add(newFile);
                            System.out.println(newFile.getAbsolutePath());
                            outputLists.add(newFile.getAbsolutePath());
                            // clear lines and reset lineNumber
                            lines.clear();
                            lineNumber = 0;
                            partNumber++;
                        }
                    }
                    // write remaining lines to new file
                    if (!lines.isEmpty()) {
                        String fileName = editName(file) + ".part" + partNumber + ".txt";
                        File newFile = new File(file.getParentFile(), fileName);
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                            for (String l : lines) {
                                writer.write(l);
                                writer.newLine();
                            }
                        }
                        splitFile.add(newFile); // Makes the new files
                        outputLists.add(fileName);// prints file directories
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Handles IOException
                }
            }

            else if (!file.isFile()) {
                System.out.println("File does not exist or is directory.");
            }
        } else {
            // Remote is sending Error

            if (isRemoteDIR(this.entryID)) {
                System.out.println("File is a directory, please enter a file");
            } else if (!isRemoteDIR(this.entryID)) {

                String temp = getEntriesRemoteFileName(this.entryID);
                File file = new File(temp);
                splitFile = new ArrayList<File>();
                try {
                    int counter = 1;
                    String newName = temp+".part"+ counter;
                    getEntriesRemote(Integer.parseInt(entryID));
                    System.out.println(data.size());
                    while(data.size() >=1){
                        File miniFile = new File(newName+".txt");
                        miniFile.createNewFile();
                        FileWriter myWriter = new FileWriter(newName+".txt");
                        generateLocalJson(miniFile.getAbsolutePath());

                        // loops through data(arraylist with each index being a new line) and add it to
                        // the new file
                        for (int i = 0; i < lines; i++) {
                            myWriter.write(data.get(0) + "\n");
                            data.remove(0);
                        }
                        myWriter.close();
                        counter ++;
                        newName = temp+".part"+ counter;
                    }

                } catch (Exception e) {

                }
                // try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                // String line;
                // int lineNumber = 0;
                // int partNumber = 1;
                // ArrayList<String> lines = new ArrayList<String>(this.lines);
                // while ((line = reader.readLine()) != null) {
                // System.out.println(line);
                // lines.add(line);
                // lineNumber++;
                // if (lineNumber == this.lines) {
                // // write lines to new file
                // String fileName = editName(file) + ".part" + partNumber + ".txt";
                // File newFile = new File(file.getParentFile(), fileName);
                // try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                // // write each line to the new file
                // for (String l : lines) {
                // writer.write(l);
                // writer.newLine();
                // }
                // }
                // // add the new file to splitFile
                // splitFile.add(newFile);
                // System.out.println(newFile.getAbsolutePath());
                // outputLists.add(newFile.getAbsolutePath());
                // // clear lines and reset lineNumber
                // lines.clear();
                // lineNumber = 0;
                // partNumber++;
                // }
                // }
                // // write remaining lines to new file
                // if (!lines.isEmpty()) {
                // String fileName = editName(file) + ".part" + partNumber + ".txt";
                // File newFile = new File(file.getParentFile(), fileName);
                // try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                // for (String l : lines) {
                // writer.write(l);
                // writer.newLine();
                // }
                // }
                // splitFile.add(newFile); // Makes the new files
                // outputLists.add(fileName);// prints file directories
                // }
                // } catch (IOException e) {
                // e.printStackTrace(); // Handles IOException
                // }
            }

        }
    }

    private static String editName(File file) {
        String fileNameWithoutExtension = file.getName().substring(0, file.getName().lastIndexOf('.'));
        return fileNameWithoutExtension;
    }
}
