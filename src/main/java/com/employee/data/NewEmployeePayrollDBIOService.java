package com.employee.data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewEmployeePayrollDBIOService {
    private PreparedStatement employeeDataStatement;
    private static NewEmployeePayrollDBIOService newEmployeePayrollDBIOService;

    private NewEmployeePayrollDBIOService() {
    }

    public static NewEmployeePayrollDBIOService getInstance() {
        if (newEmployeePayrollDBIOService == null)
            newEmployeePayrollDBIOService = new NewEmployeePayrollDBIOService();
        return newEmployeePayrollDBIOService;
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

    public List<EmployeeData> readEmployeeData() {
        String sqlQuery = "select * from EmployeeDetails";
        return this.getEmployeeDataUsingDB(sqlQuery);
    }

    private List<EmployeeData> getEmployeeDataUsingDB(String sqlQuery) {
        List<EmployeeData> employeeDataList = new ArrayList<>();
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            employeeDataList = this.getEmployeeData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeDataList;
    }

    private List<EmployeeData> getEmployeeData(ResultSet resultSet) {
        List<EmployeeData> employeeDataList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                double salary = resultSet.getDouble("Salary");
                LocalDate startDate = resultSet.getDate("Start").toLocalDate();
                employeeDataList.add(new EmployeeData(id, name,salary,startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeDataList;
    }

    public List<EmployeeData> getEmployeeData(String name) {
        List<EmployeeData> employeeList = null;
        if (this.employeeDataStatement == null)
            this.prepareStatementForAllData();
        try {
            employeeDataStatement.setString(1 , name);
            ResultSet resultSet = employeeDataStatement.executeQuery();
            employeeList = this.getEmployeeData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    private void prepareStatementForAllData() {
        try {
            Connection connection = this.getConnection();
            String sqlQuery = "select * from EmployeeDetails where name = ?";
            employeeDataStatement = connection.prepareStatement(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateEmployeeData(String name, double data, String columnName) {
        return this.updateEmployeeDataUsingStatement(name , data , columnName);
    }

    private int updateEmployeeDataUsingStatement(String name, double data, String columnName) {
        String sqlQuery = String.format("update EmployeeDetails set %s = %.2f where name = '%s';",columnName,data,name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeeData> getEmployeeDataForDateRange(LocalDate startDate, LocalDate endDate) {
        String sqlQuery = String.format("select * from EmployeeDetails where start between '%s' and '%s';",
                Date.valueOf(startDate),Date.valueOf(endDate));
        return this.getEmployeeDataUsingDB(sqlQuery);
    }

    public Map<String, Double> getOperationOnSalaryByGenderOnNewTable(String operation) {
        String sqlQuery = String.format("select Gender, %s(Salary) as %s_Salary from EmployeeDetails group by Gender;",operation,operation);
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

    public Map<String, Integer> getCountOfPersonsByGenderOnNewTable() {
        String sqlQuery = "select Gender, COUNT(ID) as count_id from EmployeeDetails group by Gender;";
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

    public EmployeeData addEmployeeToPayroll(String name, String gender, double salary, String phoneNo, LocalDate startDate,
                                             String department, double deductions, double taxablepay, double tax, double netpay) {
        int employeeID = -1;//If row increment automatically higher than the row count
        int departmentID = -1;
        Connection connection = null;
        EmployeeData employeeData = null;
        Salary salaryData = null;
        try {
            connection = this.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection != null ? connection.createStatement() : null) {
            String sqlQuery = String.format("insert into EmployeeDetails " +
                    "(Name, Gender, Salary, PhoneNo, Start) " +
                    "VALUES ( '%s', '%s', '%s', '%s', '%s' )",name,gender,salary,phoneNo,startDate);
            int rowAffected = statement != null ? statement.executeUpdate(sqlQuery, statement.RETURN_GENERATED_KEYS) : 0;
            if (rowAffected == 1) {
                ResultSet result = statement.getGeneratedKeys();
                if (result.next())
                    employeeID = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
                return employeeData;
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }
        try (Statement statement = connection != null ? connection.createStatement() : null) {
            String sql = String.format("insert into PayrollDetails " +
                    "( Employee_id, Basic_pay, Deductions, Taxable_pay, Tax, Net_pay)" +
                    "Values ( '%s', '%s', '%s', '%s', '%s', '%s' )",employeeID,salary,deductions,taxablepay,tax,netpay);
            int rowAffected = 0;
            if (statement != null) {
                rowAffected = statement.executeUpdate(sql);
            }
            if (rowAffected == 1) {
                salaryData = new Salary(salary,deductions,taxablepay,tax,netpay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }
        try (Statement statement = connection != null ? connection.createStatement() : null) {
            String sql = String.format("insert into DepartmentDetails ( Departname ) Values ( '%s' )",department);
            int rowAffected = 0;
            if (statement != null) {
                rowAffected = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
            }
            if (rowAffected == 1) {
                //noinspection ConstantConditions
                List payroll = new ArrayList<>();
                payroll.add(salary);
                employeeData = new EmployeeData(employeeID,name,salary,startDate,department, payroll);
                ResultSet result = statement.getGeneratedKeys();
                if (result.next())
                    departmentID = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = String.format("insert into EmployeeDepartmentRelation ( Employee_id, Department_id ) " +
                        "Values ( '%s', '%s' )", employeeID,departmentID);
                int rowAffected = statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = String.format("insert into CompanyDetails ( Employee_id ) " +
                        "Values ( '%s' )", employeeID);
                int rowAffected = statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            if (connection != null) {
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //noinspection ThrowFromFinallyBlock
                    throw new RuntimeException(e);
                }
            }
        }
        return employeeData;
    }
}
