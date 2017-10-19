package com.lintex9527.learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * JAVA 操作数据库
 * 参见：
 * http://www.cnblogs.com/centor/p/6142775.html
 *
 * 我电脑上创建这个数据库的过程见 TestDB.md
 *
 */
public class TestDB {

    /**
     * 一个查询操作的例子
     */
    public static void test01(){
        // 声明 Connection 对象
        Connection con;
        // 驱动程序名字
        String driver = "com.mysql.jdbc.Driver";

        // URL 指向要访问的数据库名
        // 通过jdbc 访问 mysql 数据库，主机 localhost，端口号 33067，数据库的名字是 sqltestdb
        String url = "jdbc:mysql://localhost:3306/sqltestdb";

        // MySQL 配置时的用户名和密码
        String user = getString("输入MySQL用户名：");
        String password = getString("输入其口令：");

        // 遍历查询结果
        try{
            // 加载驱动程序
            Class.forName(driver);
            // 1. getConnection() 方法，连接到数据库
            con = DriverManager.getConnection(url, user, password);
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
            System.out.println("数据库成功获取!!!");
        }

    }

    /**
     * 提示用户输入一行字符
     * @param prompt 提示语
     * @return 用户输入的一行字符
     */
    public static String getString(String prompt){

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
        String ans = null;

        try {
            System.out.print(prompt);
            ans = bufReader.readLine();
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static void main(String[] args) {
        test01();
    }

}
