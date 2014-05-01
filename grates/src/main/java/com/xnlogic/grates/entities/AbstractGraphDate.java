package com.xnlogic.grates.entities;

import java.util.Calendar;

import com.tinkerpop.blueprints.Vertex;

public abstract class AbstractGraphDate {
    protected Vertex backingVertex;
    protected long unixDate;
    
    public final Vertex getVertex() {
        return this.backingVertex;
    } // getVertex
        
    public final long getUnixDate() {
        return this.unixDate;
    } // getUnixDate

    public void setUnixDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        
        cal.set(year, month, day);
        
        this.unixDate = cal.getTimeInMillis() / 1000L;
    } // setUnixDate
} // AbstractGraphDate
