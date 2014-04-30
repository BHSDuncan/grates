package com.xnlogic.grates.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.tinkerpop.blueprints.Vertex;

public class GraphDate extends AbstractGraphDate {
    private final String DAY_VERT_PROP = "grates_day";
        
    private int dayValue = 0;
    private int monthValue = 0;
    private int yearValue = 0;
    
    public GraphDate(Vertex v, int yearValue, int monthValue) {
        this.backingVertex = v;
        
        this.dayValue = v.getProperty(this.DAY_VERT_PROP);
        
        this.monthValue = monthValue;
        this.yearValue = yearValue;
        
        this.setUnixDate(yearValue, monthValue, this.dayValue);        
    } // GraphDate
    	
	@Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj == this)
            return true;

        if (!(obj instanceof GraphDate))
            return false;

        GraphDate otherDate = (GraphDate)obj;

        return new EqualsBuilder()
            .append(this.unixDate, otherDate.unixDate)
            .isEquals();
    } // equals

    @Override
    public int hashCode() {
        return new HashCodeBuilder(29, 37)
            .append(this.unixDate)
            .toHashCode();
    } // hashCode
} // GraphDate
