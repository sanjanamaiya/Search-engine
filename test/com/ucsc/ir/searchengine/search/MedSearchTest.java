package com.ucsc.ir.searchengine.search;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MedSearchTest {

	private MedSearch ohsumedSearch = null;
	@Before
	public void setUp() throws Exception 
	{
		ohsumedSearch = new MedSearch();
	}

	@After
	public void tearDown() throws Exception {
	}

	/*@Test
	public void shouldRemoveStopWordsFromQuery() 
	{
		// Arrange
		String query = "how to best control pain and debilitation secondary to osteoporosis in never treated advanced disease";
		
		// Act
		String newQuery = ohsumedSearch.removeStopWords(query);
		
		// Assert
		String expectedQueryString = "best control pain debilitation secondary osteoporosis never advanced disease";
		Assert.assertEquals(expectedQueryString, newQuery);
	}*/

}
