package com.ucsc.ir.searchengine.indexer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentIndexerTest {

	private DocumentIndexer docIndexer = null;
	
	/*@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		
	}*/
	
	@Before
	public void setUp() throws Exception 
	{
		docIndexer = new DocumentIndexer();
	}

	@After
	public void tearDown() throws Exception {
	}

	/*@Test
	public void shouldRemoveSpecialCharactersInDoc() 
	{
		// Arrange
		
		// Some content in MeSH section
		String content = "Adult; Alcohol, Ethyl/*AN; Breath Tests/*; Human; Irrigation; Male; Middle Age; Mouth/*; Temperature; Water. of this.";
		int sectionCount = 2;
		
		// Act
		String formattedString = docIndexer.formatSection(content, sectionCount);
		
		// Assert
		String expectedString = "Adult Alcohol Ethyl/*AN Breath Tests/* Human Irrigation Male Middle Age Mouth/* Temperature Water of this";
		Assert.assertEquals(expectedString, formattedString);
	}*/
	
	/*@Test
	public void shouldRemoveBracketsInAbstractSectionOfDoc() 
	{
		// Arrange
		
		// Some content in MeSH section
		String content = "Sixty-four patients with (ALG Merieux) between 1980 and 1985. The actuarial survival for all patients was 53% at 6 years, with.";
		int sectionCount = 6;
		
		// Act
		String formattedString = docIndexer.formatSection(content, sectionCount);
		
		// Assert
		String expectedString = "Sixty-four patients with ALG Merieux between 1980 and 1985. The actuarial survival for all patients was 53% at 6 years, with.";
		Assert.assertEquals(expectedString, formattedString);
	}*/
	
	@Test
	public void shouldParseAndIndexFile()
	{
		// Arrange
		//String fileLocation = System.getProperty("user.dir") + File.separator + "test" + File.separator + "resources" + File.separator + "sampleDoc.txt";
		
		String fileLocation = "D:\\downloads\\info_retrieval\\homework1\\ohsumed.88-91";
		
		docIndexer.indexFile(fileLocation);
		Indexer keyIndexer = Indexer.getInstance();

		HashMap<String, HashMap<String, ArrayList<String>>> index = keyIndexer
				.getIndex();

		/*Set<String> keys = index.keySet();
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
		
		Assert.assertEquals(expected, actual);*/
	}

}
