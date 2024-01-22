package Repository;

import account.Entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    @Modifying
    @Query("update Salary s set s.salary = :salary where s.employee = :employee and s.date = :date")
    void updateSalariesByEmployeeAndDate(@Param("employee") String employee,@Param("date") YearMonth date,@Param("salary") Long salary);

    @Query("select s from Salary s where s.employee = :employee order by s.date desc")
    List<Salary> findAllByEmployeeOrderByDateDateDesc(@Param("employee") String employee);

    Optional<Salary> findByEmployeeAndDate(String employee, YearMonth date);

}
