package net.kirnu.crnk.repositories.related;

import net.kirnu.crnk.repositories.RepositoryData;
import net.kirnu.crnk.resources.related.RelatedResourceB;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryBase;
import io.crnk.core.resource.list.ResourceList;

/*
 * @author syri.
 */
public class RelatedResourceBRepository extends ResourceRepositoryBase<RelatedResourceB, Long> {

    public RelatedResourceBRepository() {
        super(RelatedResourceB.class);
    }

    @Override
    public ResourceList<RelatedResourceB> findAll(QuerySpec querySpec) {
        return querySpec.apply(RepositoryData.RESOURCE_B_LIST);
    }
}
