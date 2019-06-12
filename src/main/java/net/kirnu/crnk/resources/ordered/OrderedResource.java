package net.kirnu.crnk.resources.ordered;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;

/*
 * @author syri.
 */
@JsonApiResource(type = "orered-resource")
public class OrderedResource {

    @JsonApiId
    private long id;

    private long orderNumber;

    public OrderedResource() {
    }

    public OrderedResource(long id, long orderNumber) {
        this.id = id;
        this.orderNumber = orderNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }
}
