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
package br.com.anteros.flatfile.type.component;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.core.utils.StringUtils;


/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
@SuppressWarnings("serial")
public class FlatFile implements br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record>{

	private List<Record> records;
	
	private Set<String> repitablesRecords;
	
	private List<String> recordsOrder;
	
	private RecordFactory<Record> recordFactory;

	public FlatFile(RecordFactory<Record> iFac4Rec) {
		
		this.recordFactory = iFac4Rec;
		this.records = new ArrayList<Record>();
	}

	public Record getRecord(String idName){
		
		Record record = null;
		
		if (StringUtils.isNotBlank(idName)) {
			if (!isRepitable(idName)){	
				if (!records.isEmpty()) {
					for (Record rec : records) {
						if (idName.equals(rec.getName()))
							record = rec;
					}
				}
			}
		}

		return record;
	}
	
	public boolean isRepitable(String idName){
		
		return (ObjectUtils.isNotNull(repitablesRecords) && !repitablesRecords.isEmpty() && repitablesRecords.contains(idName));
	}
	
	public br.com.anteros.flatfile.Record createRecord(String idName){
		
		return recordFactory.create(idName);
	}
	
	public void addRecord(Record record){
		
		if(ObjectUtils.isNotNull(record))
			if(isMyRecord(record.getName()))
				records.add(record);
			else
				throw new IllegalArgumentException("Record fora de scopo!");
	}
	
	public boolean isMyRecord(String idName){
		boolean is = false;
		
		if (StringUtils.isNotBlank(idName)) {
			if(!recordsOrder.isEmpty())
				if(recordsOrder.contains(idName))
					is = true;
		}
		return is;
	}

	public void read(List<String> str) {
		
		if(str !=null){
			if(!str.isEmpty()){
		
				String line = null;
				int lineIndex = 0;
				
				FixedField<String> typeRecord = null;
				Record record = null;
				
				for(String id : recordsOrder){
					
					record = recordFactory.create(id);
					
					try{
						
						if(isRepitable(id)){
							
							boolean read = true;
							
							while(read){
								
								if(ObjectUtils.isNull(record))
									record = recordFactory.create(id);
								
								if(lineIndex < str.size())
									line = str.get(lineIndex);
								
								typeRecord = record.readID(line);
								
								read = record.getIdType().getValue().equals(typeRecord.getValue()) && (lineIndex < str.size()); 

								if(read){
									
									record.read(line);
									lineIndex++;
									addRecord(record);
									
									if(record.isHeadOfGroup()){
										lineIndex = record.readInnerRecords(str,lineIndex,recordFactory);
									}
									
									record = null;
								}
							}
							
						}else{
							if((lineIndex < str.size())){
								
								line = str.get(lineIndex);
								typeRecord = record.readID(line);
								
								if(record.getIdType().getValue().equals(typeRecord.getValue())){
									
									record.read(line);
									lineIndex++;
									addRecord(record);
									
									if(record.isHeadOfGroup()){
										lineIndex = record.readInnerRecords(str,lineIndex,recordFactory);
									}
									
									record = null;
								}
							}
						}
						
					} catch (Exception e) {
	
						throw new IllegalStateException(format(
								"Erro ao tentar ler o registro \"%s\".", record.getName()), e);
					}
				}
			}
		}
	}

	public List<String> write() {
	
		return write(StringUtils.EMPTY);
	}
	
	public List<String> write(String lineEnding) {
		
		ArrayList<String> out = new ArrayList<String>(records.size());
		
		for(String id : recordsOrder){
			
			if(isRepitable(id)){
				
				Record rec = null;
				
				for(br.com.anteros.flatfile.Record record : getRecords(id)){
					
					rec = Record.class.cast(record);
					
					try{
						
						out.add(rec.write()+lineEnding);
						
					} catch (Exception e) {
						
						throw new IllegalStateException(format(
								"Erro ao tentar escrever o registro \"%s\".", rec.getName()), e);
					}
					
					if(rec.isHeadOfGroup() && rec.hasInnerRecords()){
						out.addAll(rec.writeInnerRecords(lineEnding));
					}
				}
				
			}else{
				
				Record rec = getRecord(id);

				try{
					
					out.add(rec.write()+lineEnding);
					
				} catch (Exception e) {
					
					throw new IllegalStateException(format(
							"Erro ao tentar escrever o registro \"%s\".", rec.getName()), e);
				}
				
				if(rec.isHeadOfGroup() && rec.hasInnerRecords()){
					out.addAll(rec.writeInnerRecords(lineEnding));
				}
			}
		}
		
		return out;
	}

	public br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> addRecord(br.com.anteros.flatfile.Record record) {
		
		if(ObjectUtils.isNotNull(record)){
			Record rec = Record.class.cast(record);
			addRecord(rec);
		}
		
		return this;
	}

	public Collection<br.com.anteros.flatfile.Record> getRecords(String idName) {

		List<br.com.anteros.flatfile.Record> secRecords = new ArrayList<br.com.anteros.flatfile.Record>();

		if (StringUtils.isNotBlank(idName)) {
			if (isRepitable(idName)) {
				if (!records.isEmpty()) {
					for (Record rec : records) {
						if (idName.equals(rec.getName()))
							secRecords.add(rec);
					}
				}
			}
		}

		return secRecords;
	}
	
	public br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> addAllRecords(Collection<br.com.anteros.flatfile.Record> records) {
		// TODO IMPLEMENTAR
		throw new UnsupportedOperationException("AINDA NÃO IMPLEMENTADO!");
	}

	public br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> addRecords(String idName, Collection<br.com.anteros.flatfile.Record> records) {
		// TODO IMPLEMENTAR
		throw new UnsupportedOperationException("AINDA NÃO IMPLEMENTADO!");
	}

	public Collection<br.com.anteros.flatfile.Record> getAllRecords() {
		// TODO IMPLEMENTAR
		throw new UnsupportedOperationException("AINDA NÃO IMPLEMENTADO!");
	}

	public br.com.anteros.flatfile.type.component.Record removeRecord(String idName) {
		// TODO IMPLEMENTAR
		throw new UnsupportedOperationException("AINDA NÃO IMPLEMENTADO!");
	}

	public br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> setAllRecords(Collection<br.com.anteros.flatfile.Record> records) {
		// TODO IMPLEMENTAR
		throw new UnsupportedOperationException("AINDA NÃO IMPLEMENTADO!");
	}

	public br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> setRecords(String idName, Collection<br.com.anteros.flatfile.Record> records) {
		// TODO IMPLEMENTAR
		throw new UnsupportedOperationException("AINDA NÃO IMPLEMENTADO!");
	}
	
	public Set<String> getRepitablesRecords() {
		return repitablesRecords;
	}

	public void setRepitablesRecords(Set<String> repitablesRecords) {
		this.repitablesRecords = repitablesRecords;
	}

	public List<String> getRecordsOrder() {
		return recordsOrder;
	}

	public void setRecordsOrder(List<String> recordsOrder) {
		this.recordsOrder = recordsOrder;
	}
	
}
