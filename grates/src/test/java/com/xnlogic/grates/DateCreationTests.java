package com.xnlogic.grates;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.xnlogic.grates.datatypes.Grate;
import com.xnlogic.grates.entities.GraphDate;
import com.xnlogic.grates.entities.GraphYear;

public class DateCreationTests 
{
    private KeyIndexableGraph graph = null;
    
    private final String CAL_ROOT_PROP = "grates_calendar_name";
    
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
    public void instantiateValidGrateTest() {
        final String calendarName = "TEST_CALENDAR";
        
        Grate g = GraphHelperFunctions.createGrate(calendarName, this.graph);
        
        assertNotNull(g);
        
        g.init();
        g = null;
        
        assertEquals(1, GraphHelperFunctions.getCalendarCount(calendarName, this.graph));
    }

    @Test
    public void thereCanOnlyBeOneCalendarForAGivenNameTest() {       
        // call the test below twice to try to create the same calendar twice (calling it twice will also provide the proper assertions)
        this.instantiateValidGrateTest();
        this.instantiateValidGrateTest();                
    }
    
    @Test
    public void createValidDateTest() {
        final String calendarName = "TEST_CALENDAR";
        final int yearValue = 2014;
        final int monthValue = 4;
        final int dayValue = 30;
        
        Grate g = GraphHelperFunctions.createGrate(calendarName, this.graph);
        g.init();
        
        GraphDate gd = g.findOrCreateDate(yearValue, monthValue, dayValue);
        
        this.verifyDateExistsInGraphForCalendar(yearValue, monthValue, dayValue, calendarName);       
    }
    
    @Test
    public void tryToCreateTheSameDateTwiceTest() {
        final String calendarName = "TEST_CALENDAR";
        final int yearValue = 2014;
        final int monthValue = 4;
        final int dayValue = 30;
        
        Grate g = GraphHelperFunctions.createGrate(calendarName, this.graph);
        g.init();
        
        GraphDate gd = g.findOrCreateDate(yearValue, monthValue, dayValue);
        gd = g.findOrCreateDate(yearValue, monthValue, dayValue);
        
        this.verifyDateExistsInGraphForCalendarExactlyOnce(yearValue, monthValue, dayValue, calendarName);               
    }
    
    private void verifyDateExistsInGraphForCalendar(int yearValue, int monthValue, int dayValue, String calendarName) {
        Vertex calRoot = GraphHelperFunctions.getCalendarRoot(calendarName, this.graph);
        
        assertNotNull(calRoot);
        
        // check year
        Vertex year = GraphHelperFunctions.getYearFromGraph(yearValue, calRoot);
        
        assertNotNull(year);
        assertEquals(yearValue, year.getProperty("grates_year"));
        
        // check month
        Vertex month = GraphHelperFunctions.getMonthFromGraph(monthValue, year);
        
        assertNotNull(month);
        assertEquals(monthValue, month.getProperty("grates_month"));

        // check day
        Vertex day = GraphHelperFunctions.getDayFromGraph(dayValue, month);
        
        assertNotNull(day);
        assertEquals(dayValue, day.getProperty("grates_day"));
    }   
    
    private void verifyDateExistsInGraphForCalendarExactlyOnce(int yearValue, int monthValue, int dayValue, String calendarName) {
        // check calendar root
        Iterable<Vertex> calendarRoots = this.graph.getVertices(this.CAL_ROOT_PROP, calendarName);

        assertEquals(1, this.getIterableSize(calendarRoots));
        
        Vertex calRoot = GraphHelperFunctions.getCalendarRoot(calendarName, this.graph);
        
        assertNotNull(calRoot);
        
        // check year
        List<Vertex> years = GraphHelperFunctions.getYearsFromGraph(yearValue, calRoot);
        
        assertEquals(1, years.size());
        
        Vertex year = GraphHelperFunctions.getYearFromGraph(yearValue, calRoot);
        
        assertNotNull(year);        
        assertEquals(yearValue, year.getProperty("grates_year"));
        
        // check month
        List<Vertex> months = GraphHelperFunctions.getMonthsFromGraph(monthValue, year);
        
        assertEquals(1, months.size());
        
        Vertex month = GraphHelperFunctions.getMonthFromGraph(monthValue, year);
        
        assertNotNull(month);
        assertEquals(monthValue, month.getProperty("grates_month"));

        // check day
        List<Vertex> days = GraphHelperFunctions.getDaysFromGraph(dayValue, month);
        
        assertEquals(1, days.size());

        Vertex day = GraphHelperFunctions.getDayFromGraph(dayValue, month);
        
        assertNotNull(day);
        assertEquals(dayValue, day.getProperty("grates_day"));
    }
    
    private int getIterableSize(Iterable<?> iterable) {
        if (iterable == null) {
            return 0;
        } // if
        
        int size = 0;
        
        Iterator<?> it = iterable.iterator();
        
        while (it.hasNext()) {
            size++;
            it.next();
        } // while
        
        return size;
    }
}
