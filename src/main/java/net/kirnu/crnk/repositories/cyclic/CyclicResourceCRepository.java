package net.kirnu.crnk.repositories.cyclic;

import java.util.Collections;

import net.kirnu.crnk.resources.cyclic.CyclicResourceC;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryBase;
import io.crnk.core.resource.list.ResourceList;

/*
 * @author syri.
 */
public class CyclicResourceCRepository extends ResourceRepositoryBase<CyclicResourceC, Long> {

    public CyclicResourceCRepository() {
        super(CyclicResourceC.class);
    }

    @Override
    public ResourceList<CyclicResourceC> findAll(QuerySpec querySpec) {
        return querySpec.apply(Collections.emptyList());
    }
}
