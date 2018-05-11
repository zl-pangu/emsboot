package com.zhph.mapper.cl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhph.model.cl.ClCreditBranchCm1;
import com.zhph.model.cl.ClCreditBranchM2;
import com.zhph.model.cl.ClCreditBranchRcl;
import com.zhph.model.cl.ClCreditCoremanagerCm1;
import com.zhph.model.cl.ClCreditCoremanagerM2;
import com.zhph.model.cl.ClCreditCoremanagerM2Det;
import com.zhph.model.cl.ClCreditCoremanagerRcl;
import com.zhph.model.cl.ClSalaryP2pOrg;

/**
 * @author lxp
 * @date 2018年1月29日 下午5:28:16
 * @parameter
 * @return
 */
public interface ClSixMonthM2Mapper {

	public List<ClCreditCoremanagerM2Det> queryAllM2Detail(ClCreditCoremanagerM2Det clSixMonthM2);

	public List<ClSalaryP2pOrg> selectSalaryP2pOrg();

	// 入催率
	public void delClCreditCoremanagerRcl(@Param("gzYm") String gzYm);

	public List<ClCreditCoremanagerRcl> selectRclList();

	public void saveClCreditStoreManagerRCL(List<ClCreditCoremanagerRcl> storerclList);

	// c-m1迁徙率
	public void delClCreditCoremanagerCm1(@Param("gzYm") String gzYm);

	public List<ClCreditCoremanagerCm1> selectQxlList();

	public void saveClCreditCoremanagerCm1(List<ClCreditCoremanagerCm1> cm1list);

	// m2+逾期率
	public void delClCreditCoremanagerM2(@Param("gzYm") String gzYm);

	public List<ClCreditCoremanagerM2> selectYqlList();

	public void saveClCreditCoremanagerM2(List<ClCreditCoremanagerM2> m2List);

	// m2+明细
	public void delClCreditCoremanagerM2Det(@Param("gzYm") String gzYm);

	public List<ClCreditCoremanagerM2Det> selectM2List();

	public List<ClCreditCoremanagerM2Det> selectOverdueDaysList(String loanContractNo);

	public void saveClCreditCoremanagerM2Det(List<ClCreditCoremanagerM2Det> detaillist);

	// 分部入催率
	public void delClCreditBranchRcl(@Param("gzYm") String gzYm);

	public List<ClCreditBranchRcl> selectBrclList();

	public void saveClCreditBranchRcl(List<ClCreditBranchRcl> storerclList);

	// 分部C-M1迁徙率
	public void delClCreditBranchCm1(@Param("gzYm") String gzYm);

	public List<ClCreditBranchCm1> selectBqxlList();

	public void saveClCreditBranchCm1(List<ClCreditBranchCm1> cm1list);

	// 分部m2+
	public void delClCreditBranchM2(@Param("gzYm") String gzYm);

	public List<ClCreditBranchM2> selectByqlList();

	public void saveClCreditBranchM2(List<ClCreditBranchM2> m2List);

}
