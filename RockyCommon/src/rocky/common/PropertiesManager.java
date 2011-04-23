/**
 * PropertiesManager.java
 * Copyright Rocky.
 */
package rocky.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * PropertiesManager quản lý cấu hình của ứng dụng.
 * @author ThachLN
 * @since 1.4
 */
public class PropertiesManager {
    private static final String EXT_XML = ".xml";
    private Properties props = new Properties();
    
    public PropertiesManager(String resourcePath) throws InvalidPropertiesFormatException, IOException {
        
        if (resourcePath.startsWith("/")) { // The resource file is in the CLASSPATH
            if (resourcePath.endsWith(EXT_XML)) {
                props.loadFromXML(PropertiesManager.class.getResourceAsStream(resourcePath));
            } else {
              props.load(PropertiesManager.class.getResourceAsStream(resourcePath));
            }
            
        } else { // The resource file is the input file
            if (resourcePath.endsWith(EXT_XML)) {
                props.loadFromXML(new FileInputStream(resourcePath));
            } else {
                props.load(new FileInputStream(resourcePath));
            }
        }
    }
    /**
     * Khởi tạo properties.
     * Để sử dụng PropertiesManager, method initialize này phải được gọi trước tiên.
     * @throws IOException lỗi đọc file cấu hình. File cấu hình phải được đạt trong thư mục chứa các classes.
     */
    /*
    public static Properties newInstanceFromXML(String configPath) throws IOException {
        Properties props = new Properties();
        props.loadFromXML(PropertiesManager.class.getResourceAsStream(configPath));
        
        return props;
    }
    */
    
    /**
     * Tạo đối tượng Properties từ file .properties.
     * @param filePath đường dẫn file tương đối trong thư mục của CLASSPATH
     * @return đối tượng Properties tương ứng của file
     */
    /*
    public static Properties newInstanceFromProps(String configPath) throws IOException {
        Properties props = new Properties();
        props.load(PropertiesManager.class.getResourceAsStream(configPath));
        return props;
    }
    */
    
    /**
     * Khởi tạo properties.
     * Để sử dụng PropertiesManager, method initialize này phải được gọi trước tiên.
     * @throws IOException lỗi đọc file cấu hình. File cấu hình ex2j.properties phải được đạt trong thư mục chứa các classes.
     */
    /*
    public static void initialize(String filePath) throws IOException {
        if (props == null) {
           props = new Properties();
           if (CommonUtil.NNandNB(filePath)) {
               if (filePath.startsWith("/")) {
                   props.loadFromXML(PropertiesManager.class.getResourceAsStream(filePath));
               } else {
                   props.loadFromXML(new FileInputStream(filePath));
               }
           } else {
               // Load default configuration
               props.loadFromXML(PropertiesManager.class.getResourceAsStream("Unknown configuration file"));
           }
        }
    }
    */
    
    /**
     * Tạo đối tượng Properties từ file .properties.
     * @param filePath đường dẫn file tương đối trong thư mục của CLASSPATH
     * @return đối tượng Properties tương ứng của file
     */
    /*
    public static Properties createProps(String filePath) {
        Properties props = new Properties();
        try {
            props.load(PropertiesManager.class.getResourceAsStream(filePath));
        } catch (IOException ioe) {
            LogService.logError("createProps", ioe);
            return null;
        }
        return props;
    }
    */

    /**
     * Tạo đối tượng Properties từ file xml.
     * @param filePath đường dẫn file tương đối trong thư mục của CLASSPATH
     * @return đối tượng Properties tương ứng của file
     */
    /*
    public static Properties createPropsFromXML(String filePath) {
        Properties props = new Properties();
        try {
            props.loadFromXML(PropertiesManager.class.getResourceAsStream(filePath));
        } catch (IOException ioe) {
            LogService.logError("createPropsFromXML", ioe);
            return null;
        }
        return props;
    }
    */
    /**
     * Lấy giá trị của thuộc tính dựa vào khóa cho trước.
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }
    
    public String[] getProperties(String key, String separator) {
        String value = props.getProperty(key);
        
        return (value != null) ? value.split(separator) : null;
    }
    
    public int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }

    public Properties getProperties() {
        return props;
    }
}
