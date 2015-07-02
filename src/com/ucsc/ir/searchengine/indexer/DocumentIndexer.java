package com.ucsc.ir.searchengine.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Parses file containing OHSUMED documents
 * The format of an OHSUMED doc:
 * .I      sequential identifier 
	 	   (important note: documents should be processed in this order)
 * .U      MEDLINE identifier (UI) 
	 	   (<DOCNO> used for relevance judgements)
 * .M      Human-assigned MeSH terms (MH)
 * .T      Title (TI)
 * .P      Publication type (PT)
 * .W      Abstract (AB)
 * .A      Author (AU)
 * .S      Source (SO)
 * 
 * @author sanjana
 *
 */
public class DocumentIndexer 
{
	private static final Logger LOGGER = Logger.getLogger(DocumentIndexer.class.getName());
	public void indexFile(String fileLocation)
	{
		File sourcefile = new File(fileLocation);
		if (sourcefile.exists())
		{
			BufferedReader reader = null;
			try
			{
				StringBuilder document = new StringBuilder();
				reader = new BufferedReader(new FileReader(sourcefile));
				String content = reader.readLine();
				//int sectionCount = 0;
				while (content != null)
				{
					// Each doc within file starts at ".I"
					//sectionCount = 0;
					if (content.startsWith(IndexContants.DOC_SEQUENTIAL_IDENTIFIER))
					{
						// Start of document in file
						document = new StringBuilder();
						String docId = content.split("\\s+")[1];
						content = reader.readLine();
						while (content != null)
						{
							if (content.startsWith(IndexContants.DOC_SEQUENTIAL_IDENTIFIER))
							{
								break;
							}
							if (content.startsWith(IndexContants.DOC_MEDLINE_IDENTIFIER))
							{
								content = reader.readLine();
								//sectionCount++;
								docId = content;
							}
							else if (isSectionBeginning(content))
							{
								if (shouldSkipSection(content))
								{
									content = reader.readLine();
								}
								
								document.append(" ");
								//sectionCount++;
							}
							else
							{
								//String newcontent = formatSection(content, sectionCount);
								document.append(content);
							}
							
							content = reader.readLine();
						}
						
						// Have the complete document, parse it and send it to the Indexer
						LuceneIndexer.getInstance().indexFile(document.toString(), docId);
						//System.out.println("Done building index for doc: " + docId);
					}
				}
				
				System.out.println("Checking tokens...");
			}
			catch (IOException e)
			{
				LOGGER.warning("Exception thrown while reading file: " + fileLocation + ", Exception: " + e);
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
				LuceneIndexer.getInstance().closeWriter();
			}
		}
	}

	private boolean shouldSkipSection(String content) 
	{
		return (!(content.startsWith(IndexContants.DOC_TITLE)
				|| content.startsWith(IndexContants.DOC_MESH_TERMS) || 
				content.startsWith(IndexContants.DOC_ABSTRACT)));
	}

	/*protected String formatSection(String content, int sectionCount) 
	{
		OHSUMED docSection = OHSUMED.valueOf(sectionCount);
		switch (docSection) 
		{
		case DOC_ABSTRACT:
		case DOC_TITLE:
			content = content.replaceAll("\\(|\\)", "");
			break;
		default:
			break;
		}
		
		return content;
	}*/

	/**
	 * Tokenize the keywords and send to the Indexer 
	 * @param string
	 * @throws IOException 
	 */
	/*private void addDocumentToIndex(String docId, String docString) throws IOException 
	{
		// Maybe MeSH strings need to be split only on ; ???
		//String[] keyList = docString.split(",\\s*|;\\s*|\\.\\s*|\\s+"); //docString.split("[,;.W+]");
		//Indexer keyWordIndexer = Indexer.getInstance();
		//keyWordIndexer.processDocument(docId, keyList);
		
	}*/
	
	private boolean isSectionBeginning(String line)
	{
		return (line.startsWith(IndexContants.DOC_ABSTRACT) || line.startsWith(IndexContants.DOC_AUTHOR) ||
				line.startsWith(IndexContants.DOC_MEDLINE_IDENTIFIER) || line.startsWith(IndexContants.DOC_MESH_TERMS)
						|| line.startsWith(IndexContants.DOC_PUBLICATION_TYPE) || line.startsWith(IndexContants.DOC_SOURCE)
						|| line.startsWith(IndexContants.DOC_TITLE));
	}

}
