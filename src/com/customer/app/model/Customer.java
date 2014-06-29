/*
 * Customer.java
 * 
 * Version 	 : 1.0
 *
 * Date 	 : 28 June 2014
 * 
 * Copyright : 2014
 */
package com.customer.app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Customer.
 *
 * @author Akshata Ettam
 */
public class Customer {

    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty mobile;
    private final StringProperty city;
    private final StringProperty state;
    private final StringProperty country;
    private final StringProperty postcode;
	private SimpleIntegerProperty id;
    
     /**
     * Default constructor.
     */
    public Customer() {
        this(null, null, null, null, null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param id
     * @param name
     * @param email
     * @param mobile
     * @param city
     * @param state
     * @param country
     * @param postcode
     * 
     */
    public Customer(Integer id,String name, String email,String mobile, String city,String state, String country, String postcode) {
    	if(id != null) {
    		this.id = new SimpleIntegerProperty(id);
    	} else {
    		this.id = new SimpleIntegerProperty(0);
    	}
    	this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.mobile = new SimpleStringProperty(mobile);
        this.city = new SimpleStringProperty(city);
        this.state=new SimpleStringProperty(state);
        this.country=new SimpleStringProperty(country); 
        this.postcode = new SimpleStringProperty(postcode);               
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty firstNameProperty() {
        return name;
    }


    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }
   
    public String getMobile()
    {
    	return mobile.get();
    }
    public void setMobile(String mobile)
    {
    	this.mobile.set(mobile);
    }
    
    
    public StringProperty mobileProperty()
    {
    	return mobile;
    }
    
    
    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }
    
    public StringProperty cityProperty() {
        return city;
    }
    
    
    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }
    
    public StringProperty stateProperty() {
        return state;
    }
    
    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }
    
    public StringProperty countryProperty() {
        return country;
    }
    
    public String getPostcode() {
        return postcode.get();
    }

    public void setPostcode(String postcode) {
        this.postcode.set(postcode);
    }

    public StringProperty postcodeProperty() {
        return postcode;
    }
    }