package core.repository.dto;

/** Pagination DTO. */
public class Pagination {
  
  private final int offset;
  private final int limit;
  private final String orderBy;
  
  public Pagination(int offset, int limit, String orderBy) {
    this.offset  = offset;
    this.limit   = limit;
    this.orderBy = orderBy;
  }
  
  public int getOffset() {
    return offset;
  }
  
  public int getLimit() {
    return limit;
  }
  
  public String getOrderBy() {
    return orderBy;
  }
  
}
