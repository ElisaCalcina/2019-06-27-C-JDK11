package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenze;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getCategoria(){
		String sql="SELECT DISTINCT offense_category_id AS o " + 
				"FROM EVENTS " + 
				"ORDER BY o asc ";
		
		List<String> result= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("o"));
			}
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getGiorno(){
		String sql="SELECT distinct DAY(reported_date) AS d " + 
				"FROM EVENTS " + 
				"ORDER BY d asc " ;
		
		List<Integer> result= new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("d"));
			}
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		
	}
	
	//vertici
	public List<String> getVertici(String categoria, Integer giorno){
		String sql="SELECT distinct offense_type_id as id " + 
				"FROM EVENTS " + 
				"WHERE offense_category_id=? " + 
				"	AND DAY(reported_date)=? ";
		
		List<String> result= new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, giorno);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("id"));
			}
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

		
	}
	
	//archi
	public List<Adiacenze> getArchi(String categoria, Integer giorno){
		String sql="SELECT e1.offense_type_id AS v1, e2.offense_type_id AS v2, COUNT(DISTINCT e1.precinct_id) AS peso " + 
				"FROM EVENTS AS e1, EVENTS AS e2 " + 
				"WHERE e1.offense_type_id>e2.offense_type_id " + 
				"		AND e1.precinct_id=e2.precinct_id " + 
				"		AND e1.offense_category_id=e2.offense_category_id " + 
				"		AND e1.offense_category_id=? " + 
				"		AND DAY(e1.reported_date)=DAY(e2.reported_date) " + 
				"		AND DAY(e1.reported_date)=? " + 
				"GROUP BY v1, v2 " + 
				"HAVING peso!=0 ";
		
		List<Adiacenze> result= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, giorno);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(new Adiacenze(res.getString("v1"), res.getString("v2"), res.getDouble("peso")));
			}
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

		
	}
}
