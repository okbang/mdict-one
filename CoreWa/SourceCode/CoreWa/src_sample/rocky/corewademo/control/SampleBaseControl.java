package rocky.corewademo.control;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import openones.corewa.control.BaseControl;

public class SampleBaseControl extends BaseControl {
    final static Logger LOG = Logger.getLogger("Control");
    
    public void procInit(HttpServletRequest request, HttpServletResponse response, String nextScreenId) throws ServletException, IOException {
        LOG.log(Level.INFO, "procInit.START");
        request.getRequestDispatcher(nextScreenId).forward(request, response);
        LOG.log(Level.INFO, "procInit.END");
    }

}
