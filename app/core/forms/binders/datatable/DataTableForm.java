package core.forms.binders.datatable;

import static com.google.common.base.Strings.isNullOrEmpty;
import static core.utils.StringUtil.COMMA;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import play.mvc.QueryStringBindable;

public class DataTableForm implements QueryStringBindable<DataTableForm> {
  
  private int draw   = 0;
  private int start  = 0;
  private int length = 10;
  
  private DataTableSearchForm   search;
  private DataTableOrderForm[]  orders;
  private DataTableColumnForm[] columns;
  
  // region Getters & Setters.
  public int getDraw() {
    return draw;
  }
  
  public void setDraw(int draw) {
    this.draw = draw;
  }
  
  public int getStart() {
    return start;
  }
  
  public void setStart(int start) {
    this.start = start;
  }
  
  public int getLength() {
    return length;
  }
  
  public void setLength(int length) {
    this.length = length;
  }
  
  public DataTableSearchForm getSearch() {
    return search;
  }
  
  public void setSearch(DataTableSearchForm search) {
    this.search = search;
  }
  
  public DataTableOrderForm[] getOrders() {
    return orders;
  }
  
  public void setOrders(DataTableOrderForm[] orders) {
    this.orders = orders;
  }
  
  public DataTableColumnForm[] getColumns() {
    return columns;
  }
  
  public void setColumns(DataTableColumnForm[] columns) {
    this.columns = columns;
  }
  // endregion
  
  public boolean isFiltered() {
    return this.search != null && !isNullOrEmpty(this.search.getValue());
  }
  
  public String getOrderBy(String defaultOrder) {
    return this.orders != null && this.orders.length > 0 ?
           Arrays.stream(this.orders)
               .map(DataTableOrderForm::getDir).collect(joining(COMMA)) : defaultOrder;
  }
  
  @Override
  public Optional<DataTableForm> bind(String key, Map<String, String[]> data) {
    this.search = new DataTableSearchForm();
    
    Map<Integer, DataTableOrderForm> orderMap = new HashMap<>();
    Pattern orderPattern = Pattern.compile(
        "orders\\[(\\d)]\\.([a-zA-Z])");
    
    Map<Integer, DataTableColumnForm> columnMap = new HashMap<>();
    Pattern columnPattern = Pattern.compile(
        "columns\\[(\\d)]\\.([a-zA-Z])");
    
    for (Map.Entry<String, String[]> entry : data.entrySet()) {
      String   entryKey   = entry.getKey();
      String[] entryValue = entry.getValue();
      switch (entryKey) {
        case "draw":
          this.draw = parseInt(entryValue[0]);
          break;
        case "start":
          this.start = parseInt(entryValue[0]);
          break;
        case "length":
          this.length = parseInt(entryValue[0]);
          break;
        
        default:
          if (entryKey.startsWith("search")) {
            bindSearchForm(entryKey, entryValue);
          } else if (entryKey.startsWith("orders[")) {
            bindOrderForm(orderMap, orderPattern, entryKey, entryValue);
          } else if (entryKey.startsWith("columns[")) {
            bindColumnForm(columnMap, columnPattern, entryKey, entryValue);
          }
          break;
      }
    }
    return Optional.of(this);
  }
  
  private void bindColumnForm(Map<Integer, DataTableColumnForm> columnMap, Pattern columnPattern, String entryKey, String[] entryValue) {
    Matcher matcher = columnPattern.matcher(entryKey);
    int     idx     = parseInt(matcher.group(1));
    
    DataTableColumnForm column;
    if (columnMap.containsKey(idx)) {
      column = columnMap.get(idx);
    } else {
      column = new DataTableColumnForm();
    }
    columnMap.put(idx, column);
    
    switch (matcher.group(2)) {
      case "name":
        column.setName(entryValue[0]);
        break;
      case "data":
        column.setData(entryValue[0]);
        break;
      default:
        break;
    }
  }
  
  private void bindOrderForm(Map<Integer, DataTableOrderForm> orderMap, Pattern orderPattern, String entryKey, String[] entryValue) {
    Matcher matcher = orderPattern.matcher(entryKey);
    int     idx     = parseInt(matcher.group(1));
    
    DataTableOrderForm order;
    if (orderMap.containsKey(idx)) {
      order = orderMap.get(idx);
    } else {
      order = new DataTableOrderForm();
    }
    orderMap.put(idx, order);
    
    switch (matcher.group(2)) {
      case "column":
        order.setColumn(parseInt(entryValue[0]));
        break;
      case "dir":
        order.setDir(entryValue[0]);
        break;
      default:
        break;
    }
  }
  
  private void bindSearchForm(String entryKey, String[] entryValue) {
    switch (entryKey) {
      case "search.regex":
        this.search.setRegex(parseBoolean(entryValue[0]));
        break;
      case "search.value":
        this.search.setValue(entryValue[0]);
        break;
      
      default:
        break;
    }
  }
  
  @Override
  public String unbind(String key) {
    return toString();
  }
  
  @Override
  public String javascriptUnbind() {
    return toString();
  }
  
  @Override
  public String toString() {
    return format("draw=%d&start=%d&length=%d", this.draw, this.start, this.length) +
        // Search.
        (this.search == null ? "" : format("&search.regex=%s&search.value=%s",
            this.search.isRegex(), this.search.getValue())) +
        // Orders.
        (this.orders == null || this.orders.length == 0 ? "" : "&" + IntStream.range(0,
                this.orders.length)
            .mapToObj(idx -> format("orders[%d].column=%d&orders[%d].dir=%s", idx,
                this.orders[idx].getColumn(), idx, this.orders[idx].getDir()))
            .collect(joining("&"))) +
        // Columns.
        (this.columns == null || this.columns.length == 0 ? "" : "&" + IntStream.range(0,
                this.columns.length)
            .mapToObj(idx -> format("columns[%d].name=%s&columns[%d].data=%s", idx,
                this.columns[idx].getName(), idx, this.columns[idx].getData()))
            .collect(joining("&")))
        ;
  }
  
}
