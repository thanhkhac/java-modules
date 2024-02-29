package doctopdf;

import com.aspose.words.Document;
import java.awt.print.PrinterJob;
import java.util.Scanner;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;

/**
 *
 * @author thanh
 */
public class DocToPDF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Document doc = new Document("C:\\Users\\thanh\\Desktop\\Attendance System SRS.docx");
            doc.save("test.html");
            System.out.println("Success");
        } catch (Exception e) {
        }
    }

}
