package pe.com.bn.app.services;

 
import org.apache.log4j.Logger;
 
import java.util.List;
import pe.com.bn.app.dto.ws.DTOConsultaDatosCliente;
import pe.com.bn.app.dto.ws.DTOConsultaDatosExpediente;
import pe.com.bn.app.dto.ws.DTOConsultaDatosTarjeta;
import pe.com.bn.app.dto.ws.DTOConsultaMovimientosExpediente;
import pe.com.bn.app.dto.ws.DTOwservice;
import pe.com.bn.app.dto.ws.MovimientoTarjetaExpediente;
import pe.com.bn.app.tranversal.config.Parametros;
import pe.com.bn.app.tranversal.constantes.ConstantesWS;
import pe.com.bn.app.tranversal.exceptions.ExternalServiceMCProcesosException;
import pe.com.bn.app.tranversal.exceptions.InternalExcepcion;
 
import pe.com.bn.app.tranversal.util.Fecha;
import pe.com.bn.app.tranversal.util.SoapClientUtil;
import pe.com.bn.app.tranversal.util.StringsUtils;

import org.w3c.dom.Document;

import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ArrayList;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
 @ManagedBean(name = "wsFacade")
 @ApplicationScoped
 public class WsFacade implements Serializable {
     private static final long serialVersionUID = 1L;

     @ManagedProperty("#{parametros}")
     private Parametros parametros;

	private final static Logger logger = Logger.getLogger(WsFacade.class);

 
 
    
	public DTOConsultaMovimientosExpediente consultaMovimientoPorExpediente(
			String numCuenta,
			String tipoMoneda, 
			Date fechaExpiracion) throws InternalExcepcion {
				
		String wsdlUrl = parametros.getWsSoapMc();		
		String wsdlAS = parametros.getPrefijoNumReferenciaMc();
		
		String codEmisor = parametros.getCodigoEmisorMc();	
		String codUsuario = parametros.getCodigoUsuarioMc();	
		String numTerminal = parametros.getNumTerminalMc();	
		String comercio = parametros.getWsComercioMc();	
		String numReferenciaWS = wsdlAS + parametros.getNextNumReferencia();
		
		String usuario = parametros.getWsUsuarioMc();
		String clave = parametros.getWsClaveMc();		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    String fechaTerminal = sdf.format(new Date());
	    System.out.println("fechaTerminal:"+fechaTerminal);
	        
	    DateFormat dateFormat = new SimpleDateFormat("HHmmss");
	    String horaTerminal = dateFormat.format(new Date());	       
	    System.out.println("horaTerminal:"+horaTerminal);	
		
	    /*fecha de expiacion*/
	    SimpleDateFormat sdfanio = new SimpleDateFormat("yyyy");
	    String fchExpanio =  sdfanio.format(fechaExpiracion).substring(2,4);
	    
	    SimpleDateFormat sdfmes = new SimpleDateFormat("MM");
	    String fchExpmes = sdfmes.format(fechaExpiracion);
	    
	    String fchExp = fchExpanio + fchExpmes;
	    
	    String numeroCuenta = StringsUtils.quitarCeroIzquierdaString(numCuenta);
		  
	    
		DTOwservice dto = new DTOwservice(ConstantesWS.SOACTION_CONSULTA_MOVIMIENTOS_EXPEDIENTE);
		Class<DTOConsultaMovimientosExpediente> dtoClass = DTOConsultaMovimientosExpediente.class;
		DTOConsultaMovimientosExpediente responseDTO = null;


		Map<String, String> consultaMovimientosExpedienteMap = ConstantesWS
				.getConsultaMovimientosExpedienteMap();
				
		consultaMovimientosExpedienteMap.put(ConstantesWS.COD_EMISOR, codEmisor);
		consultaMovimientosExpedienteMap.put(ConstantesWS.COD_USUARIO, codUsuario);
		consultaMovimientosExpedienteMap.put(ConstantesWS.NUM_TERMINAL, numTerminal);
		consultaMovimientosExpedienteMap.put(ConstantesWS.NUM_REFERENCIA, numReferenciaWS);		
		consultaMovimientosExpedienteMap.put(ConstantesWS.NUM_CUENTA, numeroCuenta);		
		consultaMovimientosExpedienteMap.put(ConstantesWS.FECHA_EXPIRACION, fchExp);
		consultaMovimientosExpedienteMap.put(ConstantesWS.COMERCIO, comercio);
		consultaMovimientosExpedienteMap.put(ConstantesWS.MONEDA, tipoMoneda);
		consultaMovimientosExpedienteMap.put(ConstantesWS.FECHA_TXN_TERMINAL, fechaTerminal);
		consultaMovimientosExpedienteMap.put(ConstantesWS.HORA_TXN_TERMINAL, horaTerminal);
		consultaMovimientosExpedienteMap.put(ConstantesWS.WS_USUARIO, usuario);
		consultaMovimientosExpedienteMap.put(ConstantesWS.WS_CLAVE, clave);
		consultaMovimientosExpedienteMap.put(ConstantesWS.RESERVADO, "");
		
		String soapRequestPrevie = ConstantesWS.generarXml(
				ConstantesWS.CONSULTA_MOVIMIENTOS_EXPEDIENTE_XML, consultaMovimientosExpedienteMap);
		

		String soapRequest = dto.getSoapTemplate().replace("SOAP_CONTENT", soapRequestPrevie);

		//logger.info("Request generado: " + soapRequest);

		int maxRetries = 5;
		int attempt = 0;
		boolean success = false;

		while (attempt < maxRetries && !success) {
			attempt++;
			String dataWS = "";
			try {
				dataWS = SoapClientUtil.sendSoapRequest(wsdlUrl,
						dto.getSoapAction(),
						soapRequest);
			} catch (ExternalServiceMCProcesosException e) {
				throw e;
			}

			try {

				logger.info("Respuesta del servidor:");
				logger.info(dataWS);

				Document documentoXML = SoapClientUtil.parseXmlResponse(dataWS
						.toString());
				String resultado = SoapClientUtil.getTextFromElement(
						documentoXML, dto.getResultTag());

				String contenidoXML = resultado.substring(
						resultado.indexOf(dto.getStartTag()),
						resultado.indexOf(dto.getEndTag())
								+ dto.getEndTag().length());

				responseDTO = SoapClientUtil.convertirXMLAObjeto(
						new StringReader(contenidoXML),
						dtoClass);

				logger.info("Objeto de respuesta: " + responseDTO);
				success = true;
				logger.info("Conexi�n exitosa en el intento " + attempt);
			} catch (InternalExcepcion e) {
				throw e;
			}

		}
				
		return responseDTO;
	}
	
 	public DTOConsultaDatosExpediente consultaDatosPorExpediente(String numCuenta, String tipoMoneda, Date fechaExpiracion) throws InternalExcepcion {
		//MGL 
		/*obtener valores*/
		String wsdlUrl = parametros.getWsSoapMc();		
		String wsdlAS = parametros.getPrefijoNumReferenciaMc();
		
		String codEmisor = parametros.getCodigoEmisorMc();	
		String codUsuario = parametros.getCodigoUsuarioMc();	
		String numTerminal = parametros.getNumTerminalMc();	
		String comercio = parametros.getWsComercioMc();	
		String numReferenciaWS = wsdlAS + parametros.getNextNumReferencia();
		
		String usuario = parametros.getWsUsuarioMc();
		String clave = parametros.getWsClaveMc();		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    String fechaTerminal = sdf.format(new Date());
	    System.out.println("fechaTerminal:"+fechaTerminal);
	        
	    DateFormat dateFormat = new SimpleDateFormat("HHmmss");
	    String horaTerminal = dateFormat.format(new Date());	       
	    System.out.println("horaTerminal:"+horaTerminal);
	    
	    /*fecha de expiacion*/
	    SimpleDateFormat sdfanio = new SimpleDateFormat("yyyy");
	    String fchExpanio =  sdfanio.format(fechaExpiracion).substring(2,4);
	    
	    SimpleDateFormat sdfmes = new SimpleDateFormat("MM");
	    String fchExpmes = sdfmes.format(fechaExpiracion);
	    
	    String fchExp = fchExpanio + fchExpmes;
	    
	    
	    String nCuenta = StringsUtils.quitarCeroIzquierdaString(numCuenta);
		/**/
		
		DTOwservice dto = new DTOwservice(ConstantesWS.SOACTION_CONSULTA_DATOS_EXPEDIENTE);
		Class<DTOConsultaDatosExpediente> dtoClass = DTOConsultaDatosExpediente.class;
		DTOConsultaDatosExpediente responseDTO = null;
				
		Map<String, String> inputRequest = ConstantesWS
				.getConsultaDatosExpedienteMap();
		
		inputRequest.put(ConstantesWS.COD_EMISOR, codEmisor);
		inputRequest.put(ConstantesWS.COD_USUARIO, codUsuario);
		inputRequest.put(ConstantesWS.NUM_TERMINAL, numTerminal);
		inputRequest.put(ConstantesWS.NUM_REFERENCIA, numReferenciaWS);
		inputRequest.put(ConstantesWS.NUM_CUENTA, nCuenta);
		inputRequest.put(ConstantesWS.FECHA_EXPIRACION, fchExp);
		inputRequest.put(ConstantesWS.COMERCIO, comercio);
		inputRequest.put(ConstantesWS.MONEDA, tipoMoneda);
		inputRequest.put(ConstantesWS.FECHA_TXN_TERMINAL, fechaTerminal);
		inputRequest.put(ConstantesWS.HORA_TXN_TERMINAL, horaTerminal);
		inputRequest.put(ConstantesWS.WS_USUARIO, usuario);
		inputRequest.put(ConstantesWS.WS_CLAVE, clave);
		inputRequest.put(ConstantesWS.RESERVADO, "");
		
		String soapRequestPrevie = ConstantesWS.generarXml(
				ConstantesWS.CONSULTA_DATOS_EXPEDIENTE_XML, inputRequest);
		

		String soapRequest = dto.getSoapTemplate().replace("SOAP_CONTENT", soapRequestPrevie);

		//logger.info("Request generado: " + soapRequest);

		int maxRetries = 5;
		int attempt = 0;
		boolean success = false;

		while (attempt < maxRetries && !success) {
			attempt++;
			String dataWS = "";
			try {
				dataWS = SoapClientUtil.sendSoapRequest(wsdlUrl,
						dto.getSoapAction(),
						soapRequest);
			} catch (ExternalServiceMCProcesosException e) {
				throw e;
			}

			try {

				logger.info("Respuesta del servidor:");
				logger.info(dataWS);

				Document documentoXML = SoapClientUtil.parseXmlResponse(dataWS
						.toString());
				String resultado = SoapClientUtil.getTextFromElement(
						documentoXML, dto.getResultTag());

				String contenidoXML = resultado.substring(
						resultado.indexOf(dto.getStartTag()),
						resultado.indexOf(dto.getEndTag())
								+ dto.getEndTag().length());

				responseDTO = SoapClientUtil.convertirXMLAObjeto(
						new StringReader(contenidoXML),
						dtoClass);

				logger.info("Objeto de respuesta: " + responseDTO);
				success = true;
				logger.info("Conexi�n exitosa en el intento " + attempt);
			} catch (InternalExcepcion e) {
				throw e;
			}

		}
		return responseDTO;
	}

	public DTOConsultaDatosTarjeta informacionDeTarjeta(String numTarjeta) throws InternalExcepcion,ExternalServiceMCProcesosException {

		String wsdlUrl = parametros.getWsSoapMc();
		String wsdlAS = parametros.getPrefijoNumReferenciaMc();

		String numReferenciaWS = wsdlAS + parametros.getNextNumReferencia();


		Map<String, String> inputRequest = ConstantesWS
				.getConsultaDatosTarjetaMap();
		inputRequest.put(ConstantesWS.COD_EMISOR, "191");				//971
		inputRequest.put(ConstantesWS.COD_USUARIO, "TT9999");			//TW9999
		inputRequest.put(ConstantesWS.NUM_TERMINAL, "11010101");		//11010101
		inputRequest.put(ConstantesWS.NUM_REFERENCIA, numReferenciaWS);	//AC2020000322
		inputRequest.put(ConstantesWS.NUM_TARJETA, numTarjeta);		//000000009
		inputRequest.put(ConstantesWS.FECHA_EXPIRACION, "2601");		//2701
		inputRequest.put(ConstantesWS.COMERCIO, "4058950");				//9999999
		inputRequest.put(ConstantesWS.MONEDA, "604");
		inputRequest.put(ConstantesWS.FECHA_TXN_TERMINAL, "20210831");	//20160224
		inputRequest.put(ConstantesWS.HORA_TXN_TERMINAL, "111901");		//172020
		inputRequest.put(ConstantesWS.WS_USUARIO, "4858643428");		//prueba1234
		inputRequest.put(ConstantesWS.WS_CLAVE, "aza877azutht98b8");	//prueba1234567890
		inputRequest.put(ConstantesWS.RESERVADO, "");
		String soapRequestPrevie = ConstantesWS.generarXml(
				ConstantesWS.Consulta_Datos_Tarjeta_XML, inputRequest);
		DTOwservice dto = new DTOwservice(ConstantesWS.SOACTION_Consulta_Datos_Tarjeta);

		String soapRequest = dto.getSoapTemplate().replace("SOAP_CONTENT", soapRequestPrevie);

		//logger.info("Request generado: " + soapRequest);

		int maxRetries = 5;
		int attempt = 0;
		boolean success = false;
		DTOConsultaDatosTarjeta responseDTO = null;

		while (attempt < maxRetries && !success) {
			attempt++;
			String dataWS = "";
			try {
				dataWS = SoapClientUtil.sendSoapRequest(wsdlUrl,
						dto.getSoapAction(),
						soapRequest);
			} catch (ExternalServiceMCProcesosException e) {
				throw e;
			}

			try {

				logger.info("Respuesta del servidor:");
				logger.info(dataWS);

				Document documentoXML = SoapClientUtil.parseXmlResponse(dataWS
						.toString());
				String resultado = SoapClientUtil.getTextFromElement(
						documentoXML, dto.getResultTag());

				String contenidoXML = resultado.substring(
						resultado.indexOf(dto.getStartTag()),
						resultado.indexOf(dto.getEndTag())
								+ dto.getEndTag().length());

				responseDTO = SoapClientUtil.convertirXMLAObjeto(
						new StringReader(contenidoXML),
						DTOConsultaDatosTarjeta.class);

				logger.info("Objeto de respuesta: " + responseDTO);
				success = true;
				logger.info("Conexi�n exitosa en el intento " + attempt);
			} catch (InternalExcepcion e) {
				throw e;
			}

		}
		return responseDTO;

	}
 	
	public DTOConsultaDatosCliente consultaDatosCliente(String tipoDoc, String numDoc) throws InternalExcepcion {
		String wsdlUrl = parametros.getWsSoapMc();
		DTOwservice dto = new DTOwservice(ConstantesWS.SOACTION_CONSULTA_DATOS_CLIENTE);
		Class<DTOConsultaDatosCliente> dtoClass = DTOConsultaDatosCliente.class;
		DTOConsultaDatosCliente responseDTO = null;

		String wsdlAS = parametros.getPrefijoNumReferenciaMc();

		Map<String, String> inputRequest = ConstantesWS
				.getConsultaDatosClienteMap();
		String numReferenciaWS = wsdlAS + parametros.getNextNumReferencia();
		
		inputRequest.put(ConstantesWS.COD_EMISOR, "191");
		inputRequest.put(ConstantesWS.COD_USUARIO, "TT9999");
		inputRequest.put(ConstantesWS.NUM_TERMINAL, "11010101");
		inputRequest.put(ConstantesWS.NUM_REFERENCIA, numReferenciaWS);
		
		inputRequest.put(ConstantesWS.TIPO_DOCUMENTO, tipoDoc);
		inputRequest.put(ConstantesWS.NUM_DOCUMENTO,  numDoc);
				
		inputRequest.put(ConstantesWS.FECHA_TXN_TERMINAL, "20160224");
		inputRequest.put(ConstantesWS.HORA_TXN_TERMINAL, "172020");
		inputRequest.put(ConstantesWS.WS_USUARIO, "4858643428");
		inputRequest.put(ConstantesWS.WS_CLAVE, "aza877azutht98b8");
		inputRequest.put(ConstantesWS.RESERVADO, "");
		
		String soapRequestPrevie = ConstantesWS.generarXml(
				ConstantesWS.CONSULTA_DATOS_CLIENTE_XML, inputRequest);
		

		String soapRequest = dto.getSoapTemplate().replace("SOAP_CONTENT", soapRequestPrevie);
		int maxRetries = 5;
		int attempt = 0;
		boolean success = false;

		while (attempt < maxRetries && !success) {
			attempt++;
			String dataWS = "";
			try {
				dataWS = SoapClientUtil.sendSoapRequest(wsdlUrl,
						dto.getSoapAction(),
						soapRequest);
			} catch (ExternalServiceMCProcesosException e) {
				throw e;
			}

			try {

				logger.info("Respuesta del servidor:");
				logger.info(dataWS);

				Document documentoXML = SoapClientUtil.parseXmlResponse(dataWS
						.toString());
				String resultado = SoapClientUtil.getTextFromElement(
						documentoXML, dto.getResultTag());

				String contenidoXML = resultado.substring(
						resultado.indexOf(dto.getStartTag()),
						resultado.indexOf(dto.getEndTag())
								+ dto.getEndTag().length());

				responseDTO = SoapClientUtil.convertirXMLAObjeto(
						new StringReader(contenidoXML),
						dtoClass);

				logger.info("Objeto de respuesta: " + responseDTO);
				success = true;
				logger.info("Conexi�n exitosa en el intento " + attempt);
			} catch (InternalExcepcion e) {
				throw e;
			}

		}
		return responseDTO;
	}
	
	public List<MovimientoTarjetaExpediente> listaMovTarjExp (DTOConsultaMovimientosExpediente dato){
		
		List<MovimientoTarjetaExpediente> movimientoTarjetas = new ArrayList<MovimientoTarjetaExpediente>();
		int c = dato.getMoviUltMovimientos() == null || dato.getMoviUltMovimientos().isEmpty() ? 0
				: Integer.parseInt(dato.getMoviUltMovimientos().trim());
		if (c > 0) {
			MovimientoTarjetaExpediente movimientoTarjeta1 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta1.setId("1".trim());
			movimientoTarjeta1.setFechaTxn(Fecha.transformarADateMC(dato.getMov1FechaTxn().trim()));
			movimientoTarjeta1.setDescripcionTxn(dato.getMov1DesTxn().trim());
			movimientoTarjeta1.setMonOriginalTxn(dato.getMov1MonOriginalTxn().trim());
			movimientoTarjeta1.setMontoTxn(dato.getMov1MontoTxn().trim());
			movimientoTarjeta1.setSigMontoTxn(dato.getMov1SigMontoTxn().trim());			
			movimientoTarjeta1.setOperacionTxn(dato.getMov1Operacion().trim());
			movimientoTarjeta1.setCodAutTxn(dato.getMov1CodAutorizacion().trim());
			movimientoTarjeta1.setNumTarjetaTxn(dato.getMov1NumTarjeta().trim());
			
			movimientoTarjetas.add(movimientoTarjeta1);
		}
		if (c > 1) {
			MovimientoTarjetaExpediente movimientoTarjeta2 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta2.setId("2".trim());
			movimientoTarjeta2.setFechaTxn(Fecha.transformarADateMC(dato.getMov2FechaTxn().trim()));
			movimientoTarjeta2.setDescripcionTxn(dato.getMov2DesTxn().trim());
			movimientoTarjeta2.setMonOriginalTxn(dato.getMov2MonOriginalTxn().trim());
			movimientoTarjeta2.setMontoTxn(dato.getMov2MontoTxn().trim());
			movimientoTarjeta2.setSigMontoTxn(dato.getMov2SigMontoTxn().trim());			
			movimientoTarjeta2.setOperacionTxn(dato.getMov2Operacion().trim());
			movimientoTarjeta2.setCodAutTxn(dato.getMov2CodAutorizacion().trim());
			movimientoTarjeta2.setNumTarjetaTxn(dato.getMov2NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta2);
		}
		
		if (c > 2) {
			MovimientoTarjetaExpediente movimientoTarjeta3 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta3.setId("3".trim());
			movimientoTarjeta3.setFechaTxn(Fecha.transformarADateMC(dato.getMov3FechaTxn().trim()));
			movimientoTarjeta3.setDescripcionTxn(dato.getMov3DesTxn().trim());
			movimientoTarjeta3.setMonOriginalTxn(dato.getMov3MonOriginalTxn().trim());
			movimientoTarjeta3.setMontoTxn(dato.getMov3MontoTxn().trim());
			movimientoTarjeta3.setSigMontoTxn(dato.getMov3SigMontoTxn().trim());			
			movimientoTarjeta3.setOperacionTxn(dato.getMov3Operacion().trim());
			movimientoTarjeta3.setCodAutTxn(dato.getMov3CodAutorizacion().trim());
			movimientoTarjeta3.setNumTarjetaTxn(dato.getMov3NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta3);
		}

		if (c > 3) {
			MovimientoTarjetaExpediente movimientoTarjeta4 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta4.setId("4".trim());
			movimientoTarjeta4.setFechaTxn(Fecha.transformarADateMC(dato.getMov4FechaTxn().trim()));
			movimientoTarjeta4.setDescripcionTxn(dato.getMov4DesTxn().trim());
			movimientoTarjeta4.setMonOriginalTxn(dato.getMov4MonOriginalTxn().trim());
			movimientoTarjeta4.setMontoTxn(dato.getMov4MontoTxn().trim());
			movimientoTarjeta4.setSigMontoTxn(dato.getMov4SigMontoTxn().trim());			
			movimientoTarjeta4.setOperacionTxn(dato.getMov4Operacion().trim());
			movimientoTarjeta4.setCodAutTxn(dato.getMov4CodAutorizacion().trim());
			movimientoTarjeta4.setNumTarjetaTxn(dato.getMov4NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta4);
		}

		if (c > 4) {
			MovimientoTarjetaExpediente movimientoTarjeta5 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta5.setId("5".trim());
			movimientoTarjeta5.setFechaTxn(Fecha.transformarADateMC(dato.getMov5FechaTxn().trim()));
			movimientoTarjeta5.setDescripcionTxn(dato.getMov5DesTxn().trim());
			movimientoTarjeta5.setMonOriginalTxn(dato.getMov5MonOriginalTxn().trim());
			movimientoTarjeta5.setMontoTxn(dato.getMov5MontoTxn().trim());
			movimientoTarjeta5.setSigMontoTxn(dato.getMov5SigMontoTxn().trim());			
			movimientoTarjeta5.setOperacionTxn(dato.getMov5Operacion().trim());
			movimientoTarjeta5.setCodAutTxn(dato.getMov5CodAutorizacion().trim());
			movimientoTarjeta5.setNumTarjetaTxn(dato.getMov5NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta5);
		}

		if (c > 5) {
			MovimientoTarjetaExpediente movimientoTarjeta6 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta6.setId("6".trim());
			movimientoTarjeta6.setFechaTxn(Fecha.transformarADateMC(dato.getMov6FechaTxn().trim()));
			movimientoTarjeta6.setDescripcionTxn(dato.getMov6DesTxn().trim());
			movimientoTarjeta6.setMonOriginalTxn(dato.getMov6MonOriginalTxn().trim());
			movimientoTarjeta6.setMontoTxn(dato.getMov6MontoTxn().trim());
			movimientoTarjeta6.setSigMontoTxn(dato.getMov6SigMontoTxn().trim());			
			movimientoTarjeta6.setOperacionTxn(dato.getMov6Operacion().trim());
			movimientoTarjeta6.setCodAutTxn(dato.getMov6CodAutorizacion().trim());
			movimientoTarjeta6.setNumTarjetaTxn(dato.getMov6NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta6);
		}

		if (c > 6) {
			MovimientoTarjetaExpediente movimientoTarjeta7 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta7.setId("7".trim());
			movimientoTarjeta7.setFechaTxn(Fecha.transformarADateMC(dato.getMov7FechaTxn().trim()));
			movimientoTarjeta7.setDescripcionTxn(dato.getMov7DesTxn().trim());
			movimientoTarjeta7.setMonOriginalTxn(dato.getMov7MonOriginalTxn().trim());
			movimientoTarjeta7.setMontoTxn(dato.getMov7MontoTxn().trim());
			movimientoTarjeta7.setSigMontoTxn(dato.getMov7SigMontoTxn().trim());			
			movimientoTarjeta7.setOperacionTxn(dato.getMov7Operacion().trim());
			movimientoTarjeta7.setCodAutTxn(dato.getMov7CodAutorizacion().trim());
			movimientoTarjeta7.setNumTarjetaTxn(dato.getMov7NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta7);
		}

		if (c > 7) {
			MovimientoTarjetaExpediente movimientoTarjeta8 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta8.setId("8".trim());
			movimientoTarjeta8.setFechaTxn(Fecha.transformarADateMC(dato.getMov8FechaTxn().trim()));
			movimientoTarjeta8.setDescripcionTxn(dato.getMov8DesTxn().trim());
			movimientoTarjeta8.setMonOriginalTxn(dato.getMov8MonOriginalTxn().trim());
			movimientoTarjeta8.setMontoTxn(dato.getMov8MontoTxn().trim());
			movimientoTarjeta8.setSigMontoTxn(dato.getMov8SigMontoTxn().trim());			
			movimientoTarjeta8.setOperacionTxn(dato.getMov8Operacion().trim());
			movimientoTarjeta8.setCodAutTxn(dato.getMov8CodAutorizacion().trim());
			movimientoTarjeta8.setNumTarjetaTxn(dato.getMov8NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta8);
		}

		if (c > 8) {
			MovimientoTarjetaExpediente movimientoTarjeta9 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta9.setId("9".trim());
			movimientoTarjeta9.setFechaTxn(Fecha.transformarADateMC(dato.getMov9FechaTxn()));
			movimientoTarjeta9.setDescripcionTxn(dato.getMov9DesTxn());
			movimientoTarjeta9.setMonOriginalTxn(dato.getMov9MonOriginalTxn());
			movimientoTarjeta9.setMontoTxn(dato.getMov9MontoTxn().trim());
			movimientoTarjeta9.setSigMontoTxn(dato.getMov9SigMontoTxn());			
			movimientoTarjeta9.setOperacionTxn(dato.getMov9Operacion());
			movimientoTarjeta9.setCodAutTxn((dato.getMov9CodAutorizacion()!=null)?dato.getMov9CodAutorizacion().trim():"");
			movimientoTarjeta9.setNumTarjetaTxn(dato.getMov9NumTarjeta());
			movimientoTarjetas.add(movimientoTarjeta9);
		}

		if (c > 9) {
			MovimientoTarjetaExpediente movimientoTarjeta10 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta10.setId("10".trim());
			movimientoTarjeta10.setFechaTxn(Fecha.transformarADateMC(dato.getMov10FechaTxn().trim()));
			movimientoTarjeta10.setDescripcionTxn(dato.getMov10DesTxn().trim());
			movimientoTarjeta10.setMonOriginalTxn(dato.getMov10MonOriginalTxn().trim());
			movimientoTarjeta10.setMontoTxn(dato.getMov10MontoTxn().trim());
			movimientoTarjeta10.setSigMontoTxn(dato.getMov10SigMontoTxn().trim());			
			movimientoTarjeta10.setOperacionTxn(dato.getMov10Operacion().trim());
			movimientoTarjeta10.setCodAutTxn(dato.getMov10CodAutorizacion().trim());
			movimientoTarjeta10.setNumTarjetaTxn(dato.getMov10NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta10);
		}

		if (c > 10) {
			MovimientoTarjetaExpediente movimientoTarjeta11 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta11.setId("11".trim());
			movimientoTarjeta11.setFechaTxn(Fecha.transformarADateMC(dato.getMov11FechaTxn().trim()));
			movimientoTarjeta11.setDescripcionTxn(dato.getMov11DesTxn().trim());
			movimientoTarjeta11.setMonOriginalTxn(dato.getMov11MonOriginalTxn().trim());
			movimientoTarjeta11.setMontoTxn(dato.getMov11MontoTxn().trim());
			movimientoTarjeta11.setSigMontoTxn(dato.getMov11SigMontoTxn().trim());			
			movimientoTarjeta11.setOperacionTxn(dato.getMov11Operacion().trim());
			movimientoTarjeta11.setCodAutTxn(dato.getMov11CodAutorizacion().trim());
			movimientoTarjeta11.setNumTarjetaTxn(dato.getMov11NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta11);
		}

		if (c > 11) {
			MovimientoTarjetaExpediente movimientoTarjeta12 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta12.setId("12".trim());
			movimientoTarjeta12.setFechaTxn(Fecha.transformarADateMC(dato.getMov12FechaTxn().trim()));
			movimientoTarjeta12.setDescripcionTxn(dato.getMov12DesTxn().trim());
			movimientoTarjeta12.setMonOriginalTxn(dato.getMov12MonOriginalTxn().trim());
			movimientoTarjeta12.setMontoTxn(dato.getMov12MontoTxn().trim());
			movimientoTarjeta12.setSigMontoTxn(dato.getMov12SigMontoTxn().trim());			
			movimientoTarjeta12.setOperacionTxn(dato.getMov12Operacion().trim());
			movimientoTarjeta12.setCodAutTxn(dato.getMov12CodAutorizacion().trim());
			movimientoTarjeta12.setNumTarjetaTxn(dato.getMov12NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta12);
		}

		if (c > 12) {
			MovimientoTarjetaExpediente movimientoTarjeta13 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta13.setId("13".trim());
			movimientoTarjeta13.setFechaTxn(Fecha.transformarADateMC(dato.getMov13FechaTxn().trim()));
			movimientoTarjeta13.setDescripcionTxn(dato.getMov13DesTxn().trim());
			movimientoTarjeta13.setMonOriginalTxn(dato.getMov13MonOriginalTxn().trim());
			movimientoTarjeta13.setMontoTxn(dato.getMov13MontoTxn().trim());
			movimientoTarjeta13.setSigMontoTxn(dato.getMov13SigMontoTxn().trim());			
			movimientoTarjeta13.setOperacionTxn(dato.getMov13Operacion().trim());
			movimientoTarjeta13.setCodAutTxn(dato.getMov13CodAutorizacion().trim());
			movimientoTarjeta13.setNumTarjetaTxn(dato.getMov13NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta13);
		}

		if (c > 13) {
			MovimientoTarjetaExpediente movimientoTarjeta14 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta14.setId("14".trim());
			movimientoTarjeta14.setFechaTxn(Fecha.transformarADateMC(dato.getMov14FechaTxn().trim()));
			movimientoTarjeta14.setDescripcionTxn(dato.getMov14DesTxn().trim());
			movimientoTarjeta14.setMonOriginalTxn(dato.getMov14MonOriginalTxn().trim());
			movimientoTarjeta14.setMontoTxn(dato.getMov14MontoTxn().trim());
			movimientoTarjeta14.setSigMontoTxn(dato.getMov14SigMontoTxn().trim());			
			movimientoTarjeta14.setOperacionTxn(dato.getMov14Operacion().trim());
			movimientoTarjeta14.setCodAutTxn(dato.getMov14CodAutorizacion().trim());
			movimientoTarjeta14.setNumTarjetaTxn(dato.getMov14NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta14);
		}

		if (c > 14) {
			MovimientoTarjetaExpediente movimientoTarjeta15 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta15.setId("15".trim());
			movimientoTarjeta15.setFechaTxn(Fecha.transformarADateMC(dato.getMov15FechaTxn().trim()));
			movimientoTarjeta15.setDescripcionTxn(dato.getMov15DesTxn().trim());
			movimientoTarjeta15.setMonOriginalTxn(dato.getMov15MonOriginalTxn().trim());
			movimientoTarjeta15.setMontoTxn(dato.getMov15MontoTxn().trim());
			movimientoTarjeta15.setSigMontoTxn(dato.getMov15SigMontoTxn().trim());			
			movimientoTarjeta15.setOperacionTxn(dato.getMov15Operacion().trim());
			movimientoTarjeta15.setCodAutTxn(dato.getMov15CodAutorizacion().trim());
			movimientoTarjeta15.setNumTarjetaTxn(dato.getMov15NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta15);
		}

		if (c > 15) {
			MovimientoTarjetaExpediente movimientoTarjeta16 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta16.setId("16".trim());
			movimientoTarjeta16.setFechaTxn(Fecha.transformarADateMC(dato.getMov16FechaTxn().trim()));
			movimientoTarjeta16.setDescripcionTxn(dato.getMov16DesTxn().trim());
			movimientoTarjeta16.setMonOriginalTxn(dato.getMov16MonOriginalTxn().trim());
			movimientoTarjeta16.setMontoTxn(dato.getMov16MontoTxn().trim());
			movimientoTarjeta16.setSigMontoTxn(dato.getMov16SigMontoTxn().trim());			
			movimientoTarjeta16.setOperacionTxn(dato.getMov16Operacion().trim());
			movimientoTarjeta16.setCodAutTxn(dato.getMov16CodAutorizacion().trim());
			movimientoTarjeta16.setNumTarjetaTxn(dato.getMov16NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta16);
		}

		if (c > 16) {
			MovimientoTarjetaExpediente movimientoTarjeta17 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta17.setId("17".trim());
			movimientoTarjeta17.setFechaTxn(Fecha.transformarADateMC(dato.getMov17FechaTxn().trim()));
			movimientoTarjeta17.setDescripcionTxn(dato.getMov17DesTxn().trim());
			movimientoTarjeta17.setMonOriginalTxn(dato.getMov17MonOriginalTxn().trim());
			movimientoTarjeta17.setMontoTxn(dato.getMov17MontoTxn().trim());
			movimientoTarjeta17.setSigMontoTxn(dato.getMov17SigMontoTxn().trim());			
			movimientoTarjeta17.setOperacionTxn(dato.getMov17Operacion().trim());
			movimientoTarjeta17.setCodAutTxn(dato.getMov17CodAutorizacion().trim());
			movimientoTarjeta17.setNumTarjetaTxn(dato.getMov17NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta17);
		}

		if (c > 17) {
			MovimientoTarjetaExpediente movimientoTarjeta18 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta18.setId("18".trim());
			movimientoTarjeta18.setFechaTxn(Fecha.transformarADateMC(dato.getMov18FechaTxn().trim()));
			movimientoTarjeta18.setDescripcionTxn(dato.getMov18DesTxn().trim());
			movimientoTarjeta18.setMonOriginalTxn(dato.getMov18MonOriginalTxn().trim());
			movimientoTarjeta18.setMontoTxn(dato.getMov18MontoTxn().trim());
			movimientoTarjeta18.setSigMontoTxn(dato.getMov18SigMontoTxn().trim());			
			movimientoTarjeta18.setOperacionTxn(dato.getMov18Operacion().trim());
			movimientoTarjeta18.setCodAutTxn(dato.getMov18CodAutorizacion().trim());
			movimientoTarjeta18.setNumTarjetaTxn(dato.getMov18NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta18);
		}

		if (c > 18) {
			MovimientoTarjetaExpediente movimientoTarjeta19 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta19.setId("19".trim());
			movimientoTarjeta19.setFechaTxn(Fecha.transformarADateMC(dato.getMov19FechaTxn().trim()));
			movimientoTarjeta19.setDescripcionTxn(dato.getMov19DesTxn().trim());
			movimientoTarjeta19.setMonOriginalTxn(dato.getMov19MonOriginalTxn().trim());
			movimientoTarjeta19.setMontoTxn(dato.getMov19MontoTxn().trim());
			movimientoTarjeta19.setSigMontoTxn(dato.getMov19SigMontoTxn().trim());			
			movimientoTarjeta19.setOperacionTxn(dato.getMov19Operacion().trim());
			movimientoTarjeta19.setCodAutTxn(dato.getMov19CodAutorizacion().trim());
			movimientoTarjeta19.setNumTarjetaTxn(dato.getMov19NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta19);
		}

		if (c > 19) {
			MovimientoTarjetaExpediente movimientoTarjeta20 = new MovimientoTarjetaExpediente();
			
			movimientoTarjeta20.setId("20".trim());
			movimientoTarjeta20.setFechaTxn(Fecha.transformarADateMC(dato.getMov20FechaTxn().trim()));
			movimientoTarjeta20.setDescripcionTxn(dato.getMov20DesTxn().trim());
			movimientoTarjeta20.setMonOriginalTxn(dato.getMov20MonOriginalTxn().trim());
			movimientoTarjeta20.setMontoTxn(dato.getMov20MontoTxn().trim());
			movimientoTarjeta20.setSigMontoTxn(dato.getMov20SigMontoTxn().trim());			
			movimientoTarjeta20.setOperacionTxn(dato.getMov20Operacion().trim());
			movimientoTarjeta20.setCodAutTxn(dato.getMov20CodAutorizacion().trim());
			movimientoTarjeta20.setNumTarjetaTxn(dato.getMov20NumTarjeta().trim());
			movimientoTarjetas.add(movimientoTarjeta20);
		}
		
		return movimientoTarjetas;
	}
}
