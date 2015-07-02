package com.ucsc.ir.searchengine.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ucsc.ir.searchengine.indexer.IndexContants;

public class OhsumedQueryParser 
{
	String queryFileLocation = "";
	public OhsumedQueryParser(String queryFileLocation)
	{
		this.queryFileLocation = queryFileLocation;
	}
	
	public List<OhsumedQuery> getQueryList()
	{
		List<OhsumedQuery> queries = new ArrayList<OhsumedQuery>();
		OhsumedQuery ohsumedQuery = null;
		File queryFile = new File(queryFileLocation);
		if (queryFile.exists())
		{
			BufferedReader reader = null;
			try
			{
				String line = null;
				reader = new BufferedReader(new FileReader(queryFile));
				if (reader != null)
				{
					while ((line = reader.readLine()) != null)
					{
						if (line.equals("<top>"))
						{
							// start of query...
							ohsumedQuery = new OhsumedQuery();
							while (!(line = reader.readLine()).equals("</top>"))
							{
								if (line.startsWith(IndexContants.QUERY_NUM))
								{
									ohsumedQuery.setQueryNumber(line.substring(IndexContants.QUERY_NUM.length()));
								}
								if (line.startsWith(IndexContants.QUERY_TITLE))
								{
									ohsumedQuery.setTitle(line.substring(IndexContants.QUERY_TITLE.length()));
								}
								if (line.startsWith(IndexContants.QUERY_DESCR))
								{
									line = reader.readLine();
									if (line != null)
									{
										ohsumedQuery.setDescription(line);
									}
								}
							}
							
							queries.add(ohsumedQuery);
						}
					}
				}
			}
			catch(Throwable e)
			{
				
			}
			finally
			{
				if (reader != null)
				{
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return queries;
	}
	
	public class OhsumedQuery
	{
		private String QueryNumber;
		private String title;
		private String description;
		
		public String getQueryNumber() {
			return QueryNumber;
		}
		
		public void setQueryNumber(String queryNumber) {
			QueryNumber = queryNumber;
		}
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getDescription() {
			return description;
		}
		
		public void setDescription(String description) {
			this.description = description;
		}
		
	}

}
