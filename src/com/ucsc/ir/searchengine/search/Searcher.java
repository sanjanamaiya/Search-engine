package com.ucsc.ir.searchengine.search;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import com.ucsc.ir.searchengine.indexer.IndexContants;
import com.ucsc.ir.searchengine.indexer.LuceneIndexer;

/**
 * By default this is a boolean search. If AND/OR is not
 * specified, then the terms in the query are "SHOULD" 
 * 
 * For an explicit BooleanQuery implementation, see BooleanSearch.java 
 * @author sanjana
 *
 */
public class Searcher 
{
	IndexSearcher indexSearcher;
	QueryParser queryParser;
	IndexReader reader;

	public Searcher() throws IOException 
	{
		/*Directory indexDirectory = FSDirectory
				.open(new File("D:\\downloads\\info_retrieval\\homework1\\index").toPath());*/
		Directory indexDirectory = LuceneIndexer.getInstance().getRamDir();
		reader = DirectoryReader.open(indexDirectory);
		indexSearcher = new IndexSearcher(reader);
		queryParser = new QueryParser(IndexContants.CONTENTS, new StandardAnalyzer());
	}

	public TopDocs search(String searchQuery) throws IOException, ParseException
	{
		Query query = queryParser.parse(QueryParser.escape(searchQuery));
		return indexSearcher.search(query, IndexContants.MAX_SEARCH);
	}

	public TopDocs search(Query query) throws IOException
	{
		return indexSearcher.search(query, IndexContants.MAX_SEARCH);
	}
	public Document getDocument(ScoreDoc scoreDoc)
			throws CorruptIndexException, IOException 
	{
		return indexSearcher.doc(scoreDoc.doc);
	}
	
	public Document getDocument(int docId) throws IOException 
	{
		return indexSearcher.doc(docId);
	}

	public void close() throws IOException 
	{
		if (reader != null)
		{
			reader.close();
		}
	}

}
