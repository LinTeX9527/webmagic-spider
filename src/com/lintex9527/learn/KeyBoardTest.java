package com.lintex9527.learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 键盘读入字符串的方式
 */
public class KeyBoardTest {

    /**
     * 读入一行字符串
     */
    public static void getString(){
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("输入用户名：");
            String username = buf.readLine();
            System.out.println("输入的用户名：" + username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        getString();
    }
}
