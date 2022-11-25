package com.employee.data;

public class EmployeePayRollData {

    private String name;
    private int id;
    private double salary;

    public EmployeePayRollData(String name, int id, double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "EmployeePayRollData{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", salary=" + salary +
                '}';
    }
}
