package com.payroll.test;

import com.employee.data.EmployeePayRollData;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.payroll.test.EmployeePayRollService.IOService.DB_IO;
import static com.payroll.test.EmployeePayRollService.IOService.FILE_IO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmployeePayRollServiceTest {

    @Test
    public void addSomeEmployeeHardCodedTest() {
        EmployeePayRollData[] arrayOfEmps = {
                new EmployeePayRollData("Soumya",1,30000),
                new EmployeePayRollData("Nirmal",2,28000),
                new EmployeePayRollData("Rajesh",3,31000)
        };
        EmployeePayRollService employeePayRollService = new EmployeePayRollService(Arrays.asList(arrayOfEmps));
        employeePayRollService.writeEmployeePayRollData(FILE_IO);
        employeePayRollService.printData(FILE_IO);
        long entries = employeePayRollService.countEntries(FILE_IO);
        assertEquals(3 , entries);
    }

    @Test
    public void givenFileOnReadingFromFileShouldMatchEmployeeCount() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        List<EmployeePayRollData> employeePayRollList = employeePayRollService.readEmployeePayRollData(FILE_IO);
        assertEquals(3 , employeePayRollList.size());
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        List<EmployeePayRollData> employeePayRollList = employeePayRollService.readEmployeePayRollData(DB_IO);
        assertEquals(5 , employeePayRollList.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdatedShouldSyncWithDB() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        employeePayRollService.readEmployeePayRollData(DB_IO);
        employeePayRollService.updateData("Terisa" , 3000000.00,"salary");
        boolean result = employeePayRollService.checkEmployeePayrollInSyncWithDB("Terisa");
        assertTrue(result);
    }

    @Test
    public void givenNewBasePayForEmployee_WhenUpdatedShouldSyncWithDB() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        employeePayRollService.readEmployeePayRollData(DB_IO);
        employeePayRollService.updateData("Terisa" , 3000000.00,"basicpay");
        boolean result = employeePayRollService.checkEmployeePayrollInSyncWithDB("Terisa");
        assertTrue(result);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrievedByAParticularDateRange_ShouldMatchTheRecord() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        List<EmployeePayRollData> list = employeePayRollService.retrieveDateRange(LocalDate.parse("2018-01-01") , LocalDate.parse("2021-12-01"));
        assertEquals(3 , list.size());
    }
    @Test
    public void givenEmployeePayrollInDB_WhenDoingSumOfSalaryAnalysis_ShouldMatchTheRecord() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        //noinspection rawtypes
        List list = employeePayRollService.retrieveData("SUM");
        assertEquals(2 , list.size());
        assertEquals(58000, (Double) list.get(0), 0.0);
        assertEquals(6032000, (Double) list.get(1), 0.0);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenDoingAvgOfSalaryAnalysis_ShouldMatchTheRecord() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        //noinspection rawtypes
        List list = employeePayRollService.retrieveData("AVG");
        assertEquals(2 , list.size());
        assertEquals(29000, (Double) list.get(0), 0.0);
        assertEquals(2010666.6666666667, (Double) list.get(1),0.0);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenDoingMinOfSalaryAnalysis_ShouldMatchTheRecord() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        //noinspection rawtypes
        List list = employeePayRollService.retrieveData("MIN");
        assertEquals(2 , list.size());
        assertEquals(28000, (Double) list.get(0), 0.0);
        assertEquals(32000, (Double) list.get(1),0.0);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenDoingMaxOfSalaryAnalysis_ShouldMatchTheRecord() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        //noinspection rawtypes
        List list = employeePayRollService.retrieveData("MAX");
        assertEquals(2 , list.size());
        assertEquals(30000, (Double) list.get(0), 0.0);
        assertEquals(3000000, (Double) list.get(1),0.0);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenDoingCountOfPersonsAnalysis_ShouldMatchTheRecord() {
        EmployeePayRollService employeePayRollService = new EmployeePayRollService();
        //noinspection rawtypes
        List list = employeePayRollService.retrieveData("COUNT");
        assertEquals(2 , list.size());
        assertEquals(2, (Double) list.get(0), 0.0);
        assertEquals(3, (Double) list.get(1),0.0);
    }
}