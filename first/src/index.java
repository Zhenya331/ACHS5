

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sqlwork.GetAllFilesResult;
import sqlwork.Link2DB;


public class index extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public index() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Link2DB link = new Link2DB();
		GetAllFilesResult res = link.GetAllFiles();
        link.Dispose();
        
        response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try {
			out.print("<html><body>");
			out.print("<h3>Main</h3>");
			out.print("<ul>");
			for(int i = 0; i < res.ids.size(); i++) {
				String path = "<a href=http://localhost:8081/first/fileData?id=" + res.ids.get(i) + ">" + res.fileNames.get(i) + "</a>";
				out.print("<li>" + path + "</li>");
		    }
			out.print("</ul>");
			out.print("</body></html>");
		} finally {
			out.close();
		}
	}
}
