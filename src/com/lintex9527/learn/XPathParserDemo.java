package com.lintex9527.learn;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 练习Java XPath 的解析
 * 参见：
 * http://www.yiibai.com/java_xml/java_xpath_parse_document.html
 *
 * 必须要导入上面的包。
 *
 */
public class XPathParserDemo {
    public static void main(String[] args) {
        try {

            // 一个示例 xml 文件是 src/htmldemo.txt ，在这里使用相对路径是可以的
            File inputFile = new File(".\\src\\htmldemo.txt");

            // dbFactory 只是用来产生一个 dBuilder
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            // dBuilder 通过输入文件来产生一个 Document doc，后面的解析是通过 doc
            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            //  通过XPathFactory 得到一个 xPath
            XPath xPath =  XPathFactory.newInstance().newXPath();

            // xPath 在对应的 doc 上应用正则表达式来搜寻到我们想要的结果
            // 这里的 nodeList 就是一个个 node 节点的列表
            String expression = "/class/student";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                System.out.println("\nCurrent Element :"
                        + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    // 这里把 Node 转换成 Element 才能通过键值对取得我们想要的值
                    Element eElement = (Element) nNode;
                    System.out.println("Student roll no : "
                            + eElement.getAttribute("rollno"));
                    System.out.println("First Name : "
                            + eElement
                            .getElementsByTagName("firstname")
                            .item(0)
                            .getTextContent());
                    System.out.println("Last Name : "
                            + eElement
                            .getElementsByTagName("lastname")
                            .item(0)
                            .getTextContent());
                    System.out.println("Nick Name : "
                            + eElement
                            .getElementsByTagName("nickname")
                            .item(0)
                            .getTextContent());
                    System.out.println("Marks : "
                            + eElement
                            .getElementsByTagName("marks")
                            .item(0)
                            .getTextContent());
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}