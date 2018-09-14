package edu.sdsu.cs.FileFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFinderImpl implements FileFinder {

    private List<String> paths = new ArrayList<>();

    public List<String> findFilesWithinDirectory(String path) {
        File folder = new File(path);

        for(File fileEntry : folder.listFiles()){
            if(fileEntry.isDirectory())
                findFilesWithinDirectory(fileEntry.getPath());
            else if(fileEntry.getPath().endsWith(".txt") ||
                    fileEntry.getPath().endsWith(".java"))
                paths.add(fileEntry.getPath());
        }
        return paths;
    }

}
