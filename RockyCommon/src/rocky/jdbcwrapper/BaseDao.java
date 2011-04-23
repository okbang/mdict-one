package rocky.jdbcwrapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import rocky.common.LogService;

public class BaseDao {
    
    Map<String, Method> writeMethodMap = null;
    Map<String, Method> readMethodMap = null;
    
    /**
     * Get the map of properties and tables.
     * Default, the map contains properties and table fields are the same.
     * The derived class should override this method.
     * @return Map<String, String> <property of the entity, column field>
     */
    public Map<String, String> getMapField(Object entity) {
        Map<String, String> propColMap = new HashMap<String, String>(); // retured value
        String entityMapFile = "/" + entity.getClass().getName() + ".properties";
        Properties mapFieldProps = new Properties();
        try {
            mapFieldProps.load(this.getClass().getResourceAsStream(entityMapFile));
            
            String propertyName;
            String columnName;
            
            for (Enumeration e = mapFieldProps.keys(); e.hasMoreElements(); ) {
                propertyName = (String)e.nextElement();
                columnName = mapFieldProps.getProperty(propertyName);
                
                propColMap.put(propertyName, columnName);
            }

        } catch (IOException ioex) {
            LogService.logError(ioex);
        }
       
        return propColMap;
    }

    /**
     * Parse the ResultSet into the properties of the entity
     * @param rs
     * @param entity Output entity will be updated
     * @throws SQLException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    protected void parse(ResultSet rs, Object entity) throws SQLException {
        Map<String, String> fieldMap = this.getMapField(entity); 
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        String colName;
        int colType;
        // <property, WriteMethod object>
        Map<String, Method> methodMap = getWriteMethodMap(entity);
        Method writeMethod;
        
        // Indexing the ResultSetMetaData
        // <Column name, Column type>
        Map<String, Integer> colTypeMap = new HashMap<String, Integer>();
        String columnName;
        int columnType;
        for (int i = 1; i <= colCount; i++) {
            columnName = rsmd.getColumnName(i);
            columnType= rsmd.getColumnType(i);
            colTypeMap.put(columnName, columnType);
        }
        
        java.sql.Date valDte;
        java.sql.Timestamp valTime;
        for (String propName : fieldMap.keySet()) {
            colName = fieldMap.get(propName);
            writeMethod = methodMap.get(propName);
            // Get DataType of column
            try {

                if (colTypeMap.containsKey(colName)) {
                    colType = colTypeMap.get(colName);
                    
                    switch (colType) {
                    case Types.INTEGER:
                        writeMethod.invoke(entity, rs.getInt(colName));
                        break;
                    case Types.VARCHAR:
                    case Types.NVARCHAR:
                        writeMethod.invoke(entity, rs.getString(colName));
                        break;
                    case Types.DATE:
                        valDte = rs.getDate(colName);
                        if (valDte != null) {
                            writeMethod.invoke(entity, new Date(valDte.getTime()));
                        }
                        break;
                    case Types.TIMESTAMP:
                        valTime = rs.getTimestamp(colName);
                        if (valTime != null) {
                            writeMethod.invoke(entity, new Date(valTime.getTime()));
                        }
                        break;
                }

                } else {
                    LogService.logWarn("Could not found column name '" + colName + "");
                }
            } catch (IllegalArgumentException e) {
                LogService.logError(this.getClass(), e);
            } catch (IllegalAccessException e) {
                LogService.logError(this.getClass(), e);
            } catch (InvocationTargetException e) {
                LogService.logError(this.getClass(), e);
            }
            
        }
        
    }

    private Map<String, Method> getWriteMethodMap(Object entity) {
        if (writeMethodMap != null) {
            return writeMethodMap;
        }
        // <property, WriteMethod object>
        writeMethodMap = new HashMap<String, Method>();
        
        // Analyze the bean to build the methodMap
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
            for (PropertyDescriptor propDes : beanInfo.getPropertyDescriptors()) {
                writeMethodMap.put(propDes.getName(), propDes.getWriteMethod());
            }
        } catch (IntrospectionException iex) {
            LogService.logError(this, iex);
        }
        return writeMethodMap;
    }

}
