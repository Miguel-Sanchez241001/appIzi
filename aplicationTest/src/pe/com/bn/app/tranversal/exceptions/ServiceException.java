package pe.com.bn.app.tranversal.exceptions;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(String mensaje, Throwable causa) {
		super(mensaje, causa);
	}

}