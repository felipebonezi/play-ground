package core.forms;

import com.fasterxml.jackson.databind.JsonNode;

public class DataTableResultForm {

    public int recordsTotal;
    public int recordsFiltered;
    public JsonNode data;

    public DataTableResultForm(Integer recordsTotal, Integer recordsFiltered, JsonNode data) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

}
