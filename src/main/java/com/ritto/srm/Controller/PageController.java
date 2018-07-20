package com.ritto.srm.Controller;

import com.ritto.srm.Entity.SyncBean;
import com.ritto.srm.Util.DBUtil;
import com.ritto.srm.service.jpa.SyncRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Eiden J.P Zhou
 * @Date: 2018/7/18
 * @Description:
 * @Modified By:
 */

@RestController
@RequestMapping("/page")
public class PageController {
    @Autowired
    private SyncRepository syncRepository;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate2;

    @PostMapping("/allpage")
    public Map page(String page){
        int pages = 0;      //待显示页面
        int count = 0;      //总条数
        int totalpages = 0; //总页数
        int limit = 6 ;     //每页显示记录条数

        count = Math.toIntExact(syncRepository.count());
        //由记录总数除以每页记录条数得出总页数
        totalpages=(int)Math.ceil(count/(limit*1.0));
        //获取跳页时传进来的当前页面参数
        String strPage = page;
        //判断当前页面参数的合法性并处理非法页号（为空则显示第一页，
        // 小于0则显示最后一页，大于总数页则显示最后一页）
        if (strPage == null){
            pages = 1;
        }else {
            pages = Integer.parseInt(strPage);
        }
        if (pages < 1 ){
            pages = 1;
        }
        if (pages > totalpages){
            pages = totalpages;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select * from sync order by id limit ");
        sql.append((pages-1)*limit);
        sql.append(" , ");
        sql.append(limit);
        List<SyncBean> listAll = jdbcTemplate1.query(sql.toString(), new Object[]{},new BeanPropertyRowMapper<SyncBean>(SyncBean.class));
        //   List<SyncBean> listAll = jdbcTemplate1.queryForList(sql.toString(),SyncBean.class);
//        List<SyncBean> listAll = syncRepository.findAllPage(pages,limit);
        Map resultmap = new HashMap();
        resultmap.put("result",listAll);
        resultmap.put("currentpage",pages);
        resultmap.put("currentlimit",limit);
        resultmap.put("totalpages",totalpages);
        resultmap.put("count",count);
        return resultmap;
    }
}
