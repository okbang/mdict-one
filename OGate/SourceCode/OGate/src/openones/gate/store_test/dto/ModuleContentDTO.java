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
	private LangDTO lang;
	private String content;
	private int orderNo;
	private Date createdTime;
	private AccountDTO creator;
	private Date lastModifiedTime;
	private AccountDTO lastModifier;
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
	 * @param lang
	 * @param content
	 * @param orderNo
	 * @param createdTime
	 * @param creator
	 * @param lastModifiedTime
	 * @param lastModifier
	 */
	public ModuleContentDTO(int moduleID, LangDTO lang,
			String content, int orderNo, Date createdTime, AccountDTO creator,
			Date lastModifiedTime, AccountDTO lastModifier) {
		super();
		this.moduleID = moduleID;
		this.lang = lang;
		this.content = content;
		this.orderNo = orderNo;
		this.createdTime = createdTime;
		this.creator = creator;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifier = lastModifier;
	}
	/**
	 * @param moduleID
	 * @param module
	 * @param lang
	 * @param content
	 * @param orderNo
	 * @param createdTime
	 * @param creator
	 * @param lastModifiedTime
	 * @param lastModifier
	 */
	public ModuleContentDTO(int moduleID, ModuleDTO module, LangDTO lang,
			String content, int orderNo, Date createdTime, AccountDTO creator,
			Date lastModifiedTime, AccountDTO lastModifier) {
		super();
		this.moduleID = moduleID;
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
