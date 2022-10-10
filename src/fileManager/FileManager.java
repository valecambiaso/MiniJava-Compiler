package fileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
    private BufferedReader fileReader;
    private String line;
    private int lineNumber;
    private int charNumber;
    private boolean jumpLine;

    public final char eof = '\u001a';

    public FileManager(String file_path) throws IOException {
        fileReader = new BufferedReader(new FileReader(file_path));
        line = fileReader.readLine();
        lineNumber = 1;
        charNumber = 0;
        jumpLine = false;
    }

    public char getNextChar() throws IOException {
        char nextChar = '\n';

        if(jumpLine){
            line = fileReader.readLine();
            lineNumber++;
            charNumber = 0;
            if(line != null){
                nextChar = '\n';
            }else{
                nextChar = eof;
                fileReader.close();
            }
            jumpLine = false;
        }

        if(line != null){
            if(charNumber < line.length()){
                nextChar = line.charAt(charNumber);
                charNumber++;
                jumpLine = false;
            }else{
                jumpLine = true;
            }
        }else{
            nextChar = eof;
        }

        return nextChar;
    }

    public boolean isEOF(char character){
        return character == eof;
    }

    public int getLineNumber(){
        /*if(jumpLine)
            return lineNumber-1;
        else*/
        return lineNumber;
    }

    public int getCharNumber(){
        return charNumber - 1;
    }

    public String getLine(){ return line; }
}
