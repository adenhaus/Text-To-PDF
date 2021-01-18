package txttopdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

/**
 * Contains the main method, this is the class from which the program is executed
 * @author Aden Haussmann
 */
public class TextToPdf {

    public static void main(String[] args) throws IOException {

        /**
         * Get lines from txt file
         * Create a PdfWriter
         * Create a PdfDocument
         * Create a Document
         * Create a Paragraph
         */
        String[] items = getFileElements("file.txt");
        String dest = "txttopdf.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        Paragraph fstPara = new Paragraph();
        System.out.println("Applying formatting rules...");

        /**
         * The core of the program is inside a while loop so you can print the result multiple times
         */
        int numTimesPrint = 0;

        while (numTimesPrint < 9) {

            /**
             * Create a TextFormatter
             * Process the text and add paragraphs to the document
             */
            TextFormatter txtFormat = new TextFormatter();
            txtFormat.processText(fstPara, document, items);

            /**
             * The paragraph is emptied so the process can be started from scratch
             */
            fstPara = new Paragraph();
            numTimesPrint++;
             
        }

        /**
         * Close the document  
         */
        document.close();        
        System.out.println("PDF Created");
        
    }

    /**
     * Takes the .txt input file and creates an array of elements by splitting on newlines
     * @param path The path of the .txt file to be used
     * @return An array containing each line of the .txt fie
     * @throws FileNotFoundException
     */
    public static String[] getFileElements(String path) throws FileNotFoundException {

        String data = "";

        File inputFile = new File(path);
        Scanner sc = new Scanner(inputFile);
        while (sc.hasNext()) {
            data += sc.nextLine() + "\n";
        }
        sc.close();

        return data.split("\n");

    }
    
}
