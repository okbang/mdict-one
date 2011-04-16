package rocky.corewademo.control;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MenuControl extends SampleBaseControl {
    
    public void procList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.log(Level.INFO, "procList.START");
        LOG.log(Level.INFO, "procList.END");
    }
    public void procAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.log(Level.INFO, "procAdd.START");
        LOG.log(Level.INFO, "procAdd.END");
    }
}
