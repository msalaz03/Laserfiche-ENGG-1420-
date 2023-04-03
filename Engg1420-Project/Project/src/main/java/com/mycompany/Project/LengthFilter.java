package com.mycompany.Project;

import java.util.ArrayList;
import java.io.*;


public class LengthFilter extends Processing_elements{
    //Declarlation of Variables + Getters / Setters
    private long length = 0;
    private String Operator;
    
    public void setOperator(String operator){
        this.Operator = operator;
    }

    public String getOperator(){
        return this.Operator;
    }

    public void setLength(long length){
        this.length = length;
    }

    public long getLength(){
        return this.length;
    }

    
    //Constructor
    public LengthFilter(ArrayList<String> inputs, ArrayList<String> entries){
        String tempstr = "";
        //Extract Operators & Length Values.
        for (String text : inputs) {

            if (text.contains("Length")){
                tempstr = "Length";
            }

            if (text.contains("Operator")){
                tempstr = "Operator";
            }

            if ((text.contains("value") || text.contains("Value")) && tempstr.equals("Length")) {
                this.length = Long.parseLong((text.replaceAll("value", "").replaceAll(" ", "").replaceAll(":", ""))); 
            }

            if ((text.contains("value") || text.contains("Value")) && tempstr.equals("Operator")) {
                this.Operator = text.replaceAll("value", "").replaceAll(" ", "").replaceAll(":" , ""); 
            }
        }
        
        //Couldnt extract required variables.
        if(Operator == null || length == 0){
            System.out.println("Parameters couldn't be found in JSON, check formatting");
        }

        //add this file into inputs arrayList
        else
        {
        inputs.addAll(entries);
        loopEntries(inputs);
        }

        
        // for (String text : data) {
        //    System.out.println(text);
        // }
       
         //OUTPUTS
        //  System.out.println("EXTRACTED CONTENT");
        //  System.out.println(getOperator());
        //  System.out.println(getLength());
    }



    @Override
    public void operations()
    {
        //LOCAL CASE
        if(local){
        File subFile = new File(path);
        if (subFile.isFile()){
            System.out.println("LENGTH OF FILE: " + subFile.length());
            switch (getOperator()) {

                case "EQ":
                    if (subFile.length() == getLength())
                    {
                        addFileToList();
                    }
                    else
                    {
                        System.out.println("FILE: " + subFile.getName() + " Does not meet requirements for Operator");
                    }
                    break;
    
                case "NEQ":
                    if (subFile.length() != getLength())
                    {
                    addFileToList();
                    }
                    else
                    {
                        System.out.println("FILE: " + subFile.getName() + " Does not meet requirements for Operator");
                    }
                    break;
               
                case "GT":
                    if (subFile.length() > getLength())
                    {
                    addFileToList();
                    }
                    else
                    {
                        System.out.println("FILE: " + subFile.getName() + " Does not meet requirements for Operator");
                    }
                    break;
    
                case "GTE":
                    if (subFile.length() >= getLength())
                    {
                    addFileToList();
                    }
                    else
                    {
                        System.out.println("FILE: " + subFile.getName() + " Does not meet requirements for Operator");
                    }
                    break;
    
                case "LT":    
                    if (subFile.length() < getLength())
                    {
                    addFileToList();
                    }
                    else
                    {
                        System.out.println("FILE: " + subFile.getName() + " Does not meet requirements for Operator");
                    }
                    break;
    
                case "LTE":     
                    if (subFile.length() <= getLength())
                    {
                    addFileToList();
                    }
                    else
                    {
                        System.out.println("FILE: " + subFile.getName() + " Does not meet requirements for Operator");
                    }
                    break;
    
                default:
                    System.out.println("Operator does not exist, all files outtputted");
                    addFileToList();  
                    break;
                }
        }
        //LengthFilter only deals with directories
        else if(!subFile.isFile())
        {
            System.out.println("File does not exist or is directory.");
        }

        }

        //Remote Case
        else if (!local)
        {
            if (isRemoteDIR(entryID)){
                System.out.println("File is a directory, please enter a file");
            }
            else if (!isRemoteDIR(entryID)){
                //getEntriesRemote(Interger.parseInt(entryID));
                long fileSize = getRemoteFileSize(this.entryID);
                System.out.println("File Size: " + fileSize);

                switch (getOperator()) {

                    case "EQ":
                        if (fileSize == getLength())
                        {
                        addFileToList();
                        }
                        else{
                            entryFail();
                        }
                        break;
        
                    case "NEQ":
                        if (fileSize != getLength())
                        {
                        addFileToList();
                        }
                        else{
                            entryFail();
                        }
                        break;
                   
                    case "GT":
                        if (fileSize > getLength())
                        {
                        addFileToList();
                        }
                        else{
                            entryFail();
                        }
                        break;
        
                    case "GTE":
                        if (fileSize >= getLength())
                        {
                        addFileToList();
                        }
                        else{
                            entryFail();
                        }
                        break;
        
                    case "LT":    
                        if (fileSize < getLength())
                        {
                        addFileToList();
                        }
                        else{
                            entryFail();
                        }
                        
                        break;
        
                    case "LTE":
                        if (fileSize <= getLength())
                        {
                        addFileToList();
                        }
                        else{
                            entryFail();
                        }
                        break;
        
                    default:
                        System.out.println("Operator does not exist, all files outtputted");
                        addFileToList();  
                        break;
                    }
                
            }
        }
        }

    //Get File Size for Remote
  

    //Simple Function used in Remote switch case
    private void entryFail()
    {
        System.out.println("Entry did not meet size requirements.");
    }

}


