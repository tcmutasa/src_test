/**
 * title: PooledWeblog/LookupTask.java
 * description: The program is executed by using the command line command: 
 *‘java MyPooledWeblog logname option’ where option is one of the following:
 * 1 = count number of accesses by each remotehost; 
 * 2 = count total bytes transmitted; 
 * 3 = count total bytes by remotehost. 
 * date: February 18, 2018
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
import java.util.*;
import java.util.concurrent.*;
// Requires Java 7 for try-with-resources and multi-catch
public class PooledWeblog {

  private final static int NUM_THREADS = 4;
  public static long numberOfAccesses = 0;
  public static long total = 0;
  
  public static void main(String[] args) throws IOException {
    ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
    Queue<LogEntry> results = new LinkedList<LogEntry>();

    if (args.length!=2){
      throw new IllegalArgumentException("Exactly 2 params required!");
    }

    //the log file and option need to be entered
    //used stackoverflow for information
    int option = 0;
    try{
      option = Integer.parseInt(args[1]);
      //option = option.intValue();
      if (option>3&&option<1){
        System.out.println("Please enter option: 1, 2 or 3");
      }
    }catch(NumberFormatException b){
    }
    
    try (BufferedReader in = new BufferedReader(
      new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));) {
      for (String entry = in.readLine(); entry != null; entry = in.readLine()) {
        LookupTask task = new LookupTask(entry,option);
        Future<String> future = executor.submit(task);
        //After returning future, parse String to respective components
        //using regex
        LogEntry result = new LogEntry(entry, future);
        results.add(result);
      }
    }
    
    if (option==1){
      ArrayList<String> b = countNumberOfHosts(results);
      for(String i: b){
      System.out.println(i);  
      }
    }

    if (option==2){
      //init at zero
      total = addBytes(results);
      System.out.println("The total bytes transmitted to and from this web server: " + total);
      }

    if (option==3){
      ArrayList<String> b = sortRemoteHosts(results);
      for(String i: b){
      System.out.println(i);  
      }
    }  
    executor.shutdown();
  }

  public static ArrayList<String> countNumberOfHosts(Queue<LogEntry> results){
    Map<String,Long> conMap = new ConcurrentHashMap<String,Long>();
    long count = 0;
    String temp = "";
    //result will be 1 string, returned from LookupTask
    for(LogEntry result: results){//loop though queue of strings
      try{
        if (!conMap.containsKey(result.future.get())){//new
            temp = result.future.get();
            for(LogEntry resultInner: results){
                if (temp.equals(resultInner.future.get())){
                  count = count+1;
                }
              }
              conMap.put(temp,count);
              count=0;
        }
      }catch(NumberFormatException|InterruptedException|ExecutionException ex){
        //no requirement to print to error log, so do nothing
      }
    }

    ArrayList<String> solution = new ArrayList<String>();
    Set<String> keys = conMap.keySet();
     for (String i : keys){
       solution.add(i + " number of accesses:" + conMap.get(i));
     }

    return solution;

  }

  public static ArrayList<String> sortRemoteHosts(Queue<LogEntry> results){
    Map<String,Long> conMap = new ConcurrentHashMap<String,Long>();
    long totalBytesPerHost = 0;
    String temp= "";
    for(LogEntry result:results){
      try{
        int index = result.future.get().indexOf(" ");
        String remoteaddress = result.future.get().substring(0,index);
        String bytesPerHost = result.future.get().substring(index+1);
        bytesPerHost.trim();
        Long bytesTrans = Long.parseLong(bytesPerHost);
        long bytesPerhost = bytesTrans.longValue();
        if (remoteaddress.equals(temp)){
          totalBytesPerHost = conMap.get(temp).longValue();
          totalBytesPerHost= totalBytesPerHost + bytesPerhost;
          conMap.put(remoteaddress,new Long(totalBytesPerHost));
        }else{
          conMap.put(remoteaddress,new Long(bytesPerhost));
          temp = remoteaddress;
        }  
      }catch(NumberFormatException|InterruptedException|ExecutionException ex){
        //System.out.println(result.original);
      }
    }

    ArrayList<String> solution = new ArrayList<String>();
     Set<String> keys = conMap.keySet();
     for (String i : keys){
       solution.add(i + " transmitted: " + conMap.get(i) + " bytes");
     }
     return solution;
  }
  
  public static long addBytes(Queue<LogEntry> results){
    long totalBytes = 0;
    //sift through the Queue and pick sum bytes transmitted
    for(LogEntry result:results){
      try{
        if (result.future.get()==""){
          long bytesPerhost_ = 0;
          totalBytes = totalBytes + bytesPerhost_;
        }else{
        Long bytesPerHost = Long.parseLong((result.future.get()));//.substring(index)));
        long bytesPerhost_ = bytesPerHost.longValue();
        totalBytes = totalBytes + bytesPerhost_;
        }
      }catch(NumberFormatException|InterruptedException| ExecutionException ex){       
          //no requirement to print to error log, so do nothing  
      }
    }
    return totalBytes;
  }

  private static class LogEntry {
    String original;
    Future<String> future;
    
    LogEntry(String original, Future<String> future) {
     this.original = original;
     this.future = future;
    } 
  }

}