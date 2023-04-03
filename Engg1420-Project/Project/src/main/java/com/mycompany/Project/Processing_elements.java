package com.mycompany.Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import com.laserfiche.repository.api.clients.impl.model.Entry;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
  

abstract class Processing_elements {

    // array list for information
    protected ArrayList<String> data = new ArrayList<String>();

    private ArrayList<String> outputList = new ArrayList<String>(); 
    //the outputlist will be in a similar format to the json file

    //varaibles for passed in information
    protected String repoID = null;
    protected String entryID = null;
    protected String path = null;
    protected Boolean local = false;
    protected int remote = 0;

    // classes that will need to be defined
    protected abstract void operations();


    //this function will onlyy be called when there wasn't the proper condition
    protected void addPastEntries(ArrayList<String> pastEntries){
        outputList = pastEntries;
    }

    //will output the new arraylist in App.java
    public ArrayList<String> outputs() {
        return outputList;
    };    
    

    //will add the current file being read to the output list
    protected void addFileToList(){

        //determines if it's a remote or local and add the details respectively 
        if(local == true && path != null){
            generateLocalJson(this.path);
        }
        else if(remote == 0 && entryID != null && repoID != null){
            generateRemoteJson(this.repoID, this.entryID);
        }
    }

    //generates the local info in a json format
    protected void generateLocalJson(String path){
        outputList.add("type: local");
        outputList.add("path: " + path);
    };

    //generate the remote info in a json format
    protected void generateRemoteJson(String repoId, String entryID){
        outputList.add("type: remote");
        outputList.add("repoId: " + repoId);
        outputList.add("entryId: " + entryID);
    }

    //goes through the list of entries
    //determines if it's remote or local and passes important values
    //runs the operation of the filter for each entry
    protected void loopEntries(ArrayList<String> inputValues){

        for (String text : inputValues) {

            //prints out the entries data
            System.out.println(text);
            data.removeAll(data);

            //if statements to determine if it's a local or a remote and it's values
            //it will then call operations which will pull on the location data(path, entryId, repoID)
            //uses tolowerCase for account for many types
            if (text.toLowerCase().contains("type") && text.toLowerCase().contains("local"))
                local = true;

            if (local) {
                if (text.toLowerCase().contains("path")) {
                    path = text.substring(6, text.length()).trim();
                    System.out.println(path);
                    operations();
                    local = false;
                    path = null;

                }
            }
            if (text.toLowerCase().contains("type") && text.toLowerCase().contains("remote")) {
                remote = 3;
            }
            if (remote > 0) {
                
                if (text.toLowerCase().contains("repoid")) {
                    repoID = text.toLowerCase().replaceAll("repoid", "").replaceAll(" ", "").replaceAll(":", "");
                }
                if (text.toLowerCase().contains("entryid")) {
                    entryID = text.toLowerCase().replaceAll("entryid", "").replaceAll(" ", "").replaceAll(":", "");
                }
                remote--;
                if(entryID != null && repoID != null){
                    operations();
                    repoID = null;
                    entryID = null;
                }
            }
        }
    }


    //gets the names of files in a local folder 
    //places information in data arraylist
    protected void getEntriesLocalFileNames(String filename) {
        
        //generate location
        File folder = new File(filename);

        //get all the files inside folder
        File[] listOfFiles = folder.listFiles();

        //add them to the arralist
        for (int i = 0; i < listOfFiles.length; i++) {
            data.add(listOfFiles[i].getAbsolutePath());
        }
    }

    // generates all information inside local file or folder based on file location
    // places information in entries array
    protected void getEntriesLocal(String filename) {

        // checks if it's a txt file or a directory
        if (filename.contains("txt")) {
            File file = new File(filename);
            readfile(file);
        } else {

            // gets all files in directory
            File folder = new File(filename);
            File[] listOfFiles = folder.listFiles();

            // will read all files
            for (int i = 0; i < listOfFiles.length; i++) {

                // makes sure the file is a file and won't crash the software
                if (listOfFiles[i].isFile()) {
                    readfile(listOfFiles[i]);
                } else if (listOfFiles[i].isDirectory()) {
                    System.out.println("Read in directory");
                }
            }
        }
    }

    // reads local file line by line and store in entries array
    protected void readfile(File filename) {

        // try catch, just in case there's an error
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String s;
            // read each line indiviually while the file is not at the end
            while ((s = reader.readLine()) != null) {
                data.add(s);
            }
            reader.close();
            // exception handling
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    //get all remote entries files inside of a folder
    protected void getEntriesRemoteFileNamesDIR() {

        String servicePrincipalKey = "x0BmysMxlH_XfLoc69Kk";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiOGFkZTZjNTctZDIxNS00ZmYyLThkOTctOTE1YjRiYWUyZWIzIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogImNCeWdXYnh6YU9jRHZVcUdBU1RfcURTY0plcWw3aU9Ya19SZVFleUpiTzQiLAoJCSJ4IjogIjZNSXNuODRLanFtMEpTUmhmS2tHUTRzbGhkcldCbVNMWk9nMW5oWjhubFkiLAoJCSJ5IjogIlpkZ1M1YWIxdU0yaVdaWHVpdmpBc2VacC11LWlJUlc4MjFwZWhENVJ5bUkiLAoJCSJkIjogIldjN091cDFYV3FudjlEVFVzQWZIYmxGTDFqU3UwRWJRY3g0LXNqbG0xRmMiLAoJCSJpYXQiOiAxNjc3Mjk3NTU0Cgl9Cn0=";
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);
        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);

        ODataValueContextOfIListOfEntry result = client
                .getEntriesClient()
                .getEntryListing(this.repoID, Integer.parseInt(this.entryID), true, null, null, null, null, null, "name", null, null,
                        null)
                .join();

        //add the child entryID to data arraylst
        List<Entry> RemoteEntries = result.getValue();
        for (Entry childEntry : RemoteEntries) {
            data.add(Integer.toString(childEntry.getId()));
        }
    }
    

    //determines if entryID is a file or folder
    //if it's a file return false
    protected boolean isRemoteDIR(String entryId){
        String servicePrincipalKey = "x0BmysMxlH_XfLoc69Kk";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiOGFkZTZjNTctZDIxNS00ZmYyLThkOTctOTE1YjRiYWUyZWIzIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogImNCeWdXYnh6YU9jRHZVcUdBU1RfcURTY0plcWw3aU9Ya19SZVFleUpiTzQiLAoJCSJ4IjogIjZNSXNuODRLanFtMEpTUmhmS2tHUTRzbGhkcldCbVNMWk9nMW5oWjhubFkiLAoJCSJ5IjogIlpkZ1M1YWIxdU0yaVdaWHVpdmpBc2VacC11LWlJUlc4MjFwZWhENVJ5bUkiLAoJCSJkIjogIldjN091cDFYV3FudjlEVFVzQWZIYmxGTDFqU3UwRWJRY3g0LXNqbG0xRmMiLAoJCSJpYXQiOiAxNjc3Mjk3NTU0Cgl9Cn0=";
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        // open client
        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);

        // create instance of the client/open file location
        Entry entry = client.getEntriesClient().getEntry(this.repoID, Integer.parseInt(entryId), null).join();

        // if the entry is a file
        if (entry.getEntryType().toString() == "Document") {
            client.close();
            return false;
        }

        // if the entryID type is a folder
        else if (entry.getEntryType().toString() == "Folder") {
            client.close();
            return true;
        } else {
            System.out.println("Error occured");
            client.close();
            return false;
        }
    }

    //gets the file name of the remote entry
    protected String getEntriesRemoteFileName(String entryID) {

        String servicePrincipalKey = "x0BmysMxlH_XfLoc69Kk";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiOGFkZTZjNTctZDIxNS00ZmYyLThkOTctOTE1YjRiYWUyZWIzIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogImNCeWdXYnh6YU9jRHZVcUdBU1RfcURTY0plcWw3aU9Ya19SZVFleUpiTzQiLAoJCSJ4IjogIjZNSXNuODRLanFtMEpTUmhmS2tHUTRzbGhkcldCbVNMWk9nMW5oWjhubFkiLAoJCSJ5IjogIlpkZ1M1YWIxdU0yaVdaWHVpdmpBc2VacC11LWlJUlc4MjFwZWhENVJ5bUkiLAoJCSJkIjogIldjN091cDFYV3FudjlEVFVzQWZIYmxGTDFqU3UwRWJRY3g0LXNqbG0xRmMiLAoJCSJpYXQiOiAxNjc3Mjk3NTU0Cgl9Cn0=";
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);
        
        Entry entry = client.getEntriesClient().getEntry(this.repoID, Integer.parseInt(entryID), null).join();

        return entry.getName();
    }

    //get the absolute path of the remote entry
    protected String getEntriesAbsolutePath() {

        String servicePrincipalKey = "x0BmysMxlH_XfLoc69Kk";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiOGFkZTZjNTctZDIxNS00ZmYyLThkOTctOTE1YjRiYWUyZWIzIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogImNCeWdXYnh6YU9jRHZVcUdBU1RfcURTY0plcWw3aU9Ya19SZVFleUpiTzQiLAoJCSJ4IjogIjZNSXNuODRLanFtMEpTUmhmS2tHUTRzbGhkcldCbVNMWk9nMW5oWjhubFkiLAoJCSJ5IjogIlpkZ1M1YWIxdU0yaVdaWHVpdmpBc2VacC11LWlJUlc4MjFwZWhENVJ5bUkiLAoJCSJkIjogIldjN091cDFYV3FudjlEVFVzQWZIYmxGTDFqU3UwRWJRY3g0LXNqbG0xRmMiLAoJCSJpYXQiOiAxNjc3Mjk3NTU0Cgl9Cn0=";
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);
        Entry entry = client.getEntriesClient().getEntry(this.repoID, Integer.parseInt(this.entryID), null).join();

        return entry.getFullPath();
    }


    //I don't think we need/use this anymore 
    //get rid of it
    protected String getEntriesLength() {

        String servicePrincipalKey = "x0BmysMxlH_XfLoc69Kk";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiOGFkZTZjNTctZDIxNS00ZmYyLThkOTctOTE1YjRiYWUyZWIzIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogImNCeWdXYnh6YU9jRHZVcUdBU1RfcURTY0plcWw3aU9Ya19SZVFleUpiTzQiLAoJCSJ4IjogIjZNSXNuODRLanFtMEpTUmhmS2tHUTRzbGhkcldCbVNMWk9nMW5oWjhubFkiLAoJCSJ5IjogIlpkZ1M1YWIxdU0yaVdaWHVpdmpBc2VacC11LWlJUlc4MjFwZWhENVJ5bUkiLAoJCSJkIjogIldjN091cDFYV3FudjlEVFVzQWZIYmxGTDFqU3UwRWJRY3g0LXNqbG0xRmMiLAoJCSJpYXQiOiAxNjc3Mjk3NTU0Cgl9Cn0=";
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);
        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);

        Entry entry = client.getEntriesClient().getEntry(this.repoID, Integer.parseInt(this.entryID), null).join();

        return entry.getVolumeName();
    }

    //get the content inside the remote file
    protected void getEntriesRemote(Integer entryId) {

        // key details
        String servicePrincipalKey = "x0BmysMxlH_XfLoc69Kk";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiOGFkZTZjNTctZDIxNS00ZmYyLThkOTctOTE1YjRiYWUyZWIzIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogImNCeWdXYnh6YU9jRHZVcUdBU1RfcURTY0plcWw3aU9Ya19SZVFleUpiTzQiLAoJCSJ4IjogIjZNSXNuODRLanFtMEpTUmhmS2tHUTRzbGhkcldCbVNMWk9nMW5oWjhubFkiLAoJCSJ5IjogIlpkZ1M1YWIxdU0yaVdaWHVpdmpBc2VacC11LWlJUlc4MjFwZWhENVJ5bUkiLAoJCSJkIjogIldjN091cDFYV3FudjlEVFVzQWZIYmxGTDFqU3UwRWJRY3g0LXNqbG0xRmMiLAoJCSJpYXQiOiAxNjc3Mjk3NTU0Cgl9Cn0=";
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        // open client
        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);

        // create instance of the client/open file location
        Entry entry = client.getEntriesClient().getEntry(this.repoID, entryId, null).join();

        // if the entry is a file
        if (entry.getEntryType().toString() == "Document") {
            createFileFromRemote(client, this.repoID, entryId);
            // place file information in arraylist
            File file = new File("Project\\remoteFile.txt");
            readfile(file);

            File deleteFile = new File("Project\\remoteFile.txt");
            deleteFile.delete();
        }

        // if the entryID type is a folder
        else if (entry.getEntryType().toString() == "Folder") {
            ODataValueContextOfIListOfEntry result = client
                    .getEntriesClient()
                    .getEntryListing(this.repoID, Integer.parseInt(this.entryID), true, null, null, null, null, null, "name", null, null, null)
                    .join();
            List<Entry> RemoteEntries = result.getValue();
            for (Entry childEntry : RemoteEntries) {
                getEntriesRemote(childEntry.getId());
            }
        } else {
            System.out.println("Error occured");
        }

        client.close();
    }

    //creates a replica of the remote file on the local computer 
    private void createFileFromRemote(RepositoryApiClient client, String repoId, int entryId) {
        // create a new file and store the remote file in a new local file

        // delete old file
        File deleteFile = new File("Project\\remoteFile.txt");
        deleteFile.delete();

        // create new file
        final String FILE_NAME = "Project\\remoteFile.txt";

        //create new file
        Consumer<InputStream> consumer = inputStream -> {
            File exportedFile = new File(FILE_NAME);
            try (FileOutputStream outputStream = new FileOutputStream(exportedFile)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = inputStream.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    outputStream.write(buffer, 0, length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // get the file details
        client.getEntriesClient()
                .exportDocument(repoId, entryId, null, consumer)
                .join();
    }

    //get the file size of the remote file by making a copy on the local drive
    //reading it's value and then deleting the file
    protected long getRemoteFileSize(String entryID) {

        String servicePrincipalKey = "x0BmysMxlH_XfLoc69Kk";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiOGFkZTZjNTctZDIxNS00ZmYyLThkOTctOTE1YjRiYWUyZWIzIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogImNCeWdXYnh6YU9jRHZVcUdBU1RfcURTY0plcWw3aU9Ya19SZVFleUpiTzQiLAoJCSJ4IjogIjZNSXNuODRLanFtMEpTUmhmS2tHUTRzbGhkcldCbVNMWk9nMW5oWjhubFkiLAoJCSJ5IjogIlpkZ1M1YWIxdU0yaVdaWHVpdmpBc2VacC11LWlJUlc4MjFwZWhENVJ5bUkiLAoJCSJkIjogIldjN091cDFYV3FudjlEVFVzQWZIYmxGTDFqU3UwRWJRY3g0LXNqbG0xRmMiLAoJCSJpYXQiOiAxNjc3Mjk3NTU0Cgl9Cn0=";
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);
        // create a new file and store the remote file in a new local file  

        // delete old file
        File deleteFile = new File("Project\\remoteFile.txt");

        // create new file
        final String FILE_NAME = "Project\\remoteFile.txt";
        Consumer<InputStream> consumer = inputStream -> {
            File exportedFile = new File(FILE_NAME);
            try (FileOutputStream outputStream = new FileOutputStream(exportedFile)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = inputStream.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    outputStream.write(buffer, 0, length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // get the file details
        client.getEntriesClient()
                .exportDocument(this.repoID, Integer.parseInt(entryID), null, consumer)
                .join();
        long length = deleteFile.length();
        deleteFile.delete();
        return length;
    }
}