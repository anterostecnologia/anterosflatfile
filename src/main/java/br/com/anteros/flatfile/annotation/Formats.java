package br.com.anteros.flatfile.annotation;

import br.com.anteros.flatfile.language.EnumFormats;

public enum Formats {

	NONE, DATE_DDMMYY, DATE_DDMMYYYY, DATE_YYMMDD, DATE_YYYYMMDD, DECIMAL_D, DECIMAL_DD, DECIMAL_DDD, DECIMAL_DDDD;

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
		}
		return null;
		
	}
}
