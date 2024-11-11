package seg3x02.employeeGql.resolvers

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository

@Controller
class EmployeesResolver(private val employeesRepository: EmployeesRepository,
                        private val mongoOperations: MongoOperations
) {

    @QueryMapping
    fun employees(): List<Employee> {
        return employeesRepository.findAll()
    }

    @QueryMapping
    fun employeeById(@Argument id: String): Employee? {
        return employeesRepository.findById(id).orElse(null)
    }

    @MutationMapping
    fun addEmployee(
        @Argument name: String,
        @Argument dateOfBirth: String,
        @Argument city: String,
        @Argument salary: Float,
        @Argument gender: String?,
        @Argument email: String?
    ): Employee {
        val newEmployee = Employee(
            name = name,
            dateOfBirth = dateOfBirth,
            city = city,
            salary = salary,
            gender = gender,
            email = email
        )
        return employeesRepository.save(newEmployee)
    }

    @MutationMapping
    fun updateEmployee(
        @Argument id: String,
        @Argument name: String?,
        @Argument dateOfBirth: String?,
        @Argument city: String?,
        @Argument salary: Float?,
        @Argument gender: String?,
        @Argument email: String?
    ): Employee? {
        val existingEmployee = employeesRepository.findById(id).orElse(null) ?: return null
        val updatedEmployee = existingEmployee.copy(
            name = name ?: existingEmployee.name,
            dateOfBirth = dateOfBirth ?: existingEmployee.dateOfBirth,
            city = city ?: existingEmployee.city,
            salary = salary ?: existingEmployee.salary,
            gender = gender ?: existingEmployee.gender,
            email = email ?: existingEmployee.email
        )
        return employeesRepository.save(updatedEmployee)
    }

    @MutationMapping
    fun deleteEmployee(@Argument id: String): Boolean {
        return if (employeesRepository.existsById(id)) {
            employeesRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}
