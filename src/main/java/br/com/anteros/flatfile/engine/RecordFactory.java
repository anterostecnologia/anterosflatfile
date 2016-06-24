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
