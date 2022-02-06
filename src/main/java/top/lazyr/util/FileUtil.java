package top.lazyr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lazyr
 * @created 2021/11/4
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static String prefixPath = "./src/main/resources/";

    /**
     * 获取传入路径 catalog 下的所有后缀为 suffix 文件的绝对路径
     * @param catalog
     * @return
     */
    public static List<String> getFilesAbsolutePath(String catalog, String suffix) {
        List<String> filesAbsolutePath = new ArrayList<>();
        getFilesAbsolutePath(catalog, suffix,filesAbsolutePath);
        return filesAbsolutePath;
    }

    private static void getFilesAbsolutePath(String path, String suffix, List<String> filesAbsolutePath) {
        File file = new File(path);
        if (!file.exists()) {
            logger.info(path + "不存在");
            return;
        }

        if (file.isDirectory()) { // 若是文件夹
            for (File subFile : file.listFiles()) {
                getFilesAbsolutePath(subFile.getAbsolutePath(), suffix, filesAbsolutePath);
            }
        } else if (file.isFile() && file.getName().contains(suffix)) { // TODO: 优化文件名后缀判断逻辑
            filesAbsolutePath.add(file.getAbsolutePath());
        }
    }

    public static void append2File(String filePath, String content) {
        write2File(filePath, content, true);
    }

    public static void write2File(String filePath, String content) {
        write2File(filePath, content, false);
    }

    private static void write2File(String filePath, String content, boolean append) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(prefixPath + filePath, append));
            out.write(content);
            out.close();
//            System.out.println(filePath + "写入成功！");
        } catch (IOException e) {
        }
    }

    public static List<String> readFileByLine(String fileName) {
        File file = new File(prefixPath + fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        List<String> content = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                content.add(tempStr);
            }
            reader.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return content;
    }

    public static int catalogNum(String path) {
        File catalog = new File(path);
        if (!catalog.isDirectory()) {
            return 0;
        }
        File[] files = catalog.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        return catalog.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        }).length;
    }

    public static void createCatalog(String path) {
        File catalog = new File(prefixPath + path);
        if (catalog.exists()) {
            return;
        }
        catalog.mkdirs();
    }



    /**
     * 删除目录下的所有文件
     * @param path 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean clearDir(String path) {
        File directory = new File(prefixPath + path);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files == null || files.length == 0) {
                return true;
            }
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        } else {
            return false;
        }
        return true;
    }
}
