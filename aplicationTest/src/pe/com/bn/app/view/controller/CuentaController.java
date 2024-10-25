package pe.com.bn.app.view.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.primefaces.context.PrimeRequestContext;
 
import pe.com.bn.app.services.WsFacade;
import pe.com.bn.app.view.model.CuentaModel;


@Getter
@Setter
@ManagedBean(name = "cuentaController")
@ViewScoped
public class CuentaController implements Serializable{
	   private static final long serialVersionUID = 1L;
	    @ManagedProperty("#{wsFacade}")
	    private WsFacade facade;
	    
	    
	    private final static Logger log = Logger.getLogger(CuentaController.class);

	    private CuentaModel cuentaModel;
	    @PostConstruct
	    public void init (){
		    cuentaModel = new CuentaModel();
	         log.info("cuentaController inicializado en " + System.identityHashCode(this));

	    }
	    
	    public void consultarDatoscuenta() {
	        try {
	            String numeroExp = cuentaModel.getNumeroCuenta();
 	            log.info("Consultando datos de cuenta. N�mero de cuenta enviado: " + numeroExp);
  	          Date fecha = new Date();
  	          
 	          
 	            cuentaModel.setCuentaExpediente(facade.consultaDatosPorExpediente(numeroExp, "604", fecha));
	            log.info("Datos de cuenta recibidos: " + cuentaModel.getCuentaExpediente());

 	            if (cuentaModel.getCuentaExpediente()!= null) {
 	            	
	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "�xito", "Datos de la cuenta consultados correctamente."));
 	            } else {
 	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se encontraron datos para la cuenta ingresada."));
	            }
	        } catch (Exception e) {
	            log.error("Error al consultar los datos de la cuenta: ", e);
 	            FacesContext.getCurrentInstance().addMessage(null,
 	                   new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",e.getMessage()));
	        }
	    }   
}
