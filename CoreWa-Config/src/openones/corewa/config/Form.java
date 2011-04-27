package openones.corewa.config;

/**
 * 
 * @author Thach Le
 */
public class Form {
    private String id;
    private String validateFile;
    private String className;
    
    public Form() {
        super();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Get value of validateFile.
     * @return the validateFile
     */
    public String getValidateFile() {
        return validateFile;
    }

    /**
     * Set the value for validateFile.
     * @param validateFile the validateFile to set
     */
    public void setValidateFile(String validateFile) {
        this.validateFile = validateFile;
    }

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    
}
