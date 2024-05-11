package com.example.depository_system.service;

import com.example.depository_system.informs.DepartmentInform;
import com.example.depository_system.informs.DepositoryInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    public static List<DepartmentInform> getDepartmentList(String departmentName) {
        List<DepartmentInform> departmentInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if(departmentName != null && !departmentName.isEmpty()) {
                jsonObject.put("department_name", departmentName);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String bodyString = ServiceBase.HttpBase("/getDepartmentInfoList", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                DepartmentInform departmentInform = new DepartmentInform();
                departmentInform.department_name = singleObject.getString("department_name");
                departmentInforms.add(departmentInform);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return departmentInforms;
    }

}
