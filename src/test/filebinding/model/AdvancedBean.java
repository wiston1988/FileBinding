package filebinding.model;

import java.math.BigDecimal;
import java.util.Date;
public class AdvancedBean {
	private String upperCaseField;
	private String stringParserThrowEField;
	private Date advancedDateField;
	private Long longField;
	private BigDecimal bigDecimalField;
	private People people;
	public People getPeople() {
		return people;
	}
	public void abc(People people) {
		this.people = people;
	}
	public String getUpperCaseField() {
		return upperCaseField;
	}
	public void setUpperCaseField(String upperCaseField) {
		this.upperCaseField = upperCaseField;
	}
	public String getStringParserThrowEField() {
		return stringParserThrowEField;
	}
	public void setStringParserThrowEField(String stringParserThrowEField) {
		this.stringParserThrowEField = stringParserThrowEField;
	}
	public Date getAdvancedDateField() {
		return advancedDateField;
	}
	public void setAdvancedDateField(Date advancedDateField) {
		this.advancedDateField = advancedDateField;
	}
	public Long getLongField() {
		return longField;
	}
	public void setLongField(Long longField) {
		this.longField = longField;
	}
	public BigDecimal getBigDecimalField() {
		return bigDecimalField;
	}
	public void setBigDecimalField(BigDecimal bigDecimalField) {
		this.bigDecimalField = bigDecimalField;
	}
	@Override
	public String toString() {
		return "AdvancedBean [upperCaseField=" + upperCaseField
				+ ", stringParserThrowEField=" + stringParserThrowEField
				+ ", advancedDateField=" + advancedDateField + ", longField="
				+ longField + ", bigDecimalField=" + bigDecimalField
				+ ", people=" + people + "]";
	}


}
