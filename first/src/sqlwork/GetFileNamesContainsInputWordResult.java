package sqlwork;

import java.util.ArrayList;

public class GetFileNamesContainsInputWordResult {
	public ArrayList<String> fileNames;
	public ArrayList<Integer> fileIds;
	public ArrayList<Integer> counts;
	
	public GetFileNamesContainsInputWordResult(ArrayList<String> fileNames, ArrayList<Integer> fileIds, ArrayList<Integer> counts) {
        this.fileNames = fileNames;
        this.fileIds = fileIds;
        this.counts = counts;
    }
}
