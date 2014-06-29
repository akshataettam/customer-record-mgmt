/*
 * Main.java
 * 
 * Version 	 : 1.0
 *
 * Date 	 : 28 June 2014
 * 
 * Copyright : 2014
 */
package com.customer.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.customer.app.model.Customer;
import com.customer.app.view.AddCustomerViewController;
import com.customer.app.view.SearchCustomerViewController;

/**
 *  
 * Main class to display the home screen. 
 *
 * @version    1.0          
 * @author 		Akshata Ettam  
 */
public class Main extends Application {
	private Stage primaryStage;
	private AnchorPane rootLayout;    
	private ObservableList<Customer> customerData = FXCollections.observableArrayList();

	public Main() {
		// Create tables if not already created
		createTables();
		// Retrieve all the customers from the database.
		fetchCustomerData();
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Customer Record Management");
		initRootLayout();	
	}

	/**
	 * Initialises the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SearchCustomerView.fxml"));	        
			rootLayout = (AnchorPane) loader.load();           
			// Set the customer into the controller.
			SearchCustomerViewController controller = loader.getController();
			controller.setMainApp(this);

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to display ADD/EDIT customer dialog.
	 */
	public boolean showAddCustomerDialog(Customer tempCustomer) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/AddCustomerView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Customer");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the customer into the controller.
			AddCustomerViewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCustomer(tempCustomer);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			//Return the users response , Save/Cancel click.
			return controller.isOkClicked();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Function to get the database connection.
	 */
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		return DriverManager.getConnection("jdbc:h2:~/test;IGNORECASE=TRUE", "sa", "");
	}

	/**
	 * Function to retrieve list of customers from database.
	 */
	private void fetchCustomerData() {
		try (Connection con = getConnection()) {
			// Clear the existing customer list.
			customerData.clear();
			Statement st = con.createStatement();      
			ResultSet rs = st.executeQuery("select a.id as customer_id, name, email, mobile, city, state, country, postcode from customer c , address a where c.address_id = a.id;");
			while (rs.next()) {
				customerData.add(new Customer(rs.getInt(1),rs.getString("name"), rs.getString("email"), rs.getString("mobile"), rs.getString("city"),rs.getString("state"),rs.getString("country"), rs.getString("postcode")));
			}
			System.out.println("fetchCustomerData::customerData:" + customerData);  
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println("fetchCustomerData::Exception:" + ex);
		}
	}
	
	/**
	 * Returns customerData.
	 */
	public ObservableList<Customer> getCustomerData() {
		return customerData;
	}

	/**
	 * Function to Add/Edit customer record.
	 */
	public void saveCustomerData(Customer customer) {

		try (Connection con = getConnection()) {
			int cutomerID = customer.getId();
			System.out.println("saveCustomerData::CustomerID" + cutomerID);			
			if (cutomerID > 0) {
				// Update Customer
				
				// First update the customer Address.
				PreparedStatement prep = con.prepareStatement("UPDATE address set city=?,state=?,country=?,postcode=? where id in (SELECT address_id FROM customer WHERE customer.id=?)");
				prep.setString(1, customer.getCity());
				prep.setString(2, customer.getState());
				prep.setString(3, customer.getCountry());
				prep.setString(4, customer.getPostcode());
				prep.setInt (5, cutomerID);
				prep.executeUpdate();
				System.out.println("Updated Records:" + prep.toString());
				
				// Update the Customer.
				prep = con.prepareStatement("UPDATE customer SET name=?, email=?, mobile=? WHERE id =?");
				prep.setString(1, customer.getName());
				prep.setString(2, customer.getEmail());
				prep.setString(3, customer.getMobile());
				prep.setInt(4, cutomerID);
				prep.executeUpdate();     		    	            

			} else {
				//Add Customer 	
				
				//First add the Customer Address
				PreparedStatement prep = con.prepareStatement("INSERT INTO address(city, state, country, postcode) VALUES (?,?,?,?)", new String[]{"id"});
				prep.setString(1, customer.getCity());
				prep.setString(2, customer.getState());
				prep.setString(3, customer.getCountry());
				prep.setString(4, customer.getPostcode());
				prep.executeUpdate();
				// Get the addressID from auto generated keys. 
				ResultSet rs = prep.getGeneratedKeys();
				int addressID = 0;
				while(rs.next()) {
					addressID =  rs.getInt(1);        	 	
				}
				
				// Add customer along with addressID
				prep = con.prepareStatement("INSERT INTO customer(name, email, mobile, address_id) VALUES (?,?,?,?)");
				prep.setString(1, customer.getName());
				prep.setString(2, customer.getEmail());
				prep.setString(3, customer.getMobile());
				prep.setInt(4, addressID);
				prep.executeUpdate();	            
			}
			con.setAutoCommit(true);
			con.close();
			//Get the latest customerData from database.
			fetchCustomerData();
			
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println("Exception:" + ex);
		}

	}
	
	/**
	 * Function to Search customer records on fields name, email and mobile.
	 */	
	public void searchCustomer(String searchName, String searchEmail, String searchMobile) {
		try (Connection con = getConnection()) {
			Statement st = con.createStatement();
			//Remove the currently displayed customers. 
			customerData.clear();
			List<String> params = new ArrayList<String>();
			// Prepare queries for name, email and mobile.
			if(!( searchName.equals(null) || searchName.trim().equals(""))) {
				params.add("name like '%" + searchName.trim() + "%'");
			}
			if(!( searchEmail.equals(null) || searchEmail.trim().equals(""))) {
				params.add("email like '%" + searchEmail.trim() + "%'");
			}
			if(!( searchMobile.equals(null) || searchMobile.trim().equals(""))) {
				params.add("mobile like '%" + searchMobile.trim() + "%'");
			}
			System.out.println("searchCustomer::Params :" + params);
			String queryPart = String.join("OR ", params);
			String query;
			// Build the query
			if(!queryPart.equals("")) {
				query = "select a.id as customer_id, name, email, mobile, city, state, country, postcode from customer c , address a where (" + queryPart + ") and c.address_id = a.id";    		
			} else {
				query = "select a.id as customer_id, name, email, mobile, city, state, country, postcode from customer c , address a where c.address_id = a.id";
			}	
			System.out.println("searchCustomer::Query :" + query);
			ResultSet rs = st.executeQuery(query);
			// Populate customerData. 
			while (rs.next()) {
				customerData.add(new Customer(rs.getInt(1), rs.getString("name"), rs.getString("email"), rs.getString("mobile"), rs.getString("city"),rs.getString("state"),rs.getString("country"), rs.getString("postcode")));
			}
			System.out.println("searchCustomer::customerData :" + customerData);
			
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println("Exception:" + ex);
		}	
	}
	
	/**
	 * Function to Remove customer record.
	 */	
	public void removeCustomerData(Customer customer) {
		
		int customerID = customer.getId(); 
		String query = "SELECT address_id FROM Customer where id=" + customerID;
		try (Connection con = getConnection()) {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			int addressID = 0;
			while (rs.next()) {
				addressID =  rs.getInt(1);
			}
			// First delete the customer Address
			query = "DELETE FROM Customer where id=" + customerID;
			st.executeUpdate(query);
			//Delete the customer record.
			query = "DELETE FROM Address where id=" + addressID;			
			st.executeUpdate(query);
			// Delete customer from customerData list.
			customerData.remove(customer);			
		}catch (SQLException | ClassNotFoundException ex) {
			System.out.println("Exception:" + ex);
		}
	}

	/**
	 * Create the Customer and Address tables, if not already present.
	 */	
	public void createTables() {
		try (Connection con = getConnection()) {
			Statement st = con.createStatement();
			String customerQuery = "CREATE TABLE IF NOT EXISTS Customer (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) ,  email  VARCHAR(255) ,  mobile VARCHAR(10), address_id INT)";			
			String addressQuery = "CREATE TABLE IF NOT EXISTS Address (id INT PRIMARY KEY AUTO_INCREMENT, city VARCHAR(50) , state VARCHAR(50),  country  VARCHAR(50) ,  postcode VARCHAR(10))";			
			String fkQuery = "ALTER TABLE Customer ADD CONSTRAINT IF NOT EXISTS fk_customer FOREIGN KEY(id) REFERENCES Address(id)";            
			// Create Customer table
			st.execute(customerQuery);
			// Create Address table
			st.execute(addressQuery);
			// Add foreign key relation.
			st.execute(fkQuery);

		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println("Exception:" + ex);
		}
	}

}