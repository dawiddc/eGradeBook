package org.dawiddc.egradebook.model;

import org.mongodb.morphia.annotations.Entity;

@Entity("ids")
public class IdGenerator {
    private long studentIndex;
    private int courseId;
    private int gradeId;

    public IdGenerator() {
    }

    public IdGenerator(long studentId, int courseId, int gradeId) {
        this.studentIndex = studentId;
        this.courseId = courseId;
        this.gradeId = gradeId;
    }

    @Override
    public String toString() {
        return "IdGenerator{" +
                "studentIndex=" + studentIndex +
                ", courseId=" + courseId +
                ", gradeId=" + gradeId +
                '}';
    }

    public long getStudentIndex() {

        return studentIndex;
    }

    public void setStudentIndex(int studentIndex) {
        this.studentIndex = studentIndex;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }


}
