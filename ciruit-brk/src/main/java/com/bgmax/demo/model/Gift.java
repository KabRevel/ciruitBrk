package com.bgmax.demo.model;

public class Gift {

	public Gift() {
		
	}
	
	private Integer price;
	private String code;
	
	public Gift(Integer price, String code) {
		super();
		this.price = price;
		this.code = code;
	}
	
	public Integer getId() {
		return price;
	}
	public void setId(Integer id) {
		this.price = id;
	}
	public String getFirstName() {
		return code;
	}
	public void setFirstName(String firstName) {
		this.code = firstName;
	}
	
	@Override
	public String toString() {
		return "Employee [code=" + code + " price " + price + "]";
	}
}
