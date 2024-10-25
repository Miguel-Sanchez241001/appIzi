package pe.com.bn.app.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
 
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
 
import lombok.Getter;
import lombok.Setter;

import pe.com.bn.app.services.WsFacade;
import pe.com.bn.app.view.model.MovimientoModel;

@Getter
@Setter
@ManagedBean(name = "movimientoController")
@ViewScoped
public class MovimientoController implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{wsFacade}")
	private WsFacade facade;

	private final static Logger log = Logger
			.getLogger(MovimientoController.class);

	private MovimientoModel movimientoModel;

	@PostConstruct
	public void init() {
		movimientoModel = new MovimientoModel();
		log.info("movimientoController inicializado en "
				+ System.identityHashCode(this));

	}

	public void consultarMovimiento() {
		try {
			String numeroCuenta = movimientoModel.getNumeroCuenta();
			log.info("Consultando datos de movimiento. Número de movimiento enviado: "
					+ numeroCuenta);
			movimientoModel.setEntyMoviemiento(facade
					.consultaMovimientoPorExpediente(numeroCuenta, "604",
							new Date()));
			log.info("Datos de movimiento recibidos: "
					+ movimientoModel.getEntyMoviemiento());

			if (movimientoModel.getEntyMoviemiento() != null
					&& movimientoModel.getEntyMoviemiento().getCodRespuesta()
							.equals("0000")) {

				movimientoModel.setMovimientos(facade
						.listaMovTarjExp(movimientoModel.getEntyMoviemiento()));
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"Éxito",
										"Datos de la movimiento consultados correctamente."));
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Advertencia",
								"Error en la data de respeusta: "
										+ movimientoModel.getEntyMoviemiento()
												.getDescRespuesta()));
			}
		} catch (Exception e) {
			log.error("Error al consultar los datos de la movimiento: ", e);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e
							.getMessage()));
		}
	}

	// Método preProcess para añadir un título en la parte superior
	public void preProcessXLS(Object document) {
	    HSSFWorkbook workbook = (HSSFWorkbook) document;
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    // Estilo para el título
	    HSSFCellStyle titleStyle = workbook.createCellStyle();
	    titleStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
	    titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    HSSFFont font = workbook.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex()); // Texto blanco
	    font.setBold(true); // Negrita en lugar de setBoldweight
	    font.setFontHeightInPoints((short) 16); // Tamaño de fuente
	    titleStyle.setFont(font);

	    // Crear las dos primeras filas para el título
	    HSSFRow titleRow = sheet.createRow(0);
	    HSSFRow titleRow2 = sheet.createRow(1);

	    // Combinar celdas para que el título abarque las 8 columnas y 2 filas
	    sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 7));

	    // Insertar el título en la primera celda y aplicar el estilo
	    HSSFCell titleCell = titleRow.createCell(0);
	    titleCell.setCellValue("Título de la Exportación");
	    titleCell.setCellStyle(titleStyle);

	    // Ajustar la altura de las filas para visibilidad
	    titleRow.setHeightInPoints(30);
	    titleRow2.setHeightInPoints(30);
	}
	public void postProcessXLS(Object document) {
	    HSSFWorkbook workbook = (HSSFWorkbook) document;
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    // Estilo para la fecha
	    HSSFCellStyle dateStyle = workbook.createCellStyle();
	    dateStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
	    dateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    HSSFFont font = workbook.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex()); // Texto blanco
	    font.setBold(true); // Usa setBold en lugar de setBoldweight
	    dateStyle.setFont(font);

	    // Obtener la última fila para añadir la fecha
	    int lastRow = sheet.getLastRowNum();
	    HSSFRow emptyRow = sheet.createRow(lastRow + 1);
	    HSSFRow dateRow = sheet.createRow(lastRow + 2);

	    // Combinar las celdas de la fila de fecha en 8 columnas
	    sheet.addMergedRegion(new CellRangeAddress(lastRow + 2, lastRow + 2, 0, 7));

	    // Configurar la fecha actual
	    String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

	    // Crear la celda de fecha y aplicarle el estilo
	    HSSFCell dateCell = dateRow.createCell(0);
	    dateCell.setCellValue("Fecha de exportación: " + currentDate);
	    dateCell.setCellStyle(dateStyle);

	    // Ajustar la altura de la fila para visibilidad
	    dateRow.setHeightInPoints(20);
	}
}
