package com.xnlogic.sewergrates.helpers;

import java.util.Set;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;

public class DateGraphHelper 
{
	public static void checkAndCreateIndices(KeyIndexableGraph graph)
	{
		// TODO: Move this into properties file.
		final String dateTypeKey = "dateType";
		
		Set<String> keys = graph.getIndexedKeys(Vertex.class);
		
		if (!keys.contains(dateTypeKey))
		{
			graph.createKeyIndex(dateTypeKey, Vertex.class);
		} // if
	} // checkAndCreateIndices
} // GraphHelper
