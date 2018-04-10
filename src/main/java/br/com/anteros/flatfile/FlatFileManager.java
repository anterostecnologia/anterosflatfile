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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.core.utils.IOUtils;
import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.flatfile.annotation.FlatFile;
import br.com.anteros.flatfile.annotation.Formats;
import br.com.anteros.flatfile.annotation.IdType;
import br.com.anteros.flatfile.annotation.InnerRecord;
import br.com.anteros.flatfile.annotation.Record;
import br.com.anteros.flatfile.annotation.RecordData;
import br.com.anteros.flatfile.language.EnumTypes;
import br.com.anteros.flatfile.language.MetaField;
import br.com.anteros.flatfile.language.MetaFlatFile;
import br.com.anteros.flatfile.language.MetaGroupFields;
import br.com.anteros.flatfile.language.MetaGroupRecords;
import br.com.anteros.flatfile.language.MetaLayout;
import br.com.anteros.flatfile.language.MetaOrderedField;
import br.com.anteros.flatfile.language.MetaRecord;
import br.com.anteros.flatfile.language.MetaTexgit;

public class FlatFileManager {

	private MetaTexgit metadata;

	public FlatFileManager() {

	}

	private byte[] readAnnotations(Object model, String[] groups) throws FlatFileManagerException, JAXBException {

		this.metadata = new MetaTexgit();

		FlatFile flatFileAnnotation = model.getClass().getAnnotation(FlatFile.class);

		Assert.notNull(flatFileAnnotation, "Não foi encontrado anotação @FlatFile no modelo passado como parâmetro.");

		/**
		 * Cria layout
		 */
		MetaLayout metaLayout = new MetaLayout();
		metaLayout.setName(flatFileAnnotation.name());
		metaLayout.setDescription(flatFileAnnotation.description());
		metaLayout.setVersion(flatFileAnnotation.version());

		/**
		 * Cria metadata do arquivo
		 */
		MetaFlatFile metaFlatFile = new MetaFlatFile();
		metaFlatFile.setLayout(metaLayout);

		/**
		 * Cria grupo de registros
		 */
		MetaGroupRecords metaGroupRecords = new MetaGroupRecords();
		metaFlatFile.setGroupOfRecords(metaGroupRecords);

		/**
		 * Busca todos os campos e métodos do modelo
		 */
		Field[] fields = ReflectionUtils.getAllDeclaredFields(model.getClass());

		Method[] methods = ReflectionUtils.getAllDeclaredMethods(model.getClass());
		Set<Field> fieldsSet = new LinkedHashSet<Field>();
		fieldsSet.addAll(Arrays.asList(fields));

		for (Method method : methods) {
			Field fieldByMethodSetter = ReflectionUtils.getFieldByMethodSetter(method);
			if (fieldByMethodSetter != null) {
				fieldsSet.add(fieldByMethodSetter);
			}
		}

		/**
		 * Le os registros
		 */

		List<Record> records = readRecords(fieldsSet);

		for (Record annotation : records) {
			if (isContainsGroups(groups, annotation.groups()) || (groups.length == 0)) {
				Field field = getFieldByRecord(fieldsSet, annotation);

				MetaRecord metaRecord = new MetaRecord();
				metaRecord.setDescription(annotation.description());
				metaRecord.setName(annotation.name());
				metaRecord.setRepeatable(annotation.repeatable());
				metaRecord.setField(field);
				metaRecord.setGroups(annotation.groups());
				if (annotation.repeatable()
						&& !ReflectionUtils.isImplementsInterface(field.getType(), RecordData.class)) {
					throw new FlatFileManagerException(
							"A classe " + field.getType().getName() + " não implementa a interface RecordData.");
				}

				metaGroupRecords.getRecords().add(metaRecord);

				MetaGroupFields metaGroupFields = new MetaGroupFields();
				metaRecord.setGroupOfFields(metaGroupFields);

				readFields(metaGroupFields, field.getType());
				if (metaGroupFields.getFields().isEmpty()) {
					throw new FlatFileManagerException(
							"Não foram encontrados campos para o registro " + annotation.name());
				}
			}
		}

		/**
		 * Le os registros filhos
		 */
		List<InnerRecord> innerRecords = readInnerRecords(fieldsSet);

		for (InnerRecord annotation : innerRecords) {
			if (isContainsGroups(groups, annotation.groups()) || groups.length == 0) {
				Field field = getFieldByInnerRecord(fieldsSet, annotation);
				MetaRecord recordOwner = findRecordOwner(metaFlatFile, annotation.recordOwner());
				if (recordOwner == null) {
					 throw new FlatFileManagerException("Não foi encontrado registro pai " + annotation.recordOwner() + " usado no registro filho " + annotation.name());
				}
				MetaRecord innerRecord = new MetaRecord();
				if (recordOwner.getGroupOfInnerRecords() == null) {
					recordOwner.setGroupOfInnerRecords(new MetaGroupRecords());
				}
				recordOwner.getGroupOfInnerRecords().getRecords().add(innerRecord);
				innerRecord.setName(annotation.name());
				innerRecord.setDescription(annotation.description());
				innerRecord.setRepeatable(annotation.repeatable());
				innerRecord.setField(field);
				innerRecord.setGroups(annotation.groups());

				MetaGroupFields metaGroupFields = new MetaGroupFields();
				innerRecord.setGroupOfFields(metaGroupFields);

				readFields(metaGroupFields, field.getType());
				if (metaGroupFields.getFields().isEmpty()) {
					throw new FlatFileManagerException(
							"Não foram encontrados campos para o registro filho " + annotation.name());
				}
			}
		}

		metadata.setFlatFile(metaFlatFile);

		JAXBContext jaxbContext = JAXBContext.newInstance(MetaTexgit.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(metadata, baos);

		return baos.toByteArray();
	}

	private Field getFieldByInnerRecord(Set<Field> fieldsSet, InnerRecord annotation) {
		for (Field field : fieldsSet) {
			Method setterAccessor = ReflectionUtils.getSetterAccessor(field.getType(), field);
			if (field.isAnnotationPresent(InnerRecord.class)) {
				if (field.getAnnotation(InnerRecord.class).name().equals(annotation.name())) {
					return field;
				}
			}

			if (setterAccessor != null && setterAccessor.isAnnotationPresent(InnerRecord.class)) {
				if (setterAccessor.getAnnotation(InnerRecord.class).name().equals(annotation.name())) {
					return field;
				}
			}
		}
		return null;
	}

	private Field getFieldByRecord(Set<Field> fieldsSet, Record annotation) {
		for (Field field : fieldsSet) {
			Method setterAccessor = ReflectionUtils.getSetterAccessor(field.getType(), field);
			if (field.isAnnotationPresent(Record.class)) {
				if (field.getAnnotation(Record.class).name().equals(annotation.name())) {
					return field;
				}
			}

			if (setterAccessor != null && setterAccessor.isAnnotationPresent(Record.class)) {
				if (setterAccessor.getAnnotation(Record.class).name().equals(annotation.name())) {
					return field;
				}
			}
		}
		return null;
	}

	private List<Record> readRecords(Set<Field> fieldsSet) {
		List<Record> result = new ArrayList<Record>();
		for (Field field : fieldsSet) {
			if (field.isAnnotationPresent(Record.class)) {
				Record annotation = field.getAnnotation(Record.class);
				result.add(annotation);
			}
		}
		result.sort(new Comparator<Record>() {
			public int compare(Record o1, Record o2) {
				return Integer.valueOf(o1.order()).compareTo(Integer.valueOf(o2.order()));
			}
		});
		return result;
	}

	private List<InnerRecord> readInnerRecords(Set<Field> fieldsSet) {
		List<InnerRecord> result = new ArrayList<InnerRecord>();
		for (Field field : fieldsSet) {
			if (field.isAnnotationPresent(InnerRecord.class)) {
				InnerRecord annotation = field.getAnnotation(InnerRecord.class);
				result.add(annotation);
			}
		}
		result.sort(new Comparator<InnerRecord>() {
			public int compare(InnerRecord o1, InnerRecord o2) {
				return Integer.valueOf(o1.order()).compareTo(Integer.valueOf(o2.order()));
			}
		});
		return result;
	}

	private void readFields(MetaGroupFields metaGroupFields, Class<?> type) throws FlatFileManagerException {
		Field[] fieldsRecord = ReflectionUtils.getAllDeclaredFields(type);
		Set<Field> fieldsRecordSet = new LinkedHashSet<Field>();
		fieldsRecordSet.addAll(Arrays.asList(fieldsRecord));

		Method[] methodsRecord = ReflectionUtils.getAllDeclaredMethods(type);
		for (Method methodRecord : methodsRecord) {
			Field fieldByMethodSetter = ReflectionUtils.getFieldByMethodSetter(methodRecord);
			if (fieldByMethodSetter != null) {
				fieldsRecordSet.add(fieldByMethodSetter);
			}
		}

		for (Field fieldRecord : fieldsRecordSet) {
			if (fieldRecord.isAnnotationPresent(IdType.class)) {
				IdType annotationIdType = fieldRecord.getAnnotation(IdType.class);
				MetaOrderedField idType = new MetaOrderedField();
				idType.setBlankAccepted(annotationIdType.blankAccepted());
				idType.setLength(annotationIdType.length());
				idType.setName(annotationIdType.name());
				idType.setPosition(annotationIdType.positionField());
				idType.setType(EnumTypes.STRING);
				idType.setValue(annotationIdType.value());
				metaGroupFields.setIdType(idType);
			}
			if (fieldRecord.isAnnotationPresent(br.com.anteros.flatfile.annotation.Field.class)) {
				br.com.anteros.flatfile.annotation.Field annotationField = fieldRecord
						.getAnnotation(br.com.anteros.flatfile.annotation.Field.class);

				if (annotationField.type().equals(EnumTypes.BIGDECIMAL) && annotationField.format() == Formats.NONE) {
					throw new FlatFileManagerException(
							"Para campos do tipo BIGDECIMAL informe o format para o mesmo. Campo "
									+ annotationField.name() + ".");
				}
				MetaField metaField = new MetaField();
				metaField.setBlankAccepted(annotationField.blankAccepted());
				metaField.setFormat(annotationField.format().convertToEnumFormats());
				metaField.setLength(annotationField.length());
				metaField.setPadding(annotationField.padding().convertToEnumPaddings());
				metaField.setTruncate(annotationField.truncate());
				metaField.setType(annotationField.type());
				metaField.setValue(annotationField.value());
				metaField.setName(annotationField.name());
				metaField.setField(fieldRecord);
				metaGroupFields.getFields().add(metaField);
			}
		}
	}

	private MetaRecord findRecordOwner(MetaFlatFile metaFlatFile, String recordOwner) {
		for (MetaRecord record : metaFlatFile.getGroupOfRecords().getRecords()) {
			if (record.getName().equals(recordOwner)) {
				return record;
			}
		}
		return null;
	}

	public byte[] getXMLSchema(Object model) throws FlatFileManagerException, JAXBException {
		return readAnnotations(model, new String[] {});
	}

	public br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> read(Object model,
			InputStream dataInputStream) throws FlatFileManagerException, JAXBException, IllegalArgumentException,
			IllegalAccessException, IOException {
		Assert.notNull(model, "Informe um objeto que contenha o modelo de dados.");

		if (!model.getClass().isAnnotationPresent(FlatFile.class)) {
			throw new FlatFileManagerException("O objeto passado como modelo não é um válido.");
		}
		readAnnotations(model, new String[] { "GLOBAL" });

		br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> flatFile = Texgit.createFlatFile(metadata);

		List<String> lines = IOUtils.readLines(dataInputStream);
		List<String> sanitizedLines = new ArrayList<String>();
		for (String line : lines) {
			if (StringUtils.isNotBlank(line))
				sanitizedLines.add(line);
		}
		flatFile.read(sanitizedLines);
		return flatFile;
	}

	public br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> read(Object model, String[] groups,
			InputStream dataInputStream) throws FlatFileManagerException, JAXBException, IllegalArgumentException,
			IllegalAccessException, IOException {
		Assert.notNull(model, "Informe um objeto que contenha o modelo de dados.");

		if (!model.getClass().isAnnotationPresent(FlatFile.class)) {
			throw new FlatFileManagerException("O objeto passado como modelo não é um válido.");
		}
		readAnnotations(model, groups);

		br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> flatFile = Texgit.createFlatFile(metadata);

		List<String> lines = IOUtils.readLines(dataInputStream);
		List<String> sanitizedLines = new ArrayList<String>();
		for (String line : lines) {
			if (StringUtils.isNotBlank(line))
				sanitizedLines.add(line);
		}
		flatFile.read(sanitizedLines);
		return flatFile;
	}

	public byte[] generate(Object model) throws FlatFileManagerException, JAXBException, IllegalArgumentException,
			IllegalAccessException, IOException {
		return generate(model, new String[] { "GLOBAL" });
	}

	public byte[] generate(Object model, String[] groups) throws FlatFileManagerException, JAXBException,
			IllegalArgumentException, IllegalAccessException, IOException {

		Assert.notNull(model, "Informe um objeto que contenha o modelo de dados.");

		if (!model.getClass().isAnnotationPresent(FlatFile.class)) {
			throw new FlatFileManagerException("O objeto passado como modelo não é um válido.");
		}
		readAnnotations(model, groups);

		br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> flatFile = Texgit.createFlatFile(metadata);

		int countRows = 0;
		for (MetaRecord metaRecord : metadata.getFlatFile().getGroupOfRecords().getRecords()) {
			if (isContainsGroups(groups, metaRecord.getGroups())) {
				if (metaRecord.isRepeatable()) {
					int countRepeatable = 0;
					RecordData recordData = (RecordData) metaRecord.getField().get(model);
					for (int i = 0; i < recordData.getNumberOfRecords(); i++) {
						countRows++;
						countRepeatable++;
						recordData.readRowData(i, countRepeatable);
						br.com.anteros.flatfile.Record record = getRecordByModel(flatFile, metaRecord, recordData);
						flatFile.addRecord(record);
						if (metaRecord.getGroupOfInnerRecords() != null && metaRecord.getGroupOfInnerRecords().getRecords().size() > 0) {
							for (MetaRecord innerRecord : metaRecord.getGroupOfInnerRecords().getRecords()) {
								RecordData innerRecordData = (RecordData) innerRecord.getField().get(model);
								countRows++;
								countRepeatable++;
								innerRecordData.readRowData(i, countRepeatable);
								record.addInnerRecord(getRecordByModel(flatFile, innerRecord, innerRecordData));
							}
						}
					}
				} else {
					countRows++;
					RecordData rd = (RecordData) metaRecord.getField().get(model);
					rd.readRowData(0, countRows);
					flatFile.addRecord(getRecordByModel(flatFile, metaRecord, rd));
				}
			}
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.writeLines(flatFile.write(), IOUtils.LINE_SEPARATOR, baos);

		return baos.toByteArray();
	}

	protected boolean isContainsGroups(String[] filter, String[] groups) {
		List<String> groupList = Arrays.asList(groups);
		for (String group : filter) {
			if (groupList.contains(group)) {
				return true;
			}
		}
		return false;
	}

	private br.com.anteros.flatfile.Record getRecordByModel(
			br.com.anteros.flatfile.FlatFile<br.com.anteros.flatfile.Record> flatFile, MetaRecord metaRecord,
			Object objectRecord) throws IllegalArgumentException, IllegalAccessException {
		br.com.anteros.flatfile.Record result = flatFile.createRecord(metaRecord.getName());
		for (MetaField field : metaRecord.getGroupOfFields().getFields()) {
			Object value = field.getField().get(objectRecord);
			if (value != null)
				result.setValue(field.getName(), value);
		}

		return result;

	}

}
