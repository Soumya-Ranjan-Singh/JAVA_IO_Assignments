package com.employee.data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayRollDBIOService {

    private  PreparedStatement employeePayrollDataStatement;

    private static EmployeePayRollDBIOService employeePayRollDBIOService;

    private EmployeePayRollDBIOService() {
    }

    public static EmployeePayRollDBIOService getInstance() {
        if (employeePayRollDBIOService == null)
            employeePayRollDBIOService = new EmployeePayRollDBIOService();
        return employeePayRollDBIOService;
    }
    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service";
        String userName = "root";
        String password = "Soumya@42558";
        Connection con;
        System.out.println("Connecting to database: "+jdbcURL);
        con = DriverManager.getConnection(jdbcURL,userName,password);
        System.out.println("Connection is successful!!"+con);
        return con;
    }

    private List<EmployeePayRollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayRollData> employeePayRollDataList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayRollDataList.add(new EmployeePayRollData(name,id,salary,startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayRollDataList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sqlQuery = "select * from employee_payroll where name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<EmployeePayRollData> readData() {
        List<EmployeePayRollData> employeePayRollDataList = new ArrayList<>();
        String sqlQuery = "select * from employee_payroll";
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            employeePayRollDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayRollDataList;
    }

    private int updateEmployeeDataUsingStatement(String name, double data, String columnName) {
        String sqlQuery = String.format("update employee_payroll set %s = %.2f where name = '%s';",columnName,data,name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int updateEmployeeData(String name, double data, String columnName) {
        return this.updateEmployeeDataUsingStatement(name , data , columnName);
    }

    public List<EmployeePayRollData> getEmployeePayrollData(String name) {
        List<EmployeePayRollData> employeePayRollList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1 , name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayRollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayRollList;
    }

    public List<EmployeePayRollData> retrieveEmployeeData(LocalDate fromDate, LocalDate toDate) {
        String sqlQuery = "select * from Employee_Payroll where start between cast('"+fromDate+"' as DATE) and DATE('"+toDate+"')";
        List<EmployeePayRollData> employeePayRollDataList = new ArrayList<>();
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            employeePayRollDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayRollDataList;
    }

    public List<?> getData(String operations) {
        String sqlQuery;
        if (operations.equals("COUNT"))
            sqlQuery = String.format("select %s(id) from employee_payroll GROUP BY gender;",operations);
        else
            sqlQuery = String.format("select %s(salary) from employee_payroll GROUP BY gender;",operations);
        List<Double> dataList = new ArrayList<>();
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next())
            {
                double data = resultSet.getDouble(1);
                dataList.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
