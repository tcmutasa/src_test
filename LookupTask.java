import java.net.*;
import java.util.concurrent.Callable;
import java.util.regex.*;
import java.lang.*;

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

public class LookupTask implements Callable<String>{

    public String line;
    public int option;
    public String linePattern = "^([\\a-z0-9.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" ([\\a-z0-9]+) (\\d.+?|.+)";
    public final int NUM_FIELDS = 7;
    public String remotehost = ""; //initialize @ empty string
    public String bytesTransmitted = ""; //initialize at zero bytes transmitted

    public LookupTask(String line){
        this.line = line;
    }

    public LookupTask(String line, int option){
        this.line = line;
        this.option = option;
    }

    @Override
    public String call(){
        try{
            Pattern p = Pattern.compile(linePattern);
            Matcher m = p.matcher(line); 
            if (!m.matches() || NUM_FIELDS != m.groupCount()){
                return "Bad entry: " + line;
            }    
            if (option==1){
                    remotehost = m.group(1);
                    return  remotehost;
            }
            if (option==2){
                    bytesTransmitted = m.group(7);
                    return bytesTransmitted;
            }
            if (option==3||option==2||option==1){
                    return m.group(1) + " " + m.group(7);
            }else{
                return "options not specified correctly";
                }
        }catch(Exception ex){
            return "no dice";
        }
    }
}