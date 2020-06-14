package com.utility.print.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrinterResolution;

import com.utility.print.handler.PrintJobHandler;

/**
 * @author Minu.Kim
 */
public class JavaPrintService {
	public void doStreamPrint(String printName, String command) throws Exception {
		if (printName == null)
			throw new IllegalArgumentException("Please, Input printer name.");

		this.streamPrint(printName, new ByteArrayInputStream(command.getBytes()));
	}

	/**
	 * File 방식의 Print 실행
	 * 
	 * @param printName
	 * @param filePath
	 * @throws Exception
	 */
	public void doFilePrint(String printName, String filePath) throws Exception {
		try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
			this.streamPrint(printName, fileInputStream);
		}
	}

	/**
	 * Stream 방식의 Print 실행
	 * 
	 * @param printerName
	 * @param inputSteam
	 * @throws Exception
	 */
	private void streamPrint(String printerName, InputStream inputSteam) throws Exception {
		int width = 210, height = 297;

		PrintService printService = this.getPrintService(printerName);
		PrinterResolution printerResolution = new PrinterResolution(1200, 1200, PrinterResolution.DPI);

		DocAttributeSet das = new HashDocAttributeSet();
		das.add(printerResolution);
		// das.add(new MediaPrintableArea(0, 0, 1000, 100, MediaPrintableArea.MM));
		// das.add(new PrintQuality(3));

		Doc doc = new SimpleDoc(inputSteam, DocFlavor.INPUT_STREAM.JPEG, das);

		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new Copies(1));
		pras.add(printerResolution);
		// pras.add(MediaSizeName.ISO_A4);
		// pras.add(OrientationRequested.LANDSCAPE);
		// pras.add(new MediaPrintableArea(0, 0, 210, 297, MediaPrintableArea.MM));

		if (width > 0 && height > 0)
			pras.add(new MediaPrintableArea(0, 0, (width / 10), (height / 10), MediaPrintableArea.MM));

		DocPrintJob job = printService.createPrintJob();
		job.addPrintJobListener(new PrintJobHandler());

		job.print(doc, pras);
	}

    /**
     * PrintService 가져오기 실행.
     *
     * @param printName
     * @return
     */
    public static PrintService getPrintService(String printName) {
        PrintService printService = null;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        for (PrintService service : services) {
            if (service.getName().equals(printName)) {
                printService = service;
                break;
            }
        }

        if (printService == null)
            throw new IllegalArgumentException("Invalid Print Name.");

        return printService;
    }

	/**
	 * Print 실행 Log
	 * 
	 * @param printName
	 * @param command
	 */
	@SuppressWarnings("unused")
	private void doPrintLog(String printName, String command) {
		StringBuilder info = new StringBuilder();
		info.append("\n============== Print Info Start =======================\n");
		info.append("Print Name : ").append(printName).append("\n");
		info.append("command : \n").append(command);
		info.append("\n============== Print Info End =======================\n");

		System.out.println(info.toString());
	}
}