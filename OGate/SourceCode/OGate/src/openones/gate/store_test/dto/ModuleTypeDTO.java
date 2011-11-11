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
	private String langCd;
	private LangDTO lang;
	private String name;
	private Date createdTime;
	private int creatorID;
	private AccountDTO creator;
	private Date lastModifiedTime;
	private int lastModifierID;
	private AccountDTO lastModifier;

	/**
	 * @return the moduleTypeID
	 */
	public int getModuleTypeID() {
		return moduleTypeID;
	}

	/**
	 * @param moduleTypeID
	 *            the moduleTypeID to set
	 */
	public void setModuleTypeID(int moduleTypeID) {
		this.moduleTypeID = moduleTypeID;
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
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the creatorID
	 */
	public int getCreatorID() {
		return creatorID;
	}

	/**
	 * @param creatorID
	 *            the creatorID to set
	 */
	public void setCreatorID(int creatorID) {
		this.creatorID = creatorID;
	}

	/**
	 * @return the creator
	 */
	public AccountDTO getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(AccountDTO creator) {
		this.creator = creator;
	}

	/**
	 * @return the lastModifiedTime
	 */
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * @param lastModifiedTime
	 *            the lastModifiedTime to set
	 */
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	/**
	 * @return the lastModifierID
	 */
	public int getLastModifierID() {
		return lastModifierID;
	}

	/**
	 * @param lastModifierID
	 *            the lastModifierID to set
	 */
	public void setLastModifierID(int lastModifierID) {
		this.lastModifierID = lastModifierID;
	}

	/**
	 * @return the lastModifier
	 */
	public AccountDTO getLastModifier() {
		return lastModifier;
	}

	/**
	 * @param lastModifier
	 *            the lastModifier to set
	 */
	public void setLastModifier(AccountDTO lastModifier) {
		this.lastModifier = lastModifier;
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
	 * @param lastModifiedTime
	 * @param lastModifier
	 */
	public ModuleTypeDTO(int moduleTypeID, LangDTO lang, String name,
			Date createdTime, AccountDTO creator, Date lastModifiedTime,
			AccountDTO lastModifier) {
		super();
		this.moduleTypeID = moduleTypeID;
		this.lang = lang;
		this.name = name;
		this.createdTime = createdTime;
		this.creator = creator;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifier = lastModifier;
	}

	/**
	 * @param moduleTypeID
	 * @param langCd
	 * @param name
	 * @param createdTime
	 * @param creatorID
	 * @param lastModifiedTime
	 * @param lastModifierID
	 */
	public ModuleTypeDTO(int moduleTypeID, String langCd, String name,
			Date createdTime, int creatorID, Date lastModifiedTime,
			int lastModifierID) {
		super();
		this.moduleTypeID = moduleTypeID;
		this.langCd = langCd;
		this.name = name;
		this.createdTime = createdTime;
		this.creatorID = creatorID;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifierID = lastModifierID;
	}

}
