package com.ucsc.ir.searchengine.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;

import com.ucsc.ir.searchengine.indexer.IndexContants;

public class MedScoreQuery extends CustomScoreQuery {

	private Query query;
	private List<String> queryStringList = null;
	private int numOfDocs;
	
	public MedScoreQuery(Query subQuery) {
		super(subQuery);
		this.query = subQuery;
	}
	
	public void setQueryString(List<String> queryStringList)
	{
		this.queryStringList = queryStringList;
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

            	double termFrequency = 0;
            	float tfidf = 0.0F;
            	HashMap<String, Integer> termFrequencyMap = new HashMap<String, Integer>();
                // Return score based on term frequency
            	LeafReader reader = context.reader();
            	
            	Terms termsVector = reader.getTermVector(doc, IndexContants.CONTENTS);
            	int numOfTermsFound = 0;
            	SortedSet<PositionAndTermContainer> termPositionsInDoc = 
            	      new TreeSet<PositionAndTermContainer>();
            	ArrayList<Integer> nearbyTermDistance = new ArrayList<Integer>();
            	if (termsVector != null && termsVector.size() > 0)
            	{
            		TermsEnum termsEnum = termsVector.iterator(null);

            		for (String term : queryStringList) 
            		{
            			BytesRef ref = new BytesRef(term.getBytes());
						if (termsEnum.seekExact(ref))
						{
							termFrequencyMap.put(term, 
							     termFrequencyMap.containsKey(term) ? 
							         termFrequencyMap.get(term) + 1 : 1);
							termFrequency = termsEnum.totalTermFreq();
							long totalFreqOfTerm = reader.totalTermFreq(new Term(IndexContants.CONTENTS, term));
							float idf = (float) (Math.log(numOfDocs/(double)(totalFreqOfTerm+1)) + 1.0);
							tfidf += termFrequency * idf ;
							numOfTermsFound++;
							
							// Find positions of this term in the doc.
							 PostingsEnum postingsEnum =  termsEnum.postings(null, null, PostingsEnum.POSITIONS);
							 postingsEnum.nextDoc();
							 
							 
							//Retrieve the term frequency in the current document
				            int freq = postingsEnum.freq();
				            for(int i = 0; i < freq; i++){
				               int position = postingsEnum.nextPosition();
				               termPositionsInDoc.add(new PositionAndTermContainer(position, term));
		                    }
						}
					}
            		
            		if (numOfTermsFound > 1) {
            		  nearbyTermDistance = 
            		      PositionAndTermContainer.getPositionDiffsBetweenDistinctTerms(termPositionsInDoc);
            		}
				}

            	return (tfidf *  numOfTermsFound * (float) Math.pow(1.3, nearbyTermDistance.size()));
            }
		};
	}

}
