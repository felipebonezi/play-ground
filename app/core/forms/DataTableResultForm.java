package core.forms;

import com.fasterxml.jackson.databind.JsonNode;

public class DataTableResultForm {
  
  private final int      recordsTotal;
  private final int      recordsFiltered;
  private final JsonNode data;
  
  public DataTableResultForm(Integer recordsTotal, Integer recordsFiltered, JsonNode data) {
    this.recordsTotal    = recordsTotal;
    this.recordsFiltered = recordsFiltered;
    this.data            = data;
  }
  
  public int getRecordsTotal() {
    return recordsTotal;
  }
  
  public int getRecordsFiltered() {
    return recordsFiltered;
  }
  
  public JsonNode getData() {
    return data;
  }
  
}
