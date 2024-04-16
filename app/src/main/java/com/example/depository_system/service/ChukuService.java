package com.example.depository_system.service;

import com.example.depository_system.informs.ChukuActionInform;
import com.example.depository_system.informs.ChukuRecordInform;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class ChukuService {

    public String action(ChukuActionInform chukuInform){
        ServiceBase serviceBase = new ServiceBase();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deposit_id", chukuInform.depotId);
            jsonObject.put("goods_id", chukuInform.materialId);
            jsonObject.put("applier", chukuInform.applier);
            jsonObject.put("apply_department_name",chukuInform.applyDepartmentName);
            jsonObject.put("apply_project_name", chukuInform.applyProjectName);
            jsonObject.put("director", chukuInform.director);
            jsonObject.put("goods_number", chukuInform.number);
            jsonObject.put("images", chukuInform.images);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String response = serviceBase.HttpBase("/outbound", "POST", jsonObject);
        return response;
    }

//    public List<ChukuRecordInform> getChukuRecordListByDateOrFactory(String date, String factoryName) {
//
//    }
}
