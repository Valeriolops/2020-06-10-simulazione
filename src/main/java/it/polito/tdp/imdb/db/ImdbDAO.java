package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<String> getGenere(){
		String sql="SELECT DISTINCT(mv.genre) AS genere   " + 
				"FROM movies_genres mv  " + 
				"ORDER BY genere  ";
		
		
		
		List<String>result= new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String genere = res.getString("genere");
				result.add(genere);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		
		
	}
	
	public void MAPlistAllActors(Map<Integer,Actor>idMap){
		String sql = "SELECT * FROM actors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				idMap.put(res.getInt("id"), actor);
				
			}}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public List<Actor> getAttoreVertice(String genereSelezionato,Map<Integer,Actor>idMap){
		String sql="SELECT distinct(r.actor_id) AS attore   " + 
				"FROM roles r,movies_genres mv   " + 
				"WHERE r.movie_id=mv.movie_id AND mv.genre=?    " + 
				"ORDER BY attore  " ;
		
		List<Actor>result= new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genereSelezionato);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Actor a= idMap.get(res.getInt("attore"));
				result.add(a);

				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Adiacenza>getAdiacenzaArtisti(String genereSelezionato,Map<Integer,Actor>idMap){
		
		
		String sql="SELECT r1.actor_id,r2.actor_id, COUNT(DISTINCT(r1.actor_id)) AS peso   " + 
				"FROM roles r1, roles r2,movies_genres mv    " + 
				"WHERE r1.actor_id< r2.actor_id AND r1.movie_id=mv.movie_id AND    " + 
				"r2.movie_id=mv.movie_id AND mv.genre=?   " + 
				"GROUP BY r1.actor_id,r2.actor_id   " ;
		
		
		
		
		
		
		List<Adiacenza>result= new ArrayList<Adiacenza>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genereSelezionato);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Actor a1=idMap.get(res.getInt("r1.actor_id"));
				Actor a2= idMap.get(res.getInt("r2.actor_id"));
				Integer peso =res.getInt("peso");
				Adiacenza ad = new Adiacenza(a1, a2, peso);
				result.add(ad);
				

				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
				
		
		
		
		
		
		
	}
	
	
	
	
	
}
