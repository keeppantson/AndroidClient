package com.zgmz.ls.utils;

import com.zgmz.ls.AppContext;
import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.model.District;
import com.zgmz.ls.model.FamilyIncome;
import com.zgmz.ls.model.FamilyProperty;
import com.zgmz.ls.model.FamilySituation;
import com.zgmz.ls.model.FamilySpending;
import com.zgmz.ls.model.IdCard;

public class PreviewTools implements Const{

	private static final String ENTER = "\n";
	
	private static final String SPACE = "  ";
	
	private static final String TAB = "    ";
	
	private static final String NO_INPUT = "未填写";
	
//	private static final String NO_COMPLETE = "未完成";
	
	public static String getIdCard(IdCard idCard) {
		if(idCard == null) return NO_INPUT;
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("姓名：").append(idCard.getName()).append(ENTER);
		buffer.append("性别：").append(idCard.getSex()).append(ENTER);
		buffer.append("民族：").append(idCard.getNation()).append(ENTER);
		buffer.append("出身日期：").append(idCard.getBirthYear()+" 年 " + idCard.getBirthMonth()+" 月 "+idCard.getBirthDay()+ " 日").append(ENTER);
		buffer.append("住址：").append(idCard.getAddress()).append(ENTER);
		buffer.append("身份证号：").append(idCard.getIdNumber()).append(ENTER);
		buffer.append("签发机关：").append(idCard.getAuthority()).append(ENTER);
		buffer.append("有效期限：").append(idCard.getStartValidDate()+" - "+idCard.getEndValidDate()).append(ENTER);
		
		return buffer.toString();
	}
	
	public static String getDistrict(District district) {
		if(district == null) return NO_INPUT;
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("省份：").append(district.getProvice()).append(ENTER);
		buffer.append("市/直辖：").append(district.getCity()).append(ENTER);
		buffer.append("区/县：").append(district.getDistric()).append(ENTER);
		buffer.append("详细住址：").append(district.getAddress()).append(ENTER);
		buffer.append("位置：").append(district.getLocation()).append(ENTER);
		
		return buffer.toString();
	}
	
	
	public static String getFamilySituation(FamilySituation situation) {
		if(situation == null) return NO_INPUT;
		AppContext context = AppContext.getAppContext();
		StringBuffer buffer = new StringBuffer();
		String text;
		buffer.append(context.getString(R.string.family_situation_base)).append(ENTER);
		if(situation.getCode1001() != INT_INVALID) {
			if(situation.getCode1001() == INT_YES) 
				text = "是";
			else 
				text = "否";
		}
		else {
			text = NO_INPUT;
		}
		buffer.append(SPACE + context.getString(R.string.name_code1001)).append(TAB+text).append(ENTER);
		
		if(situation.getCode1002() != INT_INVALID) {
			if(situation.getCode1002() == INT_YES) 
				text = "是";
			else 
				text = "否";
		}
		else {
			text = NO_INPUT;
		}
		buffer.append(SPACE + context.getString(R.string.name_code1002)).append(TAB+text).append(ENTER);
		
		if(situation.getCode1003() != INT_INVALID) {
			if(situation.getCode1003() == INT_YES) 
				text = "是";
			else 
				text = "否";
		}
		else {
			text = NO_INPUT;
		}
		buffer.append(SPACE + context.getString(R.string.name_code1003)).append(TAB+text).append(ENTER);
		
		if(situation.getCode1004() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1004)).append(TAB+situation.getCode1004()).append(" 户").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1004)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1005() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1005)).append(TAB+situation.getCode1005()).append(" 户").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1005)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		buffer.append(context.getString(R.string.family_situation_members)).append(ENTER);
		
		if(situation.getCode1006() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1006)).append(TAB+situation.getCode1006()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1006)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1007() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1007)).append(TAB+situation.getCode1007()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1007)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1008() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1008)).append(TAB+situation.getCode1008()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1008)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1009() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1009)).append(TAB+situation.getCode1009()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1009)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1010() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1010)).append(TAB+situation.getCode1010()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1010)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1011() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1011)).append(TAB+situation.getCode1011()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1011)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1012() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1012)).append(TAB+situation.getCode1012()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1012)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1013() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1013)).append(TAB+situation.getCode1013()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1013)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1014() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1014)).append(TAB+situation.getCode1014()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1014)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1015() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1015)).append(TAB+situation.getCode1015()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1015)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1016() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1016)).append(TAB+situation.getCode1016()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1016)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(situation.getCode1017() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code1017)).append(TAB+situation.getCode1017()).append(" 人").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code1017)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		return buffer.toString();
	}
	
	
	public static String getFamilyProperty(FamilyProperty property) {
		if(property == null) return NO_INPUT;
		AppContext context = AppContext.getAppContext();
		StringBuffer buffer = new StringBuffer();
		String text;
		buffer.append(context.getString(R.string.family_property_housing)).append(ENTER);
		if(property.getCode2001() != INT_INVALID) {
			if(property.getCode2001() == INT_YES) 
				text = "是";
			else 
				text = "否";
		}
		else {
			text = NO_INPUT;
		}
		buffer.append(SPACE + context.getString(R.string.name_code2001)).append(TAB+text).append(ENTER);
		
		if(property.getCode2002() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2002)).append(TAB+property.getCode2002()).append(" 平方米").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2002)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(property.getCode2003() != INT_INVALID) {
			if(property.getCode2003() == HousingStructure.WOOD) {
				text = context.getString(R.string.wood);
			}
			else if(property.getCode2003() == HousingStructure.BRICK_AND_WOOD) {
				text = context.getString(R.string.brick_and_wood);
			}
			else if(property.getCode2003() == HousingStructure.BRICK_AND_CONCRETE) {
				text = context.getString(R.string.brick_and_concrete);		
			}
			else if(property.getCode2003() == HousingStructure.THATCH) {
				text = context.getString(R.string.thatch);
			}
		}
		else {
			text = NO_INPUT;
		}
		buffer.append(SPACE + context.getString(R.string.name_code2003)).append(TAB+text).append(ENTER);
		
		buffer.append(context.getString(R.string.family_property_land)).append(ENTER);
		if(property.getCode2004() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2004)).append(TAB+property.getCode2003()).append(" 亩").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2004)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(property.getCode2005() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2005)).append(TAB+property.getCode2005()).append(" 亩").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2005)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(property.getCode2006() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2006)).append(TAB+property.getCode2006()).append(" 亩").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2006)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		buffer.append(context.getString(R.string.family_property_durable_consumer_goods)).append(ENTER);
		
		
		if(property.getCode2007() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2007)).append(TAB+property.getCode2007()).append(" 台").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2007)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(property.getCode2008() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2008)).append(TAB+property.getCode2008()).append(" 台").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2008)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(property.getCode2009() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2009)).append(TAB+property.getCode2009()).append(" 台").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2009)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(property.getCode2010() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2010)).append(TAB+property.getCode2010()).append(" 台").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2010)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(property.getCode2011() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2011)).append(TAB+property.getCode2011()).append(" 部").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2011)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(property.getCode2012() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2012)).append(TAB+property.getCode2012()).append(" 部").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2012)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		if(property.getCode2013() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code2013)).append(TAB+property.getCode2013()).append(" 辆").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code2013)).append(TAB+NO_INPUT).append(ENTER);
		}
		
		return buffer.toString();
	}
	
	
	public static String getFamilyIncome(FamilyIncome income) {
		if(income == null) return NO_INPUT;
		AppContext context = AppContext.getAppContext();
		StringBuffer buffer = new StringBuffer();
		if(income.getCode3001() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code3001)).append(TAB + income.getCode3001()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code3001)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(income.getCode3002() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code3002)).append(TAB + income.getCode3002()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code3002)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(income.getCode3003() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code3003)).append(TAB + income.getCode3003()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code3003)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(income.getCode3004() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code3004)).append(TAB + income.getCode3004()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code3004)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(income.getCode3005() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code3005)).append(TAB + income.getCode3005()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code3005)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(income.getCode3006() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code3006)).append(TAB + income.getCode3006()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code3006)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(income.getCode3007() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code3007)).append(TAB + income.getCode3007()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code3007)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(income.getCode3008() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code3008)).append(TAB + income.getCode3008()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code3008)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(income.getCode3009() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code3009)).append(TAB + income.getCode3009()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code3009)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(income.getCode3010() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code3010)).append(TAB + income.getCode3010()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code3010)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		return buffer.toString();
	}
	
	
	public static String getFamilySpending(FamilySpending spending) {
		if(spending == null) return NO_INPUT;
		AppContext context = AppContext.getAppContext();
		StringBuffer buffer = new StringBuffer();
		if(spending.getCode4001() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code4001)).append(TAB + spending.getCode4001()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code4001)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(spending.getCode4002() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code4002)).append(TAB + spending.getCode4002()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code4002)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(spending.getCode4003() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code4003)).append(TAB + spending.getCode4003()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code4003)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(spending.getCode4004() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code4004)).append(TAB + spending.getCode4004()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code4004)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(spending.getCode4005() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code4005)).append(TAB + spending.getCode4005()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code4005)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(spending.getCode4006() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code4006)).append(TAB + spending.getCode4006()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code4006)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(spending.getCode4007() != INT_INVALID) {
			buffer.append(TAB + context.getString(R.string.name_code4007)).append(TAB + spending.getCode4007()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(TAB + context.getString(R.string.name_code4007)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(spending.getCode4008() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code4008)).append(TAB + spending.getCode4008()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code4008)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		if(spending.getCode4009() != INT_INVALID) {
			buffer.append(SPACE + context.getString(R.string.name_code4009)).append(TAB + spending.getCode4009()).append(" 元").append(ENTER);
		}
		else {
			buffer.append(SPACE + context.getString(R.string.name_code4009)).append(TAB + NO_INPUT).append(ENTER);
		}
		
		
		return buffer.toString();
	}
	
	
	
}
