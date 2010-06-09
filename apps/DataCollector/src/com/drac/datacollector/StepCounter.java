package com.drac.datacollector;

public class StepCounter
{
    public float leastSquares(long time)
    {
	return m * time + b;
    }

    public void update(long time, float prevMag)
    {
    	sumXY = sumXY + time*prevMag;
    	sumX = sumX + time;
    	sumY = sumY + prevMag;
    	sumXsquared = sumXsquared + time*time;
    	numberDataPoints++;
    }

    public void initializeLine()
    {
	findSlope();
	findB();
    }

    private void findSlope()
    {
    	m = numberDataPoints*sumXY - sumX*sumY;
    	m = m/ (numberDataPoints*sumXsquared - sumX*sumX);
    }

    private void findB()
    {
    	b = (sumY - m*sumX)/numberDataPoints;
    }

    public void determineStep(long time, float accReading)
    {
	float height = this.leastSquares(time);
    	if(accReading <= height)
	    {
		flag = true;
	    }
    	else if((accReading > height) && 
		(flag == true) && 
		(Math.abs(accReading - height) > this.threshold))
	    {
    		flag = false;
    		++numSteps;
	    }
    }

    public int getNumberOfSteps()
    {
	return this.numSteps;
    }
    
    // -- least squares regression line information
    private float m = 0;
    private float b = 0;
    
    // -- 
    private float sumXY = 0;
    private float sumX = 0;
    private float sumY = 0;
    private float sumXsquared = 0;
    private float numberDataPoints = 0; 
    private boolean flag = true;
    private int numSteps = 0;
    private float threshold = 1.0f;
}