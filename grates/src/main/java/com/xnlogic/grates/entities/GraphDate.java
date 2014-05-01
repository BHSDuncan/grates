package com.xnlogic.grates.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.tinkerpop.blueprints.Vertex;

public class GraphDate extends AbstractGraphDate {
    private final String DAY_VERT_PROP = "grates_day";
        
    public GraphDate(Vertex v) {
        this.backingVertex = v;        
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
            .append(this.getUnixDate(), otherDate.getUnixDate())
            .isEquals();
    } // equals

    @Override
    public int hashCode() {
        return new HashCodeBuilder(29, 37)
            .append(this.getUnixDate())
            .toHashCode();
    } // hashCode
} // GraphDate
