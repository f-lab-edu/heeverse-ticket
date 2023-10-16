package com.heeverse.common;

public interface DataSourceMonitoringMBean {

    int getnumActive();
    int getnumIdle();
    int getmaxTotal();

}