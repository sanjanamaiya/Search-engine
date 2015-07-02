package com.ucsc.ir.searchengine.search;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import com.ucsc.ir.searchengine.indexer.IndexContants;

public class TfIdfSeach extends Searcher 
{

	public TfIdfSeach() throws IOException 
	{
		super();
	}
	
	@Override
	public TopDocs search(String searchQuery) throws IOException, ParseException
	{
		Query query = queryParser.parse(QueryParser.escape(searchQuery));
		TfIdfScoreQuery customQuery = new TfIdfScoreQuery(query);
		customQuery.setQueryString(searchQuery);

		int num = reader.numDocs();
		customQuery.setNumberOfDocs(num);
		return indexSearcher.search(customQuery, IndexContants.MAX_SEARCH);
	}

}
