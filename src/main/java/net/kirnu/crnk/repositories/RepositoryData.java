package net.kirnu.crnk.repositories;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.kirnu.crnk.resources.ordered.OrderedResource;
import net.kirnu.crnk.resources.related.RelatedResourceA;
import net.kirnu.crnk.resources.related.RelatedResourceAsub1;
import net.kirnu.crnk.resources.related.RelatedResourceB;

/*
 * @author syri.
 */
public class RepositoryData {
    public static final List<RelatedResourceA> RESOURCE_A_LIST;
    public static final List<RelatedResourceB> RESOURCE_B_LIST;

    public static final List<OrderedResource> ORDERED_RESOURE_LIST;

    static {
        RelatedResourceAsub1 relatedResourceAsub1 = new RelatedResourceAsub1();
        relatedResourceAsub1.setId(1L);

        RelatedResourceAsub1 relatedResourceAsub2 = new RelatedResourceAsub1();
        relatedResourceAsub2.setId(2L);

        RelatedResourceB relatedResourceB = new RelatedResourceB();
        relatedResourceB.setId(10L);

        relatedResourceAsub1.setRelatedResourceBS(Collections.singletonList(relatedResourceB));
        relatedResourceB.setRelatedResourceAsub1(relatedResourceAsub1);

        RESOURCE_A_LIST = Collections.unmodifiableList(Arrays.asList(relatedResourceAsub1, relatedResourceAsub2));
        RESOURCE_B_LIST = Collections.singletonList(relatedResourceB);


        ORDERED_RESOURE_LIST = Collections.unmodifiableList(
            Arrays.asList(
                new OrderedResource(1, 5),
                new OrderedResource(2, 2),
                new OrderedResource(3, 7)
            )
        );
    }
}
