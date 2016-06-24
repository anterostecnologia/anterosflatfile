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



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.anteros.flatfile.language.MetaFlatFile;
import br.com.anteros.flatfile.language.MetaRecord;
import br.com.anteros.flatfile.type.component.FlatFile;
import br.com.anteros.core.utils.ObjectUtils;



/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
public class FlatFileBuilder {

	static FlatFile build(MetaFlatFile mFlatFile) {
		
		FlatFile ff = null;
		
		List<MetaRecord> metaRecords = mFlatFile.getGroupOfRecords().getRecords();

		ff = new FlatFile(new RecordFactory(metaRecords));
		
		Set<String> repitables = new HashSet<String>();
		
		List<String> recordsOrder = new ArrayList<String>();
		
		if (ObjectUtils.isNotNull(metaRecords)) {
			
			if (!metaRecords.isEmpty()) {
				
				for (MetaRecord mRec : metaRecords) {
					
					recordsOrder.add(mRec.getName());
					
					if (mRec.isRepeatable())
						repitables.add(mRec.getName());
				}
			}
		}
			
		ff.setRecordsOrder(recordsOrder);
		ff.setRepitablesRecords(repitables);
		
		return ff;
	}
	
}
