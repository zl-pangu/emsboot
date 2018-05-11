package com.zhph.model.common;


/**
 * 职务类别
 * @author zhph
 *
 */
public enum RankType {
	/**
	 * 销管总监
	 */
	SALES_DIRECTOR("销管总监","ZW82B43"),
	/**
	 * 城市分中心经理
	 */
	CITY_MANAGER("城市分中心经理","ZW906E2"),
	/**
	 * 营业部经理
	 */
	SALES_MANAGER("营业部经理","ZW13FED"),
	/**
	 * 团队经理
	 */
	TEAM_MANAGER("团队经理","ZW7A25B"),
	/**
	 * 客户代表
	 */
	CUSTOMER_STAND("客户代表","ZW72510"),

	/**
	 * 行政专员
	 */
	ADMIN_STAFF("行政专员","ZW616F5");

    private final String name;
	private final String num;

	RankType(String name,String num) {
        this.name = name;
		this.num = num;
    }

	public static String getName(String num) {
		for (RankType rankType : RankType.values()) {
			if (rankType.getNum().equals(num)) {
				return rankType.name;
			}
		}
		return null;
	}

	public static String getNum(String name){
		for (RankType rankType:RankType.values()){
			if (name.equals(rankType.getName())){
				return rankType.getNum();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public String getNum() {
		return num;
	}
}
