/*
 * AddCustomerViewController.java
 * 
 * Version 	 : 1.0
 *
 * Date 	 : 28 June 2014
 * 
 * Copyright : 2014
 */
package com.customer.app.view;

import org.controlsfx.dialog.Dialogs;
import com.customer.app.model.Customer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Class for Handling add customer functionality.
 *
 * @author Akshata Ettam
 */
public class AddCustomerViewController {
	@FXML
	private TextField nameField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField mobileField;
	@FXML
	private TextField cityField;
	@FXML
	private TextField stateField;
	@FXML
	private TextField countryField;
	@FXML
	private TextField postcodeField;
	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;

	@FXML
	private Label add_edit_customer_lbl;

	private Stage dialogStage;
	private Customer customer;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	/**
	 * Populate the Add/Edit Customer Dialog.
	 * 
	 */	
	public void setCustomer(Customer customer) {
		this.customer = customer;

		nameField.setText(customer.getName());
		emailField.setText(customer.getEmail());
		mobileField.setText(customer.getMobile());
		cityField.setText(customer.getCity());
		stateField.setText(customer.getState());
		countryField.setText(customer.getCountry());
		postcodeField.setText(customer.getPostcode());
		if (customer.getId().equals(0)) {
			add_edit_customer_lbl.setText("Add Customer Details");
		} else {
			add_edit_customer_lbl.setText("Edit Customer Details");
		}
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Handle cancel button for Add/Edit Customer Dialog.
	 * 
	 */		
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Add/Edit Customer Dialog save functionality. 
	 * 
	 */		
	@FXML
	private void handleSave() {
		if (isInputValid()) {
			customer.setName(nameField.getText());
			customer.setEmail(emailField.getText());
			customer.setMobile(mobileField.getText());
			customer.setCity(cityField.getText());
			customer.setState(stateField.getText());
			customer.setCountry(countryField.getText());
			customer.setPostcode(postcodeField.getText());
			okClicked = true;
			dialogStage.close();
		}

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (nameField.getText() == null || nameField.getText().length() == 0) {
			errorMessage += "Please provide Name!\n";
		}
		if (emailField.getText() == null || emailField.getText().length() == 0) {
			errorMessage += "Please provide Email!\n";
		}
		if (mobileField.getText() == null
				|| mobileField.getText().length() == 0) {
			errorMessage += "Please provide Mobile!\n";
		}

		if (cityField.getText() == null || cityField.getText().length() == 0) {
			errorMessage += "Please provide City!\n";
		}
		if (stateField.getText() == null || stateField.getText().length() == 0) {
			errorMessage += "Please provide state!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Dialogs.create().title("Invalid Fields")
					.masthead("Please correct invalid fields")
					.message(errorMessage).showError();
			return false;
		}
	}
}