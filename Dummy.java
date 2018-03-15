import java.io.*;
import java.net.*;

public class dummy{

public static void main(String [] args){
    System.out.println("testing dummy");
    try{
        URL u = new URL("http://www.lolcats.com");
        InputStream in = u.openStream();
        int c;
        while ((c = in.read()) !=-1) System.out.write(c);
        in.close();
    }catch(IOException ex){
        System.err.println(ex);
    }
}
}