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

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTimeUtils;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.core.utils.DateUtil;
import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.flatfile.TextStream;

/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 * @param <G>
 */
@SuppressWarnings("serial")
public class Field<G> implements br.com.anteros.flatfile.type.Field<G>{
	
	/**
	 *<p>
	 *Nome do campo, também pode ser usado como id. 
	 * </p>
	 */
	private String name;
	
	/**
	 * <p>
	 * Valor do campo.
	 * </p>
	 */
	private G value;
	
	/**
	 * <p>
	 * Formatador utilizado na leitura e escrita do valor do campo.
	 * </p>
	 */
	private Format formatter;
	
	/**
	 * <p>
	 * Necessário para ler campos númericos em branco.
	 * </p>
	 */
	private boolean blankAccepted;
	
	/**
	 * 
	 */
	public Field() {
		super();
	}

	/**
	 * @param value
	 */
	public Field(G value) {
		super();
		setValue(value);
	}
	
	/**
	 * <p>
	 * Cria um <code>Field</code> com um valor e um formatador para o valor. Isto significa que a leitura e escrita do valor informado
	 * será de acordo com o formatador.
	 * </p>
	 * 
	 * @param value
	 * @param formatter
	 */
	public Field(G value, Format formatter){
		
		setValue(value);
		setFormatter(formatter);
	}

	/**
	 * @param name
	 * @param value
	 */
	public Field(String name, G value) {
		super();
		setName(name);
		setValue(value);
	}

	
	/**
	 * <p>
	 * Cria um <code>Field</code> com nome para identificação, valor e um formatador.
	 * </p>
	 * 
	 * @param name
	 * @param value
	 * @param formatter
	 * 
	 * @see #Field(Object, Format)
	 */
	public Field(String name, G value, Format formatter){
		
		setName(name);
		setValue(value);
		setFormatter(formatter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Field<G> clone() throws CloneNotSupportedException {
		
		return (Field<G>) super.clone();
	}

	public void read(String str) {

		Assert.notNull(str, "String inválida [null]!");
		
		try{
			
			if (this.value instanceof TextStream) {

				TextStream reader = (TextStream) this.value;
				reader.read(str);

			} else if (this.value instanceof BigDecimal) {

				readDecimalField(str);

			} else if (this.value instanceof Date) {

				readDateField(str);

			} else if (this.value instanceof Character) {

				readCharacter(str);

			} else {

				readStringOrNumericField(str);
			}
			
		}catch (Exception e) {
			
			throw new IllegalStateException(format("Falha na leitura do campo! %s",toString()),e);
		}
	}

	@SuppressWarnings("unchecked")
	private void readCharacter(String str) {
		
		if(str.length() == 1){
			
			value = (G) new Character(str.charAt(0)); 
			
		}else
			throw new IllegalArgumentException("String com mais de 1 character!");
	}

	@SuppressWarnings("unchecked")
	private void readDecimalField(String str) {
		
		DecimalFormat decimalFormat = (DecimalFormat) formatter;
		
		try {
			
			String number = parseNumber(str);
			
			Long parsedValue = (Long) formatter.parseObject(number);
			
			BigDecimal decimalValue = new BigDecimal(parsedValue.longValue());
			
			decimalValue = decimalValue.movePointLeft(decimalFormat.getMaximumFractionDigits());
							
			value = (G) decimalValue;
			
		} 
		catch (ParseException e) {
			
			throwReadError(e, str);
		}
	}

	@SuppressWarnings("unchecked")
	private void readDateField(String str) {		
		try {			
			if(StringUtils.isBlank(str) || StringUtils.repeat("0", str.length()).equals(str)){
				if(isBlankAccepted()){					
					value = null;					
				}else{					
					new IllegalArgumentException(format("Campo data vazio não permitido: [%s]!",str));
				}				
			}else{				
				value = (G) formatter.parseObject(str);
			}				
			
		} catch (ParseException e) {			
			throwReadError(e, str);
		}
	}

	@SuppressWarnings("unchecked")
	private void readStringOrNumericField(String str) {
		
		str = parseNumber(str.trim());
		
		Class<?> clazz = value.getClass();
		
		if(clazz.equals(String.class)){
			value = (G) str.trim();
		}else{
			readNumeric(clazz,str.trim());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readNumeric(Class<?> clazz, String str) {
		
		for (Constructor<?> cons : clazz.getConstructors()) {

			if (cons.getParameterTypes().length == 1){
				
				if (cons.getParameterTypes()[0].equals(String.class)){
					try {
						
						value = (G) cons.newInstance(str.trim());

					} catch (Exception e) {
						
						throwReadError(e, str);
					}
				}
			}
		}
	}

	public String write() {
		
		try{

			String str = null;
			
			if (value instanceof TextStream) {
	
				TextStream its = (TextStream) value;
	
				str = its.write();
	
			} else if (value instanceof Date) {
	
				str = writeDateField();
			}
	
			else if (value instanceof BigDecimal)
				str = writeDecimalField();
	
			else
				str = value.toString();
	
			return str;
			
		}catch (Exception e) {
			
			throw new IllegalStateException(format("Falha na escrita do campo escrita! %s",toString()),e);
		}
	}
	
	private String writeDecimalField(){
		
		BigDecimal decimalValue = (BigDecimal) value;
		
		decimalValue = decimalValue.movePointRight(((DecimalFormat)formatter).getMaximumFractionDigits());
		
		return decimalValue.toString();
	}
	
	private String writeDateField(){
		
		if (!getInvalidDate().equals((Date) value)){
		
			return formatter.format(value);
		}
		
		return StringUtils.EMPTY;
	}
	
	private String parseNumber(String str){
		
		if(StringUtils.isBlank(str)){
			
			if(isBlankAccepted())
				str = "0";
			else
				new IllegalArgumentException(format("Campo numérico vazio não permitido: [%s]!",str));
		}else
			if(!StringUtils.isNumber(str))
				new IllegalArgumentException(format("O campo deve ser numérico e não: [%s]!",str));
		
		return str;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		
		if (StringUtils.isNotEmpty(name))
			this.name = name;
		else
			throw new IllegalArgumentException(format("Nome Inválido: [%s]!",name));
	}
	
	public boolean isBlankAccepted() {
		return this.blankAccepted;
	}

	public void setBlankAccepted(boolean blankAccepted) {
		this.blankAccepted = blankAccepted;
	}

	public G getValue() {
		return value;
	}

	public void setValue(G value) {
		
		if (ObjectUtils.isNotNull(value))
			this.value = value;
		else
			throw new IllegalArgumentException(format("Valor Inválido: [%s]!",value));
	}

	public Format getFormatter() {
		return formatter;
	}

	public void setFormatter(Format formatter) {
		
		if (ObjectUtils.isNotNull(formatter))
			this.formatter = formatter;
		else
			throw new IllegalArgumentException(format("Formato inválido: [%s]!",formatter));
	}

	private void throwReadError(Exception e, String value){		
		
		throw new IllegalArgumentException(format("Falha na leitura da string: [\"%s\"]! %s",value,toString()), e);
	}
	
	
	
	private Date getInvalidDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(1, 0, 1);
		calendar.setLenient(false);
		return DateUtil.truncate(calendar.getTime(), Calendar.YEAR);
	}

	@Override
	public String toString() {
		return "Field [name=" + name + ", value=" + value + ", formatter=" + formatter + ", blankAccepted="
				+ blankAccepted + "]";
	}
	
	public void clearName() {
		this.name = "";
		
	}
}
