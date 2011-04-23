package rocky.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Util cung cấp các phương thức tiện ích cho chung cơ bản.
 * Refer CommonUtil.java
 * @author ThachLN
 * @since 1.4
 */
@Deprecated
public final class Util {

    /**
     * Kiểm tra chuỗi không rỗng.
     * @param str chuỗi cần kiểm tra
     * @return true nếu chuỗi không rỗng
     */
    public static boolean NNandNB(String str) {
        return (str != null) && (str.length() > 0);
    }
    
    /**
     * Kiểm tra danh sách khác rỗng.
     * @param lst
     * @return true nếu danh sách khác rỗng
     */
    public static boolean NNandNE(List lst) {
        return (lst != null) && (lst.size() > 0);
    }
    
    
    /**
     * Đọc nội dung của file resource với encoding cho trước
     * 
     * @param resourcePath
     *            đường dẫn file trong CLASSPATH
     * @param encoding
     * @return nội dung file. Nếu có lỗi thì trả lại null
     */
    public static String getContent(String resourcePath, String encoding) throws IOException {
        InputStream fis = null;
        InputStreamReader isReader = null;
        BufferedReader buffReader = null;
        char[] buff = new char[512];
        int len;
        StringBuffer sb = new StringBuffer();
        try {
            fis = Util.class.getResourceAsStream(resourcePath);
            isReader = new InputStreamReader(fis, encoding);
            buffReader = new BufferedReader(isReader);
            while ((len = buffReader.read(buff)) != -1) {
                sb.append(buff, 0, len);
            }
        } finally {
                if (buffReader != null) {
                    buffReader.close();
                }
                if (isReader != null) {
                    isReader.close();
                }
                if (fis != null) {
                    fis.close();
                }
        }

        return sb.toString();
        
    }
    
    /**
     * Chuyển mảng các object thành chuỗi.
     * 
     * @param objs mảng các Object.
     * @param separator chuỗi phân các phần tử.
     * @return chuỗi gồm các phần tử cách nhau bởi chuỗi phân cách separator.
     */
    public static String arrayToString(Object[] objs, String separator) {
        int len = (objs != null) ? objs.length : 0;
        StringBuffer sb = new StringBuffer();
        if (len > 0) {
            sb.append(objs[0]);
        }
        for (int i = 1; i < len; i++) {
            sb.append(separator).append(objs[i]);
        }

        return sb.toString();
    }

    public static String[] stringToList(String str, String delimeter) {
        StringTokenizer tokens = new StringTokenizer(str, delimeter);
        List lstResult = new ArrayList();
        while (tokens.hasMoreElements()) {
            lstResult.add(tokens.nextElement());
        }
        return (String[])lstResult.toArray(Constant.BLANK_STRS);
    }
    
    /**
     * Check an object whether is a primitive type or not.
     * The primitive type include boolean, char, byte, short, int, long, double, float
     * @param obj the input object will be checked
     * @return true if the obj is the primitive type.
     */
    public static boolean isPrimitive(Object obj) {
        
        final Class[] PRIMITIVE_CLASSES = { boolean.class, char.class, byte.class, short.class, int.class
                                          , long.class, double.class, float.class
                                          , Boolean.class, Character.class, Byte.class, Short.class, Integer.class
                                          , Long.class, Double.class, Float.class
                                          , String.class};
        if (obj == null) {
            return true;
        }
        
        for (Class singleClass : PRIMITIVE_CLASSES) {
            if (obj.getClass() == singleClass) {
                return true;
            }
        }
        return false;
    }
    
    public static String getCurrentWorkingFolder() {
        File f = new File(".");
        String currentPath = null;
        
        try {
            currentPath = f.getCanonicalPath();
        } catch (IOException ioex) {
            LogService.logError("Util", ioex);
        }
        
        return currentPath;
    }
    
    public static boolean existedFile(String filePath) {
        return new File(filePath).exists();
    }
    
    public static boolean rename(String filePath, String newFilePath) {
        new File(filePath).renameTo(new File(newFilePath));
        
        return true;
    }

    public static String formatDate(Date dte, String dteFormat) {
        return new SimpleDateFormat(dteFormat).format(dte);
    }


}
