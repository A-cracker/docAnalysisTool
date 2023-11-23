package com.wtb.javatool.service;

import com.fy.wetoband.tool.AbstractTool;

public interface DocumentService {
    public String getFileServerByFacilityTypeID(String facilityTypeID, String defaultUrl, AbstractTool tool) throws Exception;
}
