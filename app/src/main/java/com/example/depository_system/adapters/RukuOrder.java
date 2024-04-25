package com.example.depository_system.adapters;

import com.example.depository_system.informs.RukuRecordInform;

public class RukuOrder {

//    public String imagePath;
    public String orderId;
    public String factoryName;
    public String time;

    public RukuOrder(RukuRecordInform rukuRecordInform) {
        factoryName = rukuRecordInform.factoryName;
        orderId = rukuRecordInform.inboundId;
        time = rukuRecordInform.inboundDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public String getTime() {
        return time;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
