package com.example.depository_system.service;

import com.example.depository_system.informs.ProjectInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    public static List<ProjectInform> getProjectList(String projectName) {
        List<ProjectInform> projectInforms = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if(projectName != null && !projectName.isEmpty()) {
                jsonObject.put("project_name", projectName);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String bodyString = ServiceBase.HttpBase("/getProjectInfoList", "POST", null);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject singleObject = dataArray.getJSONObject(i);
                ProjectInform projectInform = new ProjectInform();
                projectInform.projectName = singleObject.getString("project_name");
                projectInform.projectMajor = singleObject.getString("project_major");
                projectInforms.add(projectInform);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return projectInforms;
    }

    public static boolean insertProject(String projectName, String projectMajor) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(projectName != null && !projectName.isEmpty()) {
                jsonObject.put("project_name", projectName);
            }
            if(projectMajor != null && !projectMajor.isEmpty()) {
                jsonObject.put("project_major", projectMajor);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String bodyString = ServiceBase.HttpBase("/insertProject", "POST", jsonObject);
        try {
            JSONObject bodyDict = new JSONObject(bodyString);
            JSONArray dataArray = bodyDict.getJSONArray("data");
            if(dataArray.length() >= 1) return true;
        } catch (JSONException e){
            throw new RuntimeException();
        }
        return false;
    }
}
