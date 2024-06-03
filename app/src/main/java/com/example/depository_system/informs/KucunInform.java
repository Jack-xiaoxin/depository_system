package com.example.depository_system.informs;

import com.example.depository_system.DataManagement;

public class KucunInform {

    public String kucunId;

    public String materialId;

    public Integer kucunNumber;

    public Integer alarmNumber;

    public String updateTime;

    public String depotId;

    public String depotName;

    public String projectId;

    public String projectName;

    public String materialName;

    public String materialIdentifier;

    public String materialType;

    public String materialUnit;

    public String factoryName;

    public void getMaterialInfo() {
        for(MaterialInform materialInform : DataManagement.materialInforms) {
            if(materialInform.materialId.equals(this.materialId)) {
                this.materialIdentifier = materialInform.materialIdentifier;
                this.materialName = materialInform.materialName;
                this.materialType = materialInform.materialModel;
                this.factoryName = materialInform.factoryName;
                this.materialUnit = materialInform.materialUnit;
                break;
            }
        }
    }

    public void getDepositoryInfo() {
        for(DepositoryInform depositoryInform : DataManagement.depositoryInforms) {
            if(depositoryInform.depotId.equals(this.depotId)) {
                this.depotName = depositoryInform.depotName;
                break;
            }
        }
    }

    public void getProjectInfo() {
        for(ProjectInform projectInform : DataManagement.projectInforms) {
            if(projectInform.projectId.equals(this.projectId)) {
                this.projectName = projectInform.projectName;
                break;
            }
        }
    }
}
