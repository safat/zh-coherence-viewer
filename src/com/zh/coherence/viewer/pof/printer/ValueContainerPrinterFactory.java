package com.zh.coherence.viewer.pof.printer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValueContainerPrinterFactory {
    private static Map<Integer, Printer> printers = new HashMap<Integer, Printer>();
    private static ValueContainerPrinterFactory instance = new ValueContainerPrinterFactory();

    private ValueContainerPrinterFactory() {
        load();
    }

    public static ValueContainerPrinterFactory getInstance() {
        return instance;
    }

    public Printer getPrinter(int pofId) {
        return printers.get(pofId);
    }

    public void registerPrinter(Printer printer) {
        printers.put(printer.getPofId(), printer);
    }

    public void removePrinter(Printer printer) {
        printers.remove(printer.getPofId());
    }

    public void save() {
        try {
            JAXBContext context = JAXBContext.newInstance(PrinterContainer.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            PrinterContainer container = new PrinterContainer();
            container.setPrinters(printers.values());
            System.err.println("file saved to: " + getFile());
            m.marshal(container, getFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void load() {
        try {
            PrinterContainer container;
            File file = getFile();
            if (file == null || !file.exists()) {
                return;
            }
            JAXBContext context = JAXBContext.newInstance(PrinterContainer.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            container = (PrinterContainer) unmarshaller.unmarshal(getFile());
            for(Printer printer : container.getPrinters()){
                registerPrinter(printer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private File getFile() {
        File home = new File(System.getProperty("user.home") + File.separatorChar + ".zhcv");
        if (!home.exists()) {
            home.mkdir();
        }
        return new File(home, "printers");
    }

    public List<Printer> getPrinters(){
        return new ArrayList<Printer>(printers.values());
    }
}
