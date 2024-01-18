package account.Entity;

import account.Configuration.CustomSalarySerializer;
import account.Configuration.CustomYearMonthDeserializer;
import account.Configuration.CustomYearMonthSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Generated;
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
