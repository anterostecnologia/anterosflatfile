package br.com.anteros.flatfile.annotation;

public class FlatFileManagerException extends Exception {

	public FlatFileManagerException() {
		super();
	}

	public FlatFileManagerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FlatFileManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public FlatFileManagerException(String message) {
		super(message);
	}

	public FlatFileManagerException(Throwable cause) {
		super(cause);
	}

	
}
