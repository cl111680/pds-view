// Copyright 2002 California Institute of Technology.  ALL RIGHTS RESERVED.
// U.S. Government Sponsorship acknowledged.
//

package jpl.pds.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import jpl.eda.xmlquery.Header;
import jpl.eda.xmlquery.LargeResult;
import jpl.eda.xmlquery.Result;
import jpl.eda.xmlquery.XMLQuery;
import jpl.pds.server.FileQueryHandler;
import jpl.pds.util.PDS2FName;

/**
 * Handle product queries to zip PDS products. If zipName is
 * PDS_ZIPN_SIZE then return the total size of files that PDS_ZIPN would
 * download.
 *
 * @author Crichton and Hughes.
 */
public class ZipNFileHandler extends ChunkedFileHandler {
	public XMLQuery queryForFile(XMLQuery query, File[] fileArray, String zipName) {
		boolean returnSize = zipName.equals("PDS_ZIPN_SIZE");
		int j;

		for (j=0; j<fileArray.length; j++) {
			if (!fileArray[j].isFile() || !fileArray[j].canRead()) {
				System.err.println("ZipNFileHandler can't read file " + fileArray[j]);
				return query;
			}
		}

		// Figure out what MIME type the client can digest.
		boolean compatible = false;
		List mimeAccept = new ArrayList(query.getMimeAccept());

		if (returnSize) {			// Return total size of files found
			if (mimeAccept.isEmpty())
				mimeAccept.add("text/xml");
			for (Iterator i = mimeAccept.iterator(); i.hasNext();) {
				String desiredType = (String) i.next();
				if ("text/xml".equals(desiredType) || "text/*".equals(desiredType)
					|| "*/*".equals(desiredType)) {
					compatible = true;
					break;
				}
			}
			if (!compatible) {
				System.err.println("ZipNFileHandler can't satisfy request for non-compatible MIME types");
				return query;
			}

			long totalSize = 0;
			String filenames = null;
			for (j=0; j<fileArray.length; j++) {
				// new file names get appended to filenames
				filenames = PDS2FName.getNames(fileArray[j],filenames);
			}
			if (filenames == null) {
				System.err.println("PDS2FName returned null file list");
				return query;
			}

			for (StringTokenizer tokens = new StringTokenizer(filenames); tokens.hasMoreTokens();) {
				File fileToAdd = new File(tokens.nextToken());
				totalSize += fileToAdd.length();
			}

			StringBuffer b = new StringBuffer("");
			b.append("<?xml version=\"1.0\"?>").append(NL);
			b.append("<!DOCTYPE dirresult PUBLIC \"-//JPL/DTD OODT dirresult 1.0//EN\" \"http://starbrite.jpl.nasa.gov:80/dtd/dirresult.dtd\">").append(NL);
			b.append("<dirResult>").append(NL);
			b.append("<dirEntry>").append(NL);
			b.append("<fileSize>");
			b.append(totalSize);
			b.append("</fileSize>").append(NL);
			b.append("</dirEntry>").append(NL);

			b.append("</dirResult>").append(NL);
			String id = "Size" + String.valueOf(productID++);
			Result result = new Result(id, "text/xml", "", "", Collections.EMPTY_LIST, b.toString());
			query.getResults().add(result);
			return query;

		} else {
			// Return files found
			if (mimeAccept.isEmpty())
				mimeAccept.add("application/zip");
			for (Iterator i = mimeAccept.iterator(); i.hasNext();) {
				String desiredType = (String) i.next();
				if ("application/zip".equals(desiredType) || "application/*".equals(desiredType)
					|| "*/*".equals(desiredType)) {
					compatible = true;
					break;
				}
			}
			if (!compatible) {
				System.err.println("ZipNFileHandler can't satisfy request for non-compatible MIME types");
				return query;
			}

			ZipOutputStream out = null;
			BufferedInputStream source = null;
			try {
				File tempFile = File.createTempFile("oodt", ".zip");
				tempFile.deleteOnExit();
				File productDir = new File(System.getProperty(FileQueryHandler.PRODUCT_DIR_PROPERTY, "/"));
				String productDirName = productDir.getAbsolutePath(); 
				int lenProductDirName = productDirName.length()
					+ (productDirName.endsWith(File.separator) ? 0:1);

				out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile)));

				String filenames = null;
				for (j=0; j<fileArray.length; j++) {
					// new file names get appended to filenames
					filenames = PDS2FName.getNames(fileArray[j],filenames);
				}
				if (filenames == null) {
					System.err.println("PDS2FName returned null file list");
					return query;
				}

				byte[] buf = new byte[512];
				int numRead;
				for (StringTokenizer tokens = new StringTokenizer(filenames); tokens.hasMoreTokens();) {
					String filenameToAdd = tokens.nextToken();
					File fileToAdd = new File(filenameToAdd);
					if (filenameToAdd.startsWith(productDirName)) 	// strip productDirName
						filenameToAdd = filenameToAdd.substring(lenProductDirName);
					if (filenameToAdd.matches("[a-zA-Z]:.*"))           // strip windows drive
						filenameToAdd = filenameToAdd.substring(2);
					if (filenameToAdd.charAt(0) == File.separatorChar) // strip root separatorChar
						filenameToAdd = filenameToAdd.substring(1);
					ZipEntry entry = new ZipEntry(filenameToAdd.replace(File.separatorChar, DESIRED));
					entry.setMethod(ZipEntry.DEFLATED);
					out.putNextEntry(entry);

					source = new BufferedInputStream(new FileInputStream(fileToAdd));
					int sizeOfFile = 0;
					while ((numRead = source.read(buf)) != -1) {
						out.write(buf, 0, numRead);
						sizeOfFile += numRead;
					}
						
					out.closeEntry();
					System.err.println("Added " + fileToAdd.getName() + " of size " + sizeOfFile);
				}

				String id = "zipn" + String.valueOf(productID++);
				String productsFilename = zipName + ".zip";
				out.close();
				addProduct(id, tempFile, /*temporary*/true);
				Result result = new LargeResult(id, "application/zip", "JPL.PDS.Zip", productsFilename,
					Collections.singletonList(new Header(/*name*/"File", /*type*/"binary", /*unit*/null)),
					tempFile.length());
				query.getResults().add(result);

				tempFilename = tempFile.getPath();	// used for debugging

			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (out != null) try {
					out.close();
				} catch (IOException ignore) {}
				if (source != null) try {
					source.close();
				} catch (IOException ignore) {}
			}
			return query;
		}
	}

	/** What PDS considers a newline. */
	private static final String NL = "\r\n";

	/** Desired forward-slash path separator **/  
	final char DESIRED = '/'; 

	/** Next product ID to generate. */
	private int productID = 0;      

	/** Let main grab the tempfile */
	private String tempFilename = null;      

	/**
	 * Command-line driver.
	 *
	 * @param argv Command-line arguments.
	 */
	public static void main(String[] argv) {
		if (argv.length < 1) {
			System.err.println("Usage: [dirname...]");
			System.exit(1);
		}
		String queryString = "";
		for (int i=0; i<argv.length; i++) {
			if (i > 0) queryString += " AND ";
			queryString += "ONLINE_FILE_SPECIFICATION_NAME = " + argv[i];
		}
		queryString += " AND RETURN_TYPE = PDS_ZIPN";
		XMLQuery query = new XMLQuery(queryString, /*id*/"cli-1",
			/*title*/"Command-line Query",
			/*desc*/"This query came from the command-line and is directed to the ZipNFileHandler",
			/*ddId*/null, /*resultModeId*/null, /*propType*/null, /*propLevels*/null,
			XMLQuery.DEFAULT_MAX_RESULTS, null);
		ZipNFileHandler handler = new ZipNFileHandler();
		File productDir = new File(System.getProperty(FileQueryHandler.PRODUCT_DIR_PROPERTY, "/"));
		File [] fileArray = new File[argv.length];
		for (int i=0; i<argv.length; i++) {
			fileArray[i] = new File(productDir,argv[i]);
		}
		handler.queryForFile(query, fileArray, "optName");
		System.out.println(query.getXMLDocString());

		// Grab temp file for debuging before it is deleted
		byte [] readBuffer = new byte[16384];
		int cnt;
		try {
			FileInputStream src = new FileInputStream(handler.tempFilename);
			FileOutputStream dst = new FileOutputStream(ZIPNFILE);
			while ((cnt = src.read(readBuffer,0,readBuffer.length)) > 0)
				dst.write(readBuffer,0,cnt);
			src.close();
			dst.close();
		} catch (IOException ignore) {}

		System.exit(0);
	}

	/** Location to copy zip file for debugging */
	private static final String ZIPNFILE = System.getProperty("jpl.pds.server.ZipNFileHandler.zipfile",
		"C:/temp/ZipNFile.zip");
}
