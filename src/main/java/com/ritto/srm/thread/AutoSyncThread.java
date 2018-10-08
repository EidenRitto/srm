package com.ritto.srm.thread;

import com.ritto.srm.entity.SyncBean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/7/19
 * @Description: 自动同步线程
 * @Modified By:
 */
public class AutoSyncThread extends Thread {

    public volatile boolean exit = false;

    protected JdbcTemplate jdbcTemplate1;

    protected JdbcTemplate jdbcTemplate2;

    EntityManager entityManager;

    private int time;
    List<SyncBean> list;
    public AutoSyncThread(List<SyncBean> syncBeanList,JdbcTemplate jt1,JdbcTemplate jt2,EntityManager em){
        time = 0;
        list = syncBeanList;
        jdbcTemplate1 = jt1;
        jdbcTemplate2 = jt2;
        entityManager = em;
    }
    @Override
    public void run(){
        while (!exit){

            try {
                Thread.sleep(3600000);//休眠一小时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time ++;
            list.forEach(syncBean -> {
                if(time % syncBean.getSyncRateH() == 0){
                    //开始同步
                    if (SyncThread.jdt.get(syncBean.getSyncTabName())==null || SyncThread.jdt.get(syncBean.getSyncTabName())==100){
                        SyncThread syncThread = new SyncThread(syncBean.getSyncTabName(),syncBean.getDataIndex(),jdbcTemplate1,jdbcTemplate2,entityManager);
                        syncThread.start();
                    }
                }
            });
        }
    }
}
