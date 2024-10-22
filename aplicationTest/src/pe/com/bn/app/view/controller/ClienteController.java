package pe.com.bn.app.view.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import pe.com.bn.app.services.WsFacade;
import pe.com.bn.app.view.model.ClienteModel;

@ManagedBean(name = "clienteController")
@ViewScoped
public class ClienteController implements Serializable{
	   private static final long serialVersionUID = 1L;

	    @ManagedProperty("#{wsFacade}")
	    private WsFacade facade;
	    
	    
	    private ClienteModel clienteModel;
}
