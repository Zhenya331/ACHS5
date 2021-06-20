

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sqlwork.GetFileNamesContainsInputWordResult;
import sqlwork.Link2DB;

public class Hello extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Hello() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key = request.getParameter("key");
		
        Link2DB link = new Link2DB();
        GetFileNamesContainsInputWordResult res = link.GetFileNamesContainsInputWord(key);
        link.Dispose();
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try {
			out.print("<html><body>");
			out.print("<a href=http://localhost:8081/first/index>Main</a>");
			out.print("<h3>" + key + ":</h3>");
			out.print("<table>");
			out.print("<tr><td>" + "File" + "</td><td>" + "Count" + "</td></tr>");
			
			for(int i = 0; i < res.counts.size(); i++) {
				String path = "<a href=http://localhost:8081/first/fileData?id=" + res.fileIds.get(i) + ">" + res.fileNames.get(i) + "</a>";
				out.print("<tr><td>" + path + "</td><td>" + res.counts.get(i) + "</td></tr>");
		    }
			out.print("</table>");
			out.print("</body></html>");
		} finally {
			out.close();
		}
	}
}
