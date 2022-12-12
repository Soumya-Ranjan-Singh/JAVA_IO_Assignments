package com.payroll.test;

import com.employee.data.EmployeeData;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.payroll.test.NewEmployeePayrollService.IOService.DB_IO;
import static org.junit.Assert.*;

public class NewEmployeePayrollServiceTest {
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        List<EmployeeData> employeeList = newEmployeePayrollService.readEmployeeData(DB_IO);
        assertEquals(3 , employeeList.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdatedShouldSyncWithDB() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        newEmployeePayrollService.readEmployeeData(DB_IO);
        newEmployeePayrollService.updateData("Terisa" , "300000.00","Salary");
        boolean result = newEmployeePayrollService.checkEmployeePayrollDetailsInSyncWithDB("Terisa");
        assertTrue(result);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrievedByAParticularDateRange_ShouldMatchTheRecordIn() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        newEmployeePayrollService.readEmployeeData(DB_IO);
        LocalDate startDate = LocalDate.of(2018,01,01);
        LocalDate endDate = LocalDate.now();
        List<EmployeeData> employeeData = newEmployeePayrollService.readEmployeeDataForDateRange(DB_IO,startDate,endDate);
        assertEquals(2,employeeData.size());
    }

    @Test
    public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        newEmployeePayrollService.readEmployeeData(DB_IO);
        String operation = "AVG";
        Map<String, Double> averageSalaryByGender = newEmployeePayrollService.readOperation_OnSalaryByGenderOnNewTable(DB_IO,operation);
        assertTrue(averageSalaryByGender.get("Male").equals(150000.00) &&
                averageSalaryByGender.get("Female").equals(200000.00));
    }

    @Test
    public void givenPayrollData_WhenSumOfSalaryRetrievedByGender_ShouldReturnProperValue() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        newEmployeePayrollService.readEmployeeData(DB_IO);
        String operation = "SUM";
        Map<String, Double> sumoOfSalaryByGender = newEmployeePayrollService.readOperation_OnSalaryByGenderOnNewTable(DB_IO,operation);
        assertTrue(sumoOfSalaryByGender.get("Male").equals(150000.00) &&
                sumoOfSalaryByGender.get("Female").equals(400000.00));
    }

    @Test
    public void givenPayrollData_WhenMaximumOfSalaryRetrievedByGender_ShouldReturnProperValue() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        newEmployeePayrollService.readEmployeeData(DB_IO);
        String operation = "MAX";
        Map<String, Double> maximumSalaryByGender = newEmployeePayrollService.readOperation_OnSalaryByGenderOnNewTable(DB_IO,operation);
        assertTrue(maximumSalaryByGender.get("Male").equals(150000.00) &&
                maximumSalaryByGender.get("Female").equals(300000.00));
    }

    @Test
    public void givenPayrollData_WhenMinimumOfSalaryRetrievedByGender_ShouldReturnProperValue() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        newEmployeePayrollService.readEmployeeData(DB_IO);
        String operation = "MIN";
        Map<String, Double> minimumSalaryByGender = newEmployeePayrollService.readOperation_OnSalaryByGenderOnNewTable(DB_IO,operation);
        assertTrue(minimumSalaryByGender.get("Male").equals(150000.00) &&
                minimumSalaryByGender.get("Female").equals(100000.00));
    }

    @Test
    public void givenPayrollData_WhenCountOfPersonsRetrievedByGender_ShouldReturnProperValue() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        newEmployeePayrollService.readEmployeeData(DB_IO);
        Map<String, Integer> countOfPersonsByGender = newEmployeePayrollService.readCountOfPersonsByGenderOnNewTable(DB_IO);
        assertTrue(countOfPersonsByGender.get("Male").equals(1) &&
                countOfPersonsByGender.get("Female").equals(2));
    }

    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        newEmployeePayrollService.readEmployeeData(DB_IO);
        newEmployeePayrollService.addEmployeeToPayroll("Mark", "Male", 200000, "8908525852", LocalDate.now(), "HR" );
        boolean result = newEmployeePayrollService.checkEmployeePayrollDetailsInSyncWithDB("Mark");
        assertTrue(result);
    }

    @Test
    public void givenRemoveEmployee_WhenUpdatedShouldSyncWithDB() {
        NewEmployeePayrollService newEmployeePayrollService = new NewEmployeePayrollService();
        List<EmployeeData> employeeList = newEmployeePayrollService.readEmployeeData(DB_IO);
        newEmployeePayrollService.removeData("Charlie" , false,"Is_Active");
        assertEquals(3 , employeeList.size());
    }
}
