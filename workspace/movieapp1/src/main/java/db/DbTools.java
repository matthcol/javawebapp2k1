package db;

import java.io.Reader;
import java.net.URISyntaxException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import data.Movie;

import java.io.IOException;

/**
 * Old school class to connect to db and fetch data only with JDBC
 * without ORM
 */
public class DbTools {
	
	private static String url = null;
	private static String user = null;
	private static String password = null;
	
	private final static String SQL_ALL_MOVIES = "select * from movies";
	private final static String SQL_ADD_MOVIE = "insert into movies (title,year) values (?,?)";
	
	public static void loadParams() {
		try {
			var properties = new Properties();
			properties.load(
					DbTools.class
						.getClassLoader()
						.getResourceAsStream("database.properties")); 
			url = properties.getProperty("datasource.url");
			user = properties.getProperty("datasource.user");
			password = properties.getProperty("datasource.password");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static List<Movie> readMovies() {
		try (var connection = DriverManager.getConnection(url, user, password)) {
			var movies = new ArrayList<Movie>();
			var st = connection.createStatement();
			var res = st.executeQuery(SQL_ALL_MOVIES);
			while (res.next()) {
				var movie = new Movie();
				movie.setTitle(res.getString("title"));
				movie.setYear(res.getInt("year"));
				movies.add(movie);
			}
			return movies;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
