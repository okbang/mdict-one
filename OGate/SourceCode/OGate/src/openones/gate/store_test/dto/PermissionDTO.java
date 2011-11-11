/**
 * 
 */
package openones.gate.store_test.dto;

/**
 * @author katherine
 *
 */
public class PermissionDTO {
	
	private int permissionID;
	private LangDTO lang;
	private String name;
	
	/**
	 * @return the permissionID
	 */
	public int getPermissionID() {
		return permissionID;
	}
	/**
	 * @param permissionID the permissionID to set
	 */
	public void setPermissionID(int permissionID) {
		this.permissionID = permissionID;
	}
	/**
	 * @return the lang
	 */
	public LangDTO getLang() {
		return lang;
	}
	/**
	 * @param langCd the langCd to set
	 */
	public void setLang(LangDTO lang) {
		this.lang = lang;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * 
	 */
	public PermissionDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param permissionID
	 * @param lang
	 * @param name
	 */
	public PermissionDTO(int permissionID, LangDTO lang, String name) {
		super();
		this.permissionID = permissionID;
		this.lang = lang;
		this.name = name;
	}
	
	
}
