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

@WebServlet(name = "UploadFileServlet", urlPatterns = {"/uploadFile"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
        maxFileSize = 1024 * 1024 * 200, // 20MB
        maxRequestSize = 1024 * 1024 * 200) // 50MB
public class UploadFileServletExplain extends HttpServlet {

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
        //Lấy ra đường dẫn của chương trình (đường dẫn cứng)
        //VD: "C:\Users\thanh\Desktop\java-modules\UploadFile\build\web\"
        String appPath = request.getServletContext().getRealPath("");
        //Sau đó thêm đường dẫn mà mong muốn vào
        String fullSavePath = appPath + File.separator + SAVE_DIRECTORY;
        //VD: C:\Users\thanh\Desktop\java-modules\UploadFile\build\web\\uploads

        //Do fullSavePath chỉ lưu file ở folder build (Clean là đi)
        //Nên cần lưu file ở đường dẫn source web => File sẽ được lưu ở 2 nơi 
        String sourceSavePath = fullSavePath.replace(File.separator + "build", "");
        //VD: C:\Users\thanh\Desktop\java-modules\UploadFile\web\\uploads
        ArrayList<String> savePaths = new ArrayList<>();
        savePaths.add(sourceSavePath);
        savePaths.add(fullSavePath);
        for (String savePath : savePaths) {
            File fileSaveDir = new File(savePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }
        }
        //Đây sẽ là đường dẫn được lưu vào database
        String MyPath = saveFile(request, "file", savePaths);
        if (MyPath != null) {
            response.sendRedirect(MyPath);
        }
    }

    //hàm này sẽ trả về đường dẫn web, lấy và lưu vào database
    private String saveFile(HttpServletRequest request, String partName, ArrayList<String> savePaths) {
        String myPath = null;
        try {
            Part part = request.getPart(partName);
            String fileName = extractFileName(part);
            if (fileName != null && fileName.length() > 0) {
                for (String savePath : savePaths) {
                    part.write(savePath + File.separator + fileName);
                }
                myPath = SAVE_DIRECTORY + "/" + fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myPath;
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                String clientFileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                clientFileName = clientFileName.replace("\\", "/");
                int i = clientFileName.lastIndexOf('/');
                return clientFileName.substring(i + 1);
            }
        }
        return null;
    }



}
