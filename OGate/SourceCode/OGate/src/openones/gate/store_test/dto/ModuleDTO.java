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
	private String langCd;
	private LangDTO lang;
	private String name;
	private String content;
	private ModuleTypeDTO type;
	private int orderNo;
	private Date createdTime;
	private int creatorID;
	private AccountDTO creator;
	private Date lastModifiedTime;
	private int lastModifierID;
	private AccountDTO lastModifier;
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
	 * @param moduleID
	 *            the moduleID to set
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
	 * @param module
	 *            the module to set
	 */
	public void setModule(ModuleDTO module) {
		this.module = module;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
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
	 * @param type
	 *            the type to set
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
	 * @param orderNo
	 *            the orderNo to set
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
	 * @return the moduleContent
	 */
	public ModuleContentDTO getModuleContent() {
		return moduleContent;
	}

	/**
	 * @return the managers
	 */
	public List<AccountDTO> getManagers() {
		return managers;
	}

	/**
	 * @param managers
	 *            the managers to set
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
	 * 
	 */
	public ModuleDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param moduleID
	 * @param langCd
	 * @param name
	 * @param content
	 * @param type
	 * @param orderNo
	 * @param createdTime
	 * @param creatorID
	 * @param lastModifiedTime
	 * @param lastModifierID
	 * @param managers
	 */
	public ModuleDTO(int moduleID, String langCd, String name, String content,
			ModuleTypeDTO type, int orderNo, Date createdTime, int creatorID,
			Date lastModifiedTime, int lastModifierID, List<AccountDTO> managers) {
		super();
		this.moduleID = moduleID;
		this.langCd = langCd;
		this.name = name;
		this.content = content;
		this.type = type;
		this.orderNo = orderNo;
		this.createdTime = createdTime;
		this.creatorID = creatorID;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifierID = lastModifierID;
		this.managers = managers;
	}

	/**
	 * @param module
	 * @param lang
	 * @param name
	 * @param content
	 * @param type
	 * @param orderNo
	 * @param createdTime
	 * @param creator
	 * @param lastModifiedTime
	 * @param lastModifier
	 * @param managers
	 */
	public ModuleDTO(ModuleDTO module, LangDTO lang, String name,
			String content, ModuleTypeDTO type, int orderNo, Date createdTime,
			AccountDTO creator, Date lastModifiedTime, AccountDTO lastModifier,
			List<AccountDTO> managers) {
		super();
		this.module = module;
		this.lang = lang;
		this.name = name;
		this.content = content;
		this.type = type;
		this.orderNo = orderNo;
		this.createdTime = createdTime;
		this.creator = creator;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifier = lastModifier;
		this.managers = managers;
	}

}
