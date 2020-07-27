package com.techminia.collection.util;

public enum NotificationCategory {
	NEW_USER_ACCOUNT(1),
	PASSWORD_RESET(2);

	private final int code;

	private NotificationCategory(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
}
