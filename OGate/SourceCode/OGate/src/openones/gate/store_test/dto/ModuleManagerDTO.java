/**
 * 
 */
package openones.gate.store_test.dto;

import java.util.Date;

/**
 * @author katherine
 *
 */
public class ModuleManagerDTO {
	private int managerID;
	private AccountDTO manager;
	private int moduleID;
	private ModuleDTO module;
	private Date createdTime;
	private int creatorID;
	private AccountDTO creator;
	private Date lastModifiedTime;
	private int lastModifierID;
	private AccountDTO lastModifier;
	/**
	 * @return the managerID
	 */
	public int getManagerID() {
		return managerID;
	}
	/**
	 * @param managerID the managerID to set
	 */
	public void setManagerID(int managerID) {
		this.managerID = managerID;
	}
	/**
	 * @return the manager
	 */
	public AccountDTO getManager() {
		return manager;
	}
	/**
	 * @param manager the manager to set
	 */
	public void setManager(AccountDTO manager) {
		this.manager = manager;
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
	public ModuleManagerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param manager
	 * @param module
	 * @param createdTime
	 * @param creator
	 * @param lastModifiedTime
	 * @param lastModifier
	 */
	public ModuleManagerDTO(AccountDTO manager, ModuleDTO module,
			Date createdTime, AccountDTO creator, Date lastModifiedTime,
			AccountDTO lastModifier) {
		super();
		this.manager = manager;
		this.module = module;
		this.createdTime = createdTime;
		this.creator = creator;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifier = lastModifier;
	}
	/**
	 * @param managerID
	 * @param moduleID
	 * @param createdTime
	 * @param creatorID
	 * @param lastModifiedTime
	 * @param lastModifierID
	 */
	public ModuleManagerDTO(int managerID, int moduleID, Date createdTime,
			int creatorID, Date lastModifiedTime, int lastModifierID) {
		super();
		this.managerID = managerID;
		this.moduleID = moduleID;
		this.createdTime = createdTime;
		this.creatorID = creatorID;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifierID = lastModifierID;
	}
	
	
}
