package com.nivacreation.gpa_analyzer.model;

import java.util.ArrayList;

public class Subjects {

    String number, getMethodGrade, getMethodCredit, getMethodGPA;

    ArrayList<String> grade = new ArrayList<>();
    ArrayList<String> credit = new ArrayList<>();
    ArrayList<String> gpa = new ArrayList<>();

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

        grade.add("A+");
        grade.add("A");
        grade.add("A-");
        grade.add("B+");
        grade.add("B");
        grade.add("B-");
        grade.add("C+");
        grade.add("C");
        grade.add("C-");
        grade.add("D");
        grade.add("F");

        return grade;
    }

    public void setGrade(ArrayList<String> grade) {
        this.grade = grade;
    }

    public ArrayList<String> getCredit() {

        credit.add("3");
        credit.add("2");
        credit.add("1");

        return credit;
    }

    public void setCredit(ArrayList<String> credit) {
        this.credit = credit;
    }

    public ArrayList<String> getGpa() {

        gpa.add("GPA");
        gpa.add("NGPA");

        return gpa;
    }

    public void setGpa(ArrayList<String> gpa) {
        this.gpa = gpa;
    }

   // public void get
}
