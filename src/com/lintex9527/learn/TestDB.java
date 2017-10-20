package com.lintex9527.learn;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
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
    public static void test_sql(){
        // 声明 Connection 对象
        Connection connection;

        // TODO: 用 JSON 格式文件保存用户名和密码
        // MySQL 配置时的用户名和密码
        String user = getString("输入MySQL用户名：");
        String password = getString("输入其口令：");

        // 遍历查询结果
        try{
            // 加载驱动程序
            Class.forName(DRIVER);
            // 1. getConnection() 方法，连接到数据库
            connection = DriverManager.getConnection(URL, user, password);
            if (!connection.isClosed()){
                System.out.println("成功连接至数据库。");
            }

            // 2. 创建 Statement 类对象，用来执行 SQL 语句
            Statement statement = connection.createStatement();
            // SQL 中的 UPDATE, DELETE 等语句使用这个
            PreparedStatement psql;

            int empno = 0;
            String ename = null;
            float sal = (float)0.0;
            String job = null;

            // 跟来产生随机数，用做 empno，或者 sal 增加的值
            Random random = new Random();

            boolean flag = true;
            while (flag){
                // 用按键选择对应的操作
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("--------------------------------------------------");
                String prompt = "1、查询\n2、插入\n3、更新\n4、删除\n5、退出";
                System.out.println(prompt);
                System.out.print("选择对应的操作：");
                String ans = bufReader.readLine();
                System.out.println("选择的是： " + ans);
                System.out.println("--------------------------------------------------");
                int num = Integer.parseInt(ans.trim(), 10);

                switch (num){
                    case 1:
                        // 查询

                        // 要执行的SQL语句
                        String sql = "SELECT * FROM emp ORDER BY sal";
                        // 3. ResultSet 类，用来存放获取的结果集
                        ResultSet rs = statement.executeQuery(sql);
                        System.out.println("查询结果如下：");
                        System.out.println("工号\t姓名\t职称\t工资");
                        System.out.println("----------------------------------------");

                        while (rs.next()){
                            empno = rs.getInt("empno");
                            ename = rs.getString("ename");
                            job = rs.getString("job");
                            sal = rs.getFloat("sal");
                            System.out.println(empno + "\t" + ename + "\t" + job + "\t" + sal);
                        }
                        // 最后必须关闭连接
                        rs.close();

                        break;
                    case 2:
                        // 插入
                        // 9935 李晓峰 人事  2015-03-01  3520.00

                        // 预处理添加数据
                        psql = (PreparedStatement) connection.prepareStatement("INSERT INTO emp (empno, ename, job, hiredate, sal)"
                                + "values(?, ?, ?, ?, ?)");

                        empno = random.nextInt(10000);
                        psql.setInt(1, empno);

                        psql.setString(2, "李晓峰");
                        psql.setString(3, "人事");

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date mydate = dateFormat.parse("2015-03-1");
                        psql.setDate(4, new java.sql.Date(mydate.getTime()));
                        psql.setFloat(5, (float) 3520.00);
                        psql.executeUpdate();

                        break;
                    case 3:
                        // 更新
                        // 给 sal 最低的雇员 sal += 666.00
                        String sql_lowest_sal = "SELECT * FROM emp ORDER BY sal LIMIT 1";
                        ResultSet resultSet = statement.executeQuery(sql_lowest_sal);
                        // 记住，只要是 ResultSet 就必须使用固定的如下的格式访问其中的每一个记录，哪怕只有一个记录
                        // while(resultSet.next()){}
                        while(resultSet.next()){
                            empno = resultSet.getInt("empno");
                            sal = resultSet.getFloat("sal");
                            int add = random.nextInt(1000);
                            System.out.println("随机增加了： " + add);
                            sal += add;

                            psql = connection.prepareStatement("UPDATE emp SET sal = ? WHERE empno = ?");
                            psql.setFloat(1, sal);
                            psql.setInt(2, empno);
                            psql.executeUpdate();
                        }
                        
                        break;
                    case 4:
                        // 删除

                        break;
                    case 5:
                        // 退出
                    default:
                        // 默认也是退出
                        flag = false;
                        break;
                }
            }






            // 最后必须关闭连接
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            System.out.println("操作结束。");
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
        test_sql();
    }

}
