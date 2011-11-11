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
	private String langCd;
	private LangDTO lang;
	private String name;

	/**
	 * @return the permissionID
	 */
	public int getPermissionID() {
		return permissionID;
	}

	/**
	 * @param permissionID
	 *            the permissionID to set
	 */
	public void setPermissionID(int permissionID) {
		this.permissionID = permissionID;
	}

	/**
	 * @return the langCd
	 */
	public String getLangCd() {
		return langCd;
	}

	/**
	 * @param langCd
	 *            the langCd to set
	 */
	public void setLangCd(String langCd) {
		this.langCd = langCd;
	}

	/**
	 * @return the lang
	 */
	public LangDTO getLang() {
		return lang;
	}

	/**
	 * @param lang
	 *            the lang to set
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
	 * @param name
	 *            the name to set
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

	/**
	 * @param permissionID
	 * @param langCd
	 * @param name
	 */
	public PermissionDTO(int permissionID, String langCd, String name) {
		super();
		this.permissionID = permissionID;
		this.langCd = langCd;
		this.name = name;
	}

}
