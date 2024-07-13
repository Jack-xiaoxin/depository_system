package com.example.depository_system.informs;

import android.net.Uri;

import java.util.List;

public class ChukuRecordItemInform {

    public String id;

    public String materialId;

    public String materialName;

    public String materialModel;

    public String materialIdentifier;

    public String materialUnit;

    public String factoryName;

    public String applier;

    public String outboundTime;

    public double number;

    public String projectName;

    public String projectMajor;

    public String departmentName;

    public String depository_id;

    public String depositoryName;
    public String time;

    public List<String> images;
    public List<Uri> imageUriList;

    public String outboundIdentifier;

    public String outboundItemId;

    public ChukuActionInform toChukuActionInform() {
        ChukuActionInform chukuActionInform = new ChukuActionInform();
        chukuActionInform.applier = applier;
        chukuActionInform.applyDepartmentName = departmentName;
        chukuActionInform.applyProjectName = projectName;
        chukuActionInform.depotId = depository_id;
        chukuActionInform.director = projectMajor;
        chukuActionInform.factoryName = factoryName;
        chukuActionInform.images = images;
        chukuActionInform.materialId = materialId;
        chukuActionInform.materialIdentifier = materialIdentifier;
        chukuActionInform.materialUnit = materialUnit;
        chukuActionInform.number = number;
        chukuActionInform.outboundIdentifier = outboundIdentifier;
        chukuActionInform.time = time;
        chukuActionInform.outboundItemId = outboundItemId;
        return chukuActionInform;
    }
}
