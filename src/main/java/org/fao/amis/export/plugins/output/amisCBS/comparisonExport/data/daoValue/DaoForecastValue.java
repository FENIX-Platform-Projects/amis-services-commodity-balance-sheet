package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.daoValue;

public class DaoForecastValue {

    private String notes;

    private String flags;

    private double value;

    public DaoForecastValue(String notes, String flags, double value) {
        this.notes = notes;
        this.flags = flags;
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ForecastValueModel{" +
                "notes='" + notes + '\'' +
                ", flags='" + flags + '\'' +
                ", value=" + value +
                '}';
    }
}
