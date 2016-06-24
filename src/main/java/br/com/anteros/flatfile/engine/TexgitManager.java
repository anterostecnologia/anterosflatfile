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

import java.io.InputStream;

import br.com.anteros.flatfile.language.MetaTexgit;
import br.com.anteros.flatfile.FlatFile;
import br.com.anteros.flatfile.Record;
import br.com.anteros.flatfile.TexgitException;

/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
public class TexgitManager {

	public static FlatFile<br.com.anteros.flatfile.Record> buildFlatFile(InputStream xmlDefStream) {

		FlatFile<Record> iFlatFile = null;

		try {

			MetaTexgit tgMeta = TexgitXmlReader.parse(xmlDefStream);

			iFlatFile = FlatFileBuilder.build(tgMeta.getFlatFile());

		} catch (Exception e) {
			throw new TexgitException(e);
		}

		return iFlatFile;
	}
	
	public static FlatFile<br.com.anteros.flatfile.Record> buildFlatFile(MetaTexgit tgMeta) {

		FlatFile<Record> iFlatFile = null;
		try {
			iFlatFile = FlatFileBuilder.build(tgMeta.getFlatFile());

		} catch (Exception e) {
			throw new TexgitException(e);
		}

		return iFlatFile;
	}
}