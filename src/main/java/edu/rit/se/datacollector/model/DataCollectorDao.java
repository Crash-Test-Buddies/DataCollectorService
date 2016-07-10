package edu.rit.se.datacollector.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class DataCollectorDao {
	
	/**
	 * Add list of timing records to the database
	 * @param conn Connection to the database
	 * @param timings List of timing records to be inserted into database
	 * @param phoneId Phone id for the source phone for the timing
	 * @throws SQLException
	 */
	public void addTimings(Connection conn, List<StepTimer> timings, int phoneId) throws SQLException{
		String sql = "Insert into StepTimer (Step_name, Start_time, End_time, Latitude, Longitude, Phone_id)"
				+ " values(?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement(sql);
			// Loop through the list of StepTimer records and create insert statements for each
			for (StepTimer timing : timings){
				statement.setString(1, timing.getStepName());
				statement.setTimestamp(2, timing.getStartTime());
				statement.setTimestamp(3, timing.getEndTime());
				statement.setDouble(4, timing.getLatitude());
				statement.setDouble(5,  timing.getLongitude());
				statement.setInt(6, phoneId);
				// Add the insert record to a batch
				statement.addBatch();
			}
			// Insert the records as a batch insert
			statement.executeBatch();
		} finally {
			statement.close();
		}
	}
	
	/**
	 * Adds a phone record to the database
	 * @param conn connection to the database
	 * @param phone Phone object containing phone information to enter into database
	 * @return Auto generated key for Phone record
	 * @throws SQLException
	 */
	public int addPhone(Connection conn, Phone phone) throws SQLException{
		int phoneId = 0;
		String sql = "Insert into Phone (Device_Address, Model, Operating_System, Manufacturer, "
				+ "Brand) values(?, ?, ?, ?, ?)";
		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, phone.getDeviceAddress());
			statement.setString(2, phone.getModel());
			statement.setString(3, phone.getOperatingSystem());
			statement.setString(4, phone.getManufacturer());
			statement.setString(5, phone.getBrand());
			statement.execute();
			// Get the key back from the database
			ResultSet returnedKeys = statement.getGeneratedKeys();
			if (returnedKeys.next()){
				phoneId = returnedKeys.getInt(1);
			}
		} finally {
			statement.close();
		}
		return phoneId;
	}
	
	/**
	 * Checks for a phone record in the database based on DeviceAddress
	 * @param conn Connection to the database
	 * @param phone Phone record to check for
	 * @return Phone id if found, -1 if no phone record found
	 * @throws SQLException
	 */
	public int getPhone(Connection conn, Phone phone) throws SQLException{
		int phoneId;
		String sql = "Select id from Phone where Device_address = ?";
		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement(sql);
			statement.setString(1, phone.getDeviceAddress());
			ResultSet result = statement.executeQuery();
			// Get the phoneID if the record exists
			if (result.next()){
				phoneId = result.getInt(1);
			// If the query returned no rows return -1
			} else {
				phoneId = -1;
			}
		} finally {
			statement.close();
		}
		return phoneId;
	}
}
