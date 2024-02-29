package WEB;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@WebServlet(name = "DownloadFile", urlPatterns = {"/DownloadFile"})
public class DownloadFile extends HttpServlet {

    public static final String SAVE_DIRECTORY = "uploads";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String fileName = request.getParameter("fileName");

        // Get the real path of the file
        String appPath = request.getServletContext().getRealPath("");
        String filePath = appPath + File.separator + SAVE_DIRECTORY + File.separator +
                fileName;

        // Set response content type
        response.setContentType("application/octet-stream");

        // Set the HTTP header for the file download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(headerKey, headerValue);

        // Get the output stream of the response
        try ( OutputStream outStream = response.getOutputStream();
                 FileInputStream fis = new FileInputStream(filePath)) {

            // Read the file from the input stream and write it to the output stream
            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = fis.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
// </editor-fold>

}
