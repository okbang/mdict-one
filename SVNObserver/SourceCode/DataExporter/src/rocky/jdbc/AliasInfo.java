package rocky.jdbc;

public class AliasInfo {
    private String aliasName;
    private int serverType; 
    private String serverName;
    private Integer port;
    private String databaseName;
    private String userName;
    private String password;
    public String getAliasName() {
        return aliasName;
    }
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    public int getServerType() {
        return serverType;
    }
    public void setServerType(int serverType) {
        this.serverType = serverType;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public String getDatabaseName() {
        return databaseName;
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
