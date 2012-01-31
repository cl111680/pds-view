// Copyright 2006-2011, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id: FileObject.java 9719 2011-10-27 21:01:11Z mcayanan $
package gov.nasa.pds.citool.file;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains file information to be used in registering file objects
 * to the PDS4 Registry.
 *
 * @author mcayanan, hyunlee
 *
 */
public class FileObject {
  
	/** File name. */
	private String name;

	/** File location. */
	private String location;

	/** File size. */
	private long size;

	/** File creation date time. */
	private String creationDateTime;

    /** md5 checksum. */
	private String checksum;

	/** The product identifier when registered to the PDS Storage Service. */
	private String storageServiceProductId;
  
	/** The access url of the transport service. */
	private String accessUrl;
  
	/**
	 * Constructor.
	 *
	 * @param name File name.
	 * @param location File location.
	 * @param size File size.
	 * @param creationDateTime File creation date time.
	 * @param checksum checksum of the file.
	 */
	public FileObject(String name, String location, long size,
			String creationDateTime, String checksum) {
		this.name = name;
		this.location = location;
		this.size = size;
		this.creationDateTime = creationDateTime;
		this.checksum = checksum;
		this.storageServiceProductId = null;
		this.accessUrl = null;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public long getSize() {
		return size;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setStorageServiceProductId(String productId) {
		this.storageServiceProductId = productId;
	}

	public String getStorageServiceProductId() {
		return storageServiceProductId;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public String getAccessUrl() {
		return accessUrl;
	}
}