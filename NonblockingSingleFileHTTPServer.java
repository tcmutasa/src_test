
/**
 * title: NonblockingSingleFileHTTPServer.java
 * description: The server is capable of providing multiple files - that is, it is 
 * a working HTTP server. It displays HTML webpages as well as embedded multimedia
 * content such as .pdf files or images (.jpg or .png)
 * 2 = count total bytes transmitted; 
 * 3 = count total bytes by remotehost. 
 * date: March 15, 2018
 * @author Tawanda C. Mutasa 
 * @version 1.0
 * @copyright 2018-2021 Tawanda C. Mutasa
 */

/**
 * DOCUMENTATION...
 */

/**                                                                               
 *
 *<H1>Utility reads weblogs and tallies statistics</H1>
 *
 *<H3>Purpose and Description</H3>
 *
 *<P>
 * description: The program is executed by using the command line command: 
 *‘java MyPooledWeblog logname option’ where option is one of the following:
 * 1 = count number of accesses by each remotehost; 
 * 2 = count total bytes transmitted; 
 * 3 = count total bytes by remotehost.
 *</P>
 *<P>
 * This program uses the Java io & util API's, and requires the
 * Sun Java SDK version 1.7 or better.
 *</P>

 * <H3>Classes</H3>
 *
 *<P>
 * public class PooledWeblog {<BR>
 * This is the main public class for this application. 
 *</P>
 *<P>
   public class LookupTask implements Callable<String> {<BR>
   Class LookupTask returns a future<String> processed using regular expressions
   to simpler string: the hostname and bytes transmitted, compared to a lengthy LogEntry
 *</P>

*
 * <H3>PooledWeblog Methods</H3>
 *
 *<P>
   public PooledWeblog() {<BR>
   Constructor for PooledWeblog class -  this method is used to set up the GUI
 *</P>
 *<P>
   public static void main(String args[]) {<BR>
   This method is used to execute the application
 *</P>
 *<P>
   public static ArrayList<String> countNumberOfHosts(Queue<LogEntry> results) {<BR>
   A Queue of LogEntry objects is passed in as an argument and converted to an 
   intermediate ConcurrencyHashMap. The conHashMap is looped through, and for 
   each unique key, another inner loop on the orginal Queue counts the number 
   instances of the key in question. Any ArrayList of strings showing the key:value
   pairs, hostname:count is returned by this method.
 *</P>
 *<P>
   public static ArrayList<String> sortRemoteHosts(Queue<LogEntry> results) {<BR>
   A Queue of LogEntry objects pre-processed by the LookupTask class (see class details)
   is passed in as an argument and converted to an intermediate ConcurrencyHashMap.
   The conHashMap is looped through, and for each unique key, a running sum of the bytes
   transmitted is maintained as the value of the key:value pair. Any ArrayList of strings 
   showing the key:value pairs, hostname:totalbytes is returned by this method.
 *</P>
 *<P>
    public static long addBytes(Queue<LogEntry> results){<BR>
    A Queue of LogEntry objects is passed into this method and a long is
    returned. This method sums up the total bytes tranmitted.
 *</P>


/**
 *
 * <H3>Test Plan</H3>
 *
 *<P>
 * 1. Run the application.
 * (a)option 1
 * (b)option 2
 * (c)option 3
 * EXPECTED:
 * (a)  207.195.59.160 number of accesses:6
        212.92.37.62 number of accesses:1
        10.0.0.153 number of accesses:3
        ts04-ip92.hevanet.com number of accesses:5
        64.242.88.10 number of accesses:3
 * (b)  The total bytes transmitted to and from this web server: 119496
 * (c)  207.195.59.160 transmitted: 30855 bytes
        212.92.37.62 transmitted: 5543 bytes
        ts04-ip92.hevanet.com transmitted: 10837 bytes
        64.242.88.10 transmitted: 72261 bytes

 * ACTUAL:
 *    Output displays as expected.
 *</P>
 *<P>
 * 2. Bad data cases:
 * EXPECTED:
 *    Run the following test cases by typing the input value and pressing enter:
 *    No option put in or log file name -> Exactly 2 params required!
 *    Option !=1,2 or 3 -> "Please enter option 1, 2 or 3"
 *    log has a non-numeric HTTP code, e.g., "and" instead of 200 -> output is still normal. regex handles both strings and numerics
 * ACTUAL:
 *    Results displayed as expected.
 


/*
I declare that this assignment is my own work and that all material previously 
written or published in any source by any other person has been duly acknowledged 
in the assignment. I have not submitted this work, or a significant part thereof, 
previously as part of any academic program. In submitting this assignment I give 
permission to copy it for assessment purposes only.
*/

/*
The JDK API Docs were used to write the program.
*/



import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.net.*;

public class NonblockingSingleFileHTTPServer {

  private ByteBuffer contentBuffer;
  private int port = 80;

  public NonblockingSingleFileHTTPServer(
      ByteBuffer data, String encoding, String MIMEType, int port) {
    
    this.port = port;
    String header = "HTTP/1.0 200 OK\r\n"
        + "Server: NonblockingSingleFileHTTPServer\r\n"
        + "Content-length: " + data.limit() + "\r\n"
        + "Content-type: " + MIMEType + "\r\n\r\n";
    byte[] headerData = header.getBytes(Charset.forName("US-ASCII"));

    ByteBuffer buffer = ByteBuffer.allocate(
        data.limit() + headerData.length);
    buffer.put(headerData);
    buffer.put(data);
    buffer.flip();
    this.contentBuffer = buffer;
  }
  
  public void run() throws IOException {
    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    ServerSocket  serverSocket = serverChannel.socket();
    Selector selector = Selector.open();
    InetSocketAddress localPort = new InetSocketAddress(port);
    serverSocket.bind(localPort);
    serverChannel.configureBlocking(false);
    serverChannel.register(selector, SelectionKey.OP_ACCEPT);

    while (true) {
      selector.select();
      Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
      while (keys.hasNext()) {
        SelectionKey key = keys.next();
        keys.remove();
        try {
          if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel channel = server.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
          } else if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment(); 
            if (buffer.hasRemaining()) {
               channel.write(buffer);   
            } else { // we're done
               channel.close();   
            }
          } else if (key.isReadable()) {
            // Don't bother trying to parse the HTTP header.
            // Just read something.
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(4096); 
            channel.read(buffer);
            // switch channel to write-only mode
            key.interestOps(SelectionKey.OP_WRITE);
            key.attach(contentBuffer.duplicate());
          }
        } catch (IOException ex) {
          key.cancel();
          try {
            key.channel().close();
          }
          catch (IOException cex) {}
        }
      }
    }
  }
  
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println(
        "Usage: java NonblockingSingleFileHTTPServer file port encoding"); 
      return;
    }
      
    try {
      // read the single file to serve
      String contentType = 
          URLConnection.getFileNameMap().getContentTypeFor(args[0]);
      Path file = FileSystems.getDefault().getPath(args[0]);
      byte[] data = Files.readAllBytes(file);
      ByteBuffer input = ByteBuffer.wrap(data);

      // set the port to listen on
      int port;
      try {
        port = Integer.parseInt(args[1]);
        if (port < 1 || port > 65535) port = 80;
      } catch (RuntimeException ex) {
        port = 80;
      }  
      
      String encoding = "UTF-8";
      if (args.length > 2) encoding = args[2]; 
       
      NonblockingSingleFileHTTPServer server
          = new NonblockingSingleFileHTTPServer(
          input, encoding, contentType, port);
      server.run();         
    } catch (IOException ex) {
      System.err.println(ex);
    }
  }
}