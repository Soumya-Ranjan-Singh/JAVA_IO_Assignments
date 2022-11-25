package com.payroll.test;

import com.employee.data.EmployeePayRollData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayRollService {

    private List<EmployeePayRollData> employeePayRollDataList;

    public EmployeePayRollService(List<EmployeePayRollData> employeePayRollDataList) {
        this.employeePayRollDataList = employeePayRollDataList;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayRollData> employeePayRollList = new ArrayList<>();
        EmployeePayRollService employeePayRollService = new EmployeePayRollService(employeePayRollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayRollService.readEmployeePayRollData(consoleInputReader);
        employeePayRollService.writeEmployeePayRollData();
    }

    public void readEmployeePayRollData(Scanner consoleInputReader) {
        System.out.println("Enter the name of the Employee");
        String empName = consoleInputReader.next();
        System.out.println("Enter the ID of the Employee");
        int empID = consoleInputReader.nextInt();
        System.out.println("Enter the Salary of the Employee");
        double empSalary = consoleInputReader.nextDouble();
        employeePayRollDataList.add(new EmployeePayRollData(empName,empID,empSalary));
    }

    public void writeEmployeePayRollData() {
        System.out.println("\nWriting Employee Payroll Reader to Console\n"+employeePayRollDataList);
    }
}
