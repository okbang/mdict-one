package rocky.sizecounter;


public interface ISizeCounter {
    public SourceMetaData countLOC(String filePath, String csName) throws UnsupportedFileType;
    public SourceMetaData countLOC(String filePath) throws UnsupportedFileType;
	public SizeMetaData countSize(String filePath) throws UnsupportedFileType;
    public boolean isCountable(String extFile);
}
