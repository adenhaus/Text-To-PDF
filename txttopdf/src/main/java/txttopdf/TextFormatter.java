package txttopdf;

import java.io.IOException;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

/**
 * Deals with identifying commands and formatting the text accordingly.
 * The algorithm this class implements is as follows:
 * There are some formatting flags, or switches, which determine how a piece of text
 * or paragraph should be formatted.
 * processText looks at each element in the array and if it is a command, it modifies
 * the corresponding formatting flag. If it is text, it calls formatText, which
 * applies the formatting rules to the current text, and returns the text back to processText,
 * which adds that paragraph to the Document.
 * Having these two methods in the TextFormatter class allows the creation of multiple
 * TextFormatter objects, thus introducing the ability to work with multiple input files
 * and output multiple PDFs easily and clearly.
 * @author Aden Haussmann
 */
public class TextFormatter {

    /**
     * The fields are the formatting flags. They act as switches which can be
     * set with values depending on what formatting command is identified.
     */
    private String fillType;
    private String fontType;
    private String fontSize;
    private int indent;
    private int globalIndent;

    public TextFormatter() {
        this.fillType = "nofill";
        this.fontType = "regular";
        this.fontSize = "normal";
        this.indent = 0;
        this.globalIndent = 0;
    }

    /**
     * Identifies the formatting commands and the text elements, and adds them to Paragraphs,
     * before adding those Paragraphs to the Document
     * @param fstPara An initial (empty) Paragraph which will have formatted text added to it
     * @param document An Document
     * @param items An Array of commands and text
     * @throws IOException
     */
    public void processText(Paragraph fstPara, Document document, String[] items) throws IOException {

        /**
         * The first loop iterates over the elements in the array and assigns values
         * to the formatting flags based on the commands it encounters, or adds text
         * to the paragraph if an element is not a command.
         * If the command .paragraph is encountered, a new paragraph must be created.
         * However, all the other commands do not warrant the creation of a new paragraph,
         * and must be applied to a portion of text in the current paragraph. Therefore,
         * a second loop is needed, entered when text - not a command - is encountered,
         * so that a new Paragraph won't be created every time text is encountered.
         */
        for (int i = 0; i < items.length; i++) {

            if (items[i].equals(".large")) {
                fontSize = "large";
            } else if (items[i].equals(".normal")) {
                fontSize = "normal";
            } else if (items[i].equals(".regular")) {
                fontType = "regular";
            /**
             * This allows the text to be both bold an italicised at the same time.
             */
            } else if (items[i].equals(".bold")) {
                if (fontType.equals("italics")) {
                    fontType = "boldAndItalics";
                } else {
                    fontType = "bold";
                }
            } else if (items[i].equals(".italics")) {
                if (fontType.equals("bold")) {
                    fontType = "boldAndItalics";
                } else {
                    fontType = "italics";
                }
                fontType = "italics";
            } else if (items[i].equals(".fill")) {
                fillType = "fill";
            } else if (items[i].equals(".nofill")) {
                fillType = "nofill";
            } else if (items[i].split(" ")[0].equals(".indent")) {
                indent = Integer.parseInt((items[i].split(" "))[1]);
                globalIndent += indent;
            } else if (items[i].equals(".paragraph")) {
                fstPara = new Paragraph();
            } else if (items[i].equals("")) {
                continue;
            } else {
                Text txt = new Text(items[i]);
                txt = formatText(fstPara, txt);
                fstPara.add(txt);

                /**
                 * The second loop functions similarly to the first, adding
                 * formatted text to the current paragraph. The loop will break when
                 * .paragraph is encountered so a new Paragraph can be started. Also,
                 * the iteration variable from the first loop is incremented so that
                 * the first loop will not look at the same element in the array twice.
                 */
                for (int j = i + 1; j < items.length; j++) {

                    if (items[j].equals(".large")) {
                        fontSize = "large";
                        i++;
                    } else if (items[j].equals(".normal")) {
                        fontSize = "normal";
                        i++;
                    } else if (items[j].equals(".regular")) {
                        fontType = "regular";
                        i++;
                    /**
                     * This allows the text to be both bold an italicised at the same time.
                     */
                    } else if (items[j].equals(".bold")) {
                        if (fontType.equals("italics")) {
                            fontType = "boldAndItalics";
                        } else {
                            fontType = "bold";
                        }
                        i++;
                    } else if (items[j].equals(".italics")) {
                        if (fontType.equals("bold")) {
                            fontType = "boldAndItalics";
                        } else {
                            fontType = "italics";
                        }
                        i++;
                    } else if (items[j].equals(".fill")) {
                        fillType = "fill";
                        i++;
                    } else if (items[j].equals(".nofill")) {
                        fillType = "nofill";
                        i++;
                    } else if (items[j].split(" ")[0].equals(".indent")) {
                        indent = Integer.parseInt((items[i].split(" "))[1]);
                        globalIndent += indent;
                        i++;
                    } else if (items[j].equals(".paragraph")) {
                        break;
                    } else if (items[j].equals("")) {
                        i++;
                    } else {
                        /**
                         * This is to ensure that there will not be a space before a comma
                         * if a new text element happens to start with a comma.
                         */
                        if ((items[j]).charAt(0) == ',') {
                            txt = new Text(items[j]);
                            txt = formatText(fstPara, txt);
                            fstPara.add(txt);
                            i++;
                        } else {
                            txt = new Text(" " + items[j]);
                            txt = formatText(fstPara, txt);
                            fstPara.add(txt);
                            i++;
                        }
                        
                    }

                }

                /**
                 * Add paragraph to document
                 */
                document.add(fstPara);
            }

        }

    }

    /**
     * Applies formatting rules to the text based on the formatting flags
     * @param para The current Paragraph
     * @param txt The current Text
     * @return The Text, but with formatting rules applied
     * @throws IOException
     */
    private Text formatText(Paragraph para, Text txt) throws IOException {

        PdfFont defaultFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont italicsFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
        PdfFont boldAndItalicsFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLDOBLIQUE);

        /**
         * Multiply the indent value by 20 to make the gap substantial
         */
        para.setMarginLeft((float)(globalIndent * 20));

        if (fontSize.equals("large")) {
            txt.setFontSize(30f);
        } else if (fontSize.equals("normal")) {
            txt.setFontSize(12f);
        }
        
        if (fontType.equals("boldAndItalics")) {
            txt.setFont(boldAndItalicsFont);
        } else if (fontType.equals("regular")) {
            txt.setFont(defaultFont);
        } else if (fontType.equals("bold")) {
            txt.setFont(boldFont);
        } else if (fontType.equals("italics")) {
            txt.setFont(italicsFont);
        }
        
        if (fillType.equals("fill")) {
            para.setTextAlignment(TextAlignment.JUSTIFIED);
        } else if (fillType.equals("nofill")) {
            para.setTextAlignment(TextAlignment.LEFT);
        }

        return txt;

    }

}
