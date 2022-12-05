package com.payroll.test;

import com.employee.data.EmployeePayRollDBIOService;
import com.employee.data.EmployeePayRollData;
import com.employee.data.EmployeePayRollFILEIOService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.payroll.test.EmployeePayRollService.IOService.*;

public class EmployeePayRollService {

    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}

    private List<EmployeePayRollData> employeePayRollDataList;

    public EmployeePayRollDBIOService employeePayRollDBIOService;
    public EmployeePayRollService() {
        employeePayRollDBIOService = EmployeePayRollDBIOService.getInstance();
    }

    public EmployeePayRollService(List<EmployeePayRollData> employeePayRollDataList) {
        this();
        this.employeePayRollDataList = employeePayRollDataList;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayRollData> employeePayRollList = new ArrayList<>();
        EmployeePayRollService employeePayRollService = new EmployeePayRollService(employeePayRollList);
        employeePayRollService.readEmployeePayRollData(CONSOLE_IO);
        employeePayRollService.writeEmployeePayRollData(CONSOLE_IO);
    }

    public List<EmployeePayRollData> readEmployeePayRollData(IOService ioService) {
        if (ioService.equals(CONSOLE_IO))
        {
            Scanner consoleInputReader = new Scanner(System.in);
            System.out.println("Enter the name of the Employee");
            String empName = consoleInputReader.next();
            System.out.println("Enter the ID of the Employee");
            int empID = consoleInputReader.nextInt();
            System.out.println("Enter the Salary of the Employee");
            double empSalary = consoleInputReader.nextDouble();
            employeePayRollDataList.add(new EmployeePayRollData(empName, empID, empSalary));
            return employeePayRollDataList;
        }
        else if (ioService.equals(FILE_IO)) {
            this.employeePayRollDataList = new EmployeePayRollFILEIOService().readData();
            return employeePayRollDataList;
        }
        else if (ioService.equals(DB_IO))
        {
            this.employeePayRollDataList = employeePayRollDBIOService.readData();
            return employeePayRollDataList;
        }
        return employeePayRollDataList;
    }

    public void writeEmployeePayRollData(IOService ioService) {
        if (ioService.equals(CONSOLE_IO))
            System.out.println("\nWriting Employee Payroll Reader to Console\n"+employeePayRollDataList);
        else if (ioService.equals(FILE_IO))
            new EmployeePayRollFILEIOService().writeData(employeePayRollDataList);
    }

    public void printData(IOService ioService) {
        if (ioService.equals(FILE_IO))
            new EmployeePayRollFILEIOService().printData();
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(FILE_IO))
            return new EmployeePayRollFILEIOService().countEntries();
        return 0;
    }

    public void updateData(String name, double data, String columnName) {
        int result = employeePayRollDBIOService.updateEmployeeData(name,data,columnName);
        if (result == 0) return;
        EmployeePayRollData employeePayRollData = this.getEmployeePayrollData(name);
        if (columnName.equals("salary")) {
            if (employeePayRollData != null)
                employeePayRollData.salary = data;
        }
        else if (columnName.equals("basicpay")) {
            if (employeePayRollData != null)
                employeePayRollData.name = name;
        }
    }

    private EmployeePayRollData getEmployeePayrollData(String name) {
        return  this.employeePayRollDataList.stream()
                .filter(employeePayRollDataItem -> employeePayRollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayRollData> employeePayRollDataList = employeePayRollDBIOService.getEmployeePayrollData(name);
        return employeePayRollDataList.get(0).equals(getEmployeePayrollData(name));
    }

}
