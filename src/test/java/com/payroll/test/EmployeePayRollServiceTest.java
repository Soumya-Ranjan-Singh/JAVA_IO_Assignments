package com.payroll.test;

import com.employee.data.EmployeePayRollData;
import org.junit.Test;
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
}