package com.example.demo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wuchonghua
 * @create 2021-05-18 15:06
 */
@RestController
@RequestMapping("api")
public class DemoController {

    RabbitTemplate r;
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    public static void main(String[] args) throws IOException, DocumentException {
        String filePath = "D:\\124124.xlsx";

        SAXReader reader = new SAXReader();
        Document doc = reader.read("D:\\1.xml");

        // Document doc = DocumentHelper.createDocument();
        // Element root = doc.addElement("root");
        Element root = doc.getRootElement();
        Workbook wb = readExcel(filePath);
        //Element e = null;
        if(wb != null){

            for (Sheet sheet : wb) {
                for (Row row : sheet) {
//                    Cell table = row.getCell(1);
//                    if (table != null && StringUtils.hasText(table.getStringCellValue())) {
//                        e = root.addElement(table.getStringCellValue());
//                    }
//                    Cell fieldName = row.getCell(4);
//                    Cell fieldType = row.getCell(5);
//                    if (fieldName != null && StringUtils.hasText(fieldName.getStringCellValue()) && fieldType != null && StringUtils.hasText(fieldType.getStringCellValue())) {
//                        Element e1 = e.addElement(fieldName.getStringCellValue());
//                        e1.addAttribute("FieldType", fieldType.getStringCellValue().toLowerCase(Locale.ROOT));
//                    }
                    Cell table = row.getCell(1);
                    Element e = root.element(table.getStringCellValue());
                    Cell pk = row.getCell(2);
                    e.addAttribute("pk", pk.getStringCellValue().replace('\n', ','));

                }
            }

        }
        File f = new File("D:\\1.xml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileOutputStream(f), format);
        writer.write(doc);

    }

    //读取excel
    public static Workbook readExcel(String filePath){
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return new XSSFWorkbook(is);
            }else{
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
