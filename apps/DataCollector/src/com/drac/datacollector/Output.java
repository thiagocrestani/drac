package com.drac.datacollector;

import java.io.*;

public class Output
{
    public Output(String fileName)
    {
	File outFile = new File(fileName);
	this.fileName = fileName;
	try
	    {
		this.out = new FileOutputStream(fileName);
	    }
	catch(FileNotFoundException ex)
	    {
	    }
    }

    public void write(long time, float value)
    {
	try
	    {
		out.write((time + " " + value + "\n").getBytes());
	    }
	catch(IOException ex)
	    {
	    }
    }

    public void write(String data)
    {
	try
	    {
		out.write(data.getBytes());
	    }
	catch(IOException ex)
	    {
	    }	
    }

    public void close()
    {
	try
	    {
		this.out.close();
	    }
	catch(IOException ex)
	    {
	    }
    }

    public String getFileName()
    {
	return this.fileName;
    }
    
    public static String getCommonDirectory()
    {
	return "sdcard/drac/" + CollectorService.getId() + "/";
    }

    private FileOutputStream out;
    private String fileName = "";
}