package io.gabrielmmoraes1999.exception;

public class CertificadoException extends Exception {

	public CertificadoException(String message) {
		super(message);
	}

	public CertificadoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CertificadoException(Throwable cause) {
		super(cause);
	}

}