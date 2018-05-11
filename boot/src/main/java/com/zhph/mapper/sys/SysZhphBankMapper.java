package com.zhph.mapper.sys;

import java.util.List;

import com.zhph.model.sys.SysZhphBank;

public interface SysZhphBankMapper {

	List<SysZhphBank> queryAllZhphBank(SysZhphBank params);

	Integer findBankCountById(String bankCode);

	void delById(Long priNumber);

	void addByObj(SysZhphBank readValue);

	void updateById(SysZhphBank params);

	List<SysZhphBank> queryZhphBankByCode(String code);

}
