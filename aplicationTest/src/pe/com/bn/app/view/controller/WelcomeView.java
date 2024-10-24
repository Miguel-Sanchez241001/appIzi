package pe.com.bn.app.view.controller;

  import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ManagedBean
@ApplicationScoped
public class WelcomeView implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message = "�Hola Mundo desde JSF!";

    public WelcomeView() {
        incrementGlobalVisitCount();
    }

 

    // Obtener la IP del cliente
    public String getClientIP() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return ((HttpServletRequest) facesContext.getExternalContext().getRequest()).getRemoteAddr();
    }

    // Obtener la IP del servidor
    public String getServerIP() {
        try {
            InetAddress serverIP = InetAddress.getLocalHost();
            return serverIP.getHostAddress();
        } catch (UnknownHostException e) {
            return "IP desconocida";
        }
    }

    // Incrementar el contador global de visitas a la p�gina
    private void incrementGlobalVisitCount() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();

        Integer globalVisitCount = (Integer) servletContext.getAttribute("globalVisitCount");
        if (globalVisitCount == null) {
            globalVisitCount = 0;
        }
        globalVisitCount++;
        servletContext.setAttribute("globalVisitCount", globalVisitCount);
    }

    // Obtener el n�mero global de visitas a la p�gina
    public int getGlobalVisitCount() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        Integer globalVisitCount = (Integer) servletContext.getAttribute("globalVisitCount");
        return globalVisitCount == null ? 0 : globalVisitCount;
    }

    // Obtener el navegador del cliente
    public String getClientBrowser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return ((HttpServletRequest) facesContext.getExternalContext().getRequest()).getHeader("User-Agent");
    }

    // Obtener el sistema operativo del cliente
    public String getClientOS() {
        String userAgent = getClientBrowser();
        if (userAgent.toLowerCase().contains("windows")) {
            return "Windows";
        } else if (userAgent.toLowerCase().contains("mac")) {
            return "Mac OS";
        } else if (userAgent.toLowerCase().contains("linux")) {
            return "Linux";
        } else {
            return "Sistema operativo desconocido";
        }
    }

    // Obtener la hora actual del servidor
    public Date getServerTime() {
        return new Date();
    }

    // Obtener la URL solicitada por el cliente
    public String getRequestedURL() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request.getRequestURL().toString();
    }

    // Obtener el protocolo de la solicitud (HTTP/HTTPS)
    public String getRequestProtocol() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request.getProtocol();
    }

    // Obtener el m�todo HTTP de la solicitud (GET, POST, etc.)
    public String getRequestMethod() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request.getMethod();
    }

    // Obtener el idioma preferido del cliente
    public String getClientPreferredLanguage() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request.getLocale().getLanguage();
    }

    // Obtener el nombre del servidor
    public String getServerName() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request.getServerName();
    }

    // Obtener el puerto de la solicitud
    public int getServerPort() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request.getServerPort();
    }

    // Obtener la ruta referida (de d�nde viene el cliente)
    public String getReferer() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        return request.getHeader("Referer");
    }

    // Obtener los encabezados HTTP
    public String getAllHeaders() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append("\n");
        }
        return headers.toString();
    }

   

    // Obtener el tiempo de vida de la sesi�n (en minutos)
    public long getSessionDuration() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session == null) {
            return 0;
        }
        long creationTime = session.getCreationTime();
        long currentTime = System.currentTimeMillis();
        return (currentTime - creationTime) / (1000 * 60);  // Minutos
    }
}
