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
public class AccountDTO {
	private int accountID;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private Date createdTime;
	private PermissionDTO permission;
	private List<ModuleDTO> createdModules;
	private List<ModuleDTO> modifiedModules;
	private List<ModuleContentDTO> createdModuleContents;
	private List<ModuleContentDTO> modifiedModuleContents;
	private List<ModuleManagerDTO> managedModules;
	private List<ModuleManagerDTO> createdModuleManagers;
	private List<ModuleManagerDTO> modifiedModuleManagers;
	private List<ModuleTypeDTO> createdModuleTypes;
	private List<ModuleTypeDTO> modifiedModuleTypes;
	/**
	 * @return the accountID
	 */
	public int getAccountID() {
		return accountID;
	}
	/**
	 * @param accountID the accountID to set
	 */
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the permission
	 */
	public PermissionDTO getPermission() {
		return permission;
	}
	/**
	 * @param permission the permission to set
	 */
	public void setPermission(PermissionDTO permission) {
		this.permission = permission;
	}
	/**
	 * @return the createdModules
	 */
	public List<ModuleDTO> getCreatedModules() {
		return createdModules;
	}
	/**
	 * @param createdModules the createdModules to set
	 */
	public void setCreatedModules(List<ModuleDTO> createdModules) {
		this.createdModules = createdModules;
	}
	/**
	 * @return the modifiedModules
	 */
	public List<ModuleDTO> getModifiedModules() {
		return modifiedModules;
	}
	/**
	 * @param modifiedModules the modifiedModules to set
	 */
	public void setModifiedModules(List<ModuleDTO> modifiedModules) {
		this.modifiedModules = modifiedModules;
	}
	/**
	 * @return the createdModuleContents
	 */
	public List<ModuleContentDTO> getCreatedModuleContents() {
		return createdModuleContents;
	}
	/**
	 * @param createdModuleContents the createdModuleContents to set
	 */
	public void setCreatedModuleContents(
			List<ModuleContentDTO> createdModuleContents) {
		this.createdModuleContents = createdModuleContents;
	}
	/**
	 * @return the modifiedModuleContents
	 */
	public List<ModuleContentDTO> getModifiedModuleContents() {
		return modifiedModuleContents;
	}
	/**
	 * @param modifiedModuleContents the modifiedModuleContents to set
	 */
	public void setModifiedModuleContents(
			List<ModuleContentDTO> modifiedModuleContents) {
		this.modifiedModuleContents = modifiedModuleContents;
	}
	/**
	 * @return the managedModules
	 */
	public List<ModuleManagerDTO> getManagedModules() {
		return managedModules;
	}
	/**
	 * @param managedModules the managedModules to set
	 */
	public void setManagedModules(List<ModuleManagerDTO> managedModules) {
		this.managedModules = managedModules;
	}
	/**
	 * @return the createdModuleManagers
	 */
	public List<ModuleManagerDTO> getCreatedModuleManagers() {
		return createdModuleManagers;
	}
	/**
	 * @param createdModuleManagers the createdModuleManagers to set
	 */
	public void setCreatedModuleManagers(
			List<ModuleManagerDTO> createdModuleManagers) {
		this.createdModuleManagers = createdModuleManagers;
	}
	/**
	 * @return the modifiedModuleManagers
	 */
	public List<ModuleManagerDTO> getModifiedModuleManagers() {
		return modifiedModuleManagers;
	}
	/**
	 * @param modifiedModuleManagers the modifiedModuleManagers to set
	 */
	public void setModifiedModuleManagers(
			List<ModuleManagerDTO> modifiedModuleManagers) {
		this.modifiedModuleManagers = modifiedModuleManagers;
	}
	/**
	 * @return the createdModuleTypes
	 */
	public List<ModuleTypeDTO> getCreatedModuleTypes() {
		return createdModuleTypes;
	}
	/**
	 * @param createdModuleTypes the createdModuleTypes to set
	 */
	public void setCreatedModuleTypes(List<ModuleTypeDTO> createdModuleTypes) {
		this.createdModuleTypes = createdModuleTypes;
	}
	/**
	 * @return the modifiedModuleTypes
	 */
	public List<ModuleTypeDTO> getModifiedModuleTypes() {
		return modifiedModuleTypes;
	}
	/**
	 * @param modifiedModuleTypes the modifiedModuleTypes to set
	 */
	public void setModifiedModuleTypes(List<ModuleTypeDTO> modifiedModuleTypes) {
		this.modifiedModuleTypes = modifiedModuleTypes;
	}
	/**
	 * 
	 */
	public AccountDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param accountID
	 * @param password
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param createdTime
	 * @param permission
	 */
	public AccountDTO(int accountID, String password, String email,
			String firstName, String lastName, Date createdTime,
			PermissionDTO permission) {
		super();
		this.accountID = accountID;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.createdTime = createdTime;
		this.permission = permission;
	}
	

	/**
	 * @param accountID
	 * @param password
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param createdTime
	 * @param permission
	 * @param createdModules
	 * @param modifiedModules
	 * @param createdModuleContents
	 * @param modifiedModuleContents
	 * @param managedModules
	 * @param createdModuleManagers
	 * @param modifiedModuleManagers
	 * @param createdModuleTypes
	 * @param modifiedModuleTypes
	 */
	public AccountDTO(int accountID, String password, String email,
			String firstName, String lastName, Date createdTime,
			PermissionDTO permission, List<ModuleDTO> createdModules,
			List<ModuleDTO> modifiedModules,
			List<ModuleContentDTO> createdModuleContents,
			List<ModuleContentDTO> modifiedModuleContents,
			List<ModuleManagerDTO> managedModules,
			List<ModuleManagerDTO> createdModuleManagers,
			List<ModuleManagerDTO> modifiedModuleManagers,
			List<ModuleTypeDTO> createdModuleTypes,
			List<ModuleTypeDTO> modifiedModuleTypes) {
		super();
		this.accountID = accountID;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.createdTime = createdTime;
		this.permission = permission;
		this.createdModules = createdModules;
		this.modifiedModules = modifiedModules;
		this.createdModuleContents = createdModuleContents;
		this.modifiedModuleContents = modifiedModuleContents;
		this.managedModules = managedModules;
		this.createdModuleManagers = createdModuleManagers;
		this.modifiedModuleManagers = modifiedModuleManagers;
		this.createdModuleTypes = createdModuleTypes;
		this.modifiedModuleTypes = modifiedModuleTypes;
	}
	
	
}
