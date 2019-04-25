package core.forms;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class DataTableResultForm {

    public int recordsTotal;
    public int recordsFiltered;
    public ArrayNode data;

    public DataTableResultForm(Integer recordsTotal, Integer recordsFiltered, ArrayNode data) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

}
