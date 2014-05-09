package com.xnlogic.grates.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.util.GraphUtil;

public abstract class AbstractGraphDate implements Comparable<AbstractGraphDate> {
    private final int EQUAL = 0;
    private final int GT = 1;
    private final int LT = -1;
    
    protected Vertex backingVertex;
    
    public final Vertex getVertex() {
        return this.backingVertex;
    } // getVertex
        
    public final long getUnixDate() {
        return this.backingVertex != null ? GraphUtil.getUnixTimeFromVertex(this.backingVertex) : 0;
    } // getUnixDate
    
    public boolean isGreaterThanOrEqualTo(AbstractGraphDate rhs) {
        return (this.isGreaterThan(rhs) || this.compareTo(rhs) == EQUAL);
    } // isGreaterThanOrEqualTo
    
    public boolean isGreaterThan(AbstractGraphDate rhs) {
        return this.compareTo(rhs) == GT; 
    } // isGreaterThan

    public boolean isLessThanOrEqualTo(AbstractGraphDate rhs) {
        return (this.isLessThan(rhs) || this.compareTo(rhs) == EQUAL);
    } // isLessThanOrEqualTo
    
    public boolean isLessThan(AbstractGraphDate rhs) {
        return this.compareTo(rhs) == LT;
    } // isLessThan
    
    public int compareTo(AbstractGraphDate o) {
        if (this.equals(o)) {
            return this.EQUAL;
        } // if
        
        if (this.getUnixDate() > o.getUnixDate()) {
            return this.GT;
        } // if
            
        if (this.getUnixDate() < o.getUnixDate()) {
            return this.LT;
        } // if
        
        return this.EQUAL;
    } // compareTo
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj == this)
            return true;

        if (!(obj instanceof AbstractGraphDate))
            return false;

        AbstractGraphDate otherDate = (AbstractGraphDate)obj;

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
} // AbstractGraphDate
