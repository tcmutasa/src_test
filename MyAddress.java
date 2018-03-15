import java.net.*;

public class MyAddress{

    public static void main(String [] args){
        try{
            InetAddress me = InetAddress.getLocalHost();
            byte[] address = me.getAddress();
            int [] intAddress = new intAddress[address.length];
            for (int i = 0;i<address.length;i++){
            int unsignedByte = address[i] < 0 ? address[i] + 256: address[i]; 
            intAddress[i] = unsignedByte;
            }
            System.out.println("My Address is " + intAddress);
        }catch(UnknownHostException ex){
            System.out.println("I'm sorry. I dont know my own address");
        }

    }
}