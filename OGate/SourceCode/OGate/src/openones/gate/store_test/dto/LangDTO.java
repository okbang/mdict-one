/**
 * 
 */
package openones.gate.store_test.dto;

import java.util.List;

/**
 * @author katherine
 *
 */
public class LangDTO {
	private String langCd;
	private String name;
	private List<PermissionDTO> permissions;
	private List<ModuleDTO> modules;
	private List<ModuleContentDTO> moduleContents;
	private List<ModuleManagerDTO> moduleManagers;
	private List<ModuleContentDTO> moduleTypes;
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
	 * @return the permissions
	 */
	public List<PermissionDTO> getPermissions() {
		return permissions;
	}
	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(List<PermissionDTO> permissions) {
		this.permissions = permissions;
	}
	/**
	 * @return the modules
	 */
	public List<ModuleDTO> getModules() {
		return modules;
	}
	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<ModuleDTO> modules) {
		this.modules = modules;
	}
	/**
	 * @return the moduleContents
	 */
	public List<ModuleContentDTO> getModuleContents() {
		return moduleContents;
	}
	/**
	 * @param moduleContents the moduleContents to set
	 */
	public void setModuleContents(List<ModuleContentDTO> moduleContents) {
		this.moduleContents = moduleContents;
	}
	/**
	 * @return the moduleManagers
	 */
	public List<ModuleManagerDTO> getModuleManagers() {
		return moduleManagers;
	}
	/**
	 * @param moduleManagers the moduleManagers to set
	 */
	public void setModuleManagers(List<ModuleManagerDTO> moduleManagers) {
		this.moduleManagers = moduleManagers;
	}
	/**
	 * @return the moduleTypes
	 */
	public List<ModuleContentDTO> getModuleTypes() {
		return moduleTypes;
	}
	/**
	 * @param moduleTypes the moduleTypes to set
	 */
	public void setModuleTypes(List<ModuleContentDTO> moduleTypes) {
		this.moduleTypes = moduleTypes;
	}
	/**
	 * 
	 */
	public LangDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param langCd
	 * @param name
	 */
	public LangDTO(String langCd, String name) {
		super();
		this.langCd = langCd;
		this.name = name;
	}
	/**
	 * @param langCd
	 * @param name
	 * @param permissions
	 * @param modules
	 * @param moduleContents
	 * @param moduleManagers
	 * @param moduleTypes
	 */
	public LangDTO(String langCd, String name, List<PermissionDTO> permissions,
			List<ModuleDTO> modules, List<ModuleContentDTO> moduleContents,
			List<ModuleManagerDTO> moduleManagers,
			List<ModuleContentDTO> moduleTypes) {
		super();
		this.langCd = langCd;
		this.name = name;
		this.permissions = permissions;
		this.modules = modules;
		this.moduleContents = moduleContents;
		this.moduleManagers = moduleManagers;
		this.moduleTypes = moduleTypes;
	}
	
	
}
