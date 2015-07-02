package com.ucsc.ir.searchengine.search;

import java.io.IOException;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;

import com.ucsc.ir.searchengine.indexer.IndexContants;

public class TfIdfScoreQuery extends CustomScoreQuery
{

	private Query query;
	private String queryString = null;
	private int numOfDocs;
	public TfIdfScoreQuery(Query subQuery) 
	{
		super(subQuery);
		this.query = subQuery;
	}
	
	public void setQueryString(String queryString)
	{
		this.queryString = queryString;
	}
	
	public void setNumberOfDocs(int num)
	{
		numOfDocs = num;
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

            	long termFrequency = 0;
            	float tfidf = 1.0F;
                // Return score based on term frequency
            	LeafReader reader = context.reader();
            	
            	Terms termsVector = reader.getTermVector(doc, IndexContants.CONTENTS);
            	if (termsVector != null && termsVector.size() > 0)
            	{
            		TermsEnum termsEnum = termsVector.iterator(null);

            		queryString = queryString.toLowerCase();
            		String[] queryTermList = queryString.split(",\\s*|;\\s*|\\.\\s*|\\s+");
            		for (String term : queryTermList) 
            		{
            			BytesRef ref = new BytesRef(term.getBytes());
						if (termsEnum.seekExact(ref))
						{
							// the term is present in the contents of the 
							// file. Find its term frequency ...
							System.out.print("Term : " + term + " Document: " + reader.document(doc).getField(IndexContants.DOCID));
							termFrequency = termsEnum.totalTermFreq();
							long totalFreqOfTerm = reader.totalTermFreq(new Term(IndexContants.CONTENTS, term));
							float idf = (float) (Math.log(numOfDocs/(double)(totalFreqOfTerm+1)) + 1.0); 
							tfidf += termFrequency * idf ;
						}
					}
				}
            	
            	System.out.println(" ----------- Tfidf : " + tfidf);
            	return tfidf;
            }
		};
	}

}
