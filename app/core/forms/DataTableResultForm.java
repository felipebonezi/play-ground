package core.forms;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class DataTableResultForm {

    public int recordsTotal;
    public int recordsFiltered;
    public ArrayNode array;

    public DataTableResultForm(Integer recordsTotal, Integer recordsFiltered, ArrayNode array) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.array = array;
    }

}
