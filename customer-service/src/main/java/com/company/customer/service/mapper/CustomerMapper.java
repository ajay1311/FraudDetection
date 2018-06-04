package com.company.customer.service.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.company.customer.service.model.Customer;

import java.util.List;

@Mapper
public interface CustomerMapper {

    @Select("select * from customer")
    List<Customer> findAll();

    @Insert("insert into customer(ssn,first_name,last_name,dob,gender) values(#{ssn},#{first_name},#{last_name},#{dob},#{gender})")
    @SelectKey(statement = "SELECT ssn from customer where ssn=#{ssn}", keyProperty = "ssn",before = false, resultType = Integer.class)
    void insert(Customer customer);
    
    @Select("SELECT * FROM customer WHERE ssn = #{ssn}")
	public Customer getCustomerBySSN(long ssn);

	@Delete("DELETE FROM customer WHERE ssn = #{ssn}")
	public int deleteBySSN(long ssn);
	
	@Delete("UPDATE customer SET first_name=#{first_name},last_name=#{last_name},dob=#{dob},gender=#{gender} WHERE ssn = #{ssn}")
	public int updateBySSN(Customer customer);
}
