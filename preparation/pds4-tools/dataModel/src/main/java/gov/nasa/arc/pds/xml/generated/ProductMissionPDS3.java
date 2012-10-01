//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.19 at 05:23:51 PM PDT 
//


package gov.nasa.arc.pds.xml.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 *  An Mission product describes a mission. This product captures the PDS3 catalog mission information. 
 * 
 * <p>Java class for Product_Mission_PDS3 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Product_Mission_PDS3">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pds.nasa.gov/pds4/pds/v03}Product">
 *       &lt;sequence>
 *         &lt;element name="Reference_List" type="{http://pds.nasa.gov/pds4/pds/v03}Reference_List" minOccurs="0"/>
 *         &lt;element name="Mission_PDS3" type="{http://pds.nasa.gov/pds4/pds/v03}Mission_PDS3"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Product_Mission_PDS3", propOrder = {
    "referenceList",
    "missionPDS3"
})
@XmlRootElement(name = "Product_Mission_PDS3")
public class ProductMissionPDS3
    extends Product
{

    @XmlElement(name = "Reference_List")
    protected ReferenceList referenceList;
    @XmlElement(name = "Mission_PDS3", required = true)
    protected MissionPDS3 missionPDS3;

    /**
     * Gets the value of the referenceList property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceList }
     *     
     */
    public ReferenceList getReferenceList() {
        return referenceList;
    }

    /**
     * Sets the value of the referenceList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceList }
     *     
     */
    public void setReferenceList(ReferenceList value) {
        this.referenceList = value;
    }

    /**
     * Gets the value of the missionPDS3 property.
     * 
     * @return
     *     possible object is
     *     {@link MissionPDS3 }
     *     
     */
    public MissionPDS3 getMissionPDS3() {
        return missionPDS3;
    }

    /**
     * Sets the value of the missionPDS3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MissionPDS3 }
     *     
     */
    public void setMissionPDS3(MissionPDS3 value) {
        this.missionPDS3 = value;
    }

}