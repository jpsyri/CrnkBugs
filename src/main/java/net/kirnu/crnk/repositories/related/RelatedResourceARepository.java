package net.kirnu.crnk.repositories.related;

import net.kirnu.crnk.repositories.RepositoryData;
import net.kirnu.crnk.resources.related.RelatedResourceA;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryBase;
import io.crnk.core.resource.list.ResourceList;

/*
 * @author syri.
 */
public class RelatedResourceARepository extends ResourceRepositoryBase<RelatedResourceA, Long> {

    public RelatedResourceARepository() {
        super(RelatedResourceA.class);
    }

    @Override
    public ResourceList<RelatedResourceA> findAll(QuerySpec querySpec) {
        return querySpec.apply(RepositoryData.RESOURCE_A_LIST);
    }
}
