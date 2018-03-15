/**
 * title: SourceViewer.java
 * description: The program is executed by using the command line command: 
 *‘java  SourceViewer URL searchString’ where searchString  is one of the following:
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
   This method is used to execute the application.
   The bulk of the program is run here.
   An InputStream chained to intermediates and ultimately to a BufferedReader
   reads in one line at a time for processing. The searchString is compiled
   to a pattern which the Matcher then attemps to "find" in the line in question.
   When a match is found, the entire read in line is printed.
 *</P>
 *<P>
   public static boolean isValid(String url) {<BR>
   This method accepts a URL and runs a test to check if the  string 
   is a valid url.
 *</P>
 


/**
 *
 * <H3>Test Plan</H3>
 *
 *<P>
 * 1. Run the application.
 * (a)searchString = "Hello", Line="Hello World"
 * (b)searchString="jump", Line="See the small fox jump the high fence"
 * EXPECTED:
 * (a)  "Hello World"
 * (b)  "See the small fox jump the high fence"
 * ACTUAL:
 *    Output displays as expected.
 *</P>
 *<P>
 * 2. Bad data cases:
 * EXPECTED:
 *    Run the following test cases by typing the input value and pressing enter:
 *    No searchString -> Please enter a URL + search string
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
import java.io.BufferedReader;
import java.net.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;
import java.util.regex.PatternSyntaxException;

public class SourceViewer {

  public static void main (String[] args) {

    //this code ensures that a search string is entered. URL + search string
    if (args.length!=2){
      throw new IllegalArgumentException("Please enter a URL + search string");
    }

    //check validity of URL - done
    if (isValid(args[0])){
      System.out.println("valid URL...proceeding to search for:" + args[1]);
    }else{
      throw new IllegalArgumentException("Illegal URL..please enter valid URL");
    } 

    
    String searchString = "";
    if (args.length > 0) {
      InputStream in = null; //flush stream
      try {
        //set up searchString
        searchString = args[1]; //set args[1] to searchString
        Pattern p = Pattern.compile(searchString);
        // Open the URL for reading
        URL u = new URL(args[0]);
        in = u.openStream();
        // buffer the input to increase performance 
        in = new BufferedInputStream(in);       
        // chain the InputStream to a Reader
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        //int c;
        String str;
        while ((str=r.readLine()) != null){
          Matcher m = p.matcher(str);
            if (m.find()){
              System.out.println(str);
            }
            //if (m.hitEnd() && !m.find()){
              //System.out.println("Could not find entered search string on this line");
            //}
        } 
      } catch (MalformedURLException ex) {
        System.err.println(args[0] + " is not a parseable URL");
      } catch (IOException ex) {
        System.err.println(ex);
      }catch(PatternSyntaxException e){
        System.err.println("Invalid string pattern " + e.getMessage());
        System.exit(1);
      }   
      finally {
        if (in != null) {
          try {
            in.close();
          } catch (IOException e) {
            // ignore
          }
        }
      }
    }
  }

  //check validity of url
  public static boolean isValid(String url){
    try{
      new URL(url).toURI();
      return true;
    }catch(Exception e){
      return false;
    }  
  }

} 