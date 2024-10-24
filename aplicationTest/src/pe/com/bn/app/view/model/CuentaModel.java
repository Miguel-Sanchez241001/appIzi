package pe.com.bn.app.view.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import pe.com.bn.app.dto.ws.DTOConsultaDatosExpediente;


@Setter
@Getter
public class CuentaModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String numeroCuenta;
    private DTOConsultaDatosExpediente cuentaExpediente;
    public CuentaModel(){
    	this.numeroCuenta = "";
    }
}
