package com.zh.coherence.viewer.pof.printer;

import javax.xml.bind.annotation.*;
import java.util.Collection;

@XmlRootElement(name = "printer-container")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrinterContainer {

    @XmlElement
    private Collection<Printer> printers;

    public Collection<Printer> getPrinters() {
        return printers;
    }

    public void setPrinters(Collection<Printer> printers) {
        this.printers = printers;
    }
}
