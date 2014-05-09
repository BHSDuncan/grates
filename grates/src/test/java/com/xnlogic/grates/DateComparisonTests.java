package com.xnlogic.grates;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.xnlogic.grates.datatypes.Grate;
import com.xnlogic.grates.entities.GraphDate;
import com.xnlogic.grates.exceptions.InvalidDateException;
import com.xnlogic.grates.exceptions.MissingBackingVertexException;
import com.xnlogic.grates.util.GraphUtil;

public class DateComparisonTests {

    private KeyIndexableGraph graph = null;
    
    @Before
    public void setUp() throws Exception {
        this.graph = new TinkerGraph();
    }

    @After
    public void tearDown() throws Exception {
        this.graph.shutdown();
        this.graph = null;
    }

    @Test
    public void tryToGetUnixDateFromNullVertex() {
        assertEquals(0, GraphUtil.getUnixTimeFromVertex(null));
    }
    
    @Test
    public void graphDateObjectEqualityTest() throws InvalidDateException, MissingBackingVertexException {
        final String calendarName = "TEST_CALENDAR";
        final int YEAR = 2014;
        final int MONTH = 4;
        final int DAY = 30;

        Grate g = new Grate(calendarName, this.graph);
        
        assertNotNull(g);
        
        g.init();
        
        GraphDate gd1 = g.findOrCreateDate(YEAR, MONTH, DAY);
        GraphDate gd2 = g.findOrCreateDate(YEAR, MONTH, DAY);
        
        assertEquals(gd1, gd2);
        
        boolean testEquals = gd1.equals(gd2);

        assertTrue(testEquals);

        testEquals = gd1.equals(null);

        assertFalse(testEquals);

        testEquals = gd1.equals(gd1);

        assertTrue(testEquals);

        testEquals = gd1.equals(new Object());

        assertFalse(testEquals);

        assertTrue(gd1.hashCode() == gd2.hashCode());
    }
    
    @Test
    public void graphDateCreationAndRetrievalTest() throws InvalidDateException, MissingBackingVertexException {
        final String calendarName = "TEST_CALENDAR";
        final int YEAR = 2014;
        final int MONTH = 4;
        final int DAY = 30;

        Grate g = new Grate(calendarName, this.graph);
        
        g.init();
        
        GraphDate gd1 = g.findOrCreateDate(YEAR, MONTH, DAY);
        
        g = null;
        
        g = new Grate(calendarName, this.graph);
        
        g.init();
        
        GraphDate gd2 = g.findDate(YEAR, MONTH, DAY);
        
        assertEquals(gd1, gd2);
    }
    
    @Test
    public void relativeComparisonTest() throws InvalidDateException, MissingBackingVertexException {
        final String calendarName = "TEST_CALENDAR";
        final int YEAR_1 = 2014;
        final int MONTH_1 = 4;
        final int DAY_1 = 30;
 
        final int YEAR_2 = 2013;
        final int MONTH_2 = 4;
        final int DAY_2 = 30;

        final int YEAR_3 = 2012;
        final int MONTH_3 = 4;
        final int DAY_3 = 30;

        Grate g = new Grate(calendarName, this.graph);
        
        g.init();
        
        GraphDate gd1 = g.findOrCreateDate(YEAR_1, MONTH_1, DAY_1);
        
        GraphDate gd2 = g.findOrCreateDate(YEAR_2, MONTH_2, DAY_2);
        
        GraphDate gd3 = g.findOrCreateDate(YEAR_1, MONTH_1, DAY_1);
        
        GraphDate gd4 = g.findOrCreateDate(YEAR_3, MONTH_3, DAY_3);
        
        assertTrue(gd1.isGreaterThan(gd2));
        assertTrue(gd1.isGreaterThanOrEqualTo(gd2));
        assertEquals(0, gd1.compareTo(gd3));
        assertEquals(0, gd3.compareTo(gd1));

        assertTrue(gd2.isLessThan(gd1));
        assertTrue(gd2.isLessThanOrEqualTo(gd1));
        assertEquals(1, gd1.compareTo(gd2));
        assertEquals(-1, gd2.compareTo(gd1));
        
        assertTrue(gd1.isGreaterThanOrEqualTo(gd3));
        assertTrue(gd1.isLessThanOrEqualTo(gd3));

        assertTrue(gd3.isGreaterThanOrEqualTo(gd1));
        assertTrue(gd3.isLessThanOrEqualTo(gd1));
        
        assertTrue(gd4.isLessThan(gd3));
        assertTrue(gd4.isLessThan(gd2));

        assertTrue(gd3.isGreaterThan(gd4));
        assertTrue(gd2.isGreaterThan(gd4));

    }
}
