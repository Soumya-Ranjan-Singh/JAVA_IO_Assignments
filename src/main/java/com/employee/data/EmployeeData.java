package com.employee.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.util.List;

public class EmployeeData {
    private final int id;
    private final String name;
    private double salary;
    private final LocalDate startDate;
    private String department;
    private List<Salary> payrollList;

    public EmployeeData(int id, String name, double salary, LocalDate startDate, String department, List<Salary> payrollList) {
        this(id, name, salary, startDate);
        this.department = department;
        this.payrollList = payrollList;
    }

    public EmployeeData(int id, String name, double salary, LocalDate startDate) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "EmployeeData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", startDate=" + startDate +
                ", department='" + department + '\'' +
                ", payrollList=" + payrollList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EmployeeData that)) return false;

        return new EqualsBuilder().append(id, that.id).append(salary, that.salary).append(name, that.name).append(startDate, that.startDate).append(department, that.department).append(payrollList, that.payrollList).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(salary).append(startDate).append(department).append(payrollList).toHashCode();
    }
}
