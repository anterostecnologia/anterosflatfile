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
package br.com.anteros.flatfile.type;

import java.io.Serializable;

import br.com.anteros.flatfile.TextStream;

public interface Filler extends Serializable{

	/**
	 * Preenche o campo com o caracter especificado e no lado especificado.
	 * 
	 * <p>
	 * Exemplo:
	 * <br/>
	 * Se <code>sideToFill == SideToFill.LEFT</code>, o caracter especificado será adicionado à String
	 * no lado esquerdo até que o campo fique com o tamanho que foi definido.
	 * </p>
	 * 
	 * @param toFill
	 * @param length
	 * @return String preenchida
	 * 
	 * @since 0.2
	 */
	String fill(String toFill, int length);

	/**
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * 
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * 
	 * @see Filler#fill(String, int)
	 * 
	 * @since 0.2
	 */
	String fill(long tofill, int length);

	/**
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * 
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * 
	 * @see Filler#fill(String, int)
	 * 
	 * @since 0.2
	 */
	String fill(int tofill, int length);

	/**
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * 
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * 
	 * @see Filler#fill(String, int)
	 * 
	 * @since 0.2
	 */
	String fill(short tofill, int length);

	/**
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * 
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * 
	 * @see Filler#fill(String, int)
	 * 
	 * @since 0.2
	 */
	String fill(byte tofill, int length);

	/**
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * 
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * 
	 * @see Filler#fill(String, int)
	 * 
	 * @since 0.2
	 */
	String fill(char tofill, int length);

	/**
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * 
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * 
	 * @see Filler#fill(String, int)
	 * 
	 * @since 0.2
	 */
	String fill(double tofill, int length);

	/**
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * 
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * 
	 * @see Filler#fill(String, int)
	 * 
	 * @since
	 */
	String fill(float tofill, int length);

	/**
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>toFill.toString()</code>.
     *
	 * <p>
	 * Caso <code>toFill</code> seja <code>null</code>, o método 
	 * <code>fill(String, int)</code> receberá uma String nula como parâmetro.
	 * </p>
	 * 
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * 
	 * @see Filler#fill(String, int)
	 * 
	 * @since 0.2
	 */
	String fill(Object tofill, int length);

	/**
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>toFill.write()</code>.
     *
	 * <p>
	 * Caso <code>toFill</code> seja <code>null</code>, o método 
	 * <code>fill(String, int)</code> receberá uma String nula como parâmetro.
	 * </p>
	 * 
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * 
	 * @see Filler#fill(String, int)
	 * 
	 * @since 0.2
	 */
	String fill(TextStream tofill, int length);
}