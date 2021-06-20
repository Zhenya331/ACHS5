package sqlwork;

import java.util.ArrayList;

public class GetAllFilesResult {
	public ArrayList<Integer> ids;
	public ArrayList<String> fileNames;
	
	public GetAllFilesResult(ArrayList<Integer> Ids, ArrayList<String> FileNames) {
		ids = Ids;
		fileNames = FileNames;
	}
}
