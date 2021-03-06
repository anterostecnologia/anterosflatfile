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



import br.com.anteros.flatfile.TextStream;


/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
public enum Fillers implements br.com.anteros.flatfile.type.Filler{

	/**
	 * Filler padrão para preenchimento com zeros a esquerda.
	 */
	ZERO_LEFT(new Filler<Integer>(0, Side.LEFT)),
	
	/**
	 * Filler padrão para preenchimento com zeros a direita.
	 */
	ZERO_RIGHT(new Filler<Integer>(0, Side.RIGHT)),
	
	/**
	 * Filler padrão para preenchimento com espaços em branco a esquerda.
	 */
	WHITE_SPACE_LEFT(new Filler<String>(" ", Side.LEFT)),
	
	/**
	 * Filler padrão para preenchimento com espaços em branco a direita.
	 */
	WHITE_SPACE_RIGHT(new Filler<String>(" ", Side.RIGHT));
	
	private Filler<?> filler;
	
	private Fillers(Filler<?> filler){
		this.filler = filler;
	}
	
	/**
	 * @param toFill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(java.lang.String, int)
	 */
	public String fill(String toFill, int length) {
		return filler.fill(toFill, length);
	}

	/**
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(long, int)
	 */
	public String fill(long tofill, int length) {
		return filler.fill(tofill, length);
	}

	/**
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(int, int)
	 */
	public String fill(int tofill, int length) {
		return filler.fill(tofill, length);
	}

	/**
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(short, int)
	 */
	public String fill(short tofill, int length) {
		return filler.fill(tofill, length);
	}

	/**
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(byte, int)
	 */
	public String fill(byte tofill, int length) {
		return filler.fill(tofill, length);
	}

	/**
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(char, int)
	 */
	public String fill(char tofill, int length) {
		return filler.fill(tofill, length);
	}

	/**
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(double, int)
	 */
	public String fill(double tofill, int length) {
		return filler.fill(tofill, length);
	}

	/**
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(float, int)
	 */
	public String fill(float tofill, int length) {
		return filler.fill(tofill, length);
	}

	/**
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(java.lang.Object, int)
	 */
	public String fill(Object tofill, int length) {
		return filler.fill(tofill, length);
	}

	/**
	 * @param tofill
	 * @param length
	 * @return String preenchida
	 * @see br.com.anteros.flatfile.type.component.Filler#fill(br.com.anteros.flatfile.TextStream, int)
	 */
	public String fill(TextStream tofill, int length) {
		return filler.fill(tofill, length);
	}

}
