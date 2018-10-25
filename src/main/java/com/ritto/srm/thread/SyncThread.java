package com.ritto.srm.thread;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/7/20
 * @Description: 手动同步线程
 * @Modified By:
 */
public class SyncThread extends Thread{
    public static Map<String,Integer> jdt = new HashMap<>();
    protected JdbcTemplate jdbcTemplate1;

    protected JdbcTemplate jdbcTemplate2;

    EntityManager entityManager;

    private String tabname;
    private int dataindex;
    public SyncThread(String name,int index,JdbcTemplate jt1,JdbcTemplate jt2,EntityManager em){
        List<Integer> list = jt1.queryForList("SELECT data_index from sync WHERE sync_tab_name ='"+name+"'",Integer.class);
        tabname = name;
        dataindex = list.get(0);
        jdbcTemplate1 = jt1;
        jdbcTemplate2 = jt2;
        entityManager = em;
        jdt.put(tabname,0);
    }

//    @Transactional
    @Override
    public void run(){
        try {
            int count = Integer.parseInt(entityManager.createNativeQuery("SELECT COUNT(*) FROM "+tabname).getSingleResult().toString());
            for (int i=dataindex;i<count;i+=10){
                Query query = entityManager.createNativeQuery("SELECT * FROM "+tabname+" WHERE 1=1 LIMIT "+i+",10");
//			query.setParameter(1,tablename);
                List objectList = query.getResultList();
                int k = 0;
                Iterator it = objectList.iterator();
                while(it.hasNext()){
                    Object[] object = (Object[])it.next();
                    StringBuffer sql = new StringBuffer();
                    sql.append("INSERT INTO ");
                    sql.append(tabname);
                    sql.append(" VALUES( ");
                    for (int j=0;j < object.length;j++){
                        if (null!=object[j]){
                            if (object[j].getClass().getName().equals("java.lang.String")
                                    ||object[j].getClass().getName().equals("java.sql.Timestamp")
                                    ||object[j].getClass().getName().equals("java.sql.Date")
                                    ||object[j].getClass().getName().equals("java.lang.Character")){
                                sql.append("'"+object[j].toString()+"'");
                            }else {
                                sql.append(object[j]);
                            }
                        }else {
                            sql.append(object[j]);
                        }

                        sql.append(" ,");
                    }
                    sql.deleteCharAt(sql.length()-1);
                    sql.append(" )");
                    if(jdbcTemplate2.update(sql.toString())>0){
                        System.out.println("seccess");
                        k++;
                        jdbcTemplate1.update("UPDATE sync SET data_index = "+ (i+k+1) + " WHERE sync_tab_name = '" + tabname +"'");
                    }
                }
                jdt.put(tabname,i*100/count>100?100:i*100/count);
            }
            jdt.put(tabname,100);
            //同步完成后
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE sync SET last_sync_state ='已同步' WHERE sync_tab_name = '");
            sql.append(tabname);
            sql.append("'");
            if (jdbcTemplate1.update(sql.toString())>0){
                System.out.println("同步成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            jdt.remove(tabname);
        }

    }
}
