package com.ritto.srm.service.jpa;

import com.ritto.srm.entity.SyncBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/7/17
 * @Description:
 * @Modified By:
 */
public interface SyncRepository extends JpaRepository<SyncBean,Integer> {
    @Query(value = "select count(*) from sync", nativeQuery = true)

    @Override
    public long count();

    public SyncBean findBySyncTabName(String tabname);

}
