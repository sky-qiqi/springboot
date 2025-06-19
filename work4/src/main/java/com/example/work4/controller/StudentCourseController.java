package com.example.work4.controller; // 包名

import com.example.work4.entity.Course; // 导入 Course 实体
import com.example.work4.entity.Student; // 导入 Student 实体
import com.example.work4.entity.StudentCourse; // 导入 StudentCourse 实体
import com.example.work4.entity.StudentCourseId; // 导入 StudentCourseId 实体
import com.example.work4.repository.CourseRepository; // 导入 CourseRepository
import com.example.work4.repository.StudentRepository; // 导入 StudentRepository
import com.example.work4.repository.StudentCourseRepository; // 导入 StudentCourseRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // 导入 HttpStatus
import org.springframework.http.ResponseEntity; // 导入 ResponseEntity
import org.springframework.web.bind.annotation.*; // 导入所有 Web 注解

import java.time.LocalDate; // 导入 LocalDate
import java.util.List; // 导入 List
import java.util.Optional; // 导入 Optional

// 确保您已经在 StudentCourse.java 实体类的 student 和 course 字段上添加了 @JsonIgnore 注解，
// 以避免 JSON 序列化时的循环引用问题。这是非常重要的。

@RestController // 标记这是一个 REST 控制器
@RequestMapping("/api/enrollments") // 设置这个控制器处理的基础 URL 路径
public class StudentCourseController {

    @Autowired // 注入 StudentCourseRepository
    private StudentCourseRepository studentCourseRepository;

    @Autowired // 注入 StudentRepository
    private StudentRepository studentRepository;

    @Autowired // 注入 CourseRepository
    private CourseRepository courseRepository;


    // --- StudentCourse (Enrollment) CRUD Operations ---

    // 注册学生选课 (创建 StudentCourse 记录) - 包含了解决 NonUniqueObjectException 的修改
    // POST http://localhost:8080/api/enrollments
    // Request Body Example: { "id": { "studentId": 1, "courseId": 101 }, "enrollmentDate": "2023-09-01" }
    @PostMapping
    public ResponseEntity<StudentCourse> createEnrollment(@RequestBody StudentCourse enrollmentDetails) {
        // 1. 检查请求体中是否提供了学生 ID 和课程 ID
        if (enrollmentDetails.getId() == null || enrollmentDetails.getId().getStudentId() == null || enrollmentDetails.getId().getCourseId() == null) {
            return ResponseEntity.badRequest().body(null); // 返回 400 Bad Request 如果 ID 缺失
        }

        Integer studentId = enrollmentDetails.getId().getStudentId();
        Integer courseId = enrollmentDetails.getId().getCourseId();

        // 2. 查找关联的 Student 和 Course 实体
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        // 如果学生或课程不存在，返回 404 Not Found
        if (!studentOptional.isPresent() || !courseOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOptional.get();
        Course course = courseOptional.get();

        // 3. 检查是否已经存在该选课记录，避免重复创建
        StudentCourseId existingId = new StudentCourseId(studentId, courseId);
        Optional<StudentCourse> existingEnrollmentOptional = studentCourseRepository.findById(existingId);

        if (existingEnrollmentOptional.isPresent()) {
            // 如果已存在，返回 409 Conflict，并可以返回已存在的记录
            return ResponseEntity.status(HttpStatus.CONFLICT).body(existingEnrollmentOptional.get());
        }

        // 4. 创建新的 StudentCourse 实体 (此时 newEnrollment 还是游离态 detached)
        // 构造函数会设置 Student 和 Course 的 ManyToOne 关联以及 EmbeddedId
        StudentCourse newEnrollment = new StudentCourse(student, course, enrollmentDetails.getEnrollmentDate());


        // *** 解决 NonUniqueObjectException 的关键修改点：不再在这里显式地将 newEnrollment 添加到 student 和 course 的集合中 ***
        // 这是为了避免在保存 StudentCourse 之前，它通过其他托管实体被 Hibernate 提前注册，从而导致 NonUniqueObjectException
        // student.addStudentCourse(newEnrollment); // <-- 删除或注释掉这行
        // course.addStudentCourse(newEnrollment); // <-- 删除或注释掉这行


        // 5. 直接保存 StudentCourse 实体本身
        // 这应该能够成功，因为 newEnrollment 在保存前没有被添加到其他托管实体的集合中
        // JPA 会根据 StudentCourse 作为 owning side 来处理数据库中的外键和 join table 记录
        StudentCourse savedEnrollment = studentCourseRepository.save(newEnrollment);


        // 返回创建成功的记录和 201 状态码。返回 savedEnrollment，它是持久态的。
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEnrollment);
    }


    // 根据学生 ID 获取其所有选课记录
    // GET http://localhost:8080/api/enrollments/by-student/{studentId}
    @GetMapping("/by-student/{studentId}")
    public ResponseEntity<List<StudentCourse>> getEnrollmentsByStudent(@PathVariable Integer studentId) {
        // 可以先检查学生是否存在，以返回更准确的 404 而不是空列表 (取决于业务需求)
        if (!studentRepository.existsById(studentId)) {
            return ResponseEntity.notFound().build(); // 学生不存在
        }
        // 使用 Spring Data JPA 自动生成的方法根据 Student ID 查找关联的 StudentCourse 记录
        List<StudentCourse> enrollments = studentCourseRepository.findByStudent_StudentId(studentId);
        return ResponseEntity.ok(enrollments); // 返回选课记录列表和 200 状态码
    }

    // 根据课程 ID 获取所有选课记录
    // GET http://localhost:8080/api/enrollments/by-course/{courseId}
    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<List<StudentCourse>> getEnrollmentsByCourse(@PathVariable Integer courseId) {
        // 可以先检查课程是否存在
        if (!courseRepository.existsById(courseId)) {
            return ResponseEntity.notFound().build(); // 课程不存在
        }
        // 使用 Spring Data JPA 自动生成的方法根据 Course ID 查找关联的 StudentCourse 记录
        List<StudentCourse> enrollments = studentCourseRepository.findByCourse_CourseId(courseId);
        return ResponseEntity.ok(enrollments); // 返回选课记录列表和 200 状态码
    }

    // 根据学生 ID 和课程 ID 获取特定的选课记录 (复合主键)
    // GET http://localhost:8080/api/enrollments/{studentId}/{courseId}
    @GetMapping("/{studentId}/{courseId}")
    public ResponseEntity<StudentCourse> getEnrollmentById(@PathVariable Integer studentId, @PathVariable Integer courseId) {
        // 创建 StudentCourseId 复合主键对象
        StudentCourseId id = new StudentCourseId(studentId, courseId);
        // 使用 findById 根据复合主键查找 StudentCourse 实体
        Optional<StudentCourse> enrollment = studentCourseRepository.findById(id);
        return enrollment.map(ResponseEntity::ok) // 如果找到，返回 200 OK 和记录
                .orElseGet(() -> ResponseEntity.notFound().build()); // 如果未找到，返回 404 Not Found
    }


    // 更新特定选课记录 (例如修改选课日期)
    // PUT http://localhost:8080/api/enrollments/{studentId}/{courseId}
    // Request Body Example: { "enrollmentDate": "新的日期" }
    @PutMapping("/{studentId}/{courseId}")
    public ResponseEntity<StudentCourse> updateEnrollmentDate(@PathVariable Integer studentId, @PathVariable Integer courseId, @RequestBody StudentCourse enrollmentDetails) {
        // 创建 StudentCourseId 复合主键对象
        StudentCourseId id = new StudentCourseId(studentId, courseId);
        // 查找要更新的现有选课记录
        Optional<StudentCourse> enrollmentOptional = studentCourseRepository.findById(id);

        if (enrollmentOptional.isPresent()) {
            StudentCourse enrollment = enrollmentOptional.get(); // 获取现有实体

            // 只更新允许更新的字段，例如 enrollmentDate
            // 注意：通常我们不会通过 PUT 来修改学生 ID 或课程 ID，那相当于创建新的选课记录
            enrollment.setEnrollmentDate(enrollmentDetails.getEnrollmentDate());

            // 保存更新后的 StudentCourse 实体
            StudentCourse updatedEnrollment = studentCourseRepository.save(enrollment);
            return ResponseEntity.ok(updatedEnrollment); // 返回 200 OK 和更新后的记录
        } else {
            // 未找到要更新的记录
            return ResponseEntity.notFound().build(); // 返回 404 Not Found
        }
    }

    // 删除特定选课记录 (例如学生退课) - 包含了解决 500 错误的修改
    // DELETE http://localhost:8080/api/enrollments/{studentId}/{courseId}
    @DeleteMapping("/{studentId}/{courseId}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Integer studentId, @PathVariable Integer courseId) {
        // 创建 StudentCourseId 复合主键对象
        StudentCourseId id = new StudentCourseId(studentId, courseId);
        // 查找要删除的选课记录
        Optional<StudentCourse> enrollmentOptional = studentCourseRepository.findById(id);

        if (enrollmentOptional.isPresent()) {
            // *** 解决 500 错误的修改点：移除在调用 deleteById 前，手动断开关联的代码 ***
            // 之前代码：
            // StudentCourse enrollment = enrollmentOptional.get(); // 这一行现在不需要了，因为我们不再使用 enrollment 对象的引用
            // if (enrollment.getStudent() != null) {
            //     enrollment.getStudent().removeStudentCourse(enrollment); // 使用辅助方法
            // }
            // if (enrollment.getCourse() != null) {
            //     enrollment.getCourse().removeStudentCourse(enrollment); // 使用辅助方法
            // }
            // 直接根据复合主键删除 StudentCourse 实体
            // JPA/Hibernate 会处理数据库行的删除和关系的解除。
            studentCourseRepository.deleteById(id);

            return ResponseEntity.noContent().build(); // 返回 204 No Content 表示成功删除且无返回体
        } else {
            // 未找到要删除的记录
            return ResponseEntity.notFound().build(); // 返回 404 Not Found
        }
    }
}