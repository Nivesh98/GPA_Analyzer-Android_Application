package com.nivacreation.gpa_analyzer.model;

import java.util.ArrayList;

public class Subjects {

    String number, getMethodGrade, getMethodCredit, getMethodGPA, subjectName, subjectCode;



    ArrayList<String> Grade = new ArrayList<>();
    ArrayList<String> Credit = new ArrayList<>();
    ArrayList<String> Gpa = new ArrayList<>();

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getGetMethodGrade() {
        return getMethodGrade;
    }

    public void setGetMethodGrade(String getMethodGrade) {
        this.getMethodGrade = getMethodGrade;
    }

    public String getGetMethodCredit() {
        return getMethodCredit;
    }

    public void setGetMethodCredit(String getMethodCredit) {
        this.getMethodCredit = getMethodCredit;
    }

    public String getGetMethodGPA() {
        return getMethodGPA;
    }

    public void setGetMethodGPA(String getMethodGPA) {
        this.getMethodGPA = getMethodGPA;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<String> getGrade() {

        Grade.add("A+");
        Grade.add("A");
        Grade.add("A-");
        Grade.add("B+");
        Grade.add("B");
        Grade.add("B-");
        Grade.add("C+");
        Grade.add("C");
        Grade.add("C-");
        Grade.add("D+");
        Grade.add("F");

        return Grade;
    }

    public void setGrade(ArrayList<String> grade) {
        this.Grade = grade;
    }

    public ArrayList<String> getCredit() {

        Credit.add("3");
        Credit.add("2");
        Credit.add("1");
        Credit.add("4");
        Credit.add("5");
        Credit.add("6");
        Credit.add("7");
        Credit.add("8");
        Credit.add("9");

        return Credit;
    }

    public void setCredit(ArrayList<String> credit) {
        this.Credit = credit;
    }

    public ArrayList<String> getGpa() {

        Gpa.add("GPA");
        Gpa.add("NGPA");

        return Gpa;
    }

    public void setGpa(ArrayList<String> gpa) {
        this.Gpa = gpa;
    }

   // public void get
}
