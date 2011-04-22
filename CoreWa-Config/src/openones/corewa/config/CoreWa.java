package openones.corewa.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data model of xml configuration with below format:
 * <root>
    <home-screen>PerCal</home-screen>
    <form id="AddEventBean" class="rocky.googleapp.lunarcal.form"/>
    
    
    <screen id="Layout" control="" input="">
        <event id="" procId="" nextScreen=""></event>
   </root>
 * @author Thach Le
 *
 */
public class CoreWa {
    /** <home-screen>. */
    String homeScreenId;

    /** Store list of id in tag main-layout. */
    List<String> layoutIDList;
    
    String viewScreen;
	String editScreen;
	String helpScreen;
	private Map<String, Screen> screens = new HashMap<String, Screen>();

	/** Store tag <form>. */
    private Map<String, Form> forms = new HashMap<String, Form>();
	
    private Layout layout;
    
    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public String getViewScreen() {
		return viewScreen;
	}

	public void setViewScreen(String viewScreen) {
		this.viewScreen = viewScreen;
	}

	public String getEditScreen() {
		return editScreen;
	}

	public void setEditScreen(String editScreen) {
		this.editScreen = editScreen;
	}

	public String getHelpScreen() {
		return helpScreen;
	}

	public void setHelpScreen(String helpScreen) {
		this.helpScreen = helpScreen;
	}

	    public Map<String, Screen> getScreens() {
        return screens;
    }

    public void setScreens(Map<String, Screen> screens) {
        this.screens = screens;
    }
    
    public void addScreen(Screen scr) {
        screens.put(scr.getId(), scr);
    }
    
    public Screen getScreen(String id) {
        return screens.get(id);
    }
    
    public String getHomeScreenId() {
        return homeScreenId;
    }

    public void setHomeScreenId(String homeScreenId) {
        this.homeScreenId = homeScreenId;
    }

    public List<String> getLayoutIDList() {
        return layoutIDList;
    }

    public void setLayoutIDList(List<String> layoutIDList) {
        this.layoutIDList = layoutIDList;
    }

    public Map<String, Form> getForms() {
        return forms;
    }

    public void setForms(Map<String, Form> forms) {
        this.forms = forms;
    }
}
