/**
 * 
 */
package openones.gate.store_test.dto;

import java.util.Date;

/**
 * @author katherine
 *
 */
public class ModuleContentDTO {
	private int moduleID;
	private ModuleDTO module;
	private String langCd;
	private LangDTO lang;
	private String content;
	private int orderNo;
	private Date createdTime;
	private int creatorID;
	private AccountDTO creator;
	private Date lastModifiedTime;
	private int lastModifierID;
	private AccountDTO lastModifier;
	/**
	 * @return the langCd
	 */
	public String getLangCd() {
		return langCd;
	}
	/**
	 * @param langCd the langCd to set
	 */
	public void setLangCd(String langCd) {
		this.langCd = langCd;
	}
	/**
	 * @return the creatorID
	 */
	public int getCreatorID() {
		return creatorID;
	}
	/**
	 * @param creatorID the creatorID to set
	 */
	public void setCreatorID(int creatorID) {
		this.creatorID = creatorID;
	}
	/**
	 * @return the lastModifierID
	 */
	public int getLastModifierID() {
		return lastModifierID;
	}
	/**
	 * @param lastModifierID the lastModifierID to set
	 */
	public void setLastModifierID(int lastModifierID) {
		this.lastModifierID = lastModifierID;
	}
	/**
	 * @return the moduleID
	 */
	public int getModuleID() {
		return moduleID;
	}
	/**
	 * @param moduleID the moduleID to set
	 */
	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	/**
	 * @return the module
	 */
	public ModuleDTO getModule() {
		return module;
	}
	/**
	 * @param module the module to set
	 */
	public void setModule(ModuleDTO module) {
		this.module = module;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the orderNo
	 */
	public int getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
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
	 * 
	 */
	public ModuleContentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param moduleID
	 * @param langCd
	 * @param content
	 * @param orderNo
	 * @param createdTime
	 * @param creatorID
	 * @param lastModifiedTime
	 * @param lastModifierID
	 */
	public ModuleContentDTO(int moduleID, String langCd, String content,
			int orderNo, Date createdTime, int creatorID,
			Date lastModifiedTime, int lastModifierID) {
		super();
		this.moduleID = moduleID;
		this.langCd = langCd;
		this.content = content;
		this.orderNo = orderNo;
		this.createdTime = createdTime;
		this.creatorID = creatorID;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifierID = lastModifierID;
	}
	/**
	 * @param module
	 * @param lang
	 * @param content
	 * @param orderNo
	 * @param createdTime
	 * @param creator
	 * @param lastModifiedTime
	 * @param lastModifier
	 */
	public ModuleContentDTO(ModuleDTO module, LangDTO lang, String content,
			int orderNo, Date createdTime, AccountDTO creator,
			Date lastModifiedTime, AccountDTO lastModifier) {
		super();
		this.module = module;
		this.lang = lang;
		this.content = content;
		this.orderNo = orderNo;
		this.createdTime = createdTime;
		this.creator = creator;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifier = lastModifier;
	}
	
	
	
}
