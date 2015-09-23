package net.owl_black.tovmgr;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Write a description of class CSVExport here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CSVExport
{
    
    /**
     * Constructor for objects of class CSVExport
     */
    public CSVExport()
    {
     
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void exportToCSV(ArrayList<mySms> smsDB, File outputFile, boolean mergeDate, int nbSmsByCSV) {
        
    	CsvWriter csvOutput = openCsv(outputFile, mergeDate, true);
    	int count = 0;
    	int nbFile = 2;
    	
    	String path = outputFile.getAbsolutePath();
    	if(path.endsWith(".csv"))
		{
    		path = path.substring(0, path.indexOf(".csv"));
	    }

		try {
            for(mySms sms : smsDB) 
            {
            	if(count/nbSmsByCSV != 0 && nbSmsByCSV != -1) 
            	{
            		csvOutput.close();
            		File fd = new File( path + Integer.toString(nbFile) + ".csv");
            		
            		if(sms.getEncode() == mySms.UTF8_ARABIC)
            			csvOutput = openCsv(fd, mergeDate, true);
            		else 
            			csvOutput = openCsv(fd, mergeDate, false);
            		count = 0;
            		nbFile++;
            	}
            	
                String line;
                
                csvOutput.write(sms.getSenderName());
                csvOutput.write(sms.getPhoneNum());
                
                line = 
                Integer.toString(sms.getDay())+ 
                "/" + 
                Integer.toString(sms.getMonth()) + 
                "/" + 
                Integer.toString(sms.getYear());
                
                if(!mergeDate)
                	csvOutput.write(line);
                else
                	line = line + " ";
                
                //Add the time:
                line = line + 
                Integer.toString(sms.getHour())+ 
                ":" + 
                Integer.toString(sms.getMin()) + 
                ":" + 
                Integer.toString(sms.getSec());
                csvOutput.write(line);
                
                //Add if is in Inbox or in Outbox
                
                if(sms.getReceived())
                	csvOutput.write("Inbox");
                else
                	csvOutput.write("Outbox");
                
                //Add the text message:
                if(sms.getEncode() == mySms.UTF8_ARABIC) {
                	byte[] data = PersianProcess.persianProcess(sms.getText());
                	String s = new String(data);
                	csvOutput.write(s);
                }
                else
                	csvOutput.write(sms.getText());
                
                csvOutput.endRecord();
                
                count++;
            }
            
            csvOutput.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

    }

	public CsvWriter openCsv(File outputFile, boolean mergeDate, boolean UTF8Beacon) {
        
		boolean alreadyExists = outputFile.exists();
        CsvWriter csvOutput;
        try {
            csvOutput = new CsvWriter(new FileWriter(outputFile, true), ';');
            
         // if the file didn't already exist then we need to write out the header line
            if (!alreadyExists)
            {
            	if(UTF8Beacon)
            		csvOutput.write(PersianProcess.UTF8Beacon() + "Name");
            	else
            		csvOutput.write("Name");
            	
                csvOutput.write("Number");
                
                if(mergeDate) {
                	csvOutput.write("Date-Time (DD/MM/YYYY HH:MM:SS)");
                }
                else {
                	csvOutput.write("Date sent (DD/MM/YY)");
                    csvOutput.write("Time sent");
                }
                
                csvOutput.write("Inbox/Outbox");
                csvOutput.write("Message");
                csvOutput.endRecord();
            }
            return csvOutput;
        }// else assume that the file already has the correct header line
        catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
}
