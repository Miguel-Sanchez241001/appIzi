package pe.com.bn.app.tranversal.config;

 
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

import pe.com.bn.app.tranversal.util.NumeroALetras;
import pe.com.bn.app.view.controller.MovimientoController;

 
@Getter
@Setter
@ManagedBean(name = "parametros")
@ApplicationScoped
public class Parametros implements Serializable {
    private static final long serialVersionUID = 1L;

	  // PARAMETROS MC con valores por defecto
    private String codigoEmisorMc = "191";
    private String codigoUsuarioMc = "TT9999";
    private String numTerminalMc = "11010101";
    private String prefijoNumReferenciaMc = "AC";
    private String wsUsuarioMc = "4858643428";
    private String wsClaveMc = "aza877azutht98b8";
    private String wsSoapMc = "https://testwsgestiontarjetas.izipay.pe/WCFGestionTarjetas/Service1.svc";
    private String wsComercioMc = "4058950";
    private final static Logger log = Logger.getLogger(Parametros.class);

    private long numReferenciaActual = 2024033040L;
    @PostConstruct
    public void init() {
    	log.info("Parametros bean inicializado con numReferenciaActual: " + numReferenciaActual);
        log.info("Parametros inicializado en " + System.identityHashCode(this));

    }

   
    public synchronized String getNextNumReferencia() {
        incrementarNumReferenciaGlobal();
        Long numReferenciaActual = getNumReferenciaGlobal();
        return NumeroALetras.llenarCerosAlaIzquierda(Long.toString(numReferenciaActual), 10);
    }

    private void incrementarNumReferenciaGlobal() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

        Long numReferenciaActual = (Long) servletContext.getAttribute("numReferenciaActual");
        if (numReferenciaActual == null) {
            numReferenciaActual = 202403342L;
        }
        numReferenciaActual++;
        servletContext.setAttribute("numReferenciaActual", numReferenciaActual);
    }

     public Long getNumReferenciaGlobal() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        Long numReferenciaActual = (Long) servletContext.getAttribute("numReferenciaActual");
        return numReferenciaActual == null ? 2024032920L : numReferenciaActual; // Valor inicial por defecto
    }

 
	
}
