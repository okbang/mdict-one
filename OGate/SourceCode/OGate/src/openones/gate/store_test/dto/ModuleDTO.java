/**
 * 
 */
package openones.gate.store_test.dto;

import java.util.Date;
import java.util.List;

/**
 * @author katherine
 *
 */
public class ModuleDTO {
	private int moduleID;
	private ModuleDTO module;
	private LangDTO lang;
	private String name;
	private String content;
	private ModuleTypeDTO type;
	private int orderNo;
	private AccountDTO creator;
	private Date lastModifiedTime;
	private AccountDTO lastModifier;
	private Date createdTime;
	private ModuleContentDTO moduleContent;
	private List<AccountDTO> managers;
	private List<ModuleContentDTO> multiLangModuleContents;
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
	 * @return the type
	 */
	public ModuleTypeDTO getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(ModuleTypeDTO type) {
		this.type = type;
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
	 * @return the moduleContent
	 */
	public ModuleContentDTO getModuleContent() {
		return moduleContent;
	}
	/**
	 * @param moduleContent the moduleContent to set
	 */
	public void setModuleContent(ModuleContentDTO moduleContent) {
		this.moduleContent = moduleContent;
	}
	/**
	 * @return the managers
	 */
	public List<AccountDTO> getManagers() {
		return managers;
	}
	/**
	 * @param managers the managers to set
	 */
	public void setManagers(List<AccountDTO> managers) {
		this.managers = managers;
	}
	/**
	 * @return the multiLangModuleContents
	 */
	public List<ModuleContentDTO> getMultiLangModuleContents() {
		return multiLangModuleContents;
	}
	/**
	 * @param multiLangModuleContents the multiLangModuleContents to set
	 */
	public void setMultiLangModuleContents(
			List<ModuleContentDTO> multiLangModuleContents) {
		this.multiLangModuleContents = multiLangModuleContents;
	}
	/**
	 * 
	 */
	public ModuleDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param moduleID
	 * @param lang
	 * @param name
	 * @param content
	 * @param type
	 * @param orderNo
	 * @param creator
	 * @param lastModifiedTime
	 * @param lastModifier
	 * @param createdTime
	 */
	public ModuleDTO(int moduleID, LangDTO lang, String name, String content,
			ModuleTypeDTO type, int orderNo, AccountDTO creator,
			Date lastModifiedTime, AccountDTO lastModifier, Date createdTime) {
		super();
		this.moduleID = moduleID;
		this.lang = lang;
		this.name = name;
		this.content = content;
		this.type = type;
		this.orderNo = orderNo;
		this.creator = creator;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifier = lastModifier;
		this.createdTime = createdTime;
	}
	/**
	 * @param moduleID
	 * @param module
	 * @param lang
	 * @param name
	 * @param content
	 * @param type
	 * @param orderNo
	 * @param creator
	 * @param lastModifiedTime
	 * @param lastModifier
	 * @param createdTime
	 * @param moduleContent
	 * @param managers
	 * @param multiLangModuleContents
	 */
	public ModuleDTO(int moduleID, ModuleDTO module, LangDTO lang,
			String name, String content, ModuleTypeDTO type, int orderNo,
			AccountDTO creator, Date lastModifiedTime, AccountDTO lastModifier,
			Date createdTime, ModuleContentDTO moduleContent,
			List<AccountDTO> managers,
			List<ModuleContentDTO> multiLangModuleContents) {
		super();
		this.moduleID = moduleID;
		this.module = module;
		this.lang = lang;
		this.name = name;
		this.content = content;
		this.type = type;
		this.orderNo = orderNo;
		this.creator = creator;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifier = lastModifier;
		this.createdTime = createdTime;
		this.moduleContent = moduleContent;
		this.managers = managers;
		this.multiLangModuleContents = multiLangModuleContents;
	}
	
	
	
}
