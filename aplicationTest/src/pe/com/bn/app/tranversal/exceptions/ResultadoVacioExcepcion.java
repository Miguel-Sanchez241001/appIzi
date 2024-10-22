package pe.com.bn.app.tranversal.exceptions;

public class ResultadoVacioExcepcion extends Exception{
	public ResultadoVacioExcepcion() {
		
	}

	public ResultadoVacioExcepcion(String message) {
		super(message);
		
	}

	public ResultadoVacioExcepcion(String mensaje, Throwable causa) {
		super(mensaje, causa);
	}
}
