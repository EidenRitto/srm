package com.ritto.srm.service.jpa;

import com.ritto.srm.Entity.SyncBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Auther: Eiden J.P Zhou
 * @Date: 2018/7/17
 * @Description:
 * @Modified By:
 */
public interface SyncRepository extends JpaRepository<SyncBean,Integer> {
    @Query(value = "select count(*) from sync", nativeQuery = true)
    public long count();

    @Query(value = "select * from sync s order by id limit (s.page=:page)-1*(s.limit=:limit),s.limit=:limit",nativeQuery = true)
    public List findAllPage(@Param("page") int page, @Param("limit")int limit);
}
