package com.techminia.collection.util;

public enum NotificationType {
	/**
	 * SMS = 1
	 */
	SMS (1),
	/**
	 * EMAIL = 2
	 */
	EMAIL (2),
	/**
	 * FIREBASE = 3
	 */
	FIREBASE (3);

	private final int code;

	private NotificationType(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
}
