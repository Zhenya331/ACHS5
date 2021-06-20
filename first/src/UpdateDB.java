import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Parallel.ParallelParams;
import Parallel.ParallelResult;
import Parallel.StatisticAsync;
import company.FileStatistic;
import company.FileWork;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sqlwork.Link2DB;

public class UpdateDB extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public UpdateDB() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File folder = new File(FileWork.pathToDirectory);
		ArrayList<String> fileNames = FileWork.GetFileNames(folder);
		Map<String, FileStatistic> res = GetStatisticParallel(fileNames);
		
		Link2DB link = new Link2DB();
		link.DeleteStatistic();
		boolean insertResult = link.InsertStatistic(res);
        link.Dispose();
        
        response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try {
			out.print("<html><body>");
			out.print("<a href=http://localhost:8081/first/index>Main</a>");
			if(insertResult) {
				out.print("<h2>Updated successfully</h2>");
			} else {
				out.print("<h2>Update failed</h2>");
			}
			out.print("</body></html>");
		} finally {
			out.close();
		}
	}
	
	public static Map<String, FileStatistic> GetStatisticParallel(ArrayList<String> fileNames) {
        int maxNumThreads = 6;
        int N = Math.min(fileNames.size(), maxNumThreads);
        ParallelParams[] params = new ParallelParams[N];
        for(int i = 0; i < N; i++) {
            int koef = fileNames.size() / N;
            int remains = 0;
            if (i == N - 1) {
                remains = fileNames.size() - koef * N;
            }
            ArrayList<String> fileNamesThread = new ArrayList<>();
            for(int j = 0; j < koef; j++) {
                fileNamesThread.add(fileNames.get(j + koef * i));
            }
            for(int k = 0; k < remains; k++) {
                fileNamesThread.add(fileNames.get(fileNames.size() - remains + k));
            }
            params[i] = new ParallelParams(i, fileNamesThread);
        }

        StatisticAsync[] stat = new StatisticAsync[N];
        StatisticAsync.endThreads = new boolean[N];
        ParallelResult.data = new ArrayList<>();
        for(int i = 0; i < N; i++) {
            ParallelResult.data.add(new HashMap<>());
            StatisticAsync.endThreads[i] = false;
            stat[i] = new StatisticAsync(params[i]);
            stat[i].run();
        }

        boolean syncThreads = false;
        while(syncThreads) {
            boolean flag = true;
            for(int i = 0; i < N; i++) {
                if(!StatisticAsync.endThreads[i]) {
                    flag = false;
                }
            }
            syncThreads = flag;
        }

        Map<String, FileStatistic> result = new HashMap<>();
        for(int i = 0; i < N; i++) {
            result.putAll(ParallelResult.data.get(i));
        }
        return result;
    }
}
