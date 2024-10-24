package pe.com.bn.app.view.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import pe.com.bn.app.dto.ws.DTOConsultaDatosTarjeta;

@Getter
@Setter
public class TarjetaModel implements Serializable {
    private static final long serialVersionUID = 1L;
	private String numTarjeta;
	private DTOConsultaDatosTarjeta tarjetaDatos;
	
	public TarjetaModel(){
		this.numTarjeta = "" ;
	}
	
 

}
