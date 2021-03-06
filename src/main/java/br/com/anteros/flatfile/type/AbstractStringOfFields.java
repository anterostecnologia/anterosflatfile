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

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import br.com.anteros.core.utils.Assert;
import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.flatfile.TextStream;

/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 * @param <G>
 */
@SuppressWarnings("serial")
public abstract class AbstractStringOfFields<G extends Field<?>> implements TextStream, List<G>, Cloneable{

	/**
	 * 
	 */
	private ArrayList<G> fields;

	/**
	 * 
	 */
	public AbstractStringOfFields() {

		fields = new ArrayList<G>();
	}
	
	/**
	 * 
	 */
	public AbstractStringOfFields(Integer size) {

		Assert.notNull(size, "size");

		if (size > 0) {
			fields = new ArrayList<G>(size);
			for (int i = 1; i <= size; i++){
				fields.add(null);
			}
		} else {
			throw new IllegalArgumentException(format("A quantidade de campos [%s] deve ser um número natural > 0!", size));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected AbstractStringOfFields<G> clone() throws CloneNotSupportedException {
		
		//Clona apenas com uma referência a fields.
		AbstractStringOfFields<G> sof = (AbstractStringOfFields<G>) super.clone();
		
		//Clonagem real
		sof.fields = new ArrayList<G>();
		
		for(G gf : fields)
				sof.fields.add((G) gf.clone());
		
		return sof;
	}

	/**
	 * <p>
	 * A leitrua de uma string, ou melhor, a divisão de uma string em fields,
	 * não ocorre sem uma lógica. Assim este método deve ser implementado por
	 * cada subclasse.
	 * </p>
	 * 
	 * @see br.com.anteros.flatfile.ReadWriteStream#read(Object)
	 */
	public abstract void read(String lineOfField);

	/**
	 * <p>
	 * Escreve os campos na ordem em que estão dispostos na lista em uma única linha (string).
	 * </p>
	 * 
	 * @see br.com.anteros.flatfile.ReadWriteStream#write()
	 */
	public String write() {

		StringBuilder lineOfFields = new StringBuilder(StringUtils.EMPTY);

		Assert.notNull(fields, "Fields == null");
		Assert.notEmpty(fields, "Coleção de fields vazia!");

		for (G field : fields) {
			
			try {

				lineOfFields.append(field.write());

			} catch (Exception e) {

				throw new IllegalStateException(
						format(
								"Erro ao tentar escrever o campo \"%s\" com valor [%s] na posição [%s] no layout do registro.",
								field.getName(), field.getValue(), fields
										.indexOf(field)+1),e);
			}
		}

		return lineOfFields.toString();
	}

	/**
	 * @return the fields
	 */
	public List<G> getFields() {
		return fields;
	}

	/**
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(G e) {

		return fields.add(e);
	}

	/**
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, G element) {

		fields.add(index, element);
	}

	/**
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends G> c) {

		return fields.addAll(c);
	}

	/**
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection<? extends G> c) {

		return fields.addAll(index, c);
	}

	/**
	 * @see java.util.List#clear()
	 */
	public void clear() {

		fields.clear();
	}

	/**
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {

		return fields.contains(o);
	}

	/**
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c) {

		return fields.containsAll(c);
	}

	/**
	 * @see java.util.List#get(int)
	 */
	public G get(int index) {

		return fields.get(index);
	}

	/**
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {

		return fields.indexOf(o);
	}

	/**
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {

		return fields.isEmpty();
	}

	/**
	 * @see java.util.List#iterator()
	 */
	public Iterator<G> iterator() {

		return fields.iterator();
	}

	/**
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {

		return fields.indexOf(o);
	}

	/**
	 * @see java.util.List#listIterator()
	 */
	public ListIterator<G> listIterator() {

		return fields.listIterator();
	}

	/**
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator<G> listIterator(int index) {

		return fields.listIterator(index);
	}

	/**
	 * @see java.util.List#remove(int)
	 */
	public G remove(int index) {

		return fields.remove(index);
	}

	/**
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {

		return fields.remove(o);
	}

	/**
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c) {

		return fields.removeAll(c);
	}

	/**
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> c) {

		return fields.retainAll(c);
	}

	/**
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public G set(int index, G element) {

		return fields.set(index, element);
	}

	/**
	 * @see java.util.List#size()
	 */
	public int size() {

		return fields.size();
	}

	/**
	 * @see java.util.List#subList(int, int)
	 */
	public List<G> subList(int fromIndex, int toIndex) {

		return fields.subList(fromIndex, toIndex);
	}

	/**
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {

		return fields.toArray();
	}

	/**
	 * @see java.util.List#toArray(Object[])
	 */
	public <T> T[] toArray(T[] a) {

		return fields.toArray(a);
	}
}
