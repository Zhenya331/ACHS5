package sqlwork;

import java.util.ArrayList;

public class GetStatisticFromFileResult {
	public String fileName;
	public ArrayList<String> words;
	public ArrayList<Integer> counts;
	
	public GetStatisticFromFileResult(String fileName, ArrayList<String> words, ArrayList<Integer> counts) {
        this.fileName = fileName;
        this.words = words;
        this.counts = counts;
    }
}
