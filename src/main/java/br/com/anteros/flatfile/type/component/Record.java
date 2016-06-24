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
import java.util.List;
import java.util.Set;

import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.flatfile.type.Field;



/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
@SuppressWarnings("serial")
public class Record extends BlockOfFields implements br.com.anteros.flatfile.Record{

	private String name;
	
	private String description;
	
	private FixedField<String> idType;
	
	private FixedField<Long> sequencialNumber;
	
	private boolean headOfGroup;
	
	private List<br.com.anteros.flatfile.Record> innerRecords;
	
	private Set<String> repitablesRecords;
	
	private List<String> declaredInnerRecords;
	
	public Record() {
		super();
	}
	
	/**
	 * @param length
	 * @param size
	 */
	public Record(Integer length, Integer size) {
		super(length, size);
	}

	@Override
	public Record clone() throws CloneNotSupportedException {
		//TODO Outros atributos
		return (Record) super.clone();
	}
	
	@SuppressWarnings("null")
	public FixedField<String> readID(String lineRecord) {

		FixedField<String> ffID = null;
		
		try {
			
			ffID = getIdType().clone();
			ffID.setName("");
			
		} catch (CloneNotSupportedException e) {
			
			throw new UnsupportedOperationException(format("Quebra de contrato [%s] não suporta clonagem!",ObjectUtils.whenNull(ffID, "FixedField", ffID.getClass())), e);
		}
		
		getIdType().read(lineRecord.substring(getIdPosition(), getIdPosition() + getIdType().getFixedLength())); 

		return ffID;
	}
	
	public br.com.anteros.flatfile.type.FixedField<?> getField(String fieldName) {

		br.com.anteros.flatfile.type.FixedField<?> field = null;

		if (StringUtils.isNotBlank(fieldName))
			if (!getFields().isEmpty())
				for (FixedField<?> ff : this.getFields())
					if (ff.getName().equals(fieldName)) {
						field = ff;
						break;
					}

		return field;
	}

	public boolean isMyField(String idName){
		boolean is = false;
		
		if (StringUtils.isNotBlank(idName)) {
			if(!getFields().isEmpty())
				for(br.com.anteros.flatfile.type.Field<?> f : getFields())
					if(idName.equals(f.getName())){
						is = true;
						break;
					}
		}
		return is;
	}
	
	private int getIdPosition(){
		int pos = 0;
		
		for(FixedField<?> ff : this.getFields())
			if(!ff.getName().equals(idType.getName()))
				pos += ff.getFixedLength();
			else
				break;
		
		return pos;
	}
	
	public int readInnerRecords(List<String> lines, int lineIndex, RecordFactory<Record> iFactory) {
		
		return readInnerRecords(this,lines,lineIndex,iFactory);
	}
	
	private int readInnerRecords(Record record, List<String> lines, int lineIndex, RecordFactory<Record> iFactory) {
		
		if(ObjectUtils.isNotNull(record)){
			
			if(ObjectUtils.isNotNull(record.getDeclaredInnerRecords()) && !record.getDeclaredInnerRecords().isEmpty()){
				
				boolean read = true;
				String line = null;
				
				FixedField<String> typeRecord = null;
				Record innerRec = null;
				
				for(String id : record.getDeclaredInnerRecords()){
					
					innerRec = iFactory.create(id);
					
					try{
						
						if(isRepitable(id)){
							
							while(read){
								
								if(ObjectUtils.isNull(innerRec))
									innerRec = iFactory.create(id);
								
								if(lineIndex < lines.size())
									line = lines.get(lineIndex);
								
								typeRecord = innerRec.readID(line);
								
								read = innerRec.getIdType().getValue().equals(typeRecord.getValue()) && (lineIndex < lines.size()); 

								if(read){
									
									innerRec.read(line);
									lineIndex++;
									record.addInnerRecord(innerRec);
									
									if(innerRec.isHeadOfGroup())
										innerRec.readInnerRecords(lines,lineIndex,iFactory);
									
									innerRec = null;
								}
							}
							
						}else{
							if((lineIndex < lines.size())){
								
								line = lines.get(lineIndex);
								typeRecord = innerRec.readID(line);
								
								if(innerRec.getIdType().getValue().equals(typeRecord.getValue())){
									
									innerRec.read(line);
									lineIndex++;
									record.addInnerRecord(innerRec);
									
									if(innerRec.isHeadOfGroup())
										innerRec.readInnerRecords(lines,lineIndex,iFactory);
									
									innerRec = null;
								}
							}
						}
						
					} catch (Exception e) {

						throw new IllegalStateException(format(
								"Erro ao tentar ler o registro \"%s\".",
								innerRec.getName()), e);
					}
				}
			}		
		}
		
		return lineIndex;
	}

	public List<String> writeInnerRecords(){
		
		return writeInnerRecords(this,StringUtils.EMPTY);
	}
	
	public List<String> writeInnerRecords(String lineEnding){
		
		return writeInnerRecords(this,lineEnding);
	}
	
	private List<String> writeInnerRecords(Record record, String lineEnding){

		ArrayList<String> out = new ArrayList<String>(record.getInnerRecords().size());
		
		for(String id : getDeclaredInnerRecords()){//ordem
			
			if(isRepitable(id)){
					
				for(Record rec : getRecords(id)){
					
					try{

						out.add(rec.write()+lineEnding);
						
					} catch (Exception e) {

						throw new IllegalStateException(format(
								"Erro ao tentar escrever o registro \"%s\".", rec.getName()), e);
					}
					
					if(rec.isHeadOfGroup())
						out.addAll(rec.writeInnerRecords());
				}
				
			}else{
				
				Record rec = getRecord(id);

				try{
					
					out.add(rec.write()+lineEnding);
					
				} catch (Exception e) {

					throw new IllegalStateException(format(
							"Erro ao tentar escrever o registro \"%s\".", rec.getName()), e);
				}
				
				if(rec.isHeadOfGroup())
					out.addAll(rec.writeInnerRecords());
			}
		}
		
		return out;
	}
	
	public Record getRecord(String idName){
		
		Record record = null;
		
		if (StringUtils.isNotBlank(idName)) {
			if (!isRepitable(idName)){	
				if (!getInnerRecords().isEmpty()) {
					for (br.com.anteros.flatfile.Record iRec : getInnerRecords()) {
						Record rec = (Record) iRec;
						if (idName.equals(rec.getName()))
							record = rec;
					}
				}
			}
		}

		return record;
	}

	public List<Record> getRecords(String idName) {

		List<Record> secRecords = new ArrayList<Record>();

		if (StringUtils.isNotBlank(idName)) {
			if (isRepitable(idName)) {
				if (!getInnerRecords().isEmpty()) {
					for (br.com.anteros.flatfile.Record iRec : getInnerRecords()) {
						Record rec = (Record) iRec;
						if (idName.equals(rec.getName()))
							secRecords.add(rec);
					}
				}
			}
		}

		return secRecords;
	}
	
	public boolean isRepitable(String idName){
		
		return (ObjectUtils.isNotNull(repitablesRecords) && !repitablesRecords.isEmpty() && repitablesRecords.contains(idName));
	}
	
	public boolean isMyRecord(String idName){
		boolean is = false;
		
		if (StringUtils.isNotBlank(idName)) {
			if(!getDeclaredInnerRecords().isEmpty())
				if(getDeclaredInnerRecords().contains(idName))
					is = true;
		}
		return is;
	}
	
	public br.com.anteros.flatfile.Record addInnerRecord(br.com.anteros.flatfile.Record record) {
		
		if(ObjectUtils.isNotNull(record)){
			if(ObjectUtils.isNull(this.innerRecords))
				this.innerRecords = new ArrayList<br.com.anteros.flatfile.Record>();
		
		if(isMyRecord(Record.class.cast(record).getName()))
			this.innerRecords.add(record);
		else
			throw new IllegalArgumentException("Record fora de scopo!");
		
		}
		
		return this;
	}

	public List<br.com.anteros.flatfile.Record> getInnerRecords() {
		
		return this.innerRecords;
	}

	@SuppressWarnings("unchecked")
	public <G> G getValue(String fieldName) {
		
		G value = null;
		
		br.com.anteros.flatfile.type.Field<?> f = getField(fieldName);
		
		if(ObjectUtils.isNotNull(f))
			value = (G) f.getValue();
		
		return value;
	}

	@SuppressWarnings("unchecked")
	public <G> br.com.anteros.flatfile.Record setValue(String fieldName, G value) {
		
		br.com.anteros.flatfile.type.Field<G> f = (Field<G>) getField(fieldName);
		
		if(ObjectUtils.isNotNull(f))
			f.setValue(value);
		
		return this;
	}
	
	public boolean hasInnerRecords(){
		return getInnerRecords() != null && !getInnerRecords().isEmpty();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FixedField<String> getIdType() {
		return idType;
	}

	public void setIdType(FixedField<String> idType) {
		this.idType = idType;
	}

	public FixedField<Long> getSequencialNumber() {
		return sequencialNumber;
	}

	public void setSequencialNumber(FixedField<Long> sequencialNumber) {
		this.sequencialNumber = sequencialNumber;
	}

	public boolean isHeadOfGroup() {
		return headOfGroup;
	}

	public void setHeadOfGroup(boolean headOfGroup) {
		this.headOfGroup = headOfGroup;
	}

	public List<String> getDeclaredInnerRecords() {
		return declaredInnerRecords;
	}

	public void setDeclaredInnerRecords(List<String> declaredInnerRecords) {
		this.declaredInnerRecords = declaredInnerRecords;
	}
	
	public Set<String> getRepitablesRecords() {
		return repitablesRecords;
	}

	public void setRepitablesRecords(Set<String> repitablesRecords) {
		this.repitablesRecords = repitablesRecords;
	}

}