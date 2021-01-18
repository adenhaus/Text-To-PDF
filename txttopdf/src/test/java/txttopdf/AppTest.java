package txttopdf;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.junit.Test;

/**
 * Tests that the text in the PDF is correct. This alone is not sufficient
 * testing of the program, but it is one property that can be tested, and serve
 * as a template for testing more properties of the outputted PDF
 * @author Aden Haussmann
 */
public class AppTest {

    /**
     * Creates a test PDF. This is necessary because the main method
     * will repeat the text multiple times
     * @throws IOException
     */
    public void createTestPdf() throws IOException {
        String[] items = getFileContents("file.txt").split("\n");
        String dest = "testpdf.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        Paragraph fstPara = new Paragraph();

        TextFormatter txtFormat = new TextFormatter();
        txtFormat.processText(fstPara, document, items);
        document.close();
    }

    /**
     * Gets String of contents from PDF
     * @param pdf A PDF document
     * @throws IOException
     */
    public String parsePdf(String pdf) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        PdfDocument pdfDoc = new PdfDocument(reader);
        String text = "";
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            String contentOfPage = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i));
            text += contentOfPage;
        }
        pdfDoc.close();
        return text;
    }

    /**
     * Takes the .txt input file and creates an array of elements by splitting on newlines
     * @param path The path of the .txt file to be used
     * @return An array containing each line of the .txt fie
     * @throws FileNotFoundException
     */
    public String getFileContents(String path) throws FileNotFoundException {

        String data = "";

        File inputFile = new File(path);
        Scanner sc = new Scanner(inputFile);
        while (sc.hasNext()) {
            data += sc.nextLine() + "\n";
        }
        sc.close();

        return data;

    }

    /**
     * Removes the commands from the String read from the .txt file
     * @param content the String
     * @return A string without commands
     */
    public String removeCommands(String content) {

        String newContent = content
        .replace(".large", "")
        .replace(".normal", "")
        .replace(".regular", "")
        .replace(".bold", "")
        .replace(".italics", "")
        .replace(".paragraph", "")
        .replace(".indent +2", "")
        .replace(".indent -2", "")
        .replace(".fill", "")
        .replace(".nofill", "");

        return newContent;
    }

    /**
     * Checks that the text in the .txt file, sans commands, matches the text parsed
     * from the created PDF file
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public void testTextPreserved() throws FileNotFoundException, IOException
    {
        String expectedTxt = removeCommands(getFileContents("file.txt")).replace("\n", "").replace(" ", "");
        String actualTxt = parsePdf("testpdf.pdf").replace("\n", "").replace(" ", "");
        assertEquals(expectedTxt, actualTxt);
    }
}
