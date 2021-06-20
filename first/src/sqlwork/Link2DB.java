package sqlwork;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import company.FileStatistic;

public class Link2DB {
	private final String url = "jdbc:sqlserver://DESKTOP-B6RRAFL\\MSSQLSERVER:1433;databaseName=ACHS";
	private final String user = "sa";
	private final String password = "sa";
	private Connection connect;
	
	public Link2DB() {
		try {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			connect = DriverManager.getConnection(url, user, password);
			System.out.println("Connection to " + url);
		} catch (SQLException e) {
			System.out.println("Failed to connection to " + url);
			e.printStackTrace();
		}
	}
	
	public GetFileNamesContainsInputWordResult GetFileNamesContainsInputWord(String Word) {
		try {
			String sql = "select f.FileName, fw.Count, w.Word, f.id from Files as f	\r\n"
						+ "join Files_Words as fw on fw.File_id = f.id			\r\n"
						+ "join Words as w on w.id = fw.Word_id					\r\n"
						+ "where w.Word = ?										\r\n"
						+ "order by fw.Count desc";
			PreparedStatement sqlState = connect.prepareStatement(sql);
            sqlState.setString(1, Word);
			ResultSet rs = sqlState.executeQuery();
			
			ArrayList<String> fileNames = new ArrayList<String>();
			ArrayList<Integer> counts = new ArrayList<Integer>();
			ArrayList<Integer> fileIds = new ArrayList<Integer>();
			while (rs.next()) {
	            fileNames.add(rs.getString("FileName"));
	            counts.add(rs.getInt("Count"));
	            fileIds.add(rs.getInt("id"));
			}
			return new GetFileNamesContainsInputWordResult(fileNames, fileIds, counts);
		} catch (SQLException e) {
			System.out.println("Failed to GetFileNamesContainsInputWord");
			e.printStackTrace();
			return null;
		}
	}
	
	public GetStatisticFromFileResult GetStatisticFromFile(int fileId) {
		try {
			String sql = "select f.FileName, fw.Count, w.Word from Files as f	\r\n"
						+ "join Files_Words as fw on fw.File_id = f.id			\r\n"
						+ "join Words as w on w.id = fw.Word_id					\r\n"
						+ "where f.id = ?										\r\n"
						+ "order by fw.Count desc";
			PreparedStatement sqlState = connect.prepareStatement(sql);
            sqlState.setInt(1, fileId);
			ResultSet rs = sqlState.executeQuery();
			
			String fileName = "null";
			ArrayList<String> words = new ArrayList<String>();
			ArrayList<Integer> counts = new ArrayList<Integer>();
			while (rs.next()) {
	            fileName = rs.getString("FileName");
	            words.add(rs.getString("Word"));
	            counts.add(rs.getInt("Count"));
			}
			return new GetStatisticFromFileResult(fileName, words, counts);
		} catch (SQLException e) {
			System.out.println("Failed to GetStatisticFromFile");
			e.printStackTrace();
			return null;
		}
	}
	
	public GetFileTextResult GetFileText(int id) {
		try {
			String sql = "select * from Files where id = ?";
			PreparedStatement sqlState = connect.prepareStatement(sql);
		    sqlState.setInt(1, id);
			ResultSet rs = sqlState.executeQuery();
			while (rs.next()) {
				return new GetFileTextResult(rs.getString("FileName"), rs.getString("Text"));
			}
			return null;
		} catch (SQLException e) {
			System.out.println("Failed to GetFileText");
			e.printStackTrace();
			return null;
		}
	}
	
	public GetAllFilesResult GetAllFiles() {
		try {
			String sql = "select * from Files";
			PreparedStatement sqlState = connect.prepareStatement(sql);
			ResultSet rs = sqlState.executeQuery();
			
			ArrayList<String> fileNames = new ArrayList<String>();
			ArrayList<Integer> ids = new ArrayList<Integer>();
			while (rs.next()) {
				ids.add(rs.getInt("id"));
				fileNames.add(rs.getString("FileName"));
			}
			return new GetAllFilesResult(ids, fileNames);
		} catch (SQLException e) {
			System.out.println("Failed to GetAllFiles");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean InsertStatistic(Map<String, FileStatistic> Result) {
		try {
			for(Map.Entry<String, FileStatistic> fileStatistic : Result.entrySet()){
				String insert = "INSERT INTO Files VALUES (?, ?)";
				PreparedStatement insertState = connect.prepareStatement(insert);
				insertState.setString(1, fileStatistic.getKey());
				insertState.setString(2, fileStatistic.getValue().text);
				insertState.executeUpdate();
				
	            for(Map.Entry<String, Integer> statistic: fileStatistic.getValue().statistic.entrySet()) {
	            	String sql = "select * from Words where Word = ?";
	            	PreparedStatement sqlState = connect.prepareStatement(sql);
	                sqlState.setString(1, statistic.getKey());
	    			ResultSet rs = sqlState.executeQuery();
	    			
	    			int wordId = -1, fileId = -1;
	    			if (rs.next()) {
	    				wordId = rs.getInt("id");
	    			} else {
	    				insert = "INSERT INTO Words VALUES (?)";
	    				insertState = connect.prepareStatement(insert);
	    				insertState.setString(1, statistic.getKey());
	    				insertState.executeUpdate();
	    				
	    				sql = "select id from Words where Word = ?";
	    				sqlState = connect.prepareStatement(sql);
		                sqlState.setString(1, statistic.getKey());
	    				rs = sqlState.executeQuery();
		    			if (rs.next()) {
		    				wordId = rs.getInt("id");
		    			}
	    			}
	    			
	    			sql = "select id from Files where FileName = ?";
	    			sqlState = connect.prepareStatement(sql);
	                sqlState.setString(1, fileStatistic.getKey());
    				rs = sqlState.executeQuery();
	    			if (rs.next()) {
    					fileId = rs.getInt("id");
	    			}
    				
    				insert = "INSERT INTO Files_Words VALUES (?, ?, ?)";
    				insertState = connect.prepareStatement(insert);
    				insertState.setInt(1, fileId);
    				insertState.setInt(2, wordId);
    				insertState.setInt(3, statistic.getValue());
    				insertState.executeUpdate();
	            }
	        }
			return true;
		} catch (SQLException e) {
			System.out.println("Failed to InsertStatistic");
			e.printStackTrace();
			return false;
		}
	}
	
	public void DeleteStatistic() {
		try {
			Statement deleteState = connect.createStatement();
			String delete = "DELETE FROM Files_Words";
			deleteState.executeUpdate(delete);
			delete = "DELETE FROM Words";
			deleteState.executeUpdate(delete);
			delete = "DELETE FROM Files";
			deleteState.executeUpdate(delete);
		} catch (SQLException e) {
			System.out.println("Failed to DeleteStatistic");
			e.printStackTrace();
		}
		
	}
	
	public void Dispose() {
		try {
			System.out.println("Connection is close");
			connect.close();
		} catch (SQLException e) {
			System.out.println("Failed to close connection");
			e.printStackTrace();
		}
	}
}
