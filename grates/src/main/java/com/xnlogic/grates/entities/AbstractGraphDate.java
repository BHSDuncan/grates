package com.xnlogic.grates.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        String dateString = String.format("%d-%02d-%02d", year, month, day);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        
        try {
            Date d = sdf.parse(dateString);
            
            this.unixDate = d.getTime() / 1000L;
        } catch (Exception e) {
            
        } // try
    } // setUnixDate
} // AbstractGraphDate
