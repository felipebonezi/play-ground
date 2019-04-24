package core.forms.binders.datatable;

import com.google.common.base.Strings;
import core.utils.StringUtil;
import play.data.validation.Constraints;

import java.util.List;
import java.util.stream.Collectors;

public class DataTableForm {

    @Constraints.Min(1)
    @Constraints.Required
    public int draw;

    @Constraints.Min(0)
    @Constraints.Required
    public int start = 0;

    @Constraints.Min(1)
    @Constraints.Required
    public int length = 10;

    public DataTableSearchForm search;
    public List<DataTableOrderForm> orders;
    public List<DataTableColumnForm> columns;

    public String toCacheKey() {
        if (isFiltered()) {
            return "[" +
                    "draw=" + this.draw
                    + "-start=" + this.start
                    + "-length=" + this.length
                    + "-searchValue=" + this.search.value
                    + "]";
        } else {
            return "[" +
                    "draw=" + this.draw
                    + "-start=" + this.start
                    + "-length=" + this.length
                    + "]";
        }
    }

    public boolean isFiltered() {
        return this.search != null && !Strings.isNullOrEmpty(this.search.value);
    }

    public String getOrderBy(String defaultOrder) {
        return this.orders != null && !this.orders.isEmpty() ? String.join(StringUtil.COMMA, this.orders.stream().map(o -> o.dir).collect(Collectors.toList())) : defaultOrder;
    }

}
