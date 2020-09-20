package com.lea.task;

import com.lea.preparedstatement3.bean.Student;
import com.lea.preparedstatement3.curd.PreparedStatementQueryTest;

import java.sql.SQLOutput;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * // 在控制台输入学生准考证号或身份证号 查询学生的基本信息
 * @author lzc
 * @create 2020/09/20 下午 1:51
 */
public class QueryStudentInfo {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("请选择输入信息：");
        System.out.println("a:准考证号");
        System.out.println("b:身份证号");
        String sql = "SELECT FlowID,Type,IDCard,ExamCard,StudentName,Location, Grade FROM examstudent";
        StringBuffer sf = new StringBuffer(sql);
        while (true) {
            String choose = sc.next();
            if ("a".equals(choose.toLowerCase())) {
                System.out.println("请输入准考证号：");
                sf.append(" where ExamCard = ?");
                break;
            }else if ("b".equals(choose.toLowerCase().equals("a"))) {
                System.out.println("请输入身份证号：");
                sf.append(" where IDCard = ?");
                break;
            }else{
                System.out.println("无效选项，请重新选择");
            }
        }
        String studentNum = sc.next();
        List<Student> students = PreparedStatementQueryTest.queryList(Student.class, sf.toString(), studentNum);
        if (null == students || students.size() == 0) {
            System.out.println("未查询到学生信息");
        }else {
            System.out.println("学生信息如下：");
            students.forEach(System.out::println);
        }

    }
}
