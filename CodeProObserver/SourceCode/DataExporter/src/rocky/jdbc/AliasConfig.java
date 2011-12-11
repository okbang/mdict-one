package rocky.jdbc;

import java.util.List;

public class AliasConfig {
    private List<AliasInfo> listAliasInfo;
    private int selectedAliasIndex;

    public List<AliasInfo> getListAliasInfo() {
        return listAliasInfo;
    }

    public void setListAliasInfo(List<AliasInfo> listAliasInfo) {
        this.listAliasInfo = listAliasInfo;
    }

    public AliasInfo getAliasByName(String aliasName) {
        AliasInfo retVal = null;
        for (AliasInfo aliasInfo : listAliasInfo) {
            if (aliasInfo.getAliasName().equals(aliasName)) {
                retVal = aliasInfo;
            }
        }
        return retVal;
    }

    public List<AliasInfo> getListAliases() {
        return listAliasInfo;
    }
    
    public AliasInfo getSelectedAlias() {
        return listAliasInfo.get(selectedAliasIndex);
    }
    
    public AliasInfo getAlias(int index) {
        return listAliasInfo.get(index);
    }

    public int getSelectedAliasIndex() {
        return selectedAliasIndex;
    }

    public void setSelectedAliasIndex(int selectedAliasIndex) {
        this.selectedAliasIndex = selectedAliasIndex;
    }

}
