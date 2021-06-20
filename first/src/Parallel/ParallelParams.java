package Parallel;

import java.util.ArrayList;

public class ParallelParams {
    public int numOfThread;
    public ArrayList<String> fileNames;

    public ParallelParams(int numOfThread, ArrayList<String> fileNames) {
        this.numOfThread = numOfThread;
        this.fileNames = fileNames;
    }
}
