package edu.sdsu.cs.Parser;


public interface Parser {

    void setTarget(String target);

    void setOutputPath(String path);

    void parseCurrentTarget();

}
