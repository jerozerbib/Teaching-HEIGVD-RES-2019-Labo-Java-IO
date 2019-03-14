package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 * <p>
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

    private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
    private int lineNumber = 1;
    private boolean lineBreak = true;


    public FileNumberingFilterWriter(Writer out) {
        super(out);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++){
            write(str.charAt(i));
        }


    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(cbuf[i]);
        }
    }

    @Override
    public void write(int c) throws IOException {
        switch (c){
            case '\n':
                lineBreak = false;
                super.write(c);
                for (char ch : Integer.toString(lineNumber).toCharArray()) {
                    super.write(ch);
                }
                super.write('\t');
                lineNumber++;
                break;
            case '\r':
                lineBreak = true;
                super.write(c);
                break;
            default:
                if (lineBreak) {
                    lineBreak = false;
                    for (char ch : Integer.toString(lineNumber).toCharArray()) {
                        super.write(ch);
                    }
                    super.write('\t');
                    lineNumber++;
                }
                super.write(c);

        }

        //The commented code underneath serves the purpose of generalizing the rules for break lines
        //With the @Override close() method below, we could generalize the rules as below

//        if (c == '\n' || c == '\r') {
//            lineBreak = true;
//            super.write(c);
//        } else {
//            if (lineBreak) {
//                lineBreak = false;
//                for (char ch : Integer.toString(lineNumber).toCharArray()) {
//                    super.write(ch);
//                }
//                super.write('\t');
//                lineNumber++;
//            }
//            super.write(c);
//        }
    }

//    @Override
//    public void close() throws IOException {
//        if (lineBreak){
//            for (char ch : Integer.toString(lineNumber).toCharArray()){
//                super.write(ch);
//            }
//            super.write('\t');
//        }
//        super.close();
//    }
}
