package com.nivacreation.gpa_analyzer.model;

import java.util.ArrayList;

public class SubjectsNew {

    String Credit, Gpa, Grade, number, subjectCode, subjectName;

    ArrayList<String> GradeArray = new ArrayList<>();
    ArrayList<String> CreditArray = new ArrayList<>();
    ArrayList<String> GpaArray = new ArrayList<>();

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getGpa() {
        return Gpa;
    }

    public void setGpa(String gpa) {
        Gpa = gpa;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public ArrayList<String> getGradeArray() {

        GradeArray.add("A+");
        GradeArray.add("A");
        GradeArray.add("A-");
        GradeArray.add("B+");
        GradeArray.add("B");
        GradeArray.add("B-");
        GradeArray.add("C+");
        GradeArray.add("C");
        GradeArray.add("C-");
        GradeArray.add("D+");
        GradeArray.add("F");

        return GradeArray;
    }

    public ArrayList<String> getCreditArray() {

        CreditArray.add("3");
        CreditArray.add("2");
        CreditArray.add("1");
        CreditArray.add("4");
        CreditArray.add("5");
        CreditArray.add("6");
        CreditArray.add("7");
        CreditArray.add("8");
        CreditArray.add("9");

        return CreditArray;
    }

    public ArrayList<String> getGpaArray() {

        GpaArray.add("GPA");
        GpaArray.add("NGPA");

        return GpaArray;
    }
}
