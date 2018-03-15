
import java.lang.*;


public class SecurityManagerTest{

public static void main(String [] args){
    SecurityManagerTest b = new SecurityManagerTest("localhost/127.0.0.1",8075);

    b.testConnection()

}

SecurityManager s;
String hostname;
int port;

SecurityManagerTest(String hostname, int port){
    this.hostname = hostname;
    this.port = port;
    s = new SecurityManager(this.hostname, this.port);
}


public int testConnection(){
    try{
        s.checkConnect(this.hostname, this.port)
    }catch{
        
    }

}



}