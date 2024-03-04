package WEB;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import java.io.File;
import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import myutils.UploadFile;

@WebServlet(name = "UploadFileServlet", urlPatterns = {"/uploadFile"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
        maxFileSize = 1024 * 1024 * 200, // 20MB
        maxRequestSize = 1024 * 1024 * 200) // 50MB
public class UploadFileServlet extends HttpServlet {

    public static final String SAVE_DIRECTORY = "uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/uploadFile.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String appPath = request.getServletContext().getRealPath("");
        String fullSavePath = appPath + File.separator + SAVE_DIRECTORY;
        String backUpPath = fullSavePath.replace(File.separator + "build", "");
        ArrayList<String> savePaths = new ArrayList<>();
        savePaths.add(backUpPath);
        savePaths.add(fullSavePath);
        
        //Đây sẽ là đường dẫn cần lấy và lưu vào database
        String MyPath = new UploadFile().saveFile(request, "file", savePaths, SAVE_DIRECTORY);
        if (MyPath != null) {
            response.sendRedirect(MyPath);
        }
    }



}
