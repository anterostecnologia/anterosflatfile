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

import br.com.anteros.core.utils.Assert;
import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.flatfile.type.AbstractStringOfFields;
import br.com.anteros.flatfile.type.FixedLength;
import br.com.anteros.flatfile.type.FixedSize;

/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
@SuppressWarnings("serial")
public class BlockOfFields extends AbstractStringOfFields<FixedField<?>> implements FixedSize, FixedLength{

	/**
	 * Definição
	 */
	private Integer length;
	
	/**
	 * Definição
	 */
	private Integer size;
	
	/**
	 * <p>
	 * Tamanho da string de escrita do bloco.
	 * </p>
	 */
	private Integer instantLength; 
	
	/**
	 * <p>
	 * Ao ultrapassar o tamanho, define se pode truncar ou se dispara uma exceção.
	 * </p>
	 */
	private boolean truncate;
	
	/**
	 * 
	 */
	public BlockOfFields() {
		super();
	}

	/**
	 * @param length
	 * @param size
	 */
	public BlockOfFields(Integer length, Integer size) {

		super(size);

		Assert.notNull(length, "length");

		if (length > 0) {

			setLength(length);
			setSize(size);

		} else
			throw new IllegalArgumentException(format("O comprimento do bloco [%s] deve ser um número natural > 0!", length));
	}
	
	@Override
	public BlockOfFields clone() throws CloneNotSupportedException {
		
		return(BlockOfFields) super.clone();
	}

	@Override
	public void read(String lineOfFields) {

		Assert.notNull(lineOfFields, "String de leitura nula!");

		Assert.notNull(getFields(), "Fields == null");
		Assert.notEmpty(getFields(), "Coleção de fields vazia!");

		if (isSizeAsDefined() && isLengthWithDefined(lineOfFields.length())) {

			StringBuilder builder = new StringBuilder(lineOfFields);

			for (FixedField<?> field : getFields()) {

				try {

					field.read(builder.substring(0, field.getFixedLength()));
					builder.delete(0, field.getFixedLength());

				} catch (Exception e) {

					throw new IllegalStateException(
							format(
									"Erro ao tentar ler o campo \"%s\" na posição [%s] no layout do registro.",
									field.getName(), getFields().indexOf(field)+1),e);
				}
			}

			builder = null;
		}
	}
	
	@Override
	public String write() {

		Assert.notNull(getFields(), "Fields == null");
		Assert.notEmpty(getFields(), "Coleção de fields vazia!");

		String str = null;

		isSizeAsDefined();

		str = super.write();

		instantLength = str.length();
		
		if (isTruncate() && instantLength > getFixedLength()) {
			str = str.substring(0, getFixedLength());
			instantLength = getFixedLength();
		}

		isFixedAsDefined();

		return str;
	}
	
	public boolean isFixedAsDefined() throws IllegalStateException {
		
		return (isSizeAsDefined() && isLengthWithDefined());
	}
	
	private boolean isLengthWithDefined(){
		
		return isLengthWithDefined(instantLength);
	}
	
	private boolean isLengthWithDefined(int length){
		
		if(length == getFixedLength())
				return true;
		else
			throw new IllegalStateException(format("O comprimento da string [%s] é incompatível com o definido [%s] no layout do registro!",length,getFixedLength()));
	}
	
	private boolean isSizeAsDefined(){
		
		if(size() == getFixedSize())
				return true;
		else
			throw new IllegalStateException(format("O número de fields [%s] é incompatível com o definido [%s]!", size(), getFixedSize()));
	}

	/**
	 * @return the length
	 */
	public Integer getFixedLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	protected void setLength(Integer length) {
		
		if (ObjectUtils.isNotNull(length))
			this.length = length;
		else
			throw new IllegalArgumentException(format("Comprimento inválido [%s]!", length));
	}

	/**
	 * @return the size
	 */
	public Integer getFixedSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	protected void setSize(Integer size) {
		
		if (ObjectUtils.isNotNull(size))
			this.size = size;
		else
			throw new IllegalArgumentException(format("Tamanho inválido [%s]!", size));
	}
	
	public boolean isTruncate() {
		return this.truncate;
	}

	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}
}
