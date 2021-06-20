

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sqlwork.GetStatisticFromFileResult;
import sqlwork.Link2DB;

public class fileData extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public fileData() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileId = request.getParameter("id");
		
        Link2DB link = new Link2DB();
        GetStatisticFromFileResult res = link.GetStatisticFromFile(Integer.parseInt(fileId));
        link.Dispose();
        
        response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try {
			out.print("<html><body>");
			out.print("<a href=http://localhost:8081/first/index>Main</a>");
			String path = "<a href=http://localhost:8081/first/fileText?id=" + fileId + ">" + res.fileName + "</a>";
			out.print("<h3>" + path + "</h3>");
			out.print("<table>");
			out.print("<tr><td>" + "Word" + "</td><td>" + "Count" + "</td></tr>");
			
			for(int i = 0; i < res.counts.size(); i++) {
				out.print("<tr><td>" + res.words.get(i) + "</td><td>" + res.counts.get(i) + "</td></tr>");
		    }
			out.print("</table>");
			out.print("</body></html>");
		} finally {
			out.close();
		}
	}
}
