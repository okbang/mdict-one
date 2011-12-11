package csdl.locc.measures.java.javaline;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import csdl.locc.measures.java.parser.javacc.ParseException;
import csdl.locc.measures.cpp.parser.javacc.TokenMgrError;
import csdl.locc.measures.java.parser.javacc.CompilationUnit;
import csdl.locc.measures.java.parser.javacc.JavaParser;

import rocky.common.LogService;
import rocky.sizecounter.ISizeCounter;
import rocky.sizecounter.SizeMetaData;
import rocky.sizecounter.SourceMetaData;
import rocky.sizecounter.UnsupportedFileType;

public class LoccUtil implements ISizeCounter {
    /*
	public static Document parseFile(String filePath) throws FileNotFoundException, ParseException {
		CompilationUnit cu = createCU(filePath);
		Document outXmlDoc = createTemplateDoc();

		JavaParserVisitor visitor = new XmlTotalVisitor(new File(filePath), outXmlDoc);
		
		cu.jjtAccept(visitor, null);
		
		return outXmlDoc;
	}
	
	public static CompilationUnit createCU(String filePath) throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream(filePath);
		JavaParser jParser = new JavaParser(fis);
		CompilationUnit cu = jParser.TopLevel();
		return cu;
	}
    */
    
	public SourceMetaData countLOC(String filePath, String csName) {
	    SourceMetaData srcMd = null;
		FileInputStream fis = null;
		
		try {
		    if (filePath.endsWith(".java")) {
                srcMd = new SourceMetaData();
                fis = new FileInputStream(filePath);

                JavaParser jParser = (csName != null) ? new JavaParser(fis, csName) : new JavaParser(fis);
                CompilationUnit cu = jParser.TopLevel();

                srcMd.setLoc(cu.getNumLines());
                srcMd.setComment(cu.getNumCommentLines());
            }
		} catch (TokenMgrError tmex) {
		    LogService.logError(this.getClass(), tmex);
		} catch (FileNotFoundException e) {
		    LogService.logError(this.getClass(), e);
        } catch (UnsupportedEncodingException e) {
            LogService.logError(this.getClass(), e);
        } catch (ParseException e) {
            LogService.logError(this.getClass(), e);
        } finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// Do nothing
				}
			}
		}
        
        return srcMd;
	}
	/*
	private static Document createTemplateDoc() {
		Document outXmlDoc = new Document();
		Element element = new Element("root");
		outXmlDoc.setRootElement(element);
		
		return outXmlDoc;
	}
	*/

	@Override
    public SourceMetaData countLOC(String filePath) throws UnsupportedFileType {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
	public boolean isCountable(String extFile) {
		// TODO Auto-generated method stub
		return false;
	}

    /* 
     * Explain the description for this method here
     * @see rocky.sizecounter.ISizeCounter#countSize(java.lang.String)
     */
    @Override
    public SizeMetaData countSize(String filePath) throws UnsupportedFileType {
        // TODO Auto-generated method stub
        return null;
    }

}
