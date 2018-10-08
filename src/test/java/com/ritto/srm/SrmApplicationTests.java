package com.ritto.srm;

import com.ritto.srm.entity.CpuBean;
import com.ritto.srm.entity.SyncBean;
import com.ritto.srm.service.jpa.SyncRepository;
import com.ritto.srm.service.jpa.cpuRepository;
import com.ritto.srm.service.jpa2.cpuRepository2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SrmApplicationTests {
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private cpuRepository cpuRepository;

	@Autowired
	private cpuRepository2 cpuRepository2;

	@Autowired
	private SyncRepository syncRepository;

	@Autowired
	@Qualifier("primaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate1;

	@Autowired
	@Qualifier("secondaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate2;



	@Test
	public void contextLoads() {
		CpuBean cpu = cpuRepository.findById(1).get();
		Assert.assertNotNull(cpu);
		List tablelist = entityManager.createNativeQuery("select table_name from information_schema.tables where table_schema='Hotel_manage'").getResultList();
		List tablelist2 = cpuRepository2.findalltab();
	}

	@Test
	@Transactional
	public void largeTableSync () {
//		String tablename = "candidate";
//		int count = Integer.parseInt(entityManager.createNativeQuery("SELECT COUNT(*) FROM "+tablename).getSingleResult().toString());
//		for (int i=0;i<count;i+=100){
//			Query query = entityManager.createNativeQuery("SELECT * FROM "+tablename+" WHERE 1=1 LIMIT "+i+",100");
////			query.setParameter(1,tablename);
//			List objectList = query.getResultList();
//			Iterator it = objectList.iterator();
//			while(it.hasNext()){
//				Object[] object = (Object[])it.next();
//				StringBuffer sql = new StringBuffer();
//				sql.append("INSERT INTO ");
//				sql.append(tablename);
//				sql.append(" VALUES( ");
//				for (int j=0;j < object.length;j++){
//					if (null!=object[j]){
//						if (object[j].getClass().getName().equals("java.lang.String")||object[j].getClass().getName().equals("java.sql.Timestamp")){
//							sql.append("'"+object[j].toString()+"'");
//						}else {
//							sql.append(object[j]);
//						}
//					}else {
//						sql.append(object[j]);
//					}
//
//					sql.append(" ,");
//				}
//				sql.deleteCharAt(sql.length()-1);
//				sql.append(" )");
//				if(jdbcTemplate2.update(sql.toString())>0){
//					System.out.println("seccess");
//				}
//			}
//		}
	}

	@Test
	public void jpatest(){
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE sync SET last_sync_state ='已同步' WHERE sync_tab_name = '");
		sql.append("cpu");
		sql.append("'");
		if (jdbcTemplate1.update(sql.toString())>0){
			System.out.println("同步成功");
		}
	}

	@Test
	public void auto(){
		List<SyncBean> beanList = syncRepository.findAll();
		beanList.forEach(syncBean -> {
			syncBean.getSyncRateH();
		});
	}

}
