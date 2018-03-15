import java.io.*;
import java.security.*;
import java.xml.bind.*;

public class DigestThread extends Thread{

    private String filename;

    public DigestThread(filename){
        this.filename = filename;
    }

@Override
public void run(){
    FileInputStream fin = new FileInputStream(filename);
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    DigestInputStream din = new DigestInputStream(fin, sha);
    while (din.read()!=-1);
    din.close();
    byte [] digest = sha.digest();

    StringBuilder result = new StringBuilder(filename);
    result.append(" : ");
    result.append(DatatypeConverter.printHexBinary(digest))

}

}