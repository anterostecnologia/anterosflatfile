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

import java.text.Format;

import br.com.anteros.flatfile.TextStream;

/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 * @param <G>
 */
public interface Field<G> extends TextStream, Cloneable{

	public abstract String getName();

	public abstract void setName(String name);

	public abstract G getValue();

	public abstract void setValue(G value);

	public abstract Format getFormatter();

	public abstract void setFormatter(Format formatter);

	public abstract boolean isBlankAccepted();

	public abstract void setBlankAccepted(boolean blankAccepted);
	
	public abstract Field<G> clone() throws CloneNotSupportedException;
}
