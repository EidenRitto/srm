
package com.ritto.srm.controller;

import com.ritto.srm.entity.CpuBean;
import com.ritto.srm.entity.SyncBean;
import com.ritto.srm.entity2.CpuBeanBack;
import com.ritto.srm.thread.AutoSyncThread;
import com.ritto.srm.thread.SyncThread;
import com.ritto.srm.util.ObjectConvertor;
import com.ritto.srm.service.jpa.SyncRepository;
import com.ritto.srm.service.jpa.cpuRepository;
import com.ritto.srm.service.jpa2.cpuRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/4/24
 * @Description:
 * @Modified By:
 */
@RequestMapping("/list")
@RestController
public class ListController {
    @PersistenceContext //注入的是实体管理器,执行持久化操作
    EntityManager entityManager;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate2;

    @Autowired
    private SyncRepository syncRepository;

    @Autowired
    private cpuRepository cpuRepository;

    @Autowired
    private cpuRepository2 cpuRepository2;

    private ObjectConvertor convertor;

    public AutoSyncThread autoSyncThread;

    @GetMapping("/dothis")
    public String dothis(){
        convertor = new ObjectConvertor();
        List<CpuBean> cpuBeanList = cpuRepository.findAll();
        cpuBeanList.forEach(cpuBean -> {
            //连接另外一个数据库，然后持久化这个输出
            CpuBeanBack cpuBeanBack = convertor.toAnotherObj(cpuBean, CpuBeanBack.class);
            //断言不为空

            cpuRepository2.save(cpuBeanBack);
        });
        return "";
    }

    /**
     * @author : Eiden J.P Zhou
     * @Date: 2018/7/17 17:35
     * @Method: findallsync
     * @Params: []
     * @Return: java.util.List<com.ritto.srm.entity.SyncBean>
     * @Description: 查找全部同步计划
     */
    @PostMapping("/findallsync")
    public List<SyncBean> findallsync(){
        List<SyncBean> syncBeanList = syncRepository.findAll();
        return syncBeanList;
    }

    @PostMapping("/findalltab")
    public List findAlltable(){

        List tablelist = entityManager.createNativeQuery("select table_name from information_schema.tables where table_schema='Hotel_manage'").getResultList();
        return tablelist;
    }

    
    /**
     * @author : Eiden J.P Zhou
     * @Date: 2018/7/17 17:22
     * @Method: addSyncTab
     * @Params: [sb]
     * @Return: java.lang.String
     * @Description: 将一张表加入同步计划
     */
    @PostMapping("/addsynctab")
    public String addSyncTab(SyncBean sb){
        String result = "fail";
        sb.setLastSyncDate(new Timestamp(new Date().getTime()));
        sb.setLastSyncState("未同步");
        sb.setDataIndex(0);
        if (null != syncRepository.save(sb)){
            List<SyncBean> beanList = syncRepository.findAll();
            if (autoSyncThread != null){
                autoSyncThread.exit = true;
            }
            autoSyncThread = new AutoSyncThread(beanList,jdbcTemplate1,jdbcTemplate2,entityManager);
            autoSyncThread.start();
            result = "success";
        }
        return result;
    }

    /**
     * 通过表名查找条数（判断表名是否存在）
     * @param tabname
     * @return
     */
    @PostMapping("/exitbytabname")
    public String exitTabBytabname(String tabname){
        String result = "fail";
        if (null != syncRepository.findBySyncTabName(tabname)){
            result = "success";
        }
        return result;
    }

    /**
     * @author : Eiden J.P Zhou
     * @Date: 2018/7/25 16:04
     * @Method: synctab
     * @Params: [tabname]
     * @Return: java.lang.String
     * @Description: 同步一张表
     */
    @PostMapping("/synctab")
    public String synctab(String tabname){
        //判断是不是第一次进来 如果不是，那么判断是不是已经在同步了，是的话就返回
        if (SyncThread.jdt.get(tabname)!=null && SyncThread.jdt.get(tabname)!=100){
            return "exist";
        }
        int index = syncRepository.findBySyncTabName(tabname).getDataIndex();
        //判断是否三空，和上次同步是否完成 ，是就开始同步
        if(SyncThread.jdt==null||SyncThread.jdt.size()==0||SyncThread.jdt.get(tabname)==null||SyncThread.jdt.get(tabname)==100){
            SyncThread syncThread = new SyncThread(tabname,index,jdbcTemplate1,jdbcTemplate2,entityManager);
            syncThread.start();
        }
        else {
            return "fail";
        }
        return "success";
    }

    @GetMapping("/jdt")
    public String jdt(String tabname){
        return SyncThread.jdt.get(tabname).toString();
    }

    /**
     * 删除同步表（不删除原来的表，仅仅在同步记录表中删除）
     * @param id
     * @return
     */
    @PostMapping("/delettab")
    public String deleteTab(String id){
        int sid = Integer.parseInt(id);
        String result = "success";
        try{
            syncRepository.deleteById(sid);
        }catch (Exception e){
            result = "fail";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过id查找表信息
     * @param id
     * @return
     */
    @PostMapping("/findtabbyid")
    public String findTabByid(String id){
        int sid = Integer.parseInt(id);
        String result = "success";
        try{
            syncRepository.findById(sid);
        }catch (Exception e){
            result = "fail";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新表同步频率
     * @param time
     * @param id
     * @return
     */
    @Transactional
    @PostMapping("/updatetime")
    public String updateTabByid(String time,String id){
        int sid = Integer.parseInt(id);
        int stime = Integer.parseInt(time);
        String result = "";
        SyncBean syncBean =  syncRepository.findById(sid).get();
        syncBean.setSyncRateH(stime);
        if (syncRepository.save(syncBean)!=null){
            result = "success";
        }else{
            result = "fail";
        }

        return result;
    }
    /**
     * 勾选多个删除同步表
     * @param ids
     * @return
     */
    @PostMapping("/deletselecttab")
    public String deletselecttab(String [] ids){
        String result = "success";
        try{
            for (int i = 0; i < ids.length; i++) {
                syncRepository.deleteById(Integer.parseInt(ids[i]));
            }
        }catch (Exception e){
            result = "fail";
        }
        return result;

    }
}
