package pe.com.bn.app.dto.ws;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovimientoTarjetaExpediente implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Date fechaTxn;
	private String descripcionTxn;
	private String monOriginalTxn;
	private String montoTxn;	
	private String sigMontoTxn;
	private String operacionTxn; 
	private String codAutTxn;
	private String numTarjetaTxn;


}
