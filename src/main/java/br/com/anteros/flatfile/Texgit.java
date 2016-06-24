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
package br.com.anteros.flatfile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.flatfile.engine.TexgitManager;
import br.com.anteros.flatfile.language.MetaTexgit;

/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 * 
 */
public final class Texgit {
	
	public static final FlatFile<Record> createFlatFile(String xmlDefFilePath)
			throws TexgitException {

		try {

			if (StringUtils.isNotBlank(xmlDefFilePath))
				return createFlatFile(new File(xmlDefFilePath));

		} catch (Exception e) {
			throw new TexgitException(e);
		}

		return null;
	}

	public static final FlatFile<Record> createFlatFile(File xmlDefFile)
			throws TexgitException {

		try {

			if (ObjectUtils.isNotNull(xmlDefFile)) {
				
				return createFlatFile(new FileInputStream(xmlDefFile));
			}

		} catch (Exception e) {
			throw new TexgitException(e);
		}

		return null;
	}

	public static final FlatFile<Record> createFlatFile(URL xmlDefUrl)
			throws TexgitException {
		
		try {

			if (ObjectUtils.isNotNull(xmlDefUrl)) {
				
				return TexgitManager.buildFlatFile(xmlDefUrl.openStream());
			}

		} catch (Exception e) {
			throw new TexgitException(e);
		}
		
		
		return null;
	}

	public static final FlatFile<Record> createFlatFile(byte[] xmlDefBytes)
			throws TexgitException {
		
		try {
			
			if (ObjectUtils.isNotNull(xmlDefBytes)) {
				
				return TexgitManager.buildFlatFile(new ByteArrayInputStream(xmlDefBytes));
			}
			
		} catch (Exception e) {
			throw new TexgitException(e);
		}
		
		return null;
	}
	
	public static final FlatFile<Record> createFlatFile(MetaTexgit tgMeta)
			throws TexgitException {
		
		try {
			
			if (ObjectUtils.isNotNull(tgMeta)) {
				
				return TexgitManager.buildFlatFile(tgMeta);
			}
			
		} catch (Exception e) {
			throw new TexgitException(e);
		}
		
		return null;
	}
	
	public static final FlatFile<Record> createFlatFile(InputStream xmlDefStream)
			throws TexgitException {

		if (ObjectUtils.isNotNull(xmlDefStream)) {

			return TexgitManager.buildFlatFile(xmlDefStream);
		}

		return null;
	}

}
