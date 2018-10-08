package com.ritto.srm.entity2;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/7/13
 * @Description:
 * @Modified By:
 */
@Entity
@Table(name = "cpu", schema = "Hotel_manage")
public class CpuBeanBack {
    private int cpuId;
    private String brand;
    private String name;
    private Integer core;
    private Integer threds;
    private String baseClock;
    private String turboClock;

    @Id
    @Column(name = "cpu_id")
    public int getCpuId() {
        return cpuId;
    }

    public void setCpuId(int cpuId) {
        this.cpuId = cpuId;
    }

    @Basic
    @Column(name = "brand")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "core")
    public Integer getCore() {
        return core;
    }

    public void setCore(Integer core) {
        this.core = core;
    }

    @Basic
    @Column(name = "threds")
    public Integer getThreds() {
        return threds;
    }

    public void setThreds(Integer threds) {
        this.threds = threds;
    }

    @Basic
    @Column(name = "base_clock")
    public String getBaseClock() {
        return baseClock;
    }

    public void setBaseClock(String baseClock) {
        this.baseClock = baseClock;
    }

    @Basic
    @Column(name = "turbo_clock")
    public String getTurboClock() {
        return turboClock;
    }

    public void setTurboClock(String turboClock) {
        this.turboClock = turboClock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CpuBeanBack cpuBean = (CpuBeanBack) o;
        return cpuId == cpuBean.cpuId &&
                Objects.equals(brand, cpuBean.brand) &&
                Objects.equals(name, cpuBean.name) &&
                Objects.equals(core, cpuBean.core) &&
                Objects.equals(threds, cpuBean.threds) &&
                Objects.equals(baseClock, cpuBean.baseClock) &&
                Objects.equals(turboClock, cpuBean.turboClock);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cpuId, brand, name, core, threds, baseClock, turboClock);
    }
}
