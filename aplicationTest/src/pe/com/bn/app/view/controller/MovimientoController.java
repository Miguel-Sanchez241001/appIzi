package pe.com.bn.app.view.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import lombok.Getter;
import lombok.Setter;

 import pe.com.bn.app.services.WsFacade;
import pe.com.bn.app.view.model.MovimientoModel;
 
@Getter
@Setter
@ManagedBean(name = "movimientoController")
@ViewScoped
public class MovimientoController implements Serializable{
	   private static final long serialVersionUID = 1L;

	    @ManagedProperty("#{wsFacade}")
	    private WsFacade facade;

	    
	    private final static Logger log = Logger.getLogger(MovimientoController.class);

	    private MovimientoModel movimientoModel;
	    @PostConstruct
	    public void init (){
		    movimientoModel = new MovimientoModel();
	         log.info("movimientoController inicializado en " + System.identityHashCode(this));

	    }
	    
	    public void consultarMovimiento() {
	        try {
	            String numeroCuenta = movimientoModel.getNumeroCuenta();
	            log.info("Consultando datos de movimiento. Número de movimiento enviado: " + numeroCuenta);
	             movimientoModel.setEntyMoviemiento(facade.consultaMovimientoPorExpediente(numeroCuenta, "604", new Date()));
	            log.info("Datos de movimiento recibidos: " + movimientoModel.getEntyMoviemiento());

	             if (movimientoModel.getEntyMoviemiento() != null && movimientoModel.getEntyMoviemiento().getCodRespuesta().equals("0000") ) {  
	            	 
	            	 movimientoModel.setMovimientos(facade.listaMovTarjExp(movimientoModel.getEntyMoviemiento()));
	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos de la movimiento consultados correctamente."));
	            } else {
	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Error en la data de respeusta: "+movimientoModel.getEntyMoviemiento().getDescRespuesta()));
	            }
	        } catch (Exception e) {
	            log.error("Error al consultar los datos de la movimiento: ", e);
	             FacesContext.getCurrentInstance().addMessage(null,
	                     new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",e.getMessage()));
	        }
	    }
	    
}
