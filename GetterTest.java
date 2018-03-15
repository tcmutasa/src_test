import java.net.*;
import java.util.*;

public class GetterTest{

    public static void main (String[] args) throws SocketException{
        NetworkInterface etho = NetworkInterface.getByName("lo0");
        Enumeration addresses = etho.getInetAddresses();
        while(addresses.hasMoreElements()){
            System.out.println(addresses.nextElement());
        }


    }
}