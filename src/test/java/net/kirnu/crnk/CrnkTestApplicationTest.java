package net.kirnu.crnk;

import org.junit.ClassRule;
import org.junit.Test;

import net.kirnu.crnk.resources.cyclic.CyclicResourceA;
import net.kirnu.crnk.resources.ordered.OrderedResource;
import net.kirnu.crnk.resources.related.RelatedResourceA;
import net.kirnu.crnk.resources.related.RelatedResourceAsub1;

import io.crnk.client.CrnkClient;
import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.PathSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.queryspec.pagingspec.NumberSizePagingSpec;
import io.crnk.core.resource.list.ResourceList;
import io.dropwizard.testing.junit.DropwizardClientRule;

import static org.junit.Assert.*;

/*
 * @author syri.
 */
public class CrnkTestApplicationTest {

    @ClassRule
    public static final DropwizardClientRule clientRule = new DropwizardClientRule(CrnkTestApplication.getCrnkModule());

    /*

    When fetching ResourceA, the response is something like:

        {
          "data": [
            {
              "id": "1",
              "type": "related-resource-a-sub-1",
              "attributes": {
                "type": "SUB1"
              },
              "relationships": {
                "related-resource-b": {
                  "links": {
                    "self": "http://localhost:51452/application/related-resource-a/1/relationships/related-resource-b",
                    "related": "http://localhost:51452/application/related-resource-a/1/related-resource-b"
                  }
                }
              },
              "links": {
                "self": "http://localhost:51452/application/related-resource-a/1"
              }
            }
          ]
        }

    When trying to manipulate the relation, client lazily fetches relation data by using link from the relationship
    info, which results an exception:

        io.crnk.core.exception.BadRequestException: field not found: related-resource-b
        at io.crnk.core.engine.internal.dispatcher.path.PathBuilder.parseFieldPath(PathBuilder.java:154)
        at io.crnk.core.engine.internal.dispatcher.path.PathBuilder.parseIdPath(PathBuilder.java:119)
        at io.crnk.core.engine.internal.dispatcher.path.PathBuilder.parseResourcePath(PathBuilder.java:113)
        at io.crnk.core.engine.internal.dispatcher.path.PathBuilder.build(PathBuilder.java:97)
        at io.crnk.core.engine.internal.http.JsonApiRequestProcessorBase.getJsonPath(JsonApiRequestProcessorBase.java:104)
        at io.crnk.core.engine.internal.http.JsonApiRequestProcessor.accepts(JsonApiRequestProcessor.java:85)
        at io.crnk.core.engine.internal.http.HttpRequestDispatcherImpl.process(HttpRequestDispatcherImpl.java:73)
        at io.crnk.rs.CrnkFilter.filter(CrnkFilter.java:49)
            ...
     */
    @Test
    public void testInheritedRelation() {
        CrnkClient client = getCrnkClient();

        QuerySpec querySpec = new QuerySpec(RelatedResourceA.class);
        RelatedResourceA resource = client.getRepositoryForType(RelatedResourceA.class).findOne(1L, querySpec);
        RelatedResourceAsub1 subResource = (RelatedResourceAsub1)resource;
        assertEquals(1, subResource.getRelatedResourceBS().size());
    }

    // -----------------------------------------------------------------------------------------------------------------
    /*
        When we have resources with following properties:
            - CyclicResourceA has relation to CyclicResourceC (and vice versa)
            - CyclicResourceASub1 (subclass of CyclicResourceA) has relation to CyclicResourceB (and vice versa)
            - CyclicResourceB has relation to CyclicResourceC (and vice versa)

         Trying to construct repository from CrnkClient for CyclicResourceA results in exception:
            java.lang.IllegalStateException: resourceType 'resource-a' already exists, cannot add entry io.crnk.core.engine.internal.registry.LegacyRegistryEntry@52bd9a27
                at io.crnk.core.engine.internal.utils.PreconditionUtil.fail(PreconditionUtil.java:50)
                at io.crnk.core.engine.internal.utils.PreconditionUtil.verify(PreconditionUtil.java:131)
                at io.crnk.core.engine.registry.DefaultResourceRegistryPart.addEntry(DefaultResourceRegistryPart.java:47)
                at io.crnk.core.engine.internal.registry.ResourceRegistryImpl.addEntry(ResourceRegistryImpl.java:215)
                at io.crnk.core.engine.internal.registry.ResourceRegistryImpl.addEntry(ResourceRegistryImpl.java:54)
                at io.crnk.client.CrnkClient.allocateRepository(CrnkClient.java:387)
                at io.crnk.client.CrnkClient.allocateRepositoryRelation(CrnkClient.java:419)
                at io.crnk.client.CrnkClient.allocateRepositoryRelations(CrnkClient.java:408)
                at io.crnk.client.CrnkClient.allocateRepository(CrnkClient.java:389)
                at io.crnk.client.CrnkClient.allocateRepositoryRelation(CrnkClient.java:419)
                at io.crnk.client.CrnkClient.allocateRepositoryRelations(CrnkClient.java:408)
                at io.crnk.client.CrnkClient.allocateRepository(CrnkClient.java:389)
                at io.crnk.client.CrnkClient.allocateRepositoryRelation(CrnkClient.java:419)
                at io.crnk.client.CrnkClient.allocateRepositoryRelations(CrnkClient.java:408)
                at io.crnk.client.CrnkClient.allocateRepository(CrnkClient.java:389)
                at io.crnk.client.CrnkClient.access$400(CrnkClient.java:95)
                at io.crnk.client.CrnkClient$ClientResourceRegistry.findEntry(CrnkClient.java:648)
                at io.crnk.core.engine.internal.registry.ResourceRegistryImpl.findEntry(ResourceRegistryImpl.java:75)
                at io.crnk.client.CrnkClient.getRepositoryForType(CrnkClient.java:486)
                at net.kirnu.crnk.CrnkTestApplicationTest.relationQueryTest(CrnkTestApplicationTest.java:57)
                    ...

     */
    @Test
    public void testCyclicRelationsRepository() {
        CrnkClient client = getCrnkClient();
        // Crash
        client.getRepositoryForType(CyclicResourceA.class);
    }

    private CrnkClient getCrnkClient() {
        CrnkClient client = new CrnkClient(clientRule.baseUri().toString());
        CrnkTestApplication.configureObjectMapper(client.getObjectMapper());
        return client;
    }

    // -----------------------------------------------------------------------------------------------------------------


    /*
        Subclass RelatedResourceAsub1 has reference to related class RelatedResourceB. We try to filter list of subclasses
        by field in the related class. That filtering isn't applied. Checking at RelatedResourceARepository reveals that
        no filtering is included on the QuerySpec there.
     */

    @Test
    public void testFilteringViaInheritedRelation() {
        CrnkClient client = getCrnkClient();

        QuerySpec querySpec = new QuerySpec(RelatedResourceAsub1.class);
        querySpec.addFilter(
            new FilterSpec(
                PathSpec.of("relatedResourceBS", "id"),
                FilterOperator.EQ,
                10L
            )
        );
        ResourceList<RelatedResourceAsub1> result = client.getRepositoryForType(RelatedResourceAsub1.class).findAll(
            querySpec
        );

        assertEquals(1, result.size());
    }

    // -----------------------------------------------------------------------------------------------------------------


    /*
        Trying to use NumberSizePagingSpec with the client leads to ClassCastException

        io.crnk.core.queryspec.pagingspec.NumberSizePagingSpec cannot be cast to io.crnk.core.queryspec.pagingspec.OffsetLimitPagingSpec
        java.lang.ClassCastException: io.crnk.core.queryspec.pagingspec.NumberSizePagingSpec cannot be cast to io.crnk.core.queryspec.pagingspec.OffsetLimitPagingSpec
            at io.crnk.core.queryspec.pagingspec.OffsetLimitPagingBehavior.serialize(OffsetLimitPagingBehavior.java:15)
            at io.crnk.core.queryspec.mapper.DefaultQuerySpecUrlMapper.serialize(DefaultQuerySpecUrlMapper.java:289)
            at io.crnk.core.queryspec.mapper.DefaultQuerySpecUrlMapper.serialize(DefaultQuerySpecUrlMapper.java:242)
            at io.crnk.core.engine.internal.utils.JsonApiUrlBuilder.buildUrlInternal(JsonApiUrlBuilder.java:87)
            at io.crnk.core.engine.internal.utils.JsonApiUrlBuilder.buildUrl(JsonApiUrlBuilder.java:45)
            at io.crnk.core.engine.internal.utils.JsonApiUrlBuilder.buildUrl(JsonApiUrlBuilder.java:36)
            at io.crnk.client.internal.ResourceRepositoryStubImpl.findAll(ResourceRepositoryStubImpl.java:122)
            at io.crnk.client.internal.ResourceRepositoryStubImpl.findAll(ResourceRepositoryStubImpl.java:26)
            at net.kirnu.crnk.CrnkTestApplicationTest.testNumberSizePaging(CrnkTestApplicationTest.java:168)
            ...
     */
    @Test
    public void testNumberSizePaging() {
        CrnkClient client = getCrnkClient();

        QuerySpec querySpec = new QuerySpec(OrderedResource.class);
        querySpec.setPaging(
            new NumberSizePagingSpec(
                1,
                2
            )
        );

        ResourceList<OrderedResource> result = client.getRepositoryForType(OrderedResource.class).findAll(
            querySpec
        );

        // As page size is 2, we should only get 2 results
        assertEquals(2, result.size());
    }
}
