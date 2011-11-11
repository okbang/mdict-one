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
	private AccountDTO manager;
	private ModuleDTO module;
	private Date createdTime;
	private AccountDTO creator;
	private AccountDTO lastModifier;
	private Date lastModifiedTime;
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
	public ModuleManagerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param manager
	 * @param module
	 * @param createdTime
	 * @param creator
	 * @param lastModifier
	 * @param lastModifiedTime
	 */
	public ModuleManagerDTO(AccountDTO manager, ModuleDTO module,
			Date createdTime, AccountDTO creator, AccountDTO lastModifier,
			Date lastModifiedTime) {
		super();
		this.manager = manager;
		this.module = module;
		this.createdTime = createdTime;
		this.creator = creator;
		this.lastModifier = lastModifier;
		this.lastModifiedTime = lastModifiedTime;
	}
	
	
}
