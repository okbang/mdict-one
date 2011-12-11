package rocky.sizecounter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.FailOnTimeout;
import static org.junit.Assert.*;


public class TestCounter {
    @Test
    public void TestJavaFile() {
        ISizeCounter counter = SizeCounterFactory.getDefaultInstance();
        SourceMetaData sizeMd;
        try {

            String path = "D:\\DailyProgress Project\\SVN\\ProgNewsDesk\\trunk\\SVN2RDB\\src\\vn\\fpt\\fsoft\\rai\\svn2db\\SVN2DBBiz.java";
            sizeMd = counter.countLOC(path);
            if(sizeMd == null)
            {
                System.out.println("null sizemd");
            }
            System.out.println("Count line of code: " + path);
            System.out.println("LOC= " + sizeMd.getLoc());
            System.out.println("Comment= " + sizeMd.getComment());
        } catch (UnsupportedFileType e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void TestCSharpFile() {
        ISizeCounter counter = SizeCounterFactory.getDefaultInstance();
        SourceMetaData sizeMd;
        try {

            String path = "D:\\DailyProgress Project\\SVN\\ProgNewsDesk\\trunk\\DesktopReporter\\vn.fpt.fsoft.rai.DesktopReporterApp\\MainForm.Designer.cs";
            sizeMd = counter.countLOC(path);
            System.out.println("Count line of code: " + path);
            System.out.println("LOC= " + sizeMd.getLoc());
            System.out.println("Comment= " + sizeMd.getComment());
        } catch (UnsupportedFileType e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void TestFileNoExtension() {
        ISizeCounter counter = SizeCounterFactory.getDefaultInstance();
        SourceMetaData sizeMd;
        Exception eUnSupportedFileException = null;
        try {

            String path = "D:\\Temp\\FU Student\\HAIS.IEM\\.metadata\\.plugins\\org.eclipse.wst.jsdt.core\\externalLibsTimeStamps";
            sizeMd = counter.countLOC(path);
            if(sizeMd == null)
            {
                System.out.println("null sizemd");
            }
            System.out.println("Count line of code: " + path);
            System.out.println("LOC= " + sizeMd.getLoc());
            System.out.println("Comment= " + sizeMd.getComment());
           
        } catch (UnsupportedFileType e) {
            eUnSupportedFileException = e;
        }
        catch(Exception ex)
        {
                    
        }
        // asset the exception object
        Assert.assertNotNull("No expected exception", eUnSupportedFileException);
        
    }
}
