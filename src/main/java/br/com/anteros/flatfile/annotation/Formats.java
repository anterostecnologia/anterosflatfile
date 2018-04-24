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

import br.com.anteros.flatfile.language.EnumFormats;

public enum Formats {

	NONE, DATE_DDMMYY, DATE_DDMMYYYY, DATE_YYMMDD, DATE_YYYYMMDD, DECIMAL_D, DECIMAL_DD, DECIMAL_DDD, DECIMAL_DDDD, DECIMAL_DDDDD;

	public EnumFormats convertToEnumFormats() {
		if (name().equals(EnumFormats.DATE_DDMMYY.name())){
			return EnumFormats.DATE_DDMMYY;
		} else if (name().equals(EnumFormats.DATE_DDMMYYYY.name())){
			return EnumFormats.DATE_DDMMYYYY;
		} else if (name().equals(EnumFormats.DATE_YYMMDD.name())){
			return EnumFormats.DATE_YYMMDD;
		} else if (name().equals(EnumFormats.DATE_YYYYMMDD.name())){
			return EnumFormats.DATE_YYYYMMDD;
		} else if (name().equals(EnumFormats.DECIMAL_D.name())){
			return EnumFormats.DECIMAL_D;
		} else if (name().equals(EnumFormats.DECIMAL_DD.name())){
			return EnumFormats.DECIMAL_DD;
		} else if (name().equals(EnumFormats.DECIMAL_DDD.name())){
			return EnumFormats.DECIMAL_DDD;
		} else if (name().equals(EnumFormats.DECIMAL_DDDD.name())){
			return EnumFormats.DECIMAL_DDDD;
		} else if (name().equals(EnumFormats.DECIMAL_DDDDD.name())){
			return EnumFormats.DECIMAL_DDDDD;
		}
		return null;
		
	}
}
