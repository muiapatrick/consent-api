package com.techminia.collection.util;

public enum ApiCode {
	/**
	 * FAILED = 2000
	 */
	FAILED (2000),
	/**
	 * SUCCESS = 2010
	 */
	SUCCESS (2010),
	DUPLICATE_EMAIL(2012),
	DUPLICATE_PHONE(2013),
	DUPLICATE_IDENTITY_NUMBER(2014),
	DUPLICATE_SHOP_PHONE(2016);

	private final int code;

	private ApiCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
}
