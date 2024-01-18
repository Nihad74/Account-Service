package com.accountservice.Entity;

import com.accountservice.Configuration.CustomSalarySerializer;
import com.accountservice.Configuration.CustomYearMonthDeserializer;
import com.accountservice.Configuration.CustomYearMonthSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

@JsonPropertyOrder({"employee", "period", "salary"})
@Entity
@Getter
@Setter
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;


    @NotEmpty
    private String employee;

    @NotNull
    @JsonSerialize(using = CustomYearMonthSerializer.class)
    @JsonDeserialize(using = CustomYearMonthDeserializer.class)
    @DateTimeFormat(pattern = "MM-yyyy")
    @JsonProperty("period")
    private YearMonth date;

    @NotNull
    @PositiveOrZero(message = "Salary must be positive or zero!")
    @JsonSerialize(using = CustomSalarySerializer.class)
    private Long salary;

    @ManyToOne
    @JoinColumn(name ="employee", referencedColumnName = "email", insertable = false, updatable = false)
    @JsonIgnore
    private User user;

    public String toString(){
        return "Employee: " + employee + " Period: " + date + " Salary: " + salary;
    }

}
