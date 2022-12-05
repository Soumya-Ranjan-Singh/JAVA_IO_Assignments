package com.employee.data;

import java.io.Serializable;
import java.time.LocalDate;

public class EmployeePayRollData implements Serializable {

    public String name;
    private final int id;
    public double salary;
    public LocalDate startDate;

    public EmployeePayRollData(String name, int id, double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    public EmployeePayRollData(String name, int id, double salary, LocalDate startDate) {
        this(name,id,salary);
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "EmployeePayrollData{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayRollData that = (EmployeePayRollData) o;

        return id == that.id &&
                Double.compare(that.salary , salary) == 0
                && name.equals(that.name);
    }
}
