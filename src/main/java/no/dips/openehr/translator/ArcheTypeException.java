package no.dips.openehr.translator;

import java.util.Arrays;

public class ArcheTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 94503327086840572L;

	public ArcheTypeException() {
		super();

	}

	public ArcheTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ArcheTypeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ArcheTypeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ArcheTypeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ArcheTypeException [Message=" + getMessage() + ", Cause=" + getCause();
	}

}
