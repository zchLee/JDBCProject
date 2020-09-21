package com.lea.preparedstatement3.bean;

import java.lang.reflect.Type;

/**
 * @author lzc
 * @create 2020/09/20 下午 2:00
 */
public class Student {

    private Integer FlowID;

    private Integer Type;

    private String IDCard;

    private String ExamCard;

    private String StudentName;

    private String Location;

    private Integer Grade;

    public Student() {
    }



    public Integer getFlowID() {
        return FlowID;
    }

    public void setFlowID(Integer flowID) {
        FlowID = flowID;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return ExamCard;
    }

    public void setExamCard(String examCard) {
        ExamCard = examCard;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Integer getGrade() {
        return Grade;
    }

    public void setGrade(Integer grade) {
        Grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "FlowID=" + FlowID +
                ", Type=" + Type +
                ", IDCard='" + IDCard + '\'' +
                ", ExamCard='" + ExamCard + '\'' +
                ", StudentName='" + StudentName + '\'' +
                ", Location='" + Location + '\'' +
                ", Grade='" + Grade + '\'' +
                '}';
    }
}
