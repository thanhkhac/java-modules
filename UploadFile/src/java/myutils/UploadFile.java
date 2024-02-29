package myutils;

import static WEB.UploadFileServlet.SAVE_DIRECTORY;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.File;
import java.util.ArrayList;

public class UploadFile {

    //hàm này sẽ trả về đường dẫn web, lấy và lưu vào database
    public String saveFile(HttpServletRequest request, String partName, ArrayList<String> savePaths, String webPath) {
        String myPath = null;
        //Tạo folder nếu chưa có
        for (String savePath : savePaths) {
            File fileSaveDir = new File(savePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }
        }
        try {
            Part part = request.getPart(partName);
            String fileName = extractFileName(part);
            if (fileName != null && fileName.length() > 0) {
                fileName = generateUniqueFileName(fileName);
                for (String savePath : savePaths) {
                    part.write(savePath + File.separator + fileName);
                }
                myPath = webPath + "/" + fileName;
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
    
    // Sử dụng mã UUID để tạo ra tên riêng biệt
    private String generateUniqueFileName(String originalFileName) {
        TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        
        return uuidGenerator.generate().toString() + extension;
    }
}
