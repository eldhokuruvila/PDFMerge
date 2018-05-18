/**********************************************************/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.*;

public class PdfMerge {
    public static void main(String[] args) {
        List<InputStream> list = new ArrayList<InputStream>();
        try {

            System.out.println("PDF merge running");

            // Source pdfs
            //:TODO: uncomment for adding files
            //list.add(new FileInputStream(new File("")));
            //list.add(new FileInputStream(new File("")));
            //OutputStream out = new FileOutputStream(new File(""));

            //TODO: uncomment for merging pdf
            //doMerge(list, out);



            //for rotate pdf
            //:TODO: uncomment for adding files
            //InputStream inputStream = new FileInputStream(new File(""));
            //List<Integer> pageList = new ArrayList<Integer>();
            //pageList.add(1);


            //TODO: uncomment for rotate pdf
            //doRotateSinglePage(inputStream, out, pageList);

        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Merge multiple pdf into one pdf
     *
     * @param list
     *            of pdf input stream
     * @param outputStream
     *            output file output stream
     * @throws DocumentException
     * @throws IOException
     */
    public static void doMerge(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        for (InputStream in : list) {
            PdfReader reader = new PdfReader(in);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }
        }

        outputStream.flush();
        document.close();
        outputStream.close();
    }

    public static void doRotate(InputStream in, OutputStream outputStream)
            throws DocumentException, IOException {
        PdfReader reader = new PdfReader(in);
        int n = reader.getNumberOfPages();
        PdfDictionary page;
        PdfNumber rotate;
        for (int p = 1; p <= n; p++) {
            page = reader.getPageN(p);
            rotate = page.getAsNumber(PdfName.ROTATE);
            if (rotate == null) {
                page.put(PdfName.ROTATE, new PdfNumber(90));
            }
            else {
                page.put(PdfName.ROTATE, new PdfNumber((rotate.intValue() + 90) % 360));
            }
        }
        PdfStamper stamper = new PdfStamper(reader, outputStream);
        stamper.close();
        reader.close();
    }


    public static void doRotateSinglePage(InputStream in, OutputStream outputStream, List<Integer> pageNumber)
            throws DocumentException, IOException {
        PdfReader reader = new PdfReader(in);
        int n = reader.getNumberOfPages();
        PdfDictionary page;
        PdfNumber rotate;

        for (int p = 0; p < pageNumber.size(); p++) {
            page = reader.getPageN(pageNumber.get(p));
            rotate = page.getAsNumber(PdfName.ROTATE);
            if (rotate == null) {
                page.put(PdfName.ROTATE, new PdfNumber(180));
            } else {
                page.put(PdfName.ROTATE, new PdfNumber((rotate.intValue() + 180) % 360));
            }
        }

        PdfStamper stamper = new PdfStamper(reader, outputStream);
        stamper.close();
        reader.close();
    }
}
/*********************************************/