package com.example.depository_system.service;

import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.MaterialInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MaterialService {
    ServiceBase serviceBase = new ServiceBase();

    public List<MaterialInform> getMaterialList(
            String materialId,
            String materialName,
            String materialModel,
            String factoryName
    ) {
        List<MaterialInform> materialInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (materialId != null && !materialId.isEmpty()) {
                jsonObject.put("goods_id", materialId);
            }
            if (materialName != null && !materialName.isEmpty()) {
                jsonObject.put("goods_name", materialName);
            }
            if (materialModel != null && !materialModel.isEmpty()) {
                jsonObject.put("goods_model", materialModel);
            }
            if (factoryName != null && !factoryName.isEmpty()) {
                jsonObject.put("factory_name", factoryName);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = serviceBase.HttpBase("/getGoodsList", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                MaterialInform materialInform = new MaterialInform();

                materialInform.materialId = singleObject.getString("goods_id");
                materialInform.materialName = singleObject.getString("goods_name");
                materialInform.materialModel = singleObject.getString("goods_model");
                materialInform.factoryName = singleObject.getString("factory_name");

                materialInforms.add(materialInform);
            }

            return materialInforms;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}