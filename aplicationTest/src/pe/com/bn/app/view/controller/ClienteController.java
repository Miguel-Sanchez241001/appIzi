package pe.com.bn.app.view.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import pe.com.bn.app.services.WsFacade;
import pe.com.bn.app.view.model.ClienteModel;

@Getter
@Setter
@ManagedBean(name = "clienteController")
@ViewScoped
public class ClienteController implements Serializable{
	   private static final long serialVersionUID = 1L;

	    @ManagedProperty("#{wsFacade}")
	    private WsFacade facade;
	    
	    private final static Logger log = Logger.getLogger(ClienteController.class);

	    private ClienteModel clienteModel;
	    @PostConstruct
	    public void init (){
		    clienteModel = new ClienteModel();
	         log.info("ClienteController inicializado en " + System.identityHashCode(this));

	    }
	    
	    public void consultarDatosCliente() {
	        try {
	            String numeroDocumento = clienteModel.getNumeroDni();
	            String tipoDoc = clienteModel.getTipoDocumento();
	            log.info("Consultando datos de cliente. Número de cliente enviado: " + numeroDocumento);
 	            clienteModel.setCliente(facade.consultaDatosCliente(tipoDoc, numeroDocumento));
	            log.info("Datos de cliente recibidos: " + clienteModel.getCliente());

 	            if (clienteModel.getCliente()!= null) {
 	            	
	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos de la cliente consultados correctamente."));
	                RequestContext.getCurrentInstance().update("panelResultado");
	            } else {
 	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se encontraron datos para la cliente ingresada."));
	            }
	        } catch (Exception e) {
	            log.error("Error al consultar los datos de la cliente: ", e);
 	            FacesContext.getCurrentInstance().addMessage(null,
 	                   new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",e.getMessage()));
	        }
	    }   
}
