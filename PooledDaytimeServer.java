import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class PooledDaytimeServer{

    public final static int PORT = 13;

    public static void main (String [] args){

        ExcecutorService pool = Executors.newFixedThreadPool(50);

        try(ServerSocket server = new ServerSocket(PORT)){
            while(true){
                try{
                    Socket connection = server.accept();
                    Callable<Void> task = new DaytimeTask(connection);
                    pool.submit(task);
                }catch(IOException e){
                }
            }
        }catch(IOException ex){
            System.err.println("Couldnt start the server ");
        }
    }

    public static class DaytimeTask implements Callable<Void>{

        private Socket connection;

        DaytimeTask (Socket connection){
            this.connection = connection;
        }

        @Override
        public Void call(){
            try{
                Writer out = new OutputStreamWriter(connection.getOutputStream());
                Date now = new Date();
                out.write(now.toString() + "\r\n");
                out.flush();
            }catch(IOException ex){
                System.err.println(ex);
            }finally{
                try{
                    connection.close();
                }catch(IOException e){
                    //ignore
                }
            }
        }

    }
}
