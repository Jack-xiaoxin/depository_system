package com.example.depository_system;

import com.example.depository_system.informs.DepartmentInform;
import com.example.depository_system.informs.DepositoryInform;
import com.example.depository_system.informs.KucunInform;
import com.example.depository_system.informs.MaterialInform;
import com.example.depository_system.informs.ProjectInform;
import com.example.depository_system.informs.RukuRecordInform;
import com.example.depository_system.informs.UserInform;
import com.example.depository_system.service.DepartmentService;
import com.example.depository_system.service.DepositoryService;
import com.example.depository_system.service.KucunService;
import com.example.depository_system.service.MaterialService;
import com.example.depository_system.service.ProjectService;
import com.example.depository_system.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class DataManagement {

    public static List<DepositoryInform> depositoryInforms = new ArrayList<>();
    public static List<MaterialInform> materialInforms = new ArrayList<>();
    public static List<UserInform> userInforms = new ArrayList<>();
    public static List<KucunInform> kucunInforms = new ArrayList<>();
    public static List<ProjectInform> projectInforms = new ArrayList<>();
    public static List<DepartmentInform> departmentInforms = new ArrayList<>();

    public static UserInform userInform;

    public static void updateDepository() {
        depositoryInforms = DepositoryService.getDepostList(null, null);
    }

    public static void updateMaterial() {
        materialInforms = MaterialService.getMaterialList(null, null, null, null, null);
    }

    public static void updateUserInfo() {
        userInforms = UserService.getUserList(null, null, null, null);
    }

    public static void updateKucunInfo() {
        kucunInforms = KucunService.getKucunList(null, null, null, null);
    }

    public static void updateProjectInfo() {
        projectInforms = ProjectService.getProjectList(null);
    }

    public static void updateDepartmentInfo() {
        departmentInforms = DepartmentService.getDepartmentList(null);
    }

    public static void updateAll() {
        updateDepository();
        updateMaterial();
        updateUserInfo();
        updateProjectInfo();
        updateKucunInfo();
        updateDepartmentInfo();
    }
}
