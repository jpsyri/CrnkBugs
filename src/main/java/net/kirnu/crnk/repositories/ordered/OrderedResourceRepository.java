package net.kirnu.crnk.repositories.ordered;

import net.kirnu.crnk.repositories.RepositoryData;
import net.kirnu.crnk.resources.ordered.OrderedResource;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryBase;
import io.crnk.core.resource.list.ResourceList;

/*
 * @author syri.
 */
public class OrderedResourceRepository extends ResourceRepositoryBase<OrderedResource, Long> {

    public OrderedResourceRepository() {
        super(OrderedResource.class);
    }

    @Override
    public ResourceList<OrderedResource> findAll(QuerySpec querySpec) {
        return querySpec.apply(RepositoryData.ORDERED_RESOURE_LIST);
    }
}
