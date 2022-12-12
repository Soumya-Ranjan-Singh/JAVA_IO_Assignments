package com.payroll.test;

import com.employee.data.EmployeeData;
import com.employee.data.NewEmployeePayrollDBIOService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.payroll.test.NewEmployeePayrollService.IOService.DB_IO;

public class NewEmployeePayrollService {

    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}

    private List<EmployeeData> employeeDataList;

    public NewEmployeePayrollDBIOService newEmployeePayrollDBIOService;

    public NewEmployeePayrollService() {
        newEmployeePayrollDBIOService = NewEmployeePayrollDBIOService.getInstance();
    }

    public NewEmployeePayrollService(ArrayList<EmployeeData> employeeDataList) {
        this();
        this.employeeDataList = employeeDataList;
    }

    public List<EmployeeData> readEmployeeData(IOService ioService) {
        this.employeeDataList = newEmployeePayrollDBIOService.readEmployeeData();
        return employeeDataList;
    }

    public void updateData(String name, String data, String columnName) {
        int result = newEmployeePayrollDBIOService.updateEmployeeData(name,data,columnName);
        if (result == 0) return;
        EmployeeData employeeData = this.getEmployeeData(name);
        if (employeeData != null)
            employeeData.setSalary(Double.parseDouble(data));
    }

    private EmployeeData getEmployeeData(String name) {
        return  this.employeeDataList.stream()
                .filter(employeeDataItem -> employeeDataItem.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkEmployeePayrollDetailsInSyncWithDB(String name) {
        employeeDataList = newEmployeePayrollDBIOService.getEmployeeData(name);
        return employeeDataList.get(0).equals(getEmployeeData(name));
    }

    public List<EmployeeData> readEmployeeDataForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
        if (ioService.equals(DB_IO))
        {
            return newEmployeePayrollDBIOService.getEmployeeDataForDateRange(startDate,endDate);
        }
        return null;
    }

    public Map<String, Double> readOperation_OnSalaryByGenderOnNewTable(IOService ioService, String operation) {
        if (ioService.equals(DB_IO))
            return newEmployeePayrollDBIOService.getOperationOnSalaryByGenderOnNewTable(operation);
        return null;
    }

    public Map<String, Integer> readCountOfPersonsByGenderOnNewTable(IOService ioService) {
        if (ioService.equals(DB_IO))
            return newEmployeePayrollDBIOService.getCountOfPersonsByGenderOnNewTable();
        return null;
    }

    public void addEmployeeToPayroll(String name, String gender, double salary, String phoneNo, LocalDate startDate, String department) {
        double deductions = salary * 0.2;
        double taxablePay = salary - deductions;
        double tax = taxablePay * 0.1;
        double netPay = salary - tax;
        employeeDataList.add(newEmployeePayrollDBIOService.addEmployeeToPayroll(name,gender,salary,phoneNo,startDate,
                department,deductions,taxablePay,tax,netPay));
    }

    public void removeData(String name, boolean data, String columnName) {
        int result = newEmployeePayrollDBIOService.removeEmployeeData(name,data,columnName);
        if (result == 0) return;
        EmployeeData employeeData = this.getEmployeeData(name);
        if (employeeData != null)
            employeeDataList.remove(employeeData);
    }

}
