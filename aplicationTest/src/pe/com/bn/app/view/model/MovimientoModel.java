package pe.com.bn.app.view.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import pe.com.bn.app.dto.ws.DTOConsultaMovimientosExpediente;
import pe.com.bn.app.dto.ws.MovimientoTarjetaExpediente;
 

@Getter
@Setter
public class MovimientoModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String numeroCuenta;
    private DTOConsultaMovimientosExpediente entyMoviemiento;
    private List<MovimientoTarjetaExpediente> movimientos; 

    
    public MovimientoModel(){
    	this.numeroCuenta = "";
    }
}
