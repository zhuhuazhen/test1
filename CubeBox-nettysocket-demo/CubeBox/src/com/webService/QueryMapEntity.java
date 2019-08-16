package com.webService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for queryMapEntity complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;queryMapEntity&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;equipmentId&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;year&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;time&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;latitude&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;latitudeIdentity&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;longitude&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;dataStr&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;longitudeIdentity&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryMapEntity", propOrder = { "equipmentId", "year", "time",
		"latitude", "latitudeIdentity", "longitude", "dataStr",
		"longitudeIdentity" })
public class QueryMapEntity {

	@XmlElement(required = true)
	protected String equipmentId;
	protected String year;
	protected String time;
	protected String latitude;
	protected String latitudeIdentity;
	protected String longitude;
	protected String dataStr;
	protected String longitudeIdentity;

	/**
	 * Gets the value of the equipmentId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEquipmentId() {
		return equipmentId;
	}

	/**
	 * Sets the value of the equipmentId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEquipmentId(String value) {
		this.equipmentId = value;
	}

	/**
	 * Gets the value of the year property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Sets the value of the year property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setYear(String value) {
		this.year = value;
	}

	/**
	 * Gets the value of the time property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Sets the value of the time property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTime(String value) {
		this.time = value;
	}

	/**
	 * Gets the value of the latitude property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * Sets the value of the latitude property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLatitude(String value) {
		this.latitude = value;
	}

	/**
	 * Gets the value of the latitudeIdentity property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLatitudeIdentity() {
		return latitudeIdentity;
	}

	/**
	 * Sets the value of the latitudeIdentity property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLatitudeIdentity(String value) {
		this.latitudeIdentity = value;
	}

	/**
	 * Gets the value of the longitude property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * Sets the value of the longitude property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLongitude(String value) {
		this.longitude = value;
	}

	/**
	 * Gets the value of the dataStr property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataStr() {
		return dataStr;
	}

	/**
	 * Sets the value of the dataStr property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDataStr(String value) {
		this.dataStr = value;
	}

	/**
	 * Gets the value of the longitudeIdentity property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLongitudeIdentity() {
		return longitudeIdentity;
	}

	/**
	 * Sets the value of the longitudeIdentity property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLongitudeIdentity(String value) {
		this.longitudeIdentity = value;
	}

}
