/*******************************************************************************
 * Copyright 2016 Anteros Tecnologia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.08.03 at 12:27:56 AM GMT-03:00 
//

package br.com.anteros.flatfile.language;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 
 * <p>
 * Flat File � um tipo de arquivo texto com informa��es estruturadas com base em
 * formata��es e estruturas pr� definidas. veja mais em (link).
 * </p>
 * 
 * 
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetaFlatFile", propOrder = { "layout", "groupOfRecords" })
public class MetaFlatFile {

	protected MetaLayout layout;
	@XmlElement(name = "GroupOfRecords", required = true)
	protected MetaGroupRecords groupOfRecords;

	/**
	 * Gets the value of the layout property.
	 * 
	 * @return possible object is {@link MetaLayout }
	 * 
	 */
	public MetaLayout getLayout() {
		return layout;
	}

	/**
	 * Sets the value of the layout property.
	 * 
	 * @param value
	 *            allowed object is {@link MetaLayout }
	 * 
	 */
	public void setLayout(MetaLayout value) {
		this.layout = value;
	}

	/**
	 * Gets the value of the groupOfRecords property.
	 * 
	 * @return possible object is {@link MetaGroupRecords }
	 * 
	 */
	public MetaGroupRecords getGroupOfRecords() {
		return groupOfRecords;
	}

	/**
	 * Sets the value of the groupOfRecords property.
	 * 
	 * @param value
	 *            allowed object is {@link MetaGroupRecords }
	 * 
	 */
	public void setGroupOfRecords(MetaGroupRecords value) {
		this.groupOfRecords = value;
	}

}
