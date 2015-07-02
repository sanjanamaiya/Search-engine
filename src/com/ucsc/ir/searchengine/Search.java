package com.ucsc.ir.searchengine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.ucsc.ir.searchengine.indexer.DocumentIndexer;
import com.ucsc.ir.searchengine.indexer.IndexContants;
import com.ucsc.ir.searchengine.search.BooleanSearch;
import com.ucsc.ir.searchengine.search.MedSearch;
import com.ucsc.ir.searchengine.search.OhsumedQueryParser;
import com.ucsc.ir.searchengine.search.OhsumedQueryParser.OhsumedQuery;
import com.ucsc.ir.searchengine.search.Searcher;
import com.ucsc.ir.searchengine.search.TermFrequencySearch;
import com.ucsc.ir.searchengine.search.TfIdfSeach;

/**
 * Starting point of Application
 * @author sanjana
 *
 */
public class Search {

	private static final Logger LOGGER = Logger.getLogger(Search.class.getName());
	public static void main(String[] args)
	{
		Handler fileHandler  = null;
		try
		{
			fileHandler  = new FileHandler("./trec_output.log");
			LOGGER.addHandler(fileHandler);
			fileHandler.setLevel(Level.ALL);
			LOGGER.setLevel(Level.ALL);
		}
		catch (IOException e)
		{
			LOGGER.log(Level.WARNING, "Error initializing file handler for logger");
		}
		
		LOGGER.log(Level.FINE, "Starting search engine");
		LOGGER.log(Level.FINE, "Building index of terms...");
		boolean indexSuccess = false;

		String documentFileLocation = null;
		String queryFileLocation = null;
		String outputFileLocation = null;
        
		if (args.length > 0 && args[0].equals("--help"))
		{
			System.out.println("Usage: java -jar SearchEngine.jar <location of corpus document> <location of query file> <results file location>");
			return;
		}
		if (args.length != 3)
		{
		    System.out.println("Needs three arguments: documentFileLocation, "
		        +  "queryFileLocation, outputFolderLocation");
		    System.out.println("Usage: java -jar SearchEngine.jar <location of corpus document> <location of query file> <results file location>");
		    return;
		}
		
		documentFileLocation = args[0];
		queryFileLocation  = args[1];
		outputFileLocation = args[2];
		
		
		if (!documentFileLocation.isEmpty() && !queryFileLocation.isEmpty() 
		    && !outputFileLocation.isEmpty())
		{
			indexSuccess = indexInputFile(documentFileLocation);
			if (indexSuccess)
			{
				// search
				Searcher search = null;
				try 
				{
					List<OhsumedQuery> queries = new OhsumedQueryParser(queryFileLocation).getQueryList();
					if (queries == null || queries.size() == 0)
					{
						System.out.println("No query file found, exiting...");
						return;
					}
					
					outputFileLocation = outputFileLocation + File.separator + "search_results";
					// Run 4 different ranking algorithms:
					// 1. Boolean Search
					
					long startTime = System.currentTimeMillis();
					//search = new BooleanSearch();
					//runSearch(outputFileLocation + ".bool", search, queries, "bool");
					long endTime   = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					System.out.println("Running time of Boolean Search: " + totalTime);
					LOGGER.log(Level.INFO, "Running time of Boolean Search: " + totalTime);
					
					
					// 2. tf ranking
					
					/*startTime = System.currentTimeMillis();
					search = new TermFrequencySearch();
					runSearch(outputFileLocation + ".tf", search, queries, "tf");
					endTime   = System.currentTimeMillis();
					totalTime = endTime - startTime;
					System.out.println("Running time of tf Search: " + totalTime);
					LOGGER.log(Level.INFO, "Running time of tf Search: " + totalTime);*/
					
					
					// 3. tf*idf ranking
					
					startTime = System.currentTimeMillis();
					search = new TermFrequencySearch();
					search = new TfIdfSeach();
					runSearch(outputFileLocation + ".idf", search, queries, "tfidf");
					endTime   = System.currentTimeMillis();
					totalTime = endTime - startTime;
					System.out.println("Running time of tfIdf Search: " + totalTime);
					LOGGER.log(Level.INFO, "Running time of tfIdf Search: " + totalTime);
					
					
					// 4. Custom Search : MedSearch
					
					/*startTime = System.currentTimeMillis();
					search = new MedSearch();
					runSearch(outputFileLocation + ".med", search, queries, "med");
					endTime   = System.currentTimeMillis();
					totalTime = endTime - startTime;
					System.out.println("Running time of MedSearch  Search: " + totalTime);
					LOGGER.log(Level.INFO, "Running time of MedSearch Search: " + totalTime);
					
					// Run Lucene's default Search as a baseline
					startTime = System.currentTimeMillis();
					search = new Searcher();
					runSearch(outputFileLocation + ".lucene", search, queries, "lucene");
					endTime   = System.currentTimeMillis();
					totalTime = endTime - startTime;
					System.out.println("Running time of Lucene Search: " + totalTime);
					LOGGER.log(Level.INFO, "Running time of Lucene Search: " + totalTime);*/
					
				} 
				catch (IOException e) 
				{
					LOGGER.log(Level.WARNING, "Exception running search: " + e);
				} 
				catch (ParseException e) 
				{
					LOGGER.log(Level.WARNING, "Exception running search: " + e);
				}
				finally
				{
					if (search != null)
					{
						try {
							search.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		else
		{
			System.out.println("Unable to locate the document file, exiting");
			LOGGER.log(Level.WARNING, "Unable to locate the document file, exiting");
		}
	}
	
	private static void runSearch(String outputFileLocation, Searcher search,
			List<OhsumedQuery> queries, String rankType) throws IOException, ParseException 
	{
		PrintWriter writer = null;
	    try 
	    {
			writer = new PrintWriter(outputFileLocation, "UTF-8");
			for (OhsumedQuery ohsumedQuery : queries) 
			{
				TopDocs topDocs = search.search(ohsumedQuery.getDescription());
				ScoreDoc[] hits = topDocs.scoreDocs;

				// System.out.println("Number of hits : " + hits.length);
				for (int i = 0; i < hits.length; i++) 
				{
					Document doc = search.getDocument(hits[i].doc);
					// System.out.println("Doc retrieved: " +
					// doc.get(IndexContants.DOCID));

					writer.println(ohsumedQuery.getQueryNumber().trim()
							+ " Q0 " + doc.get(IndexContants.DOCID) + " "
							+ (i + 1) + " " + hits[i].score + " " + rankType);

					System.out.println(ohsumedQuery.getQueryNumber().trim()
							+ " Q0 " + doc.get(IndexContants.DOCID) + " "
							+ (i + 1) + " " + hits[i].score + " " + rankType);
				}
			}
        } 
	    catch(Throwable e) 
        {
          System.out.println("Error opening output file : " + outputFileLocation);
        }
	    finally
	    {
	    	if (writer != null)
	    	{
	    		writer.close();
	    	}
	    }
		
	}
	private static boolean indexInputFile(String documentFileLocation) 
	{
		boolean indexSuccess = false;;
		try
		{
			File docFile = new File(documentFileLocation);
			if (!docFile.exists())
			{
				System.out.println("The specified file does not exist, exiting");
			}
			else
			{
				DocumentIndexer parser = new DocumentIndexer();
				parser.indexFile(documentFileLocation);
				indexSuccess = true;
			}
		}
		catch(Throwable e)
		{
			System.out.println("Error building the index: " + e);
			LOGGER.warning("Error building the index: " + e);
		}
		return indexSuccess;
	}

}
