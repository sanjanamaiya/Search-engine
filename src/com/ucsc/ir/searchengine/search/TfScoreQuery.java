package com.ucsc.ir.searchengine.search;

import java.io.IOException;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;

import com.ucsc.ir.searchengine.indexer.IndexContants;

public class TfScoreQuery extends CustomScoreQuery
{
	private Query query;
	private String queryString = null;
	public TfScoreQuery(Query subQuery) 
	{
		super(subQuery);
		this.query = subQuery;
	}
	
	public void setQueryString(String queryString)
	{
		this.queryString = queryString;
	}
	
	@Override
	public CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) 
	{
		return new CustomScoreProvider(context)
		{
            @Override
            public float customScore(int doc,
                    float subQueryScore,
                    float valSrcScore) throws IOException 
            {

            	int termFrequency = 0;
                // Return score based on term frequency
            	LeafReader reader = context.reader();
            	
            	Terms termsVector = reader.getTermVector(doc, IndexContants.CONTENTS);
            	if (termsVector != null && termsVector.size() > 0)
            	{
            		TermsEnum termsEnum = termsVector.iterator(null);
            		//Set<Term> queryTerms = new HashSet<Term>();

            		/*BytesRef term = null;
            		while ((term = termsEnum.next()) != null) {               
            	        String termText = term.utf8ToString();                              
            	        long termFreq = termsEnum.totalTermFreq();   
            	        long docCount = termsEnum.docFreq();   

            	        //System.out.println("term: "+termText+", termFreq = "+termFreq+", docCount = "+docCount); 
            	        
            	        queryString = queryString.toLowerCase();
            	        String[] queryTermList = queryString.split(",\\s*|;\\s*|\\.\\s*|\\s+");
            	        List<String> l = Arrays.asList(queryTermList);
            	        if (l.contains(termText))
            	        {
            	        	System.out.println("Term found " + term.toString());
            	        	System.out.println("term: "+termText+", termFreq = "+termFreq+", docCount = "+docCount); 
            	        }
            	     
            	       
            	    }  */    	

            		queryString = queryString.toLowerCase();
            		String[] queryTermList = queryString.split(",\\s*|;\\s*|\\.\\s*|\\s+");
            		for (String term : queryTermList) 
            		{
            			BytesRef ref = new BytesRef(term.getBytes());
						if (termsEnum.seekExact(ref))
						{
							// the term is present in the contents of the 
							// file. Find its term frequency ...
							//System.out.println("Term : " + term);
							//System.out.println("Document: " + reader.document(doc).getField(IndexContants.DOCID));
							termFrequency += termsEnum.totalTermFreq();
						}
					}
				}
            	
            	//System.out.println("Term frequency: " + termFrequency);
            	return termFrequency;
            }
		};
	}

}
