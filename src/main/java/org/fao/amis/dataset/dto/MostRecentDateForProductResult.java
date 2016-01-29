package org.fao.amis.dataset.dto;

public class MostRecentDateForProductResult {

    private String date;

    private Integer productCode;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }
}
