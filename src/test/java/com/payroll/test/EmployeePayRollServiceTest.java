package com.payroll.test;

import com.employee.data.EmployeePayRollData;
import org.junit.Test;

import java.util.Arrays;

import static com.payroll.test.EmployeePayRollService.IOService.FILE_IO;
import static org.junit.Assert.assertEquals;

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
}
