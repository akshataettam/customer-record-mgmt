/*
 * SearchCustomerViewController.java
 * 
 * Version 	 : 1.0
 *
 * Date 	 : 28 June 2014
 * 
 * Copyright : 2014
 */
package com.customer.app.view;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import com.customer.app.Main;
import com.customer.app.model.Customer;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * Class for Handling search customer functionality.
 *
 * @author Akshata Ettam
 */
public class SearchCustomerViewController {
	@FXML
	private TableView<Customer> customerTable;
	@FXML
	private TableColumn<Customer, String> nameColumn;
	@FXML
	private TableColumn<Customer, String> emailColumn;
	@FXML
	private TableColumn<Customer, String> mobileColumn;
	@FXML
	private TableColumn<Customer, String> cityColumn;
	@FXML
	private TableColumn<Customer, String> stateColumn;
	@FXML
	private TableColumn<Customer, String> countryColumn;
	@FXML
	private TableColumn<Customer, Integer> postcodeColumn;

	@FXML
	private TextField searchNameField;
	@FXML
	private TextField searchEmailField;
	@FXML
	private TextField searchMobileField;

	@FXML
	private Label resultCountField;

	@FXML
	private Button add_customer;

	@FXML
	private Button search_customer;

	private Main mainApp;

	public SearchCustomerViewController() {
	}

	/**
	 * Initialise the Home Screen Data with available customers.
	 * 
	 */
	@FXML
	public void initialize() {
		// Populate the Customer table with the customers from database.
		nameColumn
				.setCellValueFactory(new PropertyValueFactory<Customer, String>(
						"name"));
		emailColumn
				.setCellValueFactory(new PropertyValueFactory<Customer, String>(
						"email"));
		mobileColumn
				.setCellValueFactory(new PropertyValueFactory<Customer, String>(
						"mobile"));
		cityColumn
				.setCellValueFactory(new PropertyValueFactory<Customer, String>(
						"city"));
		stateColumn
				.setCellValueFactory(new PropertyValueFactory<Customer, String>(
						"state"));
		countryColumn
				.setCellValueFactory(new PropertyValueFactory<Customer, String>(
						"country"));
		postcodeColumn
				.setCellValueFactory(new PropertyValueFactory<Customer, Integer>(
						"postcode"));
		// Create a MenuItem for right click of Customer record.
		customerTable
				.setRowFactory(new Callback<TableView<Customer>, TableRow<Customer>>() {
					@Override
					public TableRow<Customer> call(TableView<Customer> tableView) {
						final TableRow<Customer> row = new TableRow<>();
						final ContextMenu contextMenu = new ContextMenu();
						final MenuItem removeMenuItem = new MenuItem("Remove");
						final MenuItem editMenuItem = new MenuItem("Edit");
						// Set Edit MenuItem Action.
						editMenuItem
								.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										System.out.println("Row :"
												+ row.getItem().getId());
										Customer tempCustomer = row.getItem();
										// Show Edit customer dialog.
										boolean okClicked = mainApp
												.showAddCustomerDialog(tempCustomer);
										if (okClicked) {
											mainApp.saveCustomerData(tempCustomer);
										}

									}
								});

						// Set Remove MenuItem Action.
						removeMenuItem
								.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										Customer tempCustomer = row.getItem();
										// Show Delete customer dialog.
										Action response = Dialogs
												.create()
												.title("Delete Confirmation")
												.message(
														"Are you sure you want to delete Customer:"
																+ tempCustomer
																		.getName())
												.showConfirm();
										if (response == Dialog.Actions.YES) {
											mainApp.removeCustomerData(tempCustomer);
										}
									}
								});
						contextMenu.getItems().add(editMenuItem);
						contextMenu.getItems().add(removeMenuItem);
						// Set context menu on row, but use a binding to make it
						// only show for non-empty rows:
						row.contextMenuProperty().bind(
								Bindings.when(row.emptyProperty())
										.then((ContextMenu) null)
										.otherwise(contextMenu));
						return row;
					}
				});
	}

	/**
	 * Handle Search customer event.
	 * 
	 */
	@FXML
	private void searchCustomer() {
		mainApp.searchCustomer(searchNameField.getText(),
				searchEmailField.getText(), searchMobileField.getText());
		resultCountField.setText("Found " + mainApp.getCustomerData().size()
				+ " results.");
	}

	/**
	 * Handle Add customer event.
	 * 
	 */
	@FXML
	private void addCustomer() {
		Customer tempCustomer = new Customer();
		boolean okClicked = mainApp.showAddCustomerDialog(tempCustomer);
		if (okClicked) {
			mainApp.saveCustomerData(tempCustomer);
		}
	}

	/**
	 * Set the main application reference.
	 * 
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
		// Add observable list data to the table
		customerTable.setItems(mainApp.getCustomerData());
	}

}