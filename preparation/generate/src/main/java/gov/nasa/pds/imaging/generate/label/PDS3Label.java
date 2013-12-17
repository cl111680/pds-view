//*************************************************************/
//Copyright (C) NASA/JPL  California Institute of Technology. */
//PDS Imaging Node                                            */
//All rights reserved.                                        */
//U.S. Government sponsorship is acknowledged.                */
//*************************************************************/
package gov.nasa.pds.imaging.generate.label;

import gov.nasa.pds.imaging.generate.TemplateException;
import gov.nasa.pds.imaging.generate.context.ContextUtil;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Represents PDS3 Label object to provide the necessary functionality to
 *
 * @author jpadams
 * @author atinio
 * @author astanboli
 *
 */
public class PDS3Label implements PDSObject {

	private static final boolean debug = true;

    public static final String CONTEXT = "label";

    public static void main(final String[] args) {
        // PDSLabel label = new
        // PDSLabel("/mnt/scratch/ays/atlasII/atlas-ingest/etc/sample/1p216067135edn76pop2102l2m1.img");
        final PDS3Label label = new PDS3Label(
                "/Users/jpadams/dev/workspace/transform-workspace/transformation-tool/etc/sample/1p216067135edn76pop2102l2m1.img");
        System.out.println(label.toString());

    }

    private List<String> pdsObjectTypes;

    // Contains the DOM representation of
    // the PDS label as generated by the
    // PDSLabel2DOM parser
    private Document document;
    // Contains a flattened representation of
    // the PDS label. In this case, flattened
    // means everything has been normalized
    // to simple keyword=value pairs.
    private Map<String, Map> flatLabel;
    private String filePath;
    public ContextUtil ctxtUtil;

    private String configPath;

    /**
     * Constructor
     */
    public PDS3Label() {
        this.filePath = null;
        this.flatLabel = new TreeMap<String, Map>();
    }

    /**
     * Constructor
     *
     * @param filePath
     */
    public PDS3Label(final String filePath) {
        this.filePath = filePath;
        this.flatLabel = new TreeMap<String, Map>();
    }

    /**
     * Retrieves the value for the specified key
     *
     * @param key
     * @return value for key
     */
    @Override
    public final Object get(final String key) {
        final Object node = getNode(key.toUpperCase());
        if (node == null) {
          return null;
        } else if (node instanceof LabelObject) {
            return node;
        } else {
            return ((ItemNode) node).toString();
        }
    }

    /**
     * Returns the variable to be used in the Velocity Template Engine to map to
     * this object.
     */
    @Override
    public final String getContext() {
        return CONTEXT;
    }

    @Override
    public final String getFilePath() {
        return this.filePath;
    }

    @Override
    public final List getList(final String key) {
        return ((ItemNode) getNode(key)).getValues();
    }

    private final Object getNode(final String key) {
        // Handles call where . is embedded in key.
        // Mainly for IndexedGroup implementation.
        if (key.contains(".")) {
            final String[] links = key.split("\\.");
            // object->item
            // object->subobject->item
            if (links[0] == null) {
                return null;
            }
            LabelObject labelObj = (LabelObject) this.flatLabel.get(links[0]);
            if (labelObj == null) {
                return null;
            }
            Object obj = null;
            for (int i = 1; i < links.length; ++i) {
                obj = labelObj.get(links[i]);
                if (obj instanceof LabelObject) {
                    labelObj = (LabelObject) obj;
                }
            }
            return obj;
        } else {
            return this.flatLabel.get(key);
        }
    }

    @Override
    public final List<Map<String, String>> getRecords(final String... keyword)
            throws TemplateException {

        this.ctxtUtil = new ContextUtil();

        for (int i = 0; i < keyword.length; i++) {
            this.ctxtUtil.addDictionaryElement(keyword[i].toUpperCase(),
                    getList(keyword[i].toUpperCase()));
        }
        return this.ctxtUtil.getDictionary();
    }

    @Override
    public final List<Map<String, String>> getRecordsWithIndices(
            final List<String> keys, final String... keyword)
            throws TemplateException {

        this.ctxtUtil = new ContextUtil();

        final int size = keys.size();
        if (keys.size() != keyword.length) {
            throw new TemplateException("getRecords method must contain"
                    + " same number of keys and keywords.");
        }

        for (int i = 0; i < size; i++) {
            this.ctxtUtil.addDictionaryElement(keys.get(i).toUpperCase(),
                    getList(keyword[i].toUpperCase()));
        }
        return this.ctxtUtil.getDictionary();
    }

    @Override
    public final String getUnits(final String key) {
        return ((ItemNode) getNode(key)).getUnits();
    }

    /*@Override
    public final void setConfigPath(final String path) {
        this.configPath = path;
    }

    @Override
    public final void setInputPath(final String filePath) {
        this.filePath = filePath;
    }*/

    @Override
    public final void setParameters(PDSObject pdsObject, String confPath) {
    	this.configPath = confPath;
    	this.filePath = pdsObject.getFilePath();
    }

    @Override
    public void setMappings() {
        try {
            final PDS3LabelReader reader = new PDS3LabelReader();

            this.document = reader.parseLabel(this.filePath);

            // start of traversal of DOM
            final Node root = this.document.getDocumentElement();

            this.flatLabel = reader.traverseDOM(root);

        } catch (final FileNotFoundException fnfe) {
            // TODO - create a logger
            fnfe.printStackTrace();
        }
    }

    @Override
    public final String toString() {
        final StringBuffer strBuff = new StringBuffer();
        final Set<String> keys = this.flatLabel.keySet();
        for (final String key : keys) {
            // String key = (String) iter.next();
            strBuff.append(key + " = " + this.flatLabel.get(key) + "\n");
        }
        return strBuff.toString();
    }

    private final void debug(String msg) {
    	if (this.debug) {
    		System.out.println(msg);
    	}
    }

}
