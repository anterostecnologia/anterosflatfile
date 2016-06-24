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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.flatfile.language.MetaRecord;
import br.com.anteros.flatfile.type.component.Record;



/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
public class RecordFactory implements br.com.anteros.flatfile.type.component.RecordFactory<Record> {

	private Map<String, MetaRecord> name_record;

	RecordFactory(List<MetaRecord> metaRecords) {

		if (ObjectUtils.isNotNull(metaRecords)) {
			if (!metaRecords.isEmpty()) {

				name_record = new HashMap<String, MetaRecord>(metaRecords
						.size());

				for (MetaRecord mRecord : metaRecords) {

					name_record.put(mRecord.getName(), mRecord);

					if (ObjectUtils.isNotNull(mRecord.getGroupOfInnerRecords()))
						loadInnerRecords(name_record, mRecord
								.getGroupOfInnerRecords().getRecords());
				}
			}
		}
	}

	private void loadInnerRecords(Map<String, MetaRecord> name_record,
			List<MetaRecord> innerMetaRecords) {

		if (ObjectUtils.isNotNull(innerMetaRecords)) {
			if (!innerMetaRecords.isEmpty()) {

				for (MetaRecord iMetaRecord : innerMetaRecords) {

					name_record.put(iMetaRecord.getName(), iMetaRecord);

					if (ObjectUtils.isNotNull(iMetaRecord.getGroupOfInnerRecords()))
						loadInnerRecords(name_record, iMetaRecord
								.getGroupOfInnerRecords().getRecords());
				}
			}
		}

	}

	public Record create(String name) {

		Record record = null;

		if (StringUtils.isNotBlank(name))
			if (name_record.containsKey(name))
				record = RecordBuilder.build(name_record.get(name));

		return record;
	}
}
