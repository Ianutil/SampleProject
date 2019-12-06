package com.example;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by ianchang on 2018/1/9.
 */

public class TestClassLoad {

    public static void main(String[] args){


        People people = new People();
        people.print("people");

        people = new com.example.Student();
        people.print("Student");

        Person person = new Person();

        Printer.print("age="+Person.age);

        person.print();


        Printer.print("**************************");
        Printer.print("**************************");


        try {
            Class clazz = Class.forName("com.example.TestClassLoad$Person");
            String name = Person.class.getResource("").toString();
            Printer.print("name="+name);
            name = Person.class.getResource("/").toString();
            Printer.print("name="+name);
            Printer.print("##################");

            person = (Person) clazz.newInstance();
            person.print();

            Printer.print("##################");

            clazz = Class.forName(Student.class.getName());
            person = (Person) clazz.newInstance();
            person.print();


            Printer.print(person.getClass().getClassLoader().getClass().getName());
            Printer.print(person.getClass().getClassLoader().getParent().getClass().getName());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    public static class Person {
        static {
            Printer.print("static Person block");
        }

        {
            Printer.print("normal Person block");
        }

        public static int age = 10;


        public Person(){
            Printer.print("Person construct");
        }

        public void print(){
            Printer.print("Person age="+age);
        }
    }


    public static class Student extends Person{
        static {
            Printer.print("static Student block");
        }

        {
            Printer.print("normal Student block");
        }

        public static String gender = "男";


        public Student(){
            super();
            Printer.print("Student construct");
        }

        public void print(){
            Printer.print("Student age="+age + " gender="+gender);
        }
    }


}
