package edu.sdsu.cs;


import edu.sdsu.cs.FileFinder.FileFinder;
import edu.sdsu.cs.FileFinder.FileFinderImpl;
import edu.sdsu.cs.Parser.Parser;
import edu.sdsu.cs.Parser.FileParser;

import java.util.List;

public class App{

    public static void main( String[] args ){

        String directory = args[0];
        //String directory = "./";

        FileFinder finder = new FileFinderImpl();
        List<String> paths;
        paths = finder.findFilesWithinDirectory(directory);

        for(String path : paths) {
            Parser parser = new FileParser();
            parser.setTarget(path);
            parser.setOutputPath(path);
            parser.parseCurrentTarget();
        }

    }

}

