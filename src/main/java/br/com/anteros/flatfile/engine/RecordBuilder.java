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

import br.com.anteros.flatfile.language.MetaField;
import br.com.anteros.flatfile.language.MetaOrderedField;
import br.com.anteros.flatfile.language.MetaRecord;
import br.com.anteros.flatfile.type.component.FixedField;
import br.com.anteros.flatfile.type.component.Record;
import br.com.anteros.core.utils.ObjectUtils;



/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
class RecordBuilder {

	@SuppressWarnings("unchecked")
	static Record build(MetaRecord metaRecord) {

		int strLength = getStringLength(metaRecord.getGroupOfFields().getFields());
		int fldSize = metaRecord.getGroupOfFields().getFields().size();
		
		MetaOrderedField id = metaRecord.getGroupOfFields().getIdType();
		MetaOrderedField sequencialNumber = metaRecord.getGroupOfFields().getSequencialNumber(); 
		
		if(ObjectUtils.isNotNull(id)){
			fldSize += 1;
			strLength += id.getLength();
		}
		
		if(ObjectUtils.isNotNull(sequencialNumber)){
			fldSize += 1;
			strLength += sequencialNumber.getLength();
		}
				
		Record record = new Record(strLength,fldSize);
		
		record.setName(metaRecord.getName());
		record.setDescription(metaRecord.getDescription());
		
		/*
		 * getPossition eh de 1 a X
		 * e nao de 0 a X.
		 */ 
		if(ObjectUtils.isNotNull(id)){
			record.setIdType((FixedField<String>) FixedFieldBuilder.build(id));
			record.set(id.getPosition()-1, record.getIdType());
		}
		
		if(ObjectUtils.isNotNull(sequencialNumber)){
			record.setSequencialNumber((FixedField<Long>) FixedFieldBuilder.build(sequencialNumber));
			record.set(sequencialNumber.getPosition()-1,record.getSequencialNumber());
		}
		
		List<MetaField> fields = metaRecord.getGroupOfFields().getFields();
		
		/*
		 * As somas sao para caso id ou sequencia jah
		 * estejam na devida posicao.
		 */ 
		for(MetaField mField : fields){
			
			if(ObjectUtils.isNull(record.get(fields.indexOf(mField))))
				record.set(fields.indexOf(mField), FixedFieldBuilder.build(mField));
			else
				if(ObjectUtils.isNull(record.get(fields.indexOf(mField)+1)))
					record.set(fields.indexOf(mField) + 1, FixedFieldBuilder.build(mField));
				else
					record.set(fields.indexOf(mField) + 2, FixedFieldBuilder.build(mField));
		}

		// innerRecords
		if (ObjectUtils.isNotNull(metaRecord.getGroupOfInnerRecords())){
			
			record.setHeadOfGroup(true);
			record.setDeclaredInnerRecords(new ArrayList<String>(metaRecord.getGroupOfInnerRecords().getRecords().size()));
			record.setRepitablesRecords(new HashSet<String>());
			
			List<MetaRecord> metaInnerRecords = metaRecord.getGroupOfInnerRecords().getRecords();
			
			for(MetaRecord mRecord : metaInnerRecords){
				record.getDeclaredInnerRecords().add(mRecord.getName());
				if(mRecord.isRepeatable())
					record.getRepitablesRecords().add(mRecord.getName());
			}
			
		}else
			record.setHeadOfGroup(false);
		
		return record;
	}
	
	private static int getStringLength(List<MetaField> fields){
		int length = 0;
		
		for(MetaField mField : fields)
			length += mField.getLength();
		
		return length;
	}

}
