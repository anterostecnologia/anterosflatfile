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
package br.com.anteros.flatfile.annotation;

import br.com.anteros.flatfile.language.EnumPaddings;

public enum Paddings {

	NONE, ZERO_RIGHT, ZERO_LEFT, WHITE_SPACE_RIGHT, WHITE_SPACE_LEFT;

	public EnumPaddings convertToEnumPaddings() {
		if (name().equals(EnumPaddings.ZERO_RIGHT.name())){
			return EnumPaddings.ZERO_RIGHT;
		} else if (name().equals(EnumPaddings.ZERO_LEFT.name())){
				return EnumPaddings.ZERO_LEFT;	
		} else if (name().equals(EnumPaddings.WHITE_SPACE_RIGHT.name())){
			return EnumPaddings.WHITE_SPACE_RIGHT;
		} else if (name().equals(EnumPaddings.WHITE_SPACE_LEFT.name())){
			return EnumPaddings.WHITE_SPACE_LEFT;
		};
		return null;
	}
}
