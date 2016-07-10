package edu.rit.se.datacollector.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;



import edu.rit.se.datacollector.model.DataCollectorDao;
import edu.rit.se.datacollector.model.Phone;
import edu.rit.se.datacollector.model.StepTimer;
import edu.rit.se.datacollector.model.StepTimerList;

@Path("/")
public class DataCollectorResource {

	private DataCollectorDao dao = new DataCollectorDao();

	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService() {
		String result = "DataCollector Successfully started..";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/addTiming")
	/**
	 * Adds timings sent from consumer as JSON object to database
	 * @param timings
	 */
	public Response addTiming(StepTimerList timings) throws SQLException, NamingException {
		Connection conn = null;
		String result = "StepName: ";
		try {
			conn = getConnection();
			Phone phone = timings.getPhone();
			// get the phone record based on deviceAddress if it exists
			int phoneId = dao.getPhone(conn, phone);
			// If the phone record does not exist, add it
			if (phoneId == -1){
				phoneId = dao.addPhone(conn, phone);
			}
			// Add the timings to the database with the source phone id
			dao.addTimings(conn, timings.getTimings(), phoneId);
			for (StepTimer timer : timings.getTimings()){
				result = result.concat(timer.getStepName());
			}
			return Response.status(200).entity(result).build();
		} catch (NamingException e) {
			result = "Issue connecting to dateabase ";
			throw e;
//			return Response.status(500).entity(result).build();
		} catch (SQLException e) {
			result = "Issue with queries ";
			throw e;
//			return Response.status(500).entity(result).build();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// Do nothing as this does not concern the client
			}
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/addPhone")
	/**
	 * Adds phone sent as JSON object from consumer to database
	 * @param phone
	 */
	public void addPhone(Phone phone) {
		Connection conn = null;
		try {
			conn = getConnection();
			dao.addPhone(conn, phone);
		} catch (NamingException e) {
			// TODO write to a log file here or alert the user
		} catch (SQLException e) {
			// TODO write to a lot file here
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// Do nothing as this does not concern the client
			}
		}
	}

	/**
	 * Creates a database connection using the datasource on the JBoss server
	 * 
	 * @return database connection
	 * @throws SQLException
	 * @throws NamingException
	 */
	public Connection getConnection() throws SQLException, NamingException {
		// Create a context to search for the datasource on the server
		InitialContext ic = new InitialContext();
	    Context initialContext = (Context) ic.lookup("java:comp/env");
	    DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
		// Get a database connection from the datasource
		Connection conn = datasource.getConnection();
		return conn;
	}

}
