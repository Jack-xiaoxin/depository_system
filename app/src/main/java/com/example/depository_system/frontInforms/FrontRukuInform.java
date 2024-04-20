package com.example.depository_system.frontInforms;

import com.example.depository_system.informs.RukuInform;

import java.util.List;

public class FrontRukuInform {

    public String depotName;

    public String materialIdentifier;

    public String materialName;

    public String materialType;

    public int materialNum;

    public String factoryName;

    public String time;

    public String project;

    public String receiver;

    public String accepter;

    public List<String> images;

    public RukuInform convetToRukuInform() {
        RukuInform rukuInform = new RukuInform();
        rukuInform.depotName = depotName;
        rukuInform.materialIdentifer = materialIdentifier;
        rukuInform.materialModel = materialType;
        rukuInform.number = materialNum;
        rukuInform.factoryName = factoryName;
        rukuInform.time = time;
        rukuInform.projectName = project;
        rukuInform.receiver = receiver;
        rukuInform.acceptor = accepter;
        rukuInform.images = images;
        return rukuInform;
    }
}
