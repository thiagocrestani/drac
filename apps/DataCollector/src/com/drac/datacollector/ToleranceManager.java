package com.drac.datacollector;

import java.util.Vector;
import java.io.*;
import java.util.Scanner;

public class ToleranceManager extends Output
{
    public ToleranceManager(float tolerance, String output)
    {
	super(output);
	this.tolerance = tolerance;
    }

    public float getMagnitude(float currentMag)
    {	    
	if(Math.abs(currentMag - prevMag) > this.tolerance)
	    {
		this.prevMag = currentMag;
	    }

	return this.prevMag;
    }

    public void updateStepCounter(long time)
    {
	this.stepCounter.update(time, this.prevMag);
    }

    public int countSteps()
    {
	File file = new File(this.getFileName());
	try
	    {
		Scanner input = new Scanner(file);
		String[] raw;

		this.stepCounter.initializeLine();

		for(String line = ""; line != null; line = this.getLine(input))
		    {
			raw = line.split(" ");
			try
			    {
				this.stepCounter.determineStep(Long.parseLong(raw[0]),
							       Float.parseFloat(raw[1]));
			    }
			catch(NumberFormatException ex)
			    {
			    }
		    }

		input.close();
	    }
	catch(FileNotFoundException ex)
	    {
	    }
	
	return this.stepCounter.getNumberOfSteps();
    }

    public float getTolerance()
    {
	return this.tolerance;
    }

    private String getLine(Scanner input)
    {
	try
	    {
		return input.nextLine();
	    }
	catch(Exception ex)
	    {
		return null;
	    }
    }

    private float tolerance = 0;
    private float prevMag = 0;
    private StepCounter stepCounter = new StepCounter();
}