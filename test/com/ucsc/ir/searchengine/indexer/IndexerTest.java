package com.ucsc.ir.searchengine.indexer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IndexerTest {

	private Indexer keyIndexer = null;
	
	/*@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}*/

	@Before
	public void setUp() throws Exception 
	{
		keyIndexer = Indexer.getInstance();
		keyIndexer.resetIndex();
	}

	/*@After
	public void tearDown() throws Exception 
	{
		
	}*/

	@Test
	public void shouldAddNewlyFoundKeysToIndex() 
	{
		// Arrange
		String docId = "doc1";
		String[] termList = new String[] { "88000001", "br", "j", "anaesth", "8801", "effect" };
		
		// Act
		keyIndexer.processDocument(docId, termList);
		
		// Asert
		HashMap<String, HashMap<String, ArrayList<String>>> index = keyIndexer.getIndex();
		
		Set<String> keys =  index.keySet();
		Set<String> expectedKeySet = new HashSet<String>();
		expectedKeySet.add("88000001");
		expectedKeySet.add("br");
		expectedKeySet.add("j");
		expectedKeySet.add("anaesth");
		expectedKeySet.add("8801");
		expectedKeySet.add("effect");
		
		HashMap<String, ArrayList<String>> docMap = index.get("anaesth");
		Set<String> docList  = docMap.keySet();
		Set<String> expectedDocList = new HashSet<String>();
		expectedDocList.add("doc1");
		
		Assert.assertEquals(6, index.size());
		Assert.assertEquals(expectedKeySet, keys);
		Assert.assertEquals(expectedDocList, docList);
	}
	
	@Test
	public void shouldUpdateNewlyFoundDocToExistingKeys() 
	{
		// Arrange
		String docId1 = "doc1";
		String[] termList = new String[] { "88000001", "effect", "br", "j", "anaesth", "8801" };
		
		String docId2 = "doc2";
		String[] termList2 = new String[] { "88000306", "br", "j", "anaesth",
				"Electromyographic" };
		
		// Act
		keyIndexer.processDocument(docId1, termList);
		keyIndexer.processDocument(docId2, termList2);

		// Asert
		HashMap<String, HashMap<String, ArrayList<String>>> index = keyIndexer
				.getIndex();

		Set<String> keys = index.keySet();
		Set<String> expectedKeySet = new HashSet<String>();
		expectedKeySet.add("88000001");
		expectedKeySet.add("br");
		expectedKeySet.add("j");
		expectedKeySet.add("anaesth");
		expectedKeySet.add("8801");
		expectedKeySet.add("effect");
		expectedKeySet.add("88000306");
		expectedKeySet.add("Electromyographic");

		HashMap<String, ArrayList<String>> docMap = index.get("anaesth");
		Set<String> docList = docMap.keySet();
		Set<String> expectedDocList = new HashSet<String>();
		expectedDocList.add("doc1");
		expectedDocList.add("doc2");

		Assert.assertEquals(8, index.size());
		Assert.assertEquals(expectedKeySet, keys);
		Assert.assertEquals(expectedDocList, docList);
	}

	@Test
	public void shouldUpdateLocationOfKeysInExistingDocEntry()
	{
		// Arrange
		String docId1 = "doc1";
		String[] termList = new String[] { "88000001", "effect", "br", "j",
				"anaesth", "8801", "effect" };

		String docId2 = "doc2";
		String[] termList2 = new String[] { "88000306", "br", "j", "anaesth",
				"Electromyographic", "anaesth" };

		// Act
		keyIndexer.processDocument(docId1, termList);
		keyIndexer.processDocument(docId2, termList2);

		// Asert
		HashMap<String, HashMap<String, ArrayList<String>>> index = keyIndexer
				.getIndex();

		Set<String> keys = index.keySet();
		Set<String> expectedKeySet = new HashSet<String>();
		expectedKeySet.add("88000001");
		expectedKeySet.add("br");
		expectedKeySet.add("j");
		expectedKeySet.add("anaesth");
		expectedKeySet.add("8801");
		expectedKeySet.add("effect");
		expectedKeySet.add("88000306");
		expectedKeySet.add("Electromyographic");

		HashMap<String, ArrayList<String>> docMap = index.get("anaesth");
		Set<String> docList = docMap.keySet();
		Set<String> expectedDocList = new HashSet<String>();
		expectedDocList.add("doc1");
		expectedDocList.add("doc2");
		
		ArrayList<String> keyPositionsInDoc2 = docMap.get("doc2");
		ArrayList<String> expectedKeyPositionsInDoc2 = new ArrayList<String>();
		expectedKeyPositionsInDoc2.add("3");
		expectedKeyPositionsInDoc2.add("5");
		
		HashMap<String, ArrayList<String>> docMapForString2 = index.get("effect");
		ArrayList<String> keyPositionsInDoc1 = docMapForString2.get("doc1");
		ArrayList<String> expectedKeyPositionsInDoc1 = new ArrayList<String>();
		expectedKeyPositionsInDoc1.add("1");
		expectedKeyPositionsInDoc1.add("6");

		Assert.assertEquals(8, index.size());
		Assert.assertEquals(expectedKeySet, keys);
		Assert.assertEquals(expectedDocList, docList);
		Assert.assertEquals(expectedKeyPositionsInDoc1, keyPositionsInDoc1);
		Assert.assertEquals(expectedKeyPositionsInDoc2, keyPositionsInDoc2);
	}
}
