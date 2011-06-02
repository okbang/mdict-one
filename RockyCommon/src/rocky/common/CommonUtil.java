/**
 * CommonUtil.java
 */
package rocky.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ThachLN
 * 
 */
public class CommonUtil {
    /** String pattern of format string. */
    private static final String VARIABLE_NAME_PATTERN = "[\\x24][\\{]([a-zA-Z0-9_.]+)[\\}]";

    /**
     * Refer isNNNE.
     * @param lst
     * @return
     */
    @Deprecated
    public static boolean NNandNE(List<Object> lst) {
        return (lst != null) && (lst.size() > 0);
    }
    /**
     * Kiểm tra danh sách khác rỗng.
     * 
     * @param lst
     * @return true nếu danh sách khác rỗng
     */
    
    public static boolean isNNNE(List<Object> lst) {
        return (lst != null) && (lst.size() > 0);
    }

    @Deprecated
    public static boolean NNandNE(Object[] lst) {
        return (lst != null) && (lst.length > 0);
    }
    public static boolean isNNNE(Object[] lst) {
        return (lst != null) && (lst.length > 0);
    }
    @Deprecated
    public static boolean NNandNE(Map map) {
        return (map != null) && (map.size() > 0);
    }
    
    public static boolean isNNNE(Map map) {
        return (map != null) && (map.size() > 0);
    }

    public static String getDteFormat(Date dte, String dteFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dteFormat);
        return sdf.format(dte);
    }

    /**
     * Kiểm tra chuỗi không rỗng.
     * 
     * @param str
     *            chuỗi cần kiểm tra
     * @return true nếu chuỗi không rỗng
     */
    @Deprecated
    public static boolean NNandNB(String str) {
        return (str != null) && (str.length() > 0);
    }

    public static boolean isNNandNB(String str) {
        return (str != null) && (str.length() > 0);
    }
    
    public static boolean isNNandNB(List objList) {
        return (objList != null) && (objList.size() > 0);
    }
    
    public static boolean isNNandNB(Object strObj) {
        return (strObj != null) && (strObj.toString().length() > 0);
    }
    
    public static int find(Object[] array, Object obj) {
        int len = (array != null) ? array.length : 0;

        if (obj == null) {
            return -1;
        }
        for (int i = 0; i < len; i++) {
            if (obj.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Compare dates with year, month, day
     * 
     * @param srcDte
     * @param dstDte
     * @return
     */
    public static boolean equalDte(Date srcDte, Date dstDte) {
        Calendar srcCal = Calendar.getInstance();
        Calendar dstCal = Calendar.getInstance();
        srcCal.setTime(srcDte);
        dstCal.setTime(dstDte);

        return ((srcCal.get(Calendar.YEAR) == dstCal.get(Calendar.YEAR))
                && (srcCal.get(Calendar.MONTH) == dstCal.get(Calendar.MONTH)) && (srcCal.get(Calendar.DATE) == dstCal
                .get(Calendar.DATE)));
    }

    /**
     * Adds or subtracts the specified amount of days to the given date.
     * For example, to subtract 5 days from the current date, you can achieve it by calling:
     * addDay(Calendar.DAY_OF_MONTH, -5).
     * @param dte root date
     * @param numDays  the amount of days to be added to the field.
     * @return
     */
    public static Date addDay(Date dte, int numDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dte);
        cal.add(Calendar.DAY_OF_MONTH, numDays);
        
        return cal.getTime();
    }
    
    /**
     * Get year, month, day of a given date
     * 
     * @param dte
     * @return
     */
    public static Date getYMD(Date dte) {
        Calendar cal = Calendar.getInstance();
        Calendar targetCal = Calendar.getInstance();
        cal.setTime(dte);
        targetCal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        targetCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        targetCal.set(Calendar.DATE, cal.get(Calendar.DATE));

        return targetCal.getTime();
    }

    public static boolean mkdir(String path) {
        return new File(path).mkdir();
    }
    
    /**
     * Get extension of file.
     * @param fileName
     * @return extension part without dot character
     */
    public static String getExtension(String fileName) {
        if (!isNNandNB(fileName)) {
            return fileName;
        }
            
        int idxOfDot = fileName.lastIndexOf(Constant.CHAR_DOT);
        if (idxOfDot == -1) {
            return null;
        } else {
            return fileName.substring(idxOfDot + 1);
        }
    }

    public static boolean deleteDir(File dir, boolean isDeleteOwn) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]), true);
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        if (isDeleteOwn) {
            return dir.delete();
        }
        return true;
    }

    public static String outputString(Object[] objs, String separator) {
        StringBuffer sb = new StringBuffer();
        int len = (objs != null) ? objs.length : 0;
        for (int i = 0; i < len - 1; i++) {
            sb.append(objs[i].toString()).append(separator);
        }
        if (CommonUtil.isNNNE(objs)) {
            sb.append(objs[len - 1]);
        }
        return sb.toString();
    }

    public static String outputString(Map<Object, Object> map) {
        StringBuffer sb = new StringBuffer();
        Map.Entry<Object, Object> entry;
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            entry = (Map.Entry<Object, Object>) it.next();
            sb.append(entry.getKey() + "=" + entry.getValue() + ";");
        }
        return sb.toString();
    }

    /**
     * Đọc nội dung của file resource với encoding cho trước
     * 
     * @param resourcePath
     *            đường dẫn file trong CLASSPATH
     * @param encoding
     * @return nội dung file. Nếu có lỗi thì trả lại null
     */
    public static String getContent(String resourcePath, boolean isResource, String encoding) {
        InputStream fis = null;
        InputStreamReader isReader = null;
        BufferedReader buffReader = null;
        char[] buff = new char[512];
        int len;
        StringBuffer sb = new StringBuffer();
        try {
            if (isResource) {
                fis = CommonUtil.class.getResourceAsStream(resourcePath);
            } else {
                fis = new FileInputStream(resourcePath);
            }
            if (NNandNB(encoding)) {
                isReader = new InputStreamReader(fis, encoding);
            } else {
                isReader = new InputStreamReader(fis);
            }

            buffReader = new BufferedReader(isReader);
            while ((len = buffReader.read(buff)) != -1) {
                sb.append(buff, 0, len);
            }
        } catch (IOException ioe) {
            LogService.logWarn("CommonUtil.getContent(" + resourcePath + "," + encoding + ") throws", ioe);
            return null;
        } finally {
            try {
                if (buffReader != null) {
                    buffReader.close();
                }
                if (isReader != null) {
                    isReader.close();
                }
                if (fis != null) {
                    fis.close();
                }

            } catch (IOException ioe) {
                LogService.logWarn("CommonUtil.getContent", ioe);
            }
        }

        return sb.toString();

    }

    /**
     * Get working directory of the application.
     * 
     * @return String
     */
    public static String getWorkingDir() {
        return new File(".").getAbsolutePath();
    }

    public static void putNullAsBlank(Map mapData, final Object key, final Object value) {
        mapData.put(key, (value != null) ? value : Constant.BLANK);
    }

    public static void close(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }

        } catch (IOException e) {
            // Do nothing
        }
    }

    public static void close(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            // Do nothing
        }
    }

    public static InputStream loadResource(String resourcePath) throws FileNotFoundException {
        if (resourcePath.startsWith("/")) { // The resource file is in the
                                            // CLASSPATH
            return (CommonUtil.class.getResourceAsStream(resourcePath));
        } else { // The resource file is the input file
            return new FileInputStream(resourcePath);
        }
    }

    /**
     * Format a string with pattern: xxxx${var}xxxx Supported
     * @param strTemplate
     * @param valueMap
     * @return
     * @throws SQLException
     */
    public static String formatPattern(String strTemplate, Map<String, Object> valueMap) {
        Pattern pattern = Pattern.compile(VARIABLE_NAME_PATTERN);
        Matcher matcher = pattern.matcher(strTemplate);
        StringBuffer sb = new StringBuffer();
        String key;
        Object objVal;

        // Find column name
        while (matcher.find()) {
            key = matcher.group(1);
            objVal = valueMap.get(key);
            // replace the column name pattern by question mark
            if (objVal != null) {
                matcher.appendReplacement(sb, objVal.toString());
            }
        }
        // append the tail of the query template to the String buffer
        matcher.appendTail(sb);
        matcher.reset();

        return sb.toString();
    }

    /**
     * Format a string with given pattern and variable pattern.
     * @param strTemplate
     * @param varPattern
     * @param mapValue
     * @return
     */
    public static String formatPattern(String strTemplate, String varPattern, Map<String, Object> mapValue) {
        Pattern pattern = Pattern.compile(varPattern);
        Matcher matcher = pattern.matcher(strTemplate);
        StringBuffer sb = new StringBuffer();
        String key;
        Object objVal;

        // Find column name
        while (matcher.find()) {
            key = matcher.group(1);
            objVal = mapValue.get(key);
            // replace the column name pattern by question mark
            matcher.appendReplacement(sb, objVal.toString());
        }
        // append the tail of the query template to the String buffer
        matcher.appendTail(sb);
        matcher.reset();

        return sb.toString();
    }
    /**
     * Format a string with pattern: xxxx${var[.format]}xxxx Supported
     * variables: 1) Dated format 2) Environment variables Example: 1)
     * formatPattern("Today is ${CURREN_DATE.YYYYMMDD}" returns
     * "Today is 20081115" with assumption To day is 15-Nov-2008. 2)
     * 
     * @param strPattern
     * @return
     */
    public static String formatPattern(String strPattern) {
        Pattern pattern = Pattern.compile(VARIABLE_NAME_PATTERN);
        Matcher matcher = pattern.matcher(strPattern);
        StringBuffer sb = new StringBuffer();
        String keyPart;
        String varName;
        String format;
        String value;

        // Find column name
        while (matcher.find()) {
            // replace the column name pattern by question mark

            keyPart = matcher.group(1);

            // analysis keypart
            if (keyPart.indexOf(Constant.STR_DOT) == -1) { // Environment
                                                           // variable
                value = System.getenv(keyPart);
                value = value.replaceAll("\\\\", "\\\\\\\\");
                matcher.appendReplacement(sb, value);
            } else {
                // Structure of keypart is: VAR.FORMAT
                // Current version supports VAR: CURRENT_DATE
                varName = keyPart.split("\\.")[0];
                format = keyPart.split("\\.")[1];

                if ("CURRENT_DATE".equals(varName)) {
                    value = getDteFormat(new Date(), format);
                    matcher.appendReplacement(sb, value);
                }

            }
        }
        // append the tail of the query template to the String buffer
        matcher.appendTail(sb);
        matcher.reset();

        return sb.toString();
    }
    
    /**
     * Parse String to Date with given pattern.
     * @param strDate
     * @param pattern
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date dte = null;
        try {
            dte = sdf.parse(strDate);
        } catch (ParseException e) {
            // Skip
        }
        
        return dte;
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
