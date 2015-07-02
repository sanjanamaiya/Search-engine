package com.ucsc.ir.searchengine.search;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DefaultSimilarity;

import com.ucsc.ir.searchengine.indexer.IndexContants;

public class BooleanSearch extends Searcher
{

	private BooleanClause.Occur joinType = BooleanClause.Occur.SHOULD;
	public BooleanSearch(BooleanClause.Occur joinType) throws IOException 
	{
		super();
		if (joinType != null)
		{
			this.joinType = joinType;
		}
	}
	
	public BooleanSearch() throws IOException 
	{
		super();
	}
	
	@Override
	/**
	 * This creates Boolean queries based on BooleanClause specified
	 * Use the Searcher class and specify the Boolean keywords 
	 * (AND/OR) or (+/-) for the query in the Searcher class if
	 * "MUST" or "MUST NOT" need to be used
	 */
	public TopDocs search(String searchQuery) throws IOException, ParseException
	{
		BooleanQuery boolQuery = new BooleanQuery();
		searchQuery = searchQuery.toLowerCase();
		String[] queryTermList = searchQuery.split(",\\s*|;\\s*|\\.\\s*|\\s+");
		Query query = null;
		for (String term : queryTermList) 
		{
			query = new TermQuery(new Term(IndexContants.CONTENTS, term));
			boolQuery.add(query, joinType);
		}
		
		//System.out.println("Boolean query: " + boolQuery.toString());
		indexSearcher.setSimilarity(new CustomBooleanSearchSimilarity());
		return indexSearcher.search(boolQuery, IndexContants.MAX_SEARCH);
	}
	
	public class CustomBooleanSearchSimilarity extends DefaultSimilarity
	{
	    @Override
	    public float queryNorm(float sumOfSquaredWeights) {
	        return 1;
	    }

	    @Override
	    public float tf(float freq) 
	    {
	    	return (freq == 0) ? 0 : 1 ; 
	    }

	    @Override
	    public float idf(long docFreq, long numDocs) 
	    {
	        return 1;
	    }

	    @Override
	    public float coord(int overlap, int maxOverlap) 
	    {
	        return 1;
	    }
	}
	

}
