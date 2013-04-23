package de.metux.nanoweb.service;

/**
 * generic service exception. usually will trigger an
 * 500 http reply
 */
public class ServiceFailure extends Exception {
	private static final long serialVersionUID=100L;

	public ServiceFailure(String s) {
		super(s);
	}
}
