package com.smarthealth.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.smarthealth.model.*;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PdfService {

    public byte[] billPdf(Bill b) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document d = new Document(
                PageSize.A4,
                36,
                36,
                36,
                36
        );

        PdfWriter.getInstance(d, out);

        d.open();

        Font title = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                18
        );

        d.add(
                new Paragraph(
                        "SMART HEALTH HOSPITAL",
                        title
                )
        );

        d.add(
                new Paragraph(
                        "Hospital Billing Invoice"
                )
        );

        d.add(
                new Paragraph(
                        "Bill No: "
                                + b.getBillNumber()
                                + "    Date: "
                                + b.getCreatedAt()
                )
        );

        d.add(
                new Paragraph(
                        "Patient: "
                                + b.getPatient().getName()
                )
        );

        d.add(
                new Paragraph(
                        "Email: "
                                + b.getPatient().getEmail()
                                + "    Contact: "
                                + b.getPatient().getPhone()
                )
        );

        /*
         * Disease was removed from Appointment.
         * Therefore, use Symptoms here instead.
         */
        if (b.getAppointment() != null) {

            d.add(
                    new Paragraph(
                            "Symptoms: "
                                    + b.getAppointment().getSymptoms()
                                    + "    Doctor: "
                                    + b.getAppointment().getDoctorName()
                    )
            );
        }

        d.add(Chunk.NEWLINE);

        PdfPTable t = new PdfPTable(3);

        t.setWidthPercentage(100);

        t.addCell("Service / Medicine");
        t.addCell("Qty");
        t.addCell("Amount");

        add(
                t,
                "ICU Charges",
                "1",
                b.getIcuCharges()
        );

        add(
                t,
                "MRI Charges",
                "1",
                b.getMriCharges()
        );

        add(
                t,
                "Doctor Charges",
                "1",
                b.getDoctorCharges()
        );

        add(
                t,
                "Room Charges",
                "1",
                b.getRoomCharges()
        );

        add(
                t,
                "Lab/Test Charges",
                "1",
                b.getLabCharges()
        );

        add(
                t,
                "Other Charges",
                "1",
                b.getOtherCharges()
        );

        for (BillMedicine m : b.getMedicines()) {

            add(
                    t,
                    m.getName(),
                    String.valueOf(m.getQuantity()),
                    m.getLineTotal()
            );
        }

        d.add(t);

        d.add(
                new Paragraph(
                        "Subtotal: ₹"
                                + fmt(b.getSubtotal())
                )
        );

        d.add(
                new Paragraph(
                        "Tax: ₹"
                                + fmt(b.getTax())
                )
        );

        d.add(
                new Paragraph(
                        "TOTAL: ₹"
                                + fmt(b.getTotal()),
                        title
                )
        );

        if (b.getNotes() != null) {

            d.add(
                    new Paragraph(
                            "Notes: "
                                    + b.getNotes()
                    )
            );
        }

        d.close();

        return out.toByteArray();
    }

    private void add(
            PdfPTable t,
            String a,
            String q,
            Double x
    ) {

        t.addCell(a);
        t.addCell(q);
        t.addCell("₹" + fmt(x));
    }

    private String fmt(Double x) {

        return String.format(
                "%.2f",
                x == null ? 0 : x
        );
    }
}