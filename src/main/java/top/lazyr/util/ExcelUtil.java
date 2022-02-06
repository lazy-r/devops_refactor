package top.lazyr.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lazyr
 * @created 2021/11/24
 */
public class ExcelUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    private static String PATH = "./src/main/resources/";

    public static void append2Excel(String workbookName, String sheetName, List<List<String>> infos) {
        Workbook workbook = null;
        File file = new File(PATH + workbookName);
        if (file.exists()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                workbook = new XSSFWorkbook(inputStream);
            } catch (FileNotFoundException e) {
                logger.error(workbookName + " not found: " + e.getMessage());
            } catch (IOException e) {
                logger.error("read " + workbookName + " error: " + e.getMessage());
            }

        } else {
//            System.out.println("创建工作簿: " + workbookName);
            workbook = new XSSFWorkbook();
        }
        boolean isNew = false;
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            isNew = true;
//            System.out.println("创建工作表: " + sheetName);
            sheet = workbook.createSheet(sheetName);
        }
        int lastRowNum = isNew ? 0 : sheet.getLastRowNum() + 1;
        for (List<String> info: infos) {
            Row row = sheet.createRow(lastRowNum++);
            for (int i = 0; i < info.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(info.get(i));
            }
        }
        // 写入数据
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(PATH + workbookName);
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        System.out.println("写入成功");
    }

    public static void write2Excel(String fileName, String sheetName, List<List<String>> infos) {
        // 1、创建一个工作簿
        Workbook workbook = new XSSFWorkbook();
//        System.out.println("创建工作簿: " + fileName);
        Sheet sheet = workbook.createSheet(sheetName);
//        System.out.println("创建工作表: " + sheetName);
        int lastRowNum = 0;
        for (List<String> info: infos) {
            Row row = sheet.createRow(lastRowNum++);
            for (int i = 0; i < info.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(info.get(i));
            }
        }

        // 写入数据
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(PATH + fileName);
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        System.out.println("写入成功");
    }

    public static Map<String, List<List<String>>> readAllFromExcel(String fileName) {
        Map<String, List<List<String>>> map = new HashMap<>();
        try {
            FileInputStream inputStream = new FileInputStream(PATH + fileName);
            Workbook workbook = new XSSFWorkbook(inputStream);
            if (workbook == null) {
                logger.error("{} is not existed or the format is wrong.", fileName);
                return null;
            }
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                List<List<String>> infos = readSheet(workbook, workbook.getSheetName(i));
                map.put(workbook.getSheetName(i), infos);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<List<String>> readSheet(String fileName, String sheetName) {
        FileInputStream inputStream = null;
        Workbook workbook = null;
        File file = new File(PATH + fileName);
        if (!file.exists()) {
            logger.info(fileName + " is not exist");
            return new ArrayList<>();
        }
        try {
            inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<String>> infos = readSheet(workbook, sheetName);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("FileInputStream close failed: " + e.getMessage());
            }
        }
        return infos;
    }

    private static List<List<String>> readSheet(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        List<List<String>> infos = new ArrayList<>();
        if (sheet == null) {
            logger.info("the " + sheetName + " is not exist");
            return new ArrayList<>();
        }

        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            List<String> info = new ArrayList<>();
            for (int col = 0; col < lastCellNum; col++) {
                Cell cell = row.getCell(col);
                info.add(cell.getStringCellValue());
            }
            infos.add(info);
        }

        return infos;
    }



    /**
     * titles按顺序输入
     * @param titles
     * @return
     */
    public static List<String> generateTitle(String... titles) {
        List<String> info = new ArrayList<>();
        if (titles == null || titles.length == 0) {
            return info;
        }

        for (String title : titles) {
            info.add(title);
        }
        return info;
    }
}
