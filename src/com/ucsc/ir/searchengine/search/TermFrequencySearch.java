package com.ucsc.ir.searchengine.search;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import com.ucsc.ir.searchengine.indexer.IndexContants;

public class TermFrequencySearch extends Searcher 
{

	public TermFrequencySearch() throws IOException 
	{
		super();
		
	}
	
	@Override
	public TopDocs search(String searchQuery) throws IOException, ParseException
	{
		Query query = queryParser.parse(QueryParser.escape(searchQuery));
		TfScoreQuery customQuery = new TfScoreQuery(query);
		customQuery.setQueryString(searchQuery);

		return indexSearcher.search(customQuery, IndexContants.MAX_SEARCH);
	}

}
