/**
 * 
 */
package openones.gate.store_test.dto;

import java.util.Date;

/**
 * @author katherine
 *
 */
public class ModuleTypeDTO {
	private int moduleTypeID;
	private LangDTO lang;
	private String name;
	private Date createdTime;
	private AccountDTO creator;
	private AccountDTO lastModifier;
	private Date lastModifiedTime;
	/**
	 * @return the moduleTypeID
	 */
	public int getModuleTypeID() {
		return moduleTypeID;
	}
	/**
	 * @param moduleTypeID the moduleTypeID to set
	 */
	public void setModuleTypeID(int moduleTypeID) {
		this.moduleTypeID = moduleTypeID;
	}
	/**
	 * @return the lang
	 */
	public LangDTO getLang() {
		return lang;
	}
	/**
	 * @param lang the lang to set
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
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * @return the creator
	 */
	public AccountDTO getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(AccountDTO creator) {
		this.creator = creator;
	}
	/**
	 * @return the lastModifier
	 */
	public AccountDTO getLastModifier() {
		return lastModifier;
	}
	/**
	 * @param lastModifier the lastModifier to set
	 */
	public void setLastModifier(AccountDTO lastModifier) {
		this.lastModifier = lastModifier;
	}
	/**
	 * @return the lastModifiedTime
	 */
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	/**
	 * @param lastModifiedTime the lastModifiedTime to set
	 */
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	/**
	 * 
	 */
	public ModuleTypeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param moduleTypeID
	 * @param lang
	 * @param name
	 * @param createdTime
	 * @param creator
	 * @param lastModifier
	 * @param lastModifiedTime
	 */
	public ModuleTypeDTO(int moduleTypeID, LangDTO lang, String name,
			Date createdTime, AccountDTO creator, AccountDTO lastModifier,
			Date lastModifiedTime) {
		super();
		this.moduleTypeID = moduleTypeID;
		this.lang = lang;
		this.name = name;
		this.createdTime = createdTime;
		this.creator = creator;
		this.lastModifier = lastModifier;
		this.lastModifiedTime = lastModifiedTime;
	}

	
}
