package com.smarthealth.service;

import com.smarthealth.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class ExcelService {

    public byte[] appointments(List<Appointment> list) throws Exception {

        Workbook wb = new XSSFWorkbook();
        Sheet s = wb.createSheet("Appointments");

        String[] h = {
                "ID",
                "Patient",
                "Email",
                "Phone",
                "Symptoms",
                "Preferred Date",
                "Preferred Time",
                "Doctor",
                "Specialist",
                "Status"
        };

        row(s, 0, h);

        int r = 1;

        for (Appointment a : list) {

            row(
                    s,
                    r++,
                    String.valueOf(a.getId()),
                    a.getPatient().getName(),
                    a.getPatient().getEmail(),
                    a.getPatient().getPhone(),
                    a.getSymptoms(),
                    a.getPreferredDate(),
                    a.getPreferredTime(),
                    a.getDoctorName(),
                    a.getSpecialist(),
                    String.valueOf(a.getStatus())
            );
        }

        return bytes(wb);
    }

    public byte[] bills(List<Bill> list) throws Exception {

        Workbook wb = new XSSFWorkbook();
        Sheet s = wb.createSheet("Bills");

        row(
                s,
                0,
                "ID",
                "Bill Number",
                "Patient",
                "Email",
                "Hospital",
                "Subtotal",
                "Tax",
                "Total",
                "Created At"
        );

        int r = 1;

        for (Bill b : list) {

            row(
                    s,
                    r++,
                    String.valueOf(b.getId()),
                    b.getBillNumber(),
                    b.getPatient().getName(),
                    b.getPatient().getEmail(),
                    b.getHospital().getOrganizationName(),
                    String.valueOf(b.getSubtotal()),
                    String.valueOf(b.getTax()),
                    String.valueOf(b.getTotal()),
                    b.getCreatedAt()
            );
        }

        return bytes(wb);
    }

    public byte[] patients(List<User> list) throws Exception {

        Workbook wb = new XSSFWorkbook();
        Sheet s = wb.createSheet("Patients");

        row(
                s,
                0,
                "ID",
                "Name",
                "Email",
                "Phone",
                "Address",
                "Created At"
        );

        int r = 1;

        for (User u : list) {

            if (u.getRole() == Role.PATIENT) {

                row(
                        s,
                        r++,
                        String.valueOf(u.getId()),
                        u.getName(),
                        u.getEmail(),
                        u.getPhone(),
                        u.getAddress(),
                        u.getCreatedAt()
                );
            }
        }

        return bytes(wb);
    }

    private void row(
            Sheet s,
            int n,
            String... vals
    ) {

        Row r = s.createRow(n);

        for (int i = 0; i < vals.length; i++) {

            r.createCell(i).setCellValue(
                    vals[i] == null ? "" : vals[i]
            );
        }
    }

    private byte[] bytes(Workbook wb) throws Exception {

        ByteArrayOutputStream o =
                new ByteArrayOutputStream();

        wb.write(o);
        wb.close();

        return o.toByteArray();
    }
}