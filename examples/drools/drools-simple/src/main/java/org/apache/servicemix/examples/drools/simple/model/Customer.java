/**
 * 
 */
package org.apache.servicemix.examples.drools.simple.model;

/**
 * @author ghalajko
 *
 */
public class Customer {

    /**
     * Salary
     */
    private int salary;

    /**
     * IS VIP.
     */
    private CustomerType type;

    /**
     * Create customer with salary.
     * 
     * @param salary
     */
    public Customer(int salary) {
        this.salary = salary;
    }

    /**
     * @return the salary
     */
    public int getSalary() {
        return salary;
    }

    /**
     * @return the type
     */
    public CustomerType getType() {
        return type;
    }

    /**
     * @param salary
     *            the salary to set
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(CustomerType type) {
        this.type = type;
    }

    /**
     * 
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Customer [salary=").append(salary).append(", type=")
                .append(type).append("]");
        return builder.toString();
    }

}
