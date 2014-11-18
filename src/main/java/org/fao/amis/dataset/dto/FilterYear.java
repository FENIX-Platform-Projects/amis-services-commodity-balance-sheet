package org.fao.amis.dataset.dto;

public class FilterYear
{
    private Integer productCode;
    private Integer regionCode;

    public Integer getProductCode()
    {
        return this.productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    public Integer getRegionCode() {
        return this.regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }
}