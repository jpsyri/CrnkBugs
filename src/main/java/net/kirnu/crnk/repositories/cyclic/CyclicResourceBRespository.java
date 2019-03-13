package net.kirnu.crnk.repositories.cyclic;

import java.util.Collections;

import net.kirnu.crnk.resources.cyclic.CyclicResourceB;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryBase;
import io.crnk.core.resource.list.ResourceList;

/*
 * @author syri.
 */
public class CyclicResourceBRespository extends ResourceRepositoryBase<CyclicResourceB, Long> {

    public CyclicResourceBRespository() {
        super(CyclicResourceB.class);
    }

    @Override
    public ResourceList<CyclicResourceB> findAll(QuerySpec querySpec) {
        return querySpec.apply(Collections.emptyList());
    }
}
