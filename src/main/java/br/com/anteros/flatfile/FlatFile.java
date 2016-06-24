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

import java.util.Collection;

/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 * 
 * @param <G>
 */
public interface FlatFile<G extends Record> extends TextListStream {

	// Registros individuais

	public G createRecord(String idType);

	public FlatFile<G> addRecord(G record);

	public G getRecord(String idName);

	public G removeRecord(String idName);

	// Grupos de Registros (Registros que se repetem)

	public FlatFile<G> addRecords(String idName, Collection<G> records);

	public FlatFile<G> setRecords(String idName, Collection<G> records);

	public Collection<G> getRecords(String idName);

	// Todos os Registros

	public FlatFile<G> addAllRecords(Collection<G> records);

	public FlatFile<G> setAllRecords(Collection<G> records);

	public Collection<G> getAllRecords();

}
