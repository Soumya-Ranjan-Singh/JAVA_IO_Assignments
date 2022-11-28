package com.payroll.test;

import com.employee.data.EmployeePayRollData;
import com.employee.data.EmployeePayRollFILEIOService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.payroll.test.EmployeePayRollService.IOService.CONSOLE_IO;
import static com.payroll.test.EmployeePayRollService.IOService.FILE_IO;

public class EmployeePayRollService {
    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}

    private List<EmployeePayRollData> employeePayRollDataList;

    public EmployeePayRollService() {
    }

    public EmployeePayRollService(List<EmployeePayRollData> employeePayRollDataList) {
        this.employeePayRollDataList = employeePayRollDataList;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayRollData> employeePayRollList = new ArrayList<>();
        EmployeePayRollService employeePayRollService = new EmployeePayRollService(employeePayRollList);
        employeePayRollService.readEmployeePayRollData(CONSOLE_IO);
        employeePayRollService.writeEmployeePayRollData(CONSOLE_IO);
    }

    public long readEmployeePayRollData(IOService ioService) {
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
            return employeePayRollDataList.size();
        }
        else if (ioService.equals(FILE_IO)) {
            this.employeePayRollDataList = new EmployeePayRollFILEIOService().readData();
            return employeePayRollDataList.size();
        }
        return employeePayRollDataList.size();
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
}
