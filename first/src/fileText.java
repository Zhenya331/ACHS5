

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sqlwork.GetFileTextResult;
import sqlwork.Link2DB;


public class fileText extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public fileText() {}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileId = request.getParameter("id");
		
        Link2DB link = new Link2DB();
        GetFileTextResult res = link.GetFileText(Integer.parseInt(fileId));
        link.Dispose();
        
        response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try {
			out.print("<html><body>");
			out.print("<a href=http://localhost:8081/first/index>Main</a>");
			String path = "<a href=http://localhost:8081/first/fileData?id=" + fileId + ">" + res.name + "</a>";
			out.print("<h3>" + path + "</h3>");
			out.print("<h4>" + res.text + "</h4>");
			out.print("</body></html>");
		} finally {
			out.close();
		}
	}
}
