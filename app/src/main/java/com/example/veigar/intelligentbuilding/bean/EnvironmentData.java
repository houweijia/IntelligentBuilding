package com.example.veigar.intelligentbuilding.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by veigar on 2017/3/11.
 */

public class EnvironmentData implements Serializable{
    private List<String> nodedata;
    public void setNodedata(List<String> nodedata) {
        this.nodedata = nodedata;
    }
    public List<String> getNodedata() {
        return nodedata;
    }

    private List<String> camera;

    public List<String> getCamera() {
        return camera;
    }

    public void setCamera(List<String> camera) {
        this.camera = camera;
    }
}
