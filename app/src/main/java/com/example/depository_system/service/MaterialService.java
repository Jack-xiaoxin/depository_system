package com.example.depository_system.service;

import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.MaterialInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MaterialService {

    public static List<MaterialInform> getMaterialList(
            String materialId,
            String materialName,
            String materialModel,
            String materialIdentifier,
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
            if (materialIdentifier != null && !materialIdentifier.isEmpty()) {
                jsonObject.put("goods_identifier", materialIdentifier);
            }
            if (factoryName != null && !factoryName.isEmpty()) {
                jsonObject.put("factory_name", factoryName);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String bodyString = ServiceBase.HttpBase("/getGoodsList", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                MaterialInform materialInform = new MaterialInform();

                materialInform.materialId = singleObject.getString("goods_id");
                materialInform.materialName = singleObject.getString("goods_name");
                materialInform.materialModel = singleObject.getString("goods_model");
                materialInform.materialIdentifier = singleObject.getString("goods_identifier");
                materialInform.factoryName = singleObject.getString("factory_name");

                materialInforms.add(materialInform);
            }

            return materialInforms;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertMaterial(
            String materialName,
            String materialIdentifier,
            String materialType,
            String factoryName) {
        if(materialName == null || materialName.isEmpty()) return false;
        if(materialIdentifier == null || materialIdentifier.isEmpty()) return false;
        if(materialType == null || materialType.isEmpty()) return false;
        if(factoryName == null || factoryName.isEmpty()) return false;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("goods_name", materialName);
            jsonObject.put("goods_model", materialType);
            jsonObject.put("goods_identifier", materialIdentifier);
            jsonObject.put("factory_name", factoryName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodyString = ServiceBase.HttpBase("/insertGoods", "POST", jsonObject);
        if(bodyString == null || bodyString.isEmpty()) return false;
        return true;
    }
}