package com.employee.data;

import java.io.Serializable;

public class EmployeePayRollData implements Serializable {

    private final String name;
    private final int id;
    private final double salary;

    public EmployeePayRollData(String name, int id, double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "EmployeePayrollData{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", salary=" + salary +
                '}';
    }
}
