package openones.corewa.config;


public class Result {
    private String id;
    private String nextScrId;
    
    public Result() {
    }
    
    public Result(String id, String nextScrId) {
        this.id = id;
        this.nextScrId = nextScrId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNextScrId() {
        return nextScrId;
    }

    public void setNextScrId(String nextScrId) {
        this.nextScrId = nextScrId;
    }
}
