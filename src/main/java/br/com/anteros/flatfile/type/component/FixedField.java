/*
 * Copyright 2008 JRimum Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * Created at: 26/07/2008 - 12:44:41
 * 
 * ================================================================================
 * 
 * Direitos autorais 2008 JRimum Project
 * 
 * Licenciado sob a Licença Apache, Versão 2.0 ("LICENÇA"); você não pode usar
 * esse arquivo exceto em conformidade com a esta LICENÇA. Você pode obter uma
 * cópia desta LICENÇA em http://www.apache.org/licenses/LICENSE-2.0 A menos que
 * haja exigência legal ou acordo por escrito, a distribuição de software sob
 * esta LICENÇA se dará “COMO ESTÁ”, SEM GARANTIAS OU CONDIÇÕES DE QUALQUER
 * TIPO, sejam expressas ou tácitas. Veja a LICENÇA para a redação específica a
 * reger permissões e limitações sob esta LICENÇA.
 * 
 * Criado em: 26/07/2008 - 12:44:41
 * 
 */
package br.com.anteros.flatfile.type.component;

import static java.lang.String.format;

import java.text.Format;
import java.util.Objects;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.flatfile.type.Filler;


/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 * @param <G>
 */
@SuppressWarnings("serial")
public class FixedField<G> extends Field<G> implements br.com.anteros.flatfile.type.FixedField<G>{
	
	/**
	 * <p>
	 * Tamanho de especificação e parâmetro da string de leitura ou escrita do campo.
	 * </p>
	 */
	private Integer length;
	
	/**
	 * <p>
	 * Preenchedor do valor utilizado na hora da escrita.
	 * </p>
	 */
	private Filler filler;
	
	
	/**
	 * <p>
	 * Tamanho da string de escrita do campo.
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
	public FixedField() {
		super();
	}
	
	public FixedField(G value, Integer length) {
		super(value);
		setFixedLength(length);
	}

	public FixedField(G value, Integer length, Filler filler) {
		super(value);
		setFixedLength(length);
		setFiller(filler);
	}

	public FixedField(G value, Integer length, Format formatter) {
		super(value,formatter);
		setFixedLength(length);
	}

	public FixedField(G value, Integer length, Format formatter, Filler filler) {
		super(value,formatter);
		setFixedLength(length);
		setFiller(filler);
	}
	
	public FixedField(String name, G value, Integer length) {
		super(name,value);
		setFixedLength(length);
	}

	public FixedField(String name, G value, Integer length, Filler filler) {
		super(name,value);
		setFixedLength(length);
		setFiller(filler);
	}
	
	public FixedField(String name, G value, Integer length, Format formatter) {
		super(name,value,formatter);
		setFixedLength(length);
	}

	public FixedField(String name, G value, Integer length, Format formatter, Filler filler) {
		super(name,value,formatter);
		setFixedLength(length);
		setFiller(filler);
	}

	@Override
	public FixedField<G> clone() throws CloneNotSupportedException {
		
		return (FixedField<G>) super.clone();
	}

	/**
	 * @see br.com.anteros.flatfile.type.component.Field#read(java.lang.String)
	 */
	@Override
	public void read(String str) {

		Assert.notNull(str, "String inválida [null]!");

		if (str.length() == getFixedLength()) {
			super.read(str);
		} else
			throw new IllegalArgumentException(format("Tamanho da string [%s] diferente do especificado [%s]! %s",str.length(),getFixedLength(),toString()));
	}

	/**
	 * @see br.com.anteros.flatfile.type.component.Field#write()
	 */
	@Override
	public String write() {
		
		String str = fill(super.write());

		instantLength = str.length();
		
		if (isTruncate() && instantLength > getFixedLength()) {
			str = str.substring(0, getFixedLength());
			instantLength = getFixedLength();
		}
		
		isFixedAsDefined();
			
		return str;
	}

	private String fill(String str) {
		
		if(ObjectUtils.isNotNull(filler))
			str = filler.fill(str, length);
		
		return str;
	}
	
	public boolean isFixedAsDefined() throws IllegalStateException {
		
		if(instantLength.equals(getFixedLength()))
			return true;
		else
			throw new IllegalStateException(format("Tamanho da string [%s] diferente do especificado [%s]! %s",instantLength,getFixedLength(),toString()));
	}
	
	public Integer getFixedLength() {
		
		return this.length;
	}

	public void setFixedLength(Integer length) {
		
		if (ObjectUtils.isNotNull(length) && length.intValue() > 0)
			this.length = length;
		else
			throw new IllegalArgumentException(format("Comprimento inválido [%s]!",length));
		
	}
	
	public Filler getFiller() {
		return filler;
	}

	public void setFiller(Filler filler) {
		
		if(ObjectUtils.isNotNull(filler))
			this.filler = filler;
		else
			throw new IllegalArgumentException(format("Preenchedor inválido [%s]!",filler));
	}

	public boolean isTruncate() {
		return this.truncate;
	}

	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}

	
	@Override
	public String toString() {

		return format(
				"%s FixedField [length=%s, instantLength=%s, filler=%s, truncate=%s]",
				super.toString()
				, ObjectUtils.whenNull(this.length, StringUtils.EMPTY)
				, ObjectUtils.whenNull(this.instantLength, StringUtils.EMPTY)
				, ObjectUtils.whenNull(this.filler, StringUtils.EMPTY)
				, ObjectUtils.whenNull(this.truncate, StringUtils.EMPTY));
	}

}
