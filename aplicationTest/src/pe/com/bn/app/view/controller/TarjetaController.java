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
 
import pe.com.bn.app.services.WsFacade;
import pe.com.bn.app.view.model.TarjetaModel;


@Getter
@Setter
@ManagedBean(name = "tarjetaController")
@ViewScoped
public class TarjetaController implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{wsFacade}")
    private WsFacade facade;
    private TarjetaModel tarjetaModel;

    private final static Logger log = Logger.getLogger(TarjetaController.class);

    @PostConstruct
    public void init() {
        tarjetaModel = new TarjetaModel();
         log.info("TarjetaController inicializado en " + System.identityHashCode(this));
    }

     public void consultarDatosTarjeta() {
        try {
            String numeroTarjeta = tarjetaModel.getNumTarjeta();
            log.info("Consultando datos de tarjeta. Número de tarjeta enviado: " + numeroTarjeta);
             tarjetaModel.setTarjetaDatos(facade.informacionDeTarjeta(numeroTarjeta));
            log.info("Datos de tarjeta recibidos: " + tarjetaModel.getTarjetaDatos());

             if (tarjetaModel.getTarjetaDatos() != null && tarjetaModel.getTarjetaDatos().getCodRespuesta().equals("0000") ) {         	
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos de la tarjeta consultados correctamente."));
             } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se encontraron datos para la tarjeta: "+tarjetaModel.getTarjetaDatos().getDescRespuesta()));
            }
        } catch (Exception e) {
            log.error("Error al consultar los datos de la tarjeta: ", e);
             FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",e.getMessage()));
        }
    }    
 
}
