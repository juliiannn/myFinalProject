DROP TABLE IF EXISTS attendances;
DROP TABLE IF EXISTS grades;
DROP TABLE IF EXISTS schedules;
DROP TABLE IF EXISTS assignments;
DROP TABLE IF EXISTS course_subjects;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS coordinators;
DROP TABLE IF EXISTS rectors;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       document_type VARCHAR(255) NOT NULL CHECK (document_type IN ('TI','CC','CE','PASSPORT')),
                       document VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       full_name VARCHAR(255) NOT NULL,
                       phone VARCHAR(255) NOT NULL,
                       birth_date DATE NOT NULL,
                       gender VARCHAR(255) NOT NULL CHECK (gender IN ('MALE','FEMALE','OTHER')),
                       address VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL,
                       must_change_password BOOLEAN NOT NULL,
                       role VARCHAR(255) NOT NULL CHECK (role IN ('ADMIN','RECTOR','COORDINATOR','TEACHER','STUDENT')),
                       created_at TIMESTAMP NOT NULL
);

CREATE TABLE students (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          user_id INTEGER NOT NULL UNIQUE REFERENCES users(id),
                          guardian_name VARCHAR(255) NOT NULL,
                          guardian_phone VARCHAR(255) NOT NULL,
                          guardian_email VARCHAR(255) NOT NULL
);

CREATE TABLE teachers (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          user_id INTEGER NOT NULL UNIQUE REFERENCES users(id),
                          employee_code VARCHAR(255) NOT NULL UNIQUE,
                          hire_date DATE NOT NULL,
                          profession VARCHAR(255) NOT NULL,
                          specialty VARCHAR(255) NOT NULL,
                          education_level VARCHAR(255) NOT NULL CHECK (education_level IN ('BACHELOR','UNDERGRADUATE','SPECIALIST','MASTER','DOCTORATE')),
                          contract_type VARCHAR(255) NOT NULL CHECK (contract_type IN ('FULL_TIME','PART_TIME','HOURLY','TEMPORARY'))
);

CREATE TABLE coordinators (
                              id INTEGER PRIMARY KEY AUTOINCREMENT,
                              user_id INTEGER NOT NULL UNIQUE REFERENCES users(id),
                              employee_code VARCHAR(255) NOT NULL UNIQUE,
                              area VARCHAR(255) NOT NULL CHECK (area IN ('ACADEMIC','DISCIPLINE','ADMINISTRATIVE')),
                              assignment_date DATE NOT NULL,
                              education_level VARCHAR(255) NOT NULL CHECK (education_level IN ('BACHELOR','UNDERGRADUATE','SPECIALIST','MASTER','DOCTORATE'))
);

CREATE TABLE rectors (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         user_id INTEGER NOT NULL UNIQUE REFERENCES users(id),
                         employee_code VARCHAR(255) NOT NULL UNIQUE,
                         appointment_date DATE NOT NULL,
                         administrative_period VARCHAR(255) NOT NULL,
                         education_level VARCHAR(255) NOT NULL CHECK (education_level IN ('BACHELOR','UNDERGRADUATE','SPECIALIST','MASTER','DOCTORATE'))
);

CREATE TABLE courses (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         name VARCHAR(255) NOT NULL,
                         grade VARCHAR(255) NOT NULL,
                         classroom VARCHAR(255) NOT NULL,
                         school_year INTEGER NOT NULL,
                         director_teacher_id INTEGER REFERENCES teachers(id),
                         active BOOLEAN NOT NULL
);

CREATE TABLE subjects (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          active BOOLEAN NOT NULL
);

CREATE TABLE course_subjects (
                                 id INTEGER PRIMARY KEY AUTOINCREMENT,
                                 course_id INTEGER NOT NULL REFERENCES courses(id),
                                 subject_id INTEGER NOT NULL REFERENCES subjects(id),
                                 teacher_id INTEGER NOT NULL REFERENCES teachers(id),
                                 active BOOLEAN NOT NULL
);

CREATE TABLE enrollments (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             student_id INTEGER NOT NULL REFERENCES students(id),
                             course_id INTEGER NOT NULL REFERENCES courses(id),
                             enrollment_date DATE NOT NULL,
                             status VARCHAR(255) NOT NULL CHECK (status IN ('ACTIVE','CANCELLED','COMPLETED'))
);

CREATE TABLE grades (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        student_id INTEGER NOT NULL REFERENCES students(id),
                        course_subject_id INTEGER NOT NULL REFERENCES course_subjects(id),
                        period VARCHAR(255) NOT NULL CHECK (period IN ('PERIOD_1','PERIOD_2','PERIOD_3','PERIOD_4')),
                        value NUMERIC(3,1) NOT NULL,
                        UNIQUE (student_id, course_subject_id, period)
);

CREATE TABLE attendances (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             student_id INTEGER NOT NULL REFERENCES students(id),
                             course_subject_id INTEGER NOT NULL REFERENCES course_subjects(id),
                             date DATE NOT NULL,
                             status VARCHAR(255) NOT NULL CHECK (status IN ('PRESENT','ABSENT','LATE')),
                             UNIQUE (student_id, course_subject_id, date)
);

CREATE TABLE schedules (
                           id INTEGER PRIMARY KEY AUTOINCREMENT,
                           course_subject_id INTEGER NOT NULL REFERENCES course_subjects(id),
                           day_of_week VARCHAR(255) NOT NULL CHECK (day_of_week IN ('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY')),
                           start_time TIME NOT NULL,
                           end_time TIME NOT NULL
);

CREATE TABLE assignments (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             course_subject_id INTEGER NOT NULL REFERENCES course_subjects(id),
                             title VARCHAR(255) NOT NULL,
                             description TEXT NOT NULL,
                             due_date DATE NOT NULL,
                             created_at TIMESTAMP NOT NULL
);