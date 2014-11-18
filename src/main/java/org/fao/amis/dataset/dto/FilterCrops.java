package org.fao.amis.dataset.dto;

public class FilterCrops
{
    private String regionCode;
    private String productCode;

    public String getRegionCode()
    {
        return this.regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}