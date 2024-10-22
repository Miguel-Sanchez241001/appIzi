package pe.com.bn.app.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "headerController")
@RequestScoped
public class HeaderController implements Serializable {
    private static final long serialVersionUID = 1L;

    private String currentDateTime;

    @PostConstruct
    public void init() {
        updateDateTime();
    }

    // Método para actualizar la fecha y hora
    public void updateDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        currentDateTime = formatter.format(new Date());
    }

    // Getter para currentDateTime
    public String getCurrentDateTime() {
        return currentDateTime;
    }
}
