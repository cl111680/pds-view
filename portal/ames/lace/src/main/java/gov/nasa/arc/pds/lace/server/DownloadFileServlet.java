package gov.nasa.arc.pds.lace.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implements a servlet that allows a web client to download a file.
 */
@SuppressWarnings("serial")
@Singleton
public class DownloadFileServlet extends HttpServlet {

	/** The number of bytes to read, in each chunk, from the file. */
	private static final int BUFFER_SIZE = 4096;

	/** HTTP Content-Disposition header string. */
	private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

	/** Value of Content-Disposition indicating to the browser to show the
	 * file save dialog.
	 */
	private static final String ATTACHMENT_DISPOSITION = "attachment; filename=";

	private ServerConfiguration serverConfig;

	/**
	 * Creates a new instance with a given server configuration.
	 *
	 * @param serverConfig the server configuration
	 */
	@Inject
	public DownloadFileServlet(ServerConfiguration serverConfig) {
		this.serverConfig = serverConfig;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		String fileName = new File(req.getParameter("fileName")).getName();
		String desiredName = req.getParameter("desiredName");
		if (desiredName == null) {
			desiredName = fileName;
		}

		File uploadDir = serverConfig.getUploadRoot();
		File desiredFile = new File(uploadDir, fileName);

		ServletContext servletContext = getServletContext();
		String mimeType = servletContext.getMimeType(fileName);

		System.out.println("Downloading file '" + fileName + "' of MIME type " + mimeType + ".");

		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(desiredFile);

			resp.setContentType(mimeType);
			resp.setContentLength((int) desiredFile.length()); // Assuming the file is < 2GB in size
			resp.setHeader(CONTENT_DISPOSITION_HEADER, ATTACHMENT_DISPOSITION + desiredName);

			ServletOutputStream out = resp.getOutputStream();

			//copy binary content to output stream
			byte[] outputByte = new byte[BUFFER_SIZE];
			int nRead;
			while ((nRead = fileIn.read(outputByte)) > 0) {
				out.write(outputByte, 0, nRead);
			}
			fileIn.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			System.out.println("Exception during file download: " + e);
		} finally {
			if (fileIn != null) {
				fileIn.close();
				desiredFile.delete(); // We don't need the file after downloading.
			}
		}
	}

}