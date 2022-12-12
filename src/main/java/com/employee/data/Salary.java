package com.employee.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Salary {
    private double basePay;
    private double deductions;
    private double taxablePay;
    private double tax;
    private double netPay;

    public Salary(double basePay, double deductions, double taxablePay, double tax, double netPay) {
        this.basePay = basePay;
        this.deductions = deductions;
        this.taxablePay = taxablePay;
        this.tax = tax;
        this.netPay = netPay;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "basePay=" + basePay +
                ", deductions=" + deductions +
                ", taxablePay=" + taxablePay +
                ", tax=" + tax +
                ", netPay=" + netPay +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Salary salary)) return false;

        return new EqualsBuilder().append(basePay, salary.basePay).append(deductions, salary.deductions).append(taxablePay, salary.taxablePay).append(tax, salary.tax).append(netPay, salary.netPay).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(basePay).append(deductions).append(taxablePay).append(tax).append(netPay).toHashCode();
    }
}
