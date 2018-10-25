package com.ritto.srm.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/7/17
 * @Description:
 * @Modified By:
 */
@Entity
@Table(name = "sync", schema = "vcos_lyg", catalog = "")
public class SyncBean {
    private int id;
    private String syncTabName;
    private Timestamp lastSyncDate;
    private String lastSyncState;
    private Integer syncRateH;
    private Integer dataIndex;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "sync_tab_name")
    public String getSyncTabName() {
        return syncTabName;
    }

    public void setSyncTabName(String syncTabName) {
        this.syncTabName = syncTabName;
    }

    @Basic
    @Column(name = "last_sync_date")
    public Timestamp getLastSyncDate() {
        return lastSyncDate;
    }

    public void setLastSyncDate(Timestamp lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }

    @Basic
    @Column(name = "last_sync_state")
    public String getLastSyncState() {
        return lastSyncState;
    }

    public void setLastSyncState(String lastSyncState) {
        this.lastSyncState = lastSyncState;
    }

    @Basic
    @Column(name = "sync_rate_h")
    public Integer getSyncRateH() {
        return syncRateH;
    }

    public void setSyncRateH(Integer syncRateH) {
        this.syncRateH = syncRateH;
    }

    @Basic
    @Column(name = "data_index")
    public Integer getDataIndex(){return dataIndex;}

    public void setDataIndex(Integer dataIndex){this.dataIndex = dataIndex;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyncBean syncBean = (SyncBean) o;
        return id == syncBean.id &&
                Objects.equals(syncTabName, syncBean.syncTabName) &&
                Objects.equals(lastSyncDate, syncBean.lastSyncDate) &&
                Objects.equals(lastSyncState, syncBean.lastSyncState) &&
                Objects.equals(syncRateH, syncBean.syncRateH) &&
                Objects.equals(dataIndex, syncBean.dataIndex);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, syncTabName, lastSyncDate, lastSyncState, syncRateH, dataIndex);
    }
}
