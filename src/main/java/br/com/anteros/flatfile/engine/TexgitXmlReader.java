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
package br.com.anteros.flatfile.engine;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import br.com.anteros.flatfile.language.MetaTexgit;
import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.core.utils.ResourceUtils;
import br.com.anteros.flatfile.Texgit;
import br.com.anteros.flatfile.TexgitException;



/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
class TexgitXmlReader {

	public static MetaTexgit parse(InputStream xmlDefStream) throws TexgitException {

		MetaTexgit txg = null;

		if (ObjectUtils.isNotNull(xmlDefStream)) {

			try {

				JAXBContext aJaxbContext = JAXBContext
						.newInstance(MetaTexgit.class);

				Unmarshaller aUnmarshaller = aJaxbContext.createUnmarshaller();

				SchemaFactory aSchemaFactory = SchemaFactory
						.newInstance(W3C_XML_SCHEMA_NS_URI);
				
				Schema schema = aSchemaFactory.newSchema(ResourceUtils.getResource("TexgitSchema.xsd",Texgit.class));

				aUnmarshaller.setSchema(schema);

				aUnmarshaller.setEventHandler(new TexgitSchemaValidator());

				txg = (MetaTexgit) aUnmarshaller.unmarshal(xmlDefStream);

			} catch (SAXException e) {

				throw new TexgitLanguageException(e);

			} catch (JAXBException e) {

				throw new TexgitLanguageException(e);

			}
		}

		return txg;
	}

}
