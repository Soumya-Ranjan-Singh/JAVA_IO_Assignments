package com.employee.data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class EmployeePayRollFILEIOService {
    public String payrollFileName = "payroll-file.txt";

    public List<EmployeePayRollData> readData() {
        List<EmployeePayRollData> employeePayRollDataList = new ArrayList<>();
        try {
            Files.readAllLines(new File(payrollFileName).toPath()).stream().map(String::trim)
                    .forEach(line -> {
                        String[] ch = line.split("=");
                        String name = ch[1].split("'")[1].trim();
                        int id = Integer.parseInt(ch[2].split(",")[0].trim());
                        double salary = Double.parseDouble(ch[3].split("}")[0].trim());
                        employeePayRollDataList.add(new EmployeePayRollData(name,id,salary));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeePayRollDataList;
    }

    public void writeData(List<EmployeePayRollData> employeePayRollDataList) {
        StringBuffer empBuffer = new StringBuffer();
        employeePayRollDataList.forEach(employee -> {
            String employeeDataString = employee.toString().concat("\n");
            empBuffer.append(employeeDataString);
        });
        try {
            Files.write(Paths.get(payrollFileName) , empBuffer.toString().getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printData() {
        try {
            //noinspection resource
            Files.lines(new File(payrollFileName).toPath()).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long countEntries() {
        long entries = 0;
        try {
            //noinspection resource
            entries = Files.lines(new File(payrollFileName).toPath()).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }
}
