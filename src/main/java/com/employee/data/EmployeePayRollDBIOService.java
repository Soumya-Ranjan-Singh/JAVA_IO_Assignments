package com.employee.data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<EmployeePayRollData> readData() {
        String sqlQuery = "select * from employee_payroll";
        return this.getEmployeePayrollDataUsingDB(sqlQuery);
    }

    private List<EmployeePayRollData> getEmployeePayrollDataUsingDB(String sqlQuery) {
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

    public int updateEmployeeData(String name, double data, String columnName) {
        return this.updateEmployeeDataUsingStatement(name , data , columnName);
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

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sqlQuery = "select * from employee_payroll where name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        return this.getEmployeePayrollDataUsingDB(sqlQuery);
    }

    public List<EmployeePayRollData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) {
        String sqlQuery = String.format("select * from employee_payroll where start between '%s' and '%s';",
                Date.valueOf(startDate),Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sqlQuery);
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

    public Map<String, Double> getOperationOnSalaryByGender(String operation) {
        String sqlQuery = String.format("select gender, %s(salary) as %s_salary from employee_payroll group by gender;",operation,operation);
        Map<String, Double> genderToFormattedSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            while (result.next())
            {
                String gender = result.getString("gender");
                double salary = result.getDouble(operation+"_salary");
                genderToFormattedSalaryMap.put(gender,salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToFormattedSalaryMap;
    }

    public Map<String, Integer> getCountOfPersonsByGender() {
        String sqlQuery = "select gender, COUNT(id) as count_id from employee_payroll group by gender;";
        Map<String, Integer> genderToPersonCountMap = new HashMap<>();
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            while (result.next())
            {
                String gender = result.getString("gender");
                int count = result.getInt("count_id");
                genderToPersonCountMap.put(gender,count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToPersonCountMap;

    }

    public EmployeePayRollData addEmployeeToPayroll(String name, String phoneNumber, String address, String department, String gender, int salary,
                                                       int basicPay, int deduction, int taxablePay, int incomeTax, int netPay, LocalDate startDate) {
        int employeeID = -1;//If row increment automatically higher than the row count
        EmployeePayRollData employeePayRollData = null;
        String sqlQuery = String.format("insert into employee_payroll " +
                        "(name, phonenumber, address, department, gender, salary, basicpay, deduction, taxablepay, incometax, netpay, start) " +
                        "VALUES ( '%s', '%s', '%s', '%s', '%s','%s', '%s', '%s', '%s', '%s', '%s', '%s' )",name,phoneNumber,address,department,gender,
                salary,basicPay,deduction,taxablePay,incomeTax,netPay,startDate);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sqlQuery, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1)
            {
                ResultSet result = statement.getGeneratedKeys();
                if (result.next())
                    employeeID = result.getInt(1);
            }
            employeePayRollData = new EmployeePayRollData(name,employeeID,salary,startDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayRollData;
    }

}
