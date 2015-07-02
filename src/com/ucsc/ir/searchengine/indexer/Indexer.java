package com.ucsc.ir.searchengine.indexer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates inverted index of terms in the corpus
 * @author sanjana
 *
 */
public class Indexer 
{
	
	private static HashMap<String, HashMap<String, ArrayList<String>>> invertedIndex = null;
	private static Indexer keyWordIndexer = null; 
	
	protected Indexer()
	{
		
	}
	
	public static Indexer getInstance()
	{
		if ( invertedIndex == null )
		{
			invertedIndex = new HashMap<String, HashMap<String, ArrayList<String>>>();
		}
		
		if (keyWordIndexer == null)
		{
			keyWordIndexer = new Indexer();
		}
		
		return keyWordIndexer;
	}
	
	public void addKeyToIndex(String keyTerm, String docId, int position)
	{
		if (invertedIndex.containsKey(keyTerm))
		{
			// do null check instead
			HashMap<String, ArrayList<String>> docList = invertedIndex.get(keyTerm);
			if (docList.containsKey(docId))
			{
				docList.get(docId).add(String.valueOf(position));
			}
			else
			{
				ArrayList<String> positionListForDoc = new ArrayList<String>();
				positionListForDoc.add(String.valueOf(position));
				docList.put(docId, positionListForDoc);
			}
		}
		else
		{
			ArrayList<String> positionListForDoc = new ArrayList<String>();
			positionListForDoc.add(String.valueOf(position));
			HashMap<String, ArrayList<String>> valueMap = new HashMap<String, ArrayList<String>>();
			valueMap.put(docId, positionListForDoc);
			invertedIndex.put(keyTerm, valueMap);
		}
		
	}

	public void processDocument(String docId, String[] termList)
	{
		int termPos = 0;
		for (String term : termList) 
		{
			addKeyToIndex(term.trim(), docId, termPos++);
		}
	}
	
	public void resetIndex()
	{
		invertedIndex = new HashMap<String, HashMap<String, ArrayList<String>>>();
	}
	
	/**
	 * Added for unit tests
	 * @return
	 */
	protected HashMap<String, HashMap<String, ArrayList<String>>> getIndex()
	{
		return invertedIndex;
	}
}
