package com.example.depository_system.informs;

import com.example.depository_system.DataManagement;
import com.example.depository_system.R;

import java.util.List;

public class RukuRecordItemInform {

    public String id;

    public String materialId;

    public String materialName;

    public String materialModel;

    public String materialIdentifier;

    public String materialUnit;

    public String factoryName;

    public String receiver;

    public String checker;

    public String projectName;

    public String inboundTime;

    public double number;
    public String depositoryId;
    public String inboundIdentifier;
    public String inboundItemId;

    public List<String> images;

    public RukuInform toRukuInform() {
        RukuInform rukuInform = new RukuInform();
        rukuInform.materialIdentifier = this.materialIdentifier;
        rukuInform.materialName = this.materialName;
        rukuInform.materialModel = this.materialModel;
        rukuInform.materialUnit = this.materialUnit;
        rukuInform.factoryName = this.factoryName;
        rukuInform.receiver = this.receiver;
        rukuInform.acceptor = this.checker;
        rukuInform.time = this.inboundTime;
        rukuInform.projectName = this.projectName;
        for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
            if(depositoryInform.depotId.equals(this.depositoryId)) {
                rukuInform.depotName = depositoryInform.depotName;
            }
        }
        rukuInform.depotId = this.depositoryId;
        rukuInform.images = this.images;
        rukuInform.number = this.number;
        rukuInform.inboundIdentifier = this.inboundIdentifier;
        rukuInform.materialId = this.materialId;
        rukuInform.inboundItemId = this.inboundItemId;

        return rukuInform;
    }

}