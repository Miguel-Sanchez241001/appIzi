package pe.com.bn.app.tranversal.util;

 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

 
 
public class Fecha {
    private final static Logger log = Logger.getLogger(Fecha.class);

	public static Date transformarADateMC(String fecha) {
		SimpleDateFormat fechaFormato = new SimpleDateFormat("yyyyMMdd");
		try {
			return fechaFormato.parse(fecha);
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		return null;
	}

}

