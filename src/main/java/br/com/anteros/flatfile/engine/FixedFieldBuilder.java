package br.com.anteros.flatfile.engine;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.anteros.flatfile.language.EnumFormats;
import br.com.anteros.flatfile.language.EnumFormatsTypes;
import br.com.anteros.flatfile.language.MetaField;
import br.com.anteros.flatfile.type.Filler;
import br.com.anteros.flatfile.type.component.Fillers;
import br.com.anteros.flatfile.type.component.FixedField;
import br.com.anteros.flatfile.type.component.Side;
import br.com.anteros.core.utils.DateUtil;
import br.com.anteros.core.utils.ObjectUtils;
import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.flatfile.TexgitException;

/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 */
class FixedFieldBuilder {

	private final static String BASE_DECIMAL_FORMAT = "0.";
	
	static FixedField<?> build(MetaField metaField) {

		FixedField<?> fixedField = null;

		try {

			fixedField = getInstance(metaField);

		} catch (ParseException e) {
			throw new TexgitException("Field: " + metaField.getName(), e);
		}

		return fixedField;
	}

	private static FixedField<?> getInstance(MetaField metaField)
			throws ParseException {

		FixedField<?> fField = null;

		Format formatter = getFormater(metaField);

		switch (metaField.getType()) {
		
		case CHARACTER:
			FixedField<Character> fCHR = new FixedField<Character>();
			if (StringUtils.isNotBlank(metaField.getValue()))
				if(metaField.getValue().length() == 1)
					fCHR.setValue(metaField.getValue().charAt(0));
				else
					throw new IllegalArgumentException("Tipo character deve ter apenas 1!");
			else
				fCHR.setValue(' ');
			fField = fCHR;
			break;
		case STRING:
			FixedField<String> fSTR = new FixedField<String>();
			if (StringUtils.isNotBlank(metaField.getValue()))
				fSTR.setValue(metaField.getValue());
			else
				fSTR.setValue(StringUtils.EMPTY);
			fField = fSTR;
			break;
		case INTEGER:
			FixedField<Integer> fINT = new FixedField<Integer>();
			if (StringUtils.isNotBlank(metaField.getValue()))
				fINT.setValue(Integer.parseInt(metaField.getValue()));
			else
				fINT.setValue(Integer.valueOf(0));
			fField = fINT;
			break;
		case LONG:
			FixedField<Long> fLNG = new FixedField<Long>();
			if (StringUtils.isNotBlank(metaField.getValue()))
				fLNG.setValue(Long.parseLong(metaField.getValue()));
			else
				fLNG.setValue(Long.valueOf(0));
			fField = fLNG;
			break;
		case FLOAT:
			FixedField<Float> fFLT = new FixedField<Float>();
			if (StringUtils.isNotBlank(metaField.getValue()))
				fFLT.setValue(Float.parseFloat(metaField.getValue()));
			else
				fFLT.setValue(Float.valueOf(0));
			fField = fFLT;
			break;
		case DOUBLE:
			FixedField<Double> fDBE = new FixedField<Double>();
			if (StringUtils.isNotBlank(metaField.getValue()))
				fDBE.setValue(Double.parseDouble(metaField.getValue()));
			else
				fDBE.setValue(Double.valueOf(0));
			fField = fDBE;
			break;
		case BIGDECIMAL:
			FixedField<BigDecimal> fBDL = new FixedField<BigDecimal>();
			if (StringUtils.isNotBlank(metaField.getValue()))
				fBDL.setValue(new BigDecimal(DecimalFormat.class
						.cast(formatter).parse(metaField.getValue())
						.doubleValue()));
			else
				fBDL.setValue(BigDecimal.ZERO);
			fField = fBDL;
			break;
		case DATE:
			FixedField<Date> fDTE = new FixedField<Date>();
			if (StringUtils.isNotBlank(metaField.getValue())){
				
				fDTE.setValue(DateFormat.class.cast(formatter).parse(
						metaField.getValue()));
			}
			else{				
				fDTE.setValue(getInvalidDate());
			}
			fField = fDTE;
			break;
		}
		
		fField.setName(metaField.getName());
		fField.setFixedLength(metaField.getLength());
		fField.setFiller(getFiller(metaField));
		fField.setBlankAccepted(metaField.isBlankAccepted());
		fField.setTruncate(metaField.isTruncate());
		
		if(ObjectUtils.isNotNull(formatter))
			fField.setFormatter(formatter);

		return fField;
	}
	
	private static Date getInvalidDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(1, 0, 1);
		calendar.setLenient(false);
		return DateUtil.truncate(calendar.getTime(), Calendar.YEAR);
	}

	private static Filler getFiller(MetaField metaField) {

		Filler filler = null;

		if (ObjectUtils.isNotNull(metaField.getFiller())) {

			br.com.anteros.flatfile.type.component.Filler<String> filr = new br.com.anteros.flatfile.type.component.Filler<String>();
			filr.setPadding(metaField.getFiller().getPadding());
			filr.setSideToFill(Side.valueOf(metaField.getFiller()
					.getSideToFill().name()));
			filler = filr;

		} else {
			filler = Fillers.valueOf(metaField.getPadding().name());
		}

		return filler;
	}

	private static Format getFormater(MetaField metaField) {

		Format formatter = null;

		if (ObjectUtils.isNotNull(metaField.getFormatter())) {

			formatter = buildFormat(metaField.getFormatter().getFormat(),
					metaField.getFormatter().getType());

		} else {
			if(ObjectUtils.isNotNull(metaField.getFormat())){
			
				EnumFormats format = metaField.getFormat(); 
				
				EnumFormatsTypes type = EnumFormatsTypes.valueOf(format.name()
						.split("_")[0]);

				formatter = buildFormat(buildFormat(format, type), type);
			}
		}

		return formatter;
	}

	private static Format buildFormat(String strFormat, EnumFormatsTypes type) {

		Format format = null;

		switch (type) {

		case DATE:
			format = new SimpleDateFormat(strFormat);
			break;
		case DECIMAL:
			format = new DecimalFormat(strFormat);
			break;
		}

		return format;
	}

	private static String buildFormat(EnumFormats format, EnumFormatsTypes type) {

		String strFormat = StringUtils.EMPTY;
		/*
		 * DATE_DDMMYY, DATE_DDMMYYYY, DATE_YYMMDD, DATE_YYYYMMDD,
		 * DECIMAL_D,DECIMAL_DD, DECIMAL_DDD, DECIMAL_DDDD;
		 */
		String defFormat = format.name().split("_")[1];

		switch (type) {
		case DATE:

			defFormat = defFormat.replaceAll("D", "d");
			strFormat = defFormat.replaceAll("Y", "y");

			break;
		case DECIMAL:

			int lengthToFill = BASE_DECIMAL_FORMAT.length()
					+ StringUtils.countOccurrencesOf(defFormat, "D");

			strFormat = Fillers.ZERO_RIGHT.fill(
					BASE_DECIMAL_FORMAT, lengthToFill);

			break;
		}

		return strFormat;
	}
}
