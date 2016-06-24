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
