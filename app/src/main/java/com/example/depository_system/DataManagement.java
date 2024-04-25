package com.example.depository_system;

import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.RukuRecordInform;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.DepositoryService;
import com.example.depository_system.service.MaterialService;
import com.example.depository_system.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class DataManagement {

    public static List<DepositoryInform> depositoryInforms = new ArrayList<>();
    public static List<MaterialInform> materialInforms = new ArrayList<>();
    public static List<UserInform> userInforms = new ArrayList<>();

    public static List<KucunInform> kucunInforms = new ArrayList<>();

    public static List<DepositoryInform> updateDepository() {
        depositoryInforms = DepositoryService.getDepostList(null, null);
        return depositoryInforms;
    }

    public static List<MaterialInform> updateMaterial() {
        materialInforms = MaterialService.getMaterialList(null, null, null, null);
        return materialInforms;
    }

    public static List<UserInform> updateUserInfo() {
        userInforms = UserService.getUserList(null, null, null, null);
        return userInforms;
    }

    public static void updateAll() {
        updateDepository();
        updateMaterial();
        updateUserInfo();
    }
}
