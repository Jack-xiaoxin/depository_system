package com.example.depository_system.service;

import com.example.depository_system.informs.RukuInform;

import org.json.JSONException;
import org.json.JSONObject;

public class RukuService {
    public String action(RukuInform rukuInform){
        ServiceBase serviceBase = new ServiceBase();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("new_goods", rukuInform.isNew);
            jsonObject.put("goods_id", rukuInform.materialIdentifer);
            jsonObject.put("goods_name", rukuInform.materialName);
            jsonObject.put("goods_model",rukuInform.materialModel);
            jsonObject.put("depository_name", rukuInform.depotName);
            jsonObject.put("factory_name", rukuInform.factoryName);
            jsonObject.put("receiver", rukuInform.receiver);
            jsonObject.put("checker", rukuInform.acceptor);
            jsonObject.put("project_name", rukuInform.projectName);
            jsonObject.put("goods_number", rukuInform.number);
            jsonObject.put("images", rukuInform.images);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String response = serviceBase.HttpBase("/inbound", "POST", jsonObject);
        return response;
    }

//    public
}
