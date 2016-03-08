package gov.nasa.arc.pds.lace.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements an object that holds typing information for model objects.
 */
public class LabelItemType implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean isComplex;	    			// True, if this type is a container for sub-elements
	private int minOccurrences;					// Holds the minOccurrences value from the type particle
	private int maxOccurrences;	    			// Holds the maxOccurrences value from the type particle
	private int minLength;						// For simple types, the minimum value length
	private int maxLength;						// For simple types, the maximum value length
	private String elementName;		 			// The name of the element that will hold the type
	private String elementNamespace; 			// The namespace of the element that will hold the type
	private String typeName;		 			// The name for this type, or null if this is an anonymous type
	private String typeNamespace;				// The namespace for this type
	private List<LabelItem> initialContents;	// For complex types, a list of items that should be the initial contents when this type is added
	private List<String> validValues;			// A list of valid values, or null if there is no validation.
	//private enum valueType {};				// Either NUMERIC, DATE, or REGEX (needs to be an enumeration), depending on the value type constraints
	//patterns									// A list of regular expressions, for REGEX types	

	/** 
	 * Creates a new <code>LabelItemType</code> instance.
	 */
	public LabelItemType() {
		// nothing to do
	}

	/**
	 * Gets the minimum occurrences value for the label element
	 * associated with this type.
	 * 
	 * @return the minimum occurrences value
	 */
	public int getMinOccurrences() {
		return minOccurrences;
	}

	/**
	 * Sets the minimum occurrences value from the type particle.
	 * 
	 * @param minOccurrences the minimum occurrences value
	 */
	public void setMinOccurrences(int minOccurrences) {
		this.minOccurrences = minOccurrences;
	}

	/**
	 * Gets the maximum occurrences value for the label element
	 * associated with this type.
	 * 
	 * @return the maximum occurrences value
	 */
	public int getMaxOccurrences() {
		return maxOccurrences;
	}

	/**
	 * Sets the maximum occurrences value from the type particle.
	 * 
	 * @param maxOccurrences the maximum occurrences value
	 */
	public void setMaxOccurrences(int maxOccurrences) {
		this.maxOccurrences = maxOccurrences;
	}

	/**
	 * Tests whether this type is a container for sub-elements.
	 * 
	 * @return true, if this type is a container for sub-elements, false, otherwise
	 */
	public boolean isComplex() {
		return isComplex;
	}

	/**
	 * Sets a flag to indicate whether this type is a container for sub-elements or not.
	 *
	 * @param flag true, if the item is a container for sub-elements, false, otherwise
	 */
	public void setComplex(boolean flag) {
		this.isComplex = flag;
	}

	/**
	 * Gets the minimum value length for simple elements.
	 * 
	 * @return the minimum value length
	 */
	public int getMinLength() {
		return minLength;
	}

	/**
	 * Sets the minimum value length for simple elements.
	 * 
	 * @param minLength the minimum value length
	 */
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	/**
	 * Gets the maximum value length for simple elements.
	 * 
	 * @return the maximum value length
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * Sets the maximum value length for simple elements.
	 * 
	 * @param maxLength the maximum value length
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Gets the initial contents for this type.
	 * TODO: The view is modifiable--Change the method to 
	 * return an unmodifiable view of the initial contents.
	 * 
	 * @return a list of label items
	 */
	public List<LabelItem> getInitialContents() {
		//return Collections.unmodifiableList(initialContents);
		return initialContents;
	}

	/**
	 * Sets the initial contents for this type.
	 *
	 * @param initialContents a list of label items
	 */
	public void setInitialContents(List<LabelItem> initialContents) {
		this.initialContents = initialContents;
	}

	/**
	 * Gets the name of the element that will hold the type.
	 * 
	 * @return the element name
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * Sets the name of the element that will hold the type.
	 * 
	 * @param elementName the element name
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	/**
	 * Gets the namespace of the element that will hold the type.
	 * 
	 * @return a string value
	 */
	public String getElementNamespace() {
		return elementNamespace;
	}

	/**
	 * Sets the namespace of the element that will hold the type.
	 * 
	 * @param elementNamespace a string value of the element namespace
	 */
	public void setElementNamespace(String elementNamespace) {
		this.elementNamespace = elementNamespace;
	}

	/**
	 * Gets the name for this type.
	 * 
	 * @return a string value
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Sets a name for this type.
	 * 
	 * @param typeName the name for this type
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Gets the namespace for this type.
	 * 
	 * @return a string value
	 */
	public String getTypeNamespace() {
		return typeNamespace;
	}

	/**
	 * Sets the namespace for this type.
	 * 
	 * @param typeNamespace the type namespace
	 */
	public void setTypeNamespace(String typeNamespace) {
		this.typeNamespace = typeNamespace;
	}

	/**
	 * Gets the valid values.
	 *
	 * @return a list of valid values, or null if there is no validation
	 */
	public List<String> getValidValues() {
		return validValues;
	}

	/**
	 * Sets the valid values.
	 *
	 * @param values an array of string values
	 */
	public void setValidValues(String[] values) {
		validValues = new ArrayList<String>();
		for (String value : values) {
			validValues.add(value);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null || !(obj instanceof LabelItemType)) {
			return false;
		}

		LabelItemType other = (LabelItemType) obj;

		return
			isComplex == other.isComplex
			&& maxLength == other.maxLength
			&& minLength == other.minLength
			&& maxOccurrences == other.maxOccurrences
			&& minOccurrences == other.minOccurrences
			&& ((elementName==null && other.elementName==null)
					|| (elementName!=null && elementName.equals(other.elementName)))
			&& ((elementNamespace==null && other.elementNamespace==null)
					|| (elementNamespace!=null && elementNamespace.equals(other.elementNamespace)))
			&& ((typeName==null && other.typeName==null)
					|| (typeName!=null && typeName.equals(other.typeName)))
			&& ((typeNamespace==null && other.typeNamespace==null)
					|| (typeNamespace!=null && typeNamespace.equals(other.typeNamespace)))
			&& ((initialContents==null && other.initialContents==null)
					|| (initialContents!=null && initialContents.equals(other.initialContents)));
	}

	@Override
	public int hashCode() {
		String s =
			elementName + "/"
			+ elementNamespace + "/"
			+ typeName + "/"
			+ typeNamespace;

		return
			((((s.hashCode() * 17
			+ (initialContents==null ? 0 : initialContents.hashCode())) * 17
			+ (isComplex ? 1 : 0) * 17
			+ maxLength) * 17
			+ maxOccurrences) * 17
			+ minLength) * 17
			+ minOccurrences;
	}
}
