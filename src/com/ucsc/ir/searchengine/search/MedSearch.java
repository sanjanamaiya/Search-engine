package com.ucsc.ir.searchengine.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import com.ucsc.ir.searchengine.indexer.IndexContants;

public class MedSearch extends Searcher 
{

	public MedSearch() throws IOException {
		super();
	}

	@Override
	public TopDocs search(String searchQuery) throws IOException, ParseException
	{
		List<String> queryTermList = removeStopWords(searchQuery);
		Query query = queryParser.parse(QueryParser.escape(searchQuery));
		MedScoreQuery customQuery = new MedScoreQuery(query);
		customQuery.setQueryString(queryTermList);
		customQuery.setNumberOfDocs(reader.numDocs());

		return indexSearcher.search(customQuery, IndexContants.MAX_SEARCH);
	}
	
	
	
	protected List<String> removeStopWords(String queryString) 
	{
		queryString = queryString.replace("?", "");
		queryString = queryString.toLowerCase();
		String[] queryTermArray = queryString.split(",\\s*|;\\s*|\\.\\s*|\\s+");
		List<String> queryTermList = Arrays.asList(queryTermArray);
		List<String> filteredTermList = new ArrayList<String>();
		
		for (String word : queryTermList) 
		{
			if (!IndexContants.stopWords.contains(word))
			{
				if (word.endsWith("s"))
				{
					filteredTermList.add(word.substring(0, word.length() - 1));
				}
				else
				{
					//filteredTermList.add(word + "s");
				}
				filteredTermList.add(word);
				
			}
		}
		return filteredTermList;
	}
}
