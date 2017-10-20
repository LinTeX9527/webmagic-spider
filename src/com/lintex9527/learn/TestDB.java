package com.lintex9527.learn;

import com.mysql.jdbc.PreparedStatement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * JAVA 操作数据库
 * 参见：
 * http://www.cnblogs.com/centor/p/6142775.html
 *
 * 我电脑上创建这个数据库的过程见 TestDB.md
 *
 */
public class TestDB {

    // 驱动程序名字
    public static final String DRIVER = "com.mysql.jdbc.Driver";

    // URL指向要访问的数据库
    // 要访问 localhost 端口号3306，数据库名字为 sqltestdb
    public static final String URL = "jdbc:mysql://localhost:3306/sqltestdb?&useSSL=false";
    // 解决办法是：在 URL 最后加上 "?&useSSL=false"
    // JDBC提示： Establishing SSL connection without server's identity verification is not re


    /**
     * 一个查询操作的例子
     */
    public static void test_read(){
        // 声明 Connection 对象
        Connection con;

        // TODO: 用 JSON 格式文件保存用户名和密码
        // MySQL 配置时的用户名和密码
        String user = getString("输入MySQL用户名：");
        String password = getString("输入其口令：");

        // 遍历查询结果
        try{
            // 加载驱动程序
            Class.forName(DRIVER);
            // 1. getConnection() 方法，连接到数据库
            con = DriverManager.getConnection(URL, user, password);
            if (!con.isClosed()){
                System.out.println("Succeeded to connect to the database.");
            }

            // 2. 创建 Statement 类对象，用来执行 SQL 语句
            Statement statement = con.createStatement();
            // 要执行的SQL语句
            String sql = "SELECT * FROM emp";

            // 3. ResultSet 类，用来存放获取的结果集
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("-------------------");
            System.out.println("执行结果如下：");
            System.out.println("姓名\t职称");
            System.out.println("-------------------");

            String job = null;
            String id = null;
            while (rs.next()){
                job = rs.getString("job");
                id = rs.getString("ename");

                System.out.println(id + "\t" + job);
            }

            // 最后必须关闭连接
            rs.close();
            con.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println("查询操作结束。");
        }

    }

    /**
     * 数据库操作--更新
     * 插入一个记录
     * 9935 李晓峰 人事  2015-03-01  3520.00
     */
    public static void test_update(){
        // 声明 Connection 对象
        Connection connection;

        // MySQL 用户名和口令
        String username = getString("输入MySQL 用户名：");
        String password = getString("输入其口令：");

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, username, password);
            if (! connection.isClosed()){
                System.out.println("成功连接至数据库");
            }
            PreparedStatement psql;
            // 预处理添加数据
            psql = (PreparedStatement) connection.prepareStatement("INSERT INTO emp (empno, ename, job, hiredate, sal)"
                                            + "values(?, ?, ?, ?, ?)");
            Random random = new Random();
            int empno = random.nextInt(10000);
            psql.setInt(1, empno);

            psql.setString(2, "李晓峰");
            psql.setString(3, "人事");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date mydate = dateFormat.parse("2015-03-1");
            psql.setDate(4, new java.sql.Date(mydate.getTime()));
            psql.setFloat(5, (float) 3520.00);
            psql.executeUpdate();

            // Connection 最后必须关闭
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            System.out.println("插入操作结束。");
        }
    }

    /**
     * 提示用户输入一行字符
     * @param prompt 提示语
     * @return 用户输入的一行字符
     */
    public static String getString(String prompt){

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
        String ans = "";

        try {
            System.out.print(prompt);
            ans = bufReader.readLine();

            // TODO: 关闭这个流，会导致第二个 getString() 抛出流已经关闭的异常，怎么解决？
            //bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static void main(String[] args) {
//        test_read();
        test_update();
//        test_read();
    }

}
