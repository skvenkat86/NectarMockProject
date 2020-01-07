package com.infosys.nector.mock;

import java.time.LocalDate;
import java.util.Date;

public class UtilClass {

	public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
	    return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
	}
	
}
