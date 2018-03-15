import java.io.*;
import java.util.zip.*;

public class GZipRunnable implements Runnable{

    public final File input;

    public GZipRunnable(File input){
        this.input = input;
    }

    @Override
    public void run(){
        //don't compress an already compressed file
        if (!input.getName().endsWith(".gz")){
            File output = new File(input.getParent(),input.getName() + ".gz");
            if(!output.exists()){//don't overwrite an existing file
                try(
                    InputStream in = new BufferedInputStream(new FileInputStream(input));
                    OutputStream out = new BufferedOutputStream(
                        new GZIPOutputStream(
                            new FileOutputStream(output)));
                        ){
                            int b;
                            while((b=in.read())!=-1) 
                            out.write(b);
                            out.flush();
                        } catch (IOException ex){
                            System.err.println(ex);
                        }
                    
                
            }
        }

    } 
}