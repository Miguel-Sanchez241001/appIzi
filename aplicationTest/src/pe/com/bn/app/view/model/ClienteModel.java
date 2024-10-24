package pe.com.bn.app.view.model;

import java.io.Serializable;

import pe.com.bn.app.dto.ws.DTOConsultaDatosCliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteModel implements Serializable {
    private static final long serialVersionUID = 1L;
	private String numeroDni;
	private String tipoDocumento;
	private DTOConsultaDatosCliente cliente;
	 
}
